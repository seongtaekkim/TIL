package me.staek.stream.evaluation;

import java.util.List;

/**
 * 스트리은 필터를 모두 거치며 반복하는 형태로 동작한다 (loopfusion)
 */
public class LoopFusion {

    public static void loopFusion() {
        List<String> names = List.of("seongtki", "jseo", "hannkim", "san", "samin");
        names.stream()
                .filter(str -> {
                    System.out.println("first filter: " + str);
                    return str.length() > 3;
                })
                .filter(str -> {
                    System.out.println("second filter: " + str);
                    return !str.contains("ha");
                })
                .map(str -> {
                    System.out.println("map : " + str);
                    return str.toUpperCase();
                })
                .forEach(str -> {
                    System.out.println("foreach = " + str + "\n");
                });
    }

    public static void main(String[] args) {
        loopFusion();
    }
}
