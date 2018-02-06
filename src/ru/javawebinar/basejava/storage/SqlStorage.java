package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT full_name FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
            }

            sqlHelper.doOnRowsInTransaction("SELECT * FROM contact WHERE resume_uuid = ?", uuid, conn,
                    res -> addContact(res, r));
            sqlHelper.doOnRowsInTransaction("SELECT * FROM section WHERE resume_uuid = ?", uuid, conn,
                    res -> addSection(res, r));

            return r;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() != 1) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(conn, r);
            insertContact(conn, r);
            deleteSections(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            LinkedHashMap<String, Resume> map;
            ResultSet rs;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                rs = ps.executeQuery();
                map = new LinkedHashMap<>();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    String name = rs.getString("full_name");
                    map.put(uuid, new Resume(uuid, name));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                rs = ps.executeQuery();
                while (rs.next()) {
                    addContact(rs, map.get(rs.getString("resume_uuid")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(rs, map.get(rs.getString("resume_uuid")));
                }
            }

            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                String value;
                switch (e.getKey()) {
                    case PERSONAL:
                    case OBJECTIVE:
                        value = ((TextSection) e.getValue()).getContent();
                        ps.setString(3, value);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = ((ListSection) e.getValue()).getItems();
                        ps.setString(3, String.join("\n", items));
                        break;
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        deleteRecords(conn, r, "DELETE  FROM contact WHERE resume_uuid=?");
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        deleteRecords(conn, r, "DELETE  FROM section WHERE resume_uuid=?");
    }

    private void deleteRecords(Connection conn, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        if (!rs.wasNull()) {
            SectionType sectionType = SectionType.valueOf(type);
            Section section = null;
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    section = new TextSection(rs.getString("value"));
                    break;
                case QUALIFICATIONS:
                case ACHIEVEMENT:
                    section = new ListSection(rs.getString("value").split("\n"));
            }
            r.addSection(sectionType, section);
        }
    }
}