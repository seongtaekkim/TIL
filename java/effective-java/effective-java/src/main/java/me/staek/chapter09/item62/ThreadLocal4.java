package me.staek.chapter09.item62;

import java.util.Hashtable;
import java.util.Map;

/**
 * - ThreadLocal class 는 사는 일이 없어지므로 지우고 Key를 ThreadLocal로 이름을 바꾼다.
 * - 클래스를 매개변수타입으로 변환한다.
 *
 *
 * ** 기존 ThreadLocal 하고는 좀 다르다. (스레드별로 관리하는 클래스는 아니다)
 *    ThreadLocal 의 스레드 키는 Thread class 와 private package로 연결되어 있어
 *    구현해보지 못했다.
 */
public class ThreadLocal4<T> {

    static Map<ThreadLocal4<?>, Object> map = new Hashtable<>();

    public ThreadLocal4() {
    }
    public void set(T value) {
        map.put(this, value);
    }
    public T get() {
        Object e = map.get(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e;
            return result;
        }
        throw new NullPointerException();
    }

}
