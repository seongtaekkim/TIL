package me.staek.stream.exception;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 이미 소모된 스트림은 사용하지 못한다.
 */
public class UseTerminalStream {
    public static void main(String[] args) {
        int[] arr = new int[] {1,2,3,4,5};
        IntStream stream = Arrays.stream(arr);
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);
    }
}
