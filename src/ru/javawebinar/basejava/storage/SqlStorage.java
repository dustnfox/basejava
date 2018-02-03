package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String value = rs.getString("value");
                        String type = rs.getString("type");
                        if (!rs.wasNull()) {
                            r.addContact(ContactType.valueOf(type), value);
                        }

                    } while (rs.next());

                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            final String UUID = r.getUuid();

            sqlHelper.executeInTransaction("UPDATE resume SET full_name = ? WHERE uuid = ?",
                    conn,
                    (ps, e) -> {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, UUID);
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(UUID);
                        }
                    });
            sqlHelper.executeInTransaction("DELETE FROM contact WHERE resume_uuid = ?",
                    conn,
                    (ps, e) -> {
                        ps.setString(1, UUID);
                    });
            sqlHelper.executeInTransaction("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)",
                    r.getContacts().entrySet(),
                    conn,
                    (ps, e) -> {
                        ps.setString(1, UUID);
                        ps.setString(2, e.getKey().name());
                        ps.setString(3, e.getValue());
                    });
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            sqlHelper.executeInTransaction("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                    conn,
                    (ps, e) -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                    });

            sqlHelper.executeInTransaction("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)",
                    r.getContacts().entrySet(),
                    conn,
                    (ps, e) -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, e.getKey().name());
                        ps.setString(3, e.getValue());
                    });
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
        return sqlHelper.execute("" +
                        "  SELECT r.uuid, r.full_name, c.type, c.value " +
                        "    FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "ORDER BY full_name,uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    Resume r = null;
                    while (rs.next()) {
                        if (r == null || !r.getUuid().equals(rs.getString("uuid"))) {
                            r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                            resumes.add(r);
                        }
                        String type = rs.getString("type");
                        if (!rs.wasNull()) {
                            r.addContact(ContactType.valueOf(type), rs.getString("value"));
                        }
                    }
                    return resumes;
                });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }
}