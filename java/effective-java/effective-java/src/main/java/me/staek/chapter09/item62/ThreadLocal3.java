package me.staek.chapter09.item62;

import java.util.Hashtable;
import java.util.Map;

/**
 * 1. set, get은 정적메서드일 이유가 없으니 key클래스의 인스턴스 메서드로 바꾼다.
 *   ==> Key는 더이상 스레드 지역변수를 구분하기 위한 키가 아니라, 그 자체가 스레드 지역변수가 된다.
 */
public class ThreadLocal3 {

    static Map<Key, Object> map = new Hashtable<>();

    private ThreadLocal3() {
    }

    // 권한
    public static class Key {
        Key() {}

        public void set(Key key, Object value) {
            map.put(key, value);
        }

        public Object get(Key key) {
            Object e = map.get(key);
            if (e != null) {
                return e;
            }
            throw new NullPointerException();
        }
    }
    // 위조 불가능한 고유키를 생성한다.
    public static Key getKey() {
        return new Key();
    }
}
