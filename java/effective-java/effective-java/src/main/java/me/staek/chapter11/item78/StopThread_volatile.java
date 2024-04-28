package me.staek.chapter11.item78;

import java.util.concurrent.TimeUnit;

/**
 * volatile - 가시성
 */
public class StopThread_volatile {
    private static volatile boolean stopRequested;

    public static void main(String[] args)
            throws InterruptedException {

        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);

        stopRequested = true;

    }
}
