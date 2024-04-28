package me.staek.chapter11.item78;

import java.util.concurrent.TimeUnit;

/**
 * synchronized - 상호배제 + 가시성
 */
public class StopThread_synchronized {
    private static boolean stopRequested;

    private static synchronized void requestStop() {
        stopRequested = true;
    }

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }

    public static void main(String[] args)
            throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested())
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);

        requestStop();

    }
}  
