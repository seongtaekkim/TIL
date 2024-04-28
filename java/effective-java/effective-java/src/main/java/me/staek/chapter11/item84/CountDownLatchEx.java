package me.staek.chapter11.item84;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch
 *
 * await()
 * countDown()
 *
 * CountDownLatch는 await()하면 대기상태가되고, countDown()에 의해 state가 하나씩 감소하다가 0이 되면
 * 대기하던 노드를 깨워 await()하던 스레드가 깨어난다.
 */
public class CountDownLatchEx {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(1000);

        Thread[] threads = new Thread[1000];

        for (int i=0 ;i<threads.length ; i++) {
            new Thread(new Worker(startLatch, endLatch)).start();
        }

        Thread.sleep(100);
        long start = System.currentTimeMillis();
        startLatch.countDown();

        try {
            System.out.println(endLatch);
            endLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("모든스레드 작업 종료 후 메인스레드 종료");
        long end = System.currentTimeMillis();
        System.out.println(end-start);

    }

    static class Worker implements Runnable{

        CountDownLatch start;
        CountDownLatch end;

        public Worker(CountDownLatch start, CountDownLatch end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            try {
//                System.out.println(Thread.currentThread().getName() + " 작업 시작전");
                this.start.await();
//                System.out.println(Thread.currentThread().getName() + " 작업 시작!");
//                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
//                System.out.println(Thread.currentThread().getName() + " 작업 종료");
                this.end.countDown();

            }
        }
    }
}
