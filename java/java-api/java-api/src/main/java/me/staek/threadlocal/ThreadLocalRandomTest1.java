package me.staek.threadlocal;

import java.util.Random;

/**
 * ThreadLocalRandom 은 thread 별로 독립적으로 로직을 수행하기 때문에 Atomic 에 대한 지연 없이 처리가 가능하다.
 * -> local Random과 비슷하게 시간이 걸리는 같다.
 */
public class ThreadLocalRandomTest1 implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalRandomTest1 obj = new ThreadLocalRandomTest1();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(obj, "" + i);
            t.start();
        }
    }

    private static Random random = new Random();

    @Override
    public void run() {
        for (int i = 1; i< 1_000_000 ; i++)
            random.nextInt(i);
    }

}
