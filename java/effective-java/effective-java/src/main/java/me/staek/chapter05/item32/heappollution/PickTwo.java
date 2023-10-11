package me.staek.chapter05.item32.heappollution;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 힙오염 예제2
 */
public class PickTwo {
    /**
     * 매개변수로 가변인자가 들어왔는데, 값을 하나씩 꺼내지 않고
     * 참조 째로 넘기게 되면, Object 배열이 리턴되게 되므로, 결국 타입캐스팅할 때 런타임에러가 발생한다.
     */
    static <T> T[] toArray(T... args) {
        return args;
    }

    static <T> T[] pickTwo(T a, T b, T c) {
        switch(ThreadLocalRandom.current().nextInt(3)) {
            case 0: return toArray(a, b);
            case 1: return toArray(a, c);
            case 2: return toArray(b, c);
        }
        throw new AssertionError();
    }

    public static void main(String[] args) { // (194쪽)
        /**
         * Object[] 을 String[] 로 변경할 시 ClassCastException 발생.
         */
        String[] attributes = pickTwo("effective", "java", "study");
        System.out.println(Arrays.toString(attributes));

        // 31 line 은 결국 아래와 같아서 ClassCastException 발생
//        Object[] o = {"a"};
//        String[] s = new String[2];
//        s = (String[]) o;
    }
}
