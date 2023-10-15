package me.staek.chapter06.item34;

/**
 * 열거타입상수 별 기능을하는 메서드를 작성
 * - 열거타입상수가 추가될 때마다 기능도 추가해야 한다.
 * - 메서드를 작성하지 않으면 컴파일 에러가 발생하지 않는다. (런타임 에러 발생)
 * -> 유지관리 이슈
 */
public enum Operation1 {
    PLUS, MINUS, TIMES, DIVIDE;
    public double apply(double x, double y) {
        switch(this) {
            case PLUS: return x + y;
            case MINUS: return x - y;
            case TIMES: return x * y;
//            case DIVIDE: return x / y;
        }
        throw new AssertionError("fail: oparation : " + this);
    }

    public static void main(String[] args) {
        double x = Double.parseDouble("5");
        double y = Double.parseDouble("10");
        for (Operation1 op : Operation1.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));

        Operation1.DIVIDE.apply(10,2);

    }
}
