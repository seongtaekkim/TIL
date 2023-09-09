package me.staek.issue.simple_jdbctemplate._2;

/**
 * TODO User data access object 를 생성
 *      1. 요청별 함수를 추출
 *      2. 커넥션 생성은 함수로 따로 추출
 *
 */
public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        userDao.createTable();
        userDao.add();
        userDao.get();
        userDao.deleteAll();
    }
}
