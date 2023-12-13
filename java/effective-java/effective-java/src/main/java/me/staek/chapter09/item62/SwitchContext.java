
package me.staek.chapter09.item62;

/**
 * Switch의 ThreadLocal을 관리하는 클래스.
 * ThreadLocal4 와 ThreadLocal 을 각각 테스트 해볼 수 있다.
 */
public class SwitchContext {
//    private static final ThreadLocal4<Switch> LOCAL = new ThreadLocal4<>();
    private static final ThreadLocal<Switch> LOCAL = new ThreadLocal<>();

    public static void setAccount(Switch s) {
        LOCAL.set(s);
    }
    public static Switch getAccount() {
        return LOCAL.get();
    }
}
