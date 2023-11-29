package me.staek.stream.exception;

import java.util.stream.IntStream;

/**
 * distinct().limit(5) : 출력 후 무한루프
 * limit(5).distinct() : 출력 후 완료
 */
public class InfinityDistinct {
    public static void main(String[] args) {
        IntStream.iterate(10, i -> i%2)
                .distinct()
                .limit(5)
                .forEach(System.out::println);
        System.out.println("completed");
    }
}
