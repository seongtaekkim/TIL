package me.staek.chapter05.item30;

import java.util.HashSet;
import java.util.Set;

/**
 * generic method 구현
 *
 * 출력할때 책에는 [모에, 톰, 해리, 래리, 컬리, 딕] 순서로 나온다는데
 * 나는 [톰, 해리, 래리, 딕, 컬리, 모에] 로 나온다.
 * 해시구현법이 어디가 다른건가 찾아보자.
 */
public class GenericMethodUnion {

    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    public static void main(String[] args) {
        Set<String> guys = Set.of("톰", "딕", "해리");
        Set<String> stooges = Set.of("래리", "모에", "컬리");
        Set<String> aflCio = union(guys, stooges);
        System.out.println(aflCio);
    }
}
