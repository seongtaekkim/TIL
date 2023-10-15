package me.staek.chapter05.item33.super_type_token;

import java.util.ArrayList;
import java.util.List;

/**
 * neal gafter's super type token 파훼 예제
 * https://gafter.blogspot.com/2007/05/limitation-of-super-type-tokens.html
 */
class Oops {
    static Favorites2 f = new Favorites2();

    static <T> List<T> favoriteList() {
        /**
         * List<T> 타입으로 인식한다.
         */
        TypeRef<List<T>> ref = new TypeRef<>() {};
        System.out.println(ref.getType());

        List<T> result = f.get(ref);
        if (result == null) {
            result = new ArrayList<T>();
            f.put(ref, result);
        }
        return result;
    }

    /**
     * 실제 타입을 알 수 없어 런타임에러가 발생한다.
     */
    public static void main(String[] args) {
        List<String> ls = favoriteList();
        List<Integer> li = favoriteList();
        li.add(1);

        for (String s : ls) System.out.println(s);
    }
}
