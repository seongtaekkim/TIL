package me.staek.chapter05.item32.heappollution;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 힙오염예제2 개선
 * - 가변변수를 사용하지 않고 해결한다.
 */
public class SafePickTwo {
    /**
     * 제네릭 가변배열을 제네릭 배열로 리턴하면 결국 Object[] 가 되므로,
     * List를 만들어 리턴하게 되면
     * 리턴타입도 소거되어 타입캐스팅에 안전하게 된다.
     */
    static <T> List<T> pickTwo(T a, T b, T c) {
        switch(ThreadLocalRandom.current().nextInt(3)) {
            case 0: return List.of(a, b); // 내부적으로 매개변수로 가변배열로 받지만, 불변 List를 생성하여 리턴하므로 안전함.
            case 1: return List.of(a, c);
            case 2: return List.of(b, c);
        }
        throw new AssertionError();
    }

    public static void main(String[] args) {
        List<String> attributes = pickTwo("effective", "java", "string");
        System.out.println(attributes);

    }
}
