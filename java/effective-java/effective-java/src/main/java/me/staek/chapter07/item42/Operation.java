package me.staek.chapter07.item42;

import java.util.function.DoubleBinaryOperator;

/**
 * item34 개선
 * - 기존: 상수별 클래스 몸체구현
 * - 개선: 열거타입 생성자에 인스턴스 필드 (람다) 작성
 *    => 주의사항 : 람다는 이름도 없고 문서화도 못 하기 때문에 코드가 명확하지 않으면 사용하지 않는게 좋다.
 */
public enum Operation {
    PLUS  ("+", new DoubleBinaryOperator() {
        @Override
        public double applyAsDouble(double x, double y) {
            return x + y;
        }
    }),
    MINUS ("-", (x, y) -> x - y),
    TIMES ("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    private final String symbol;
    private final DoubleBinaryOperator op;

    Operation(String symbol, DoubleBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    @Override public String toString() { return symbol; }

    public double apply(double x, double y) {
        return op.applyAsDouble(x, y);
    }

    public static void main(String[] args) {
        double x = Double.parseDouble("10");
        double y = Double.parseDouble("5");
        for (Operation op : Operation.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
    }
}
