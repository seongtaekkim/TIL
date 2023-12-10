package me.staek.chapter08.item55;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

/**
 * max 메서드의 Optional 사용 전/후 비교
 * orElse 와 orElseGet 차이점
 * orElseThrow 예제
 */
public class Max {
//    // Returns maximum value in collection - throws exception if empty (Page 249)
//    public static <E extends Comparable<E>> E max(Collection<E> c) {
//        if (c.isEmpty())
//            throw new IllegalArgumentException("Empty collection");
//
//        E result = null;
//        for (E e : c)
//            if (result == null || e.compareTo(result) > 0)
//                result = Objects.requireNonNull(e);
//
//        return result;
//    }

//    // Returns maximum value in collection as an Optional<E> (Page 250)
//    public static <E extends Comparable<E>>
//    Optional<E> max(Collection<E> c) {
//        if (c.isEmpty())
//            return Optional.empty();
//
//        E result = null;
//        for (E e : c)
//            if (result == null || e.compareTo(result) > 0)
//                result = Objects.requireNonNull(e);
//
//        return Optional.of(result);
//    }

    public static <E extends Comparable<E>>
    Optional<E> max(Collection<E> c) {
        return c.stream().max(Comparator.naturalOrder());
    }

    public static String getString() {
        System.out.println("???");
        return "merry christmas !!";
    }

    public static void main(String[] args) {
        List<String> words = new ArrayList<>();

        System.out.println(max(words));

        words.add("apple");
        words.add("banana");

        String lastWordInLexicon = max(words).orElse(getString());
        System.out.println(lastWordInLexicon);
//
//        max(words).orElseThrow(new Supplier<>() {
//            @Override
//            public RuntimeException get() {
//                return new RuntimeException();
//            }
//        });


        /**
         * orElse 와 orElseGet 차이점 ?
         */
//        max(words).get();
        String s = max(words).orElseGet(() -> getString());
        System.out.println("result " + s);

    }
}
