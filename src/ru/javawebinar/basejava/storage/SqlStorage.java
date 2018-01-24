package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlUtil sqlUtil;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlUtil = new SqlUtil(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlUtil.executeChangeQuery("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return sqlUtil.executeGetQuery("SELECT * FROM resume r WHERE r.uuid =?"
                , ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
                });
    }

    @Override
    public void update(Resume r) {
        sqlUtil.executeChangeQuery("UPDATE resume SET full_name = ? WHERE uuid = ?"
                , (ps) -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(r.getUuid());
                    }
                });
    }

    @Override
    public void save(Resume r) {
        sqlUtil.executeChangeQuery("INSERT INTO resume (uuid, full_name) VALUES (?,?) ON CONFLICT DO NOTHING"
                , (ps) -> {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, r.getFullName());
                    if (ps.executeUpdate() == 0) {
                        throw new ExistStorageException(r.getUuid());
                    }
                });
    }

    @Override
    public void delete(String uuid) {
        sqlUtil.executeChangeQuery("DELETE FROM resume WHERE uuid = ?"
                , (ps) -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlUtil.executeGetQuery("SELECT * FROM resume r ORDER BY uuid"
                , ps -> {
                    List<Resume> list = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
                    }
                    return list;
                });
    }

    @Override
    public int size() {
        return sqlUtil.executeGetQuery("SELECT COUNT(*) FROM resume"
                , ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new StorageException("SQL error. Can't get the size of the table.");
                    }
                    return rs.getInt(1);
                });
    }
}
