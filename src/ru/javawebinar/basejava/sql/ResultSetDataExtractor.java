package ru.javawebinar.basejava.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetDataExtractor {
    void process(ResultSet rs) throws SQLException;
}
