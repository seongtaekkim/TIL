package me.staek.chapter05.item30;

import java.util.*;

/**
 * 재귀적 타입한정 (recursive type bound) 예제 구현
 *
 */
public class RecursiveTypeBound {
    public static <E extends Comparable<E>> Optional<Object> max(Collection<E> c) {
        if (c.isEmpty())
//            throw new IllegalArgumentException("컬렉션이 비어 있습니다.");
            return Optional.empty();

        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);

        return Optional.of(result);
    }

    public static void main(String[] args) {
        List<String> argList = List.of("effective", "java");
//        List<String> argList = new ArrayList<>();
        max(argList).ifPresent(System.out::println);
    }
}
