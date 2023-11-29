package me.staek.stream.performance;

import java.util.Arrays;

/**
 * int 덧셈 반복문 성능 측정 테스트
 * For vs Stream
 *
 * 결과: 비슷하다. parallel 로하면 스트림이 더 빠르다.
 * - 스트림이 객체생성 비용이 존재하긴 하지만, int 연산을 지원하는 IntStream등을 잘 활용하면
 * - for문가 비슷한 성능을 보여준다.
 */
public class Test {
    public long sumByFor(int[] numbers) {
        long sum =0 ;
        for (int i=0 ; i<numbers.length ; i++)
            sum += numbers[i];
        return sum;
    }
    public long sumByStream(int[] numbers) {
        return Arrays.stream(numbers).mapToLong(i -> i).sum();
//        return Arrays.stream(numbers).parallel().mapToLong(i -> i).sum();
    }

    public static void main(String[] args) {
        int[] arr = new int[Integer.MAX_VALUE / 3];
        for (int i=0 ; i<arr.length ; i++)
            arr[i] = i;
        Test test = new Test();
        Time t = new Time();
        System.out.println(t.measure(test::sumByFor, arr));;
        System.out.println(t.measure(test::sumByStream, arr));;
    }
}
