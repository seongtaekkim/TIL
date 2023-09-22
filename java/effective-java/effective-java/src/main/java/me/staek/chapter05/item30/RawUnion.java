package me.staek.chapter05.item30;

import java.util.HashSet;
import java.util.Set;

/**
 * 메서드에 제네릭 타입인 Set을 타입매개변수 없이 raw type으로 사용
 *
 * - 경고발생
 * - 런타임에러 가능 (ClassCastException)
 */
public class RawUnion {
    public static Set union(Set s1, Set s2) {
        Set result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    public static void main(String[] args) {
        Set union = union(Set.of("a"), Set.of("b"));

//        for(Object o: union)
//            System.out.println((Integer)o);
    }
}