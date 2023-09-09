package me.staek.issue.simple_jdbctemplate._7;

/**
 * 전략패턴 적용
 * - PreparedStrategy 로 전략을 만들어 추출하고, 구현체별로 요청함수를 적용
 *
 * 문제점
 * - dao별 요청함수를 생성할 때마다 구현체를 새로 만들어야 함
 *
 *
 */
public class Main {
    public static void main(String[] args) {

        UserDao userDao = new DaoFactory().userDao();

        userDao.createTable();
        userDao.add();
        userDao.get();
        userDao.deleteAll();
    }
}
