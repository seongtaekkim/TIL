package me.staek.issue.simple_jdbctemplate._9;

/**
 *
 *
 * 컨텍스트 분리
 * - context관련 로직을 작성할 JDBCContext class 생성
 * - JDBCContext는 Datasourece 인스턴스에 의존하도록 작성
 * - 의존관계: UserDao -> JDBCContext -> Datasourece 로 변경
 *
 *
 * 콜백
 * - 콜백객체는 UserDao에서 JDBCContext로 옮긴다. (콜백도 결국 jdbc관련 로직이기 때문)
 * - Userdao 에서 executeSql를 실행하면, JDBCContext에서 콜백객체가 만들어지고 그 객체는 context 함수에 전달된다.
 * - context 함수는 로직을 진행하다가 필요시점에 콜백함수를 실행한다.
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
