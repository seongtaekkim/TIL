package me.staek.chapter11.item84;


/**
 * await()를 대기상태가 아닌 바쁜대기를 유도하여 스레드 경합에 의해 전체 수행시간이 늦어진다.  (CountDownLatchEx와 비교)
 */
public class SlowCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        SlowCountDownLatch startLatch = new SlowCountDownLatch(1);
        SlowCountDownLatch endLatch = new SlowCountDownLatch(1000);

        Thread[] threads = new Thread[1000];

        for (int i=0 ;i<threads.length ; i++) {
            new Thread(new Worker(startLatch, endLatch)).start();
        }

        Thread.sleep(100);
        long start = System.currentTimeMillis();
        startLatch.countDown();

        endLatch.await(); // 메인스레드: Latch가 0 될 때까지 대기

        System.out.println("모든스레드 작업 종료 후 메인스레드 종료");
        long end = System.currentTimeMillis();
        System.out.println(end-start);

    }

    static class Worker implements Runnable{

        SlowCountDownLatch start;
        SlowCountDownLatch end;

        public Worker(SlowCountDownLatch start, SlowCountDownLatch end) {
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
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            } finally {
//                System.out.println(Thread.currentThread().getName() + " 작업 종료");
                this.end.countDown();

            }
        }
    }
    private int count;

    public SlowCountDownLatch(int count) {
        if (count < 0)
            throw new IllegalArgumentException(count + " < 0");
        this.count = count;
    }

    public void await() {
        while (true) {
            synchronized(this) {
                if (count == 0)
                    return;
            }
        }
    }
    public synchronized void countDown() {
        if (count != 0)
            count--;
    }
}
