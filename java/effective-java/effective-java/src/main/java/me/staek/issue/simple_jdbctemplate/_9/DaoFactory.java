package me.staek.issue.simple_jdbctemplate._9;

public class DaoFactory {

    public UserDao userDao() {
        JDBC jdbc = new JDBC();
        return new UserDao(jdbc);
    }


}
