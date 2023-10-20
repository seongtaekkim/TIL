package me.staek.chapter06.item38;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Operation 구현2
 */
public enum ExtendedOperation implements Operation {
    EXP("^") {
        public double apply(double x, double y) {
            return Math.pow(x, y);
        }
    },
    REMAINDER("%") {
        public double apply(double x, double y) {
            return x % y;
        }
    };
    private final String symbol;
    ExtendedOperation(String symbol) {
        this.symbol = symbol;
    }
    @Override public String toString() {
        return symbol;
    }

    /**
     * Enum opcode 인차처리 방법2
     * ExtendedOperation values 를 이용해 List를 인자로 넘겨
     * test 메서드는 한정자 <? extends Operation> 를 받아 처리할 수 있다.
     */
    public static void main(String[] args) {
        double x = Double.parseDouble("10");
        double y = Double.parseDouble("5");
        test(Arrays.asList(ExtendedOperation.values()), x, y);
    }
    private static void
    test(Collection<? extends Operation> opSet, double x, double y) {
        for (Operation op : opSet)
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }
}
