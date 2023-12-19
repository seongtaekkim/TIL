package me.staek.chapter10.item71;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Stream 연산 중 throws exception 은 처리 불가.
 */
public class Test {
    public static void main(String[] args) {
        Stream.of(List.of(new Integer[]{1, 2, 3, 4}))
                .filter(f -> {
//                    throw new IOException();
                    return f.size() == 1;
                });

    }
}
