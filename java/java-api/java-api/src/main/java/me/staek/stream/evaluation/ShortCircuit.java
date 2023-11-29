package me.staek.stream.evaluation;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * short circuit Evaluation
 * - 일련의 &&, || 등의 연산결과를 조합하는 경우, 연산 끝에 도달하기 전에 결과가 확실하다면
 * - 나머지 연산을 확인 할 필요가 없다.
 *
 * intermediate operation
 * - limit
 *
 * terminal operation
 * - findFirst(), findAny(), anyMatch(), allMath(), noneMatch()
 */
public class ShortCircuit {


//    private static boolean shortCircuit() {
//        if (evalOne() || evalTwo()) {
//            return true;
//        }
//        return false;
//    }
    private static boolean evalTwo() {
        System.out.println("call evaltwo()");
        return false;
    }

    private static boolean evalOne() {
        System.out.println("call evalOne");
        return true;
    }

    private static void shortCircuit() {
        IntStream stream = IntStream.range(10, 20).limit(5);
        IntStream filterStream = stream.filter(i -> i % 3 == 0);
        OptionalInt first = filterStream.findFirst();
        if (first.isPresent()) {
            System.out.println(first.getAsInt());
        }
    }

    public static void main(String[] args) {
        shortCircuit();
    }
}
