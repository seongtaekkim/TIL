package me.staek.chapter06.item38;


/**
 * Operation를 확장하여 opcodd method를 작성하는 Enum
 */
public enum BasicOperation implements Operation {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") ,
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    BasicOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override public String toString() {
        return symbol;
    }

    /**
     * Enum opcode 인차처리 방법1
     * BasicOperation.class 를 인자로넘긴다
     * test 메서드는 <T extends Enum<T> & Operation> 타입의 Class<T>를 처리한다.
     * Enum value를 꺼내기 위해 getEnumConstants 메서드를 사용한다.
     */
    public static void main(String[] args) {
        double x = Double.parseDouble("10");
        double y = Double.parseDouble("5");
        test(BasicOperation.class, x, y);

        /**
         * BasicOperation는 아래 세개 타입임
         */
        System.out.println(BasicOperation.MINUS instanceof Enum);
        System.out.println(BasicOperation.MINUS instanceof Operation);
        System.out.println(BasicOperation.MINUS instanceof Object);

        /**
         * isinstance test
         */
        System.out.println(BasicOperation.class.getClass().isInstance(Enum.class));
        System.out.println(BasicOperation.class.getClass().isInstance(Operation.class));
        System.out.println(BasicOperation.class.getClass().isInstance(Object.class));
        System.out.println(BasicOperation.MINUS.getClass().isInstance(Object.class)); // false

    }
    private static <T extends Enum<T> & Operation>
    void test(Class<T> opEnumType, double x, double y) {
        for (Operation op : opEnumType.getEnumConstants()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }
}
