package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlUtil {

    public static void executeChangeQuery(ConnectionFactory connectionFactory
            , String statement, SqlChangeQuery task) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            task.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static <T> T executeGetQuery(ConnectionFactory connectionFactory
            , String statement, SqlGetQuery<T> task) {
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
}


