package me.staek.chapter09.item62;

import java.util.Hashtable;
import java.util.Map;

/**
 * 해결안) 문자열대신 위조할 수 없는 키를 만들어 사용
 */
public class ThreadLocal2 {
    static Map<Key, Object> map = new Hashtable<>();
    private ThreadLocal2() {}

    // 권한
    public static class Key {
        Key() {}
    }
    // 위조 불가능한 고유키를 생성한다.
    public static Key getKey() {
        return new Key();
    }

    public static void set(Key key, Object value) {
        map.put(key, value);
    }
    public static Object get(Key key) {
        Object e = map.get(key);
        if (e != null) {
            return e;
        }
        throw new NullPointerException();
    }
}
