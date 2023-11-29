package me.staek.threadlocal;

import java.util.SplittableRandom;

/**
 */
public class ThreadLocalRandomTest3 implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalRandomTest3 obj = new ThreadLocalRandomTest3();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(obj, "" + i);
            t.start();
        }
    }

    SplittableRandom random = new SplittableRandom();
    @Override
    public void run() {
        for (int i = 1; i< 1_000_000 ; i++) {
            random.split().nextInt(100);
        }
    }

}
