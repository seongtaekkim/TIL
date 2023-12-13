package me.staek.chapter09.item62;

import java.util.Hashtable;
import java.util.Map;

/**
 * 스레드 구분용 문자열 키가 전역 이름공간에서 공유된다.
 * 클라이언트가 같은 키를 쓰면 의미가 없어진다.
 */
public class ThreadLocal1 {
    static Map<String,  Object> map = new Hashtable<>();

    private ThreadLocal1() {}
    public static void set(String key, Object value) {
        if (map.get(key) != null)
            return ;
        map.put(key, value);
    }
    public static Object get(String key) {
        Object e = map.get(key);
        if (e != null) {
            return e;
        }
        throw new NullPointerException();
    }
}
