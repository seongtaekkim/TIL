package me.staek.issue.simple_jdbctemplate._7.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class createTableStrategy implements PreparedStretegy {
    @Override
    public PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("create table if not exists users (id integer primary key, name text not null, password text not null);");
    }
}
