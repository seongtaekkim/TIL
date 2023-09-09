package me.staek.issue.simple_jdbctemplate._6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class createTableUserdao extends UserDao {
    public createTableUserdao(Datasource datasource) {
        super(datasource);
    }
    protected PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("create table if not exists users (id integer primary key, name text not null, password text not null);");
    }
}
