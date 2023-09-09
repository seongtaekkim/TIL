package me.staek.issue.simple_jdbctemplate._11;

public class DaoFactory {

    public UserDao userDao() {
        Datasource jdbc = new JDBC();
//        Datasource jdbc = new H2();
        return new UserDao(jdbc);
    }
}
