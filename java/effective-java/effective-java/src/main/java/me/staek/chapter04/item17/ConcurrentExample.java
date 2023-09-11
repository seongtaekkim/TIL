package me.staek.chapter04.item17;

import java.util.concurrent.CountDownLatch;

/**
 * ********* concurrency 학습필요 ****************
 * *********************************************
 */
public class ConcurrentExample {
    public static void main(String[] args) throws InterruptedException {
        int N = 10;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i)
            new Thread(new Worker(startSignal, doneSignal)).start();

        ready();
        startSignal.countDown();
        doneSignal.await();
        done();
    }

    private static void ready() {
        System.out.println("준비~~~");
    }

    private static void done() {
        System.out.println("끝!");
    }

    private static class Worker implements Runnable {

        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public void run() {
            try {
                startSignal.await();
                doWork();
                doneSignal.countDown();
            } catch (InterruptedException ex) {}
        }

        void doWork() {
            System.out.println("working thread: " + Thread.currentThread().getName());
        }
    }
}
