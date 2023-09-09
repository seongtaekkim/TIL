package me.staek.issue.simple_jdbctemplate._11;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2 implements Datasource {
    @Override
    public Connection newConnection() throws SQLException {
        String url = "jdbc:h2:" + new File(".").getAbsolutePath() + "/data/devh2.db";
        return DriverManager.getConnection(url,"sa","");
    }
}
