package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PsProcessor<E> {
    void process(PreparedStatement ps, E element) throws SQLException;
}
