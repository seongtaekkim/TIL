package me.staek.chapter05.item33.super_type_token;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * super type token 파훼예제
 * - 제네릭타입 서치 실패
 */
public class NotFoundGenericTypeEx {

    static abstract class Super<T> {
        T value;
    }

    static class Sub<T> extends Super<T> {

    }

    public static void main(String[] args) throws NoSuchFieldException {

        /**
         * ParameterizedType 에 타입에대한 정보가 존재하여 이를 이용해 List<String> 에 대한 이종컨테이너를 만들 수 있다.
         */
        Sub sub = new Sub();
        Type type = sub.getClass().getGenericSuperclass();
        ParameterizedType pType = (ParameterizedType) type;
        for (Type actual : pType.getActualTypeArguments()) {
            System.out.println(actual);
        }
    }
}
