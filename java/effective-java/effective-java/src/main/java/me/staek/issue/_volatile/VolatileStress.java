package me.staek.issue._volatile;

/**
 * TODO volatile 유무에 따라 걸리는 시간을 테스트.
 */
class Value {

//    private volatile int value;
    private int value;

    public int getValue() {
        return value;
    }

    public Value(int value) {
        this.value = value;
    }

    public void increase() {
        this.value++;
    }

    public void decrease() {
        this.value--;
    }
}

public class VolatileStress {

    public static void main(String[] args) throws InterruptedException {
        int limit = Integer.MAX_VALUE / 100;
        long l = System.currentTimeMillis();
        Value value = new Value(0);

        Thread t1 = new Thread(() -> {
            for (int index = 0; index < limit; index++) {
                value.increase();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int index = 0; index < limit; index++) {
                value.decrease();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t1.join();

        long l1 = System.currentTimeMillis();
        System.out.printf("operation time: %d, value: %d", (l1 - l), value.getValue() );

    }
}
