package me.staek.issue.simple_jdbctemplate._10;

public class DaoFactory {

    public UserDao userDao() {
        JDBC jdbc = new JDBC();
        return new UserDao(jdbc);
    }
}
