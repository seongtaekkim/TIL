package me.staek.chapter05.item33.super_type_token;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FindParameterizedType {

    static class Super<T> {
        T value;
    }

    static class Sub extends Super<String> {

    }

    public static void main(String[] args) throws NoSuchFieldException {
        /**
         * feild type은 런타임에 제네릭이 소거되어 Object로 조회된다.
         */
        Super<String> stringSuper = new Super<>();
        System.out.println(stringSuper.getClass().getDeclaredField("value").getType());

        /**
         * ParameterizedType 에 타입에대한 정보가 존재하여 이를 이용해 List<String> 에 대한 이종컨테이너를 만들 수 있다.
         */
        Sub sub = new Sub();
        Type type = sub.getClass().getGenericSuperclass();
//        Type type = Sub.class.getGenericSuperclass();
//        Type type = (new Super<String>() {}).getClass().getGenericSuperclass();
        ParameterizedType pType = (ParameterizedType) type;
        for (Type actual : pType.getActualTypeArguments()) {
            System.out.println(actual);
        }
    }
}
