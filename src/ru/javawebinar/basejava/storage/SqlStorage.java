package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.SqlUtil;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlUtil.executeChangeQuery(connectionFactory
                , "DELETE FROM resume"
                , PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return SqlUtil.executeGetQuery(connectionFactory
                , "SELECT * FROM resume r WHERE r.uuid =?"
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
        SqlUtil.executeChangeQuery(connectionFactory
                , "UPDATE resume SET full_name = ? WHERE uuid = ?"
                , (ps) -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    ps.execute();
                });
    }

    @Override
    public void save(Resume r) {
        SqlUtil.executeChangeQuery(connectionFactory
                , "INSERT INTO resume (uuid, full_name) VALUES (?,?) ON CONFLICT DO NOTHING"
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
        SqlUtil.executeChangeQuery(connectionFactory
                , "DELETE FROM resume WHERE uuid = ?"
                , (ps) -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlUtil.executeGetQuery(connectionFactory
                , "SELECT * FROM resume r"
                , ps -> {
                    List<Resume> list = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
                    }
                    Collections.sort(list);
                    return list;
                });
    }

    @Override
    public int size() {
        return SqlUtil.executeGetQuery(connectionFactory
                , "SELECT COUNT(*) FROM resume"
                , ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new StorageException("SQL error");
                    }
                    return rs.getInt(1);
                });
    }
}
