package me.staek.issue.simple_jdbctemplate._7.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetAllStrategy implements PreparedStretegy {

    @Override
    public PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("select id, name, password from users;");
    }
}
