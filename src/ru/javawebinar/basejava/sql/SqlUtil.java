package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlUtil {
    private ConnectionFactory connectionFactory;

    public SqlUtil(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void executeChangeQuery(String statement, SqlChangeQuery task) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            task.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> T executeGetQuery(String statement, SqlGetQuery<T> task) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            return task.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    public interface SqlChangeQuery {
        void execute(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlGetQuery<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    private interface ConnectionFactory {
        Connection getConnection() throws SQLException;
    }
}


