package me.staek.issue.simple_jdbctemplate._3;

/**
 * 템플릿 메서드 패턴 적용
 * - datasource 종류가 여러개일 수 있으니, dataSource를 추상클래스로 추출하고
 * - 이를 상속받는 JDBC를 생성.
 *
 * 문제점
 * - 상속을 해야 함
 * - dao는 특정 구현클래스에 컴파일시점 종속되어 있다.
 *   -> 함수명 변경시 하나 씩 바꿔야함
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
