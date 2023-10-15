package me.staek.chapter06.item34;

/**
 * switch 문 쓰기 좋은 사례
 * - 이미 정해진 상수코드 내에서 섞어쓸 경우
 */
public class Inverse {
    public static Operation1 inverse(Operation1 op) {
        switch(op) {
            case PLUS:   return Operation1.MINUS;
            case MINUS:  return Operation1.PLUS;
            case TIMES:  return Operation1.DIVIDE;
            case DIVIDE: return Operation1.TIMES;

            default:  throw new AssertionError("Unknown op: " + op);
        }
    }

    public static void main(String[] args) {
        double x = Double.parseDouble("10");
        double y = Double.parseDouble("5");
        for (Operation1 op : Operation1.values()) {
            Operation1 invOp = inverse(op);
            System.out.printf("%f %s %f %s %f = %f%n",
                    x, op, y, invOp, y, invOp.apply(op.apply(x, y), y));
        }
    }
}
