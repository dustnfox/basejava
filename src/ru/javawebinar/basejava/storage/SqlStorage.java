package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;
    private final String SELECT_ON_RESUME_CONTACT_JOIN = "" +
            "SELECT r.uuid, r.full_name, c.type, c.value " +
            "FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid ";

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute(SELECT_ON_RESUME_CONTACT_JOIN + "WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        updateContactOnResultSet(r, rs);
                    } while (rs.next());

                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            makeRecordInResumeTable(conn, r, true);
            sqlHelper.executeInTransaction("DELETE FROM contact WHERE resume_uuid = ?",
                    conn,
                    (ps, e) -> {
                        ps.setString(1, r.getUuid());
                        ps.executeUpdate();
                    });
            sqlContactsInsert(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            makeRecordInResumeTable(conn, r, false);
            sqlContactsInsert(conn, r);
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
        return sqlHelper.execute(SELECT_ON_RESUME_CONTACT_JOIN + "ORDER BY full_name,uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    Resume r = null;
                    while (rs.next()) {
                        if (r == null || !r.getUuid().equals(rs.getString("uuid"))) {
                            r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                            resumes.add(r);
                        }
                        updateContactOnResultSet(r, rs);
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


    private void updateContactOnResultSet(Resume r, ResultSet rs) throws SQLException {
        String value = rs.getString("value");
        String type = rs.getString("type");
        if (!rs.wasNull()) {
            r.addContact(ContactType.valueOf(type), rs.getString("value"));
        }
    }


    private void sqlContactsInsert(Connection conn, Resume r) throws SQLException {
        sqlHelper.executeInTransaction("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)",
                r.getContacts().entrySet(),
                conn,
                (ps, e) -> {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                });
    }


    private void makeRecordInResumeTable(Connection conn, Resume r, Boolean doUpdate) throws SQLException {
        final String SQL_STMNT = doUpdate ?
                "UPDATE resume SET full_name = ? WHERE uuid = ?" :
                "INSERT INTO resume (full_name, uuid) VALUES (?,?)";

        sqlHelper.executeInTransaction(SQL_STMNT,
                conn,
                (ps, e) -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    if (ps.executeUpdate() == 0 && doUpdate) {
                        throw new NotExistStorageException(r.getUuid());
                    }
                });
    }
}