package me.staek.chapter08.item49;

import java.util.Arrays;

/**
 * 일반적으로 assert는 프로그램 동작에 아무 영향을 주지 않는다.
 * 프로그램 실행 시 -ea 혹은 , —-enableassertions 플래그로 assert 수행을 할 수 있다.
 */
public class AssertTest {

    private static void sort(long a[], int offset, int length) {
        assert a != null;
        assert offset >= 0 && offset <= a.length;
        assert length >= 0 && length <= a.length - offset;

        Arrays.sort(a, offset, offset+length);
    }
    public static void main(String[] args) {
        long[] data = new long[] {7,6,5,4,3,2,1};
        sort(data,2,4);
        Arrays.stream(data).forEach(System.out::println);
    }
}
