package me.staek.issue.simple_jdbctemplate._4;

/**
 * 관심사분리, 관계설정 책임의 분리 (전략패턴)
 * - datasource를 인터페이스로 분리
 * - userdao 는 구현체를 신경 안쓰게 되었지만 그 문제가 클라이언트로 옮겨 왓다.
 *
 *
 * *** 전략패턴 적용 ***
 * 전략 패턴은 자신의 기능 맥락 context에서， 필요에 따라 변경이 필요한 알고리즘을 인터페이스를 통해 통째로 외부로 분리시키고，
 * 이를 구현한 구체적인 알고리즘 클래스를 필요에 따라 바꿔서 사용할 수 있게 하는 디자인 패턴이다.
 *
 */
public class Main {
    public static void main(String[] args) {
        JDBC jdbc = new JDBC();
        UserDao userDao = new UserDao(jdbc);
        userDao.createTable();
        userDao.add();
        userDao.get();
        userDao.deleteAll();
    }
}
