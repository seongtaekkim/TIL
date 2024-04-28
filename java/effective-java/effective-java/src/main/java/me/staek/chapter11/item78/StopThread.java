package me.staek.chapter11.item78;

import java.util.concurrent.TimeUnit;

/**
 * 일반 공유변수는 스레드가 캐싱하고 사용하기 때문에
 * 다른 스레드에서 변경한 공유데이터를 즉시 확인할 수 없다.
 */
public class StopThread {
    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
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
