package me.staek.issue.simple_jdbctemplate._10;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC implements Datasource {
    @Override
    public Connection newConnection() throws SQLException {
        String url = "jdbc:sqlite:" + new File(".").getAbsolutePath() + "/data/dev.db";
        return DriverManager.getConnection(url);
    }
}
