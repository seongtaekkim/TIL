package me.staek.chapter06.item39._04_repeatableannotation;

import java.util.ArrayList;
import java.util.List;


/**
 * @ExceptionTest 테스트 코드 작성
 * @ExceptionTest를 여러개 반복하여 작성할 수 있다. (@ExceptionTestContainer 에 배열형태로 쌓임)
 */
public class Sample4 {
    @ExceptionTest(ArithmeticException.class)
    public static void m1() {  // Test should pass
        int i = 0;
        i = i / i;
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m2() {  // Should fail (wrong exception)
        int[] a = new int[0];
        int i = a[1];
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m3() { }  // Should fail (no exception)

    @ExceptionTest(IndexOutOfBoundsException.class)
    @ExceptionTest(NullPointerException.class)
    public static void doublyBad() {
        List<String> list = new ArrayList<>();

        list.addAll(5, null);
    }
}
