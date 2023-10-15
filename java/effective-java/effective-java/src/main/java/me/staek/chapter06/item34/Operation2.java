package me.staek.chapter06.item34;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * 열거타입상수 정의시 필수 메서드를 abstract로 작성하여, 무조건 구현하게 한다.
 * - 실수하지 않는다.
 * - 마치 템플릿메서드패턴 같음
 */
public enum Operation2 {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation2(String symbol) { this.symbol = symbol; }

    @Override public String toString() { return symbol; }

    public abstract double apply(double x, double y);

    // Implementing a fromString method on an enum type (Page 164)
    public static final Map<String, Operation2> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    // Returns Operation for string, if any
    public static Optional<Operation2> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }

    public static void main(String[] args) {
        double x = Double.parseDouble("5");
        double y = Double.parseDouble("10");
        for (Operation2 op : Operation2.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));

        System.out.println(fromString("+"));

        // values 리턴타입
        Operation2[] values = Operation2.values();

        // mapping : <{valueOf}:{Enum}>
        System.out.println(Arrays.stream(Operation2.values()).collect(toMap(Object::toString, e->e)));;


        // 값 출력 테스트
        System.out.println(Operation2.valueOf(Operation2.DIVIDE.name()) + " " + Operation2.DIVIDE.name() + " " + Operation2.DIVIDE.ordinal());

        System.out.println(Operation2.DIVIDE.toString());
        System.out.println(Operation2.values()[1]);
    }
}
