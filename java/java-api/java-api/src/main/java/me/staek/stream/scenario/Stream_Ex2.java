package me.staek.stream.scenario;

import java.util.stream.Stream;

public class Stream_Ex2 {
    public static void main(String[] args) {
        System.out.println("---------------------");
        Stream.iterate(10, i -> i + 1); // iterator : 중개연산
        Stream.iterate(10, i -> i + 1)
                .skip(20)
                .limit(10)
                .forEach(t -> System.out.println(t));
    }
}
