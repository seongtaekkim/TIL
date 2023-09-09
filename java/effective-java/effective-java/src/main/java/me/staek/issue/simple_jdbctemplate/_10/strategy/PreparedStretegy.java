package me.staek.issue.simple_jdbctemplate._10.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStretegy {
   PreparedStatement createStatement(Connection conn) throws SQLException;
}
