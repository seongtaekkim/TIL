package me.staek.issue.simple_jdbctemplate._11;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Rowmapper<T> {
    public T row(ResultSet rs) throws SQLException;
}
