package me.staek.issue.simple_jdbctemplate._3;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Datasource {
    public abstract Connection newConnection() throws SQLException;

}
