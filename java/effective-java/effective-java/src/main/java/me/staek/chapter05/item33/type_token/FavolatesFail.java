package me.staek.chapter05.item33.type_token;

import java.util.HashMap;
import java.util.Map;

/**
 * 타입안전이종컨테이너 잘못된 설계
 * - Map<Class, Object>의 Class는 type token을 잘못 사용한 예이다.
 * - key인 Class는 value인 Object에 영향을 주지 못하므로 기대한 타입과 다르게 입력되어도 실행된다는 문제가 있다.
 */
public class FavolatesFail {

    private Map<Class, Object> map = new HashMap<>();
    public void put(Class clazz, Object object) {
        map.put(clazz, object);
    }
    public Object get(Class clazz) {
        return this.map.get(clazz);
    }

    /**
     * put 첫번째 인자는 모든 class literal type을 받기 때문에 값에 제약이 없다.
     */
    public static void main(String[] args) {
        FavolatesFail f = new FavolatesFail();
        f.put(String.class, "@");
        f.put(String.class, 1);
    }
}
