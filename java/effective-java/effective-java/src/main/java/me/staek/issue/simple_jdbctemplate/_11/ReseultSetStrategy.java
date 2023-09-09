package me.staek.issue.simple_jdbctemplate._11;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ReseultSetStrategy<T> {
   public T getDate(ResultSet rs) throws SQLException;
}
