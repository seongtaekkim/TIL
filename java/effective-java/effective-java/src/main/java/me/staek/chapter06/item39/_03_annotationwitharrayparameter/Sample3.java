package me.staek.chapter06.item39._03_annotationwitharrayparameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ExceptionTest 테스트 코드 작성
 * 여러개의 타입을 배열형태로 전달할 수 있다. {A.class, B.class ... }
 */
public class Sample3 {
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

    // Code containing an annotation with an array parameter (Page 185)
    @ExceptionTest({ IndexOutOfBoundsException.class,
                     NullPointerException.class, ArithmeticException.class })
    public static void doublyBad() {   // Should pass
        List<String> list = new ArrayList<>();

        // The spec permits this method to throw either
        // IndexOutOfBoundsException or NullPointerException
        list.addAll(5, null);
    }
}
