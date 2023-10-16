package me.staek.threadlocal;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocalRandom 은 thread 별로 독립적으로 로직을 수행하기 때문에 Atomic 에 대한 지연 없이 처리가 가능하다.
 * -> local Random과 비슷하게 시간이 걸리는 같다.
 */
public class ThreadLocalRandomTest2 implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalRandomTest2 obj = new ThreadLocalRandomTest2();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(obj, "" + i);
            t.start();
        }
    }

    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    @Override
    public void run() {
        Random random = new Random();
        for (int i = 1; i< 1_000_000 ; i++) {
            random.nextInt(i);
        }
    }

}
