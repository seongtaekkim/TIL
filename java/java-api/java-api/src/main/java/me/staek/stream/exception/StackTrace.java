package me.staek.stream.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Stream 에서 예외가 발생하면 stack trace 추적하는게 상대적으로 복잡하다.
 */
public class StackTrace {
    private double divideByFor(List<Integer> divided, int dividedNumber) {
        double sum = 0.0;
        for (int i : divided)
            sum += i / dividedNumber;
        return sum;
    }
    private double divideByStream(List<Integer> divided, int dividedNumber) {
        return IntStream.range(0, divided.size())
                .mapToDouble(idx -> divided.get(idx) / dividedNumber)
                .sum();
    }
    public static void main(String[] args) {
        Integer[] arr = new Integer[] {1,2,3,4,5};
        StackTrace test = new StackTrace();
//        test.divideByFor(Arrays.asList(arr), 0);
        test.divideByStream(Arrays.asList(arr), 0);
    }
}
