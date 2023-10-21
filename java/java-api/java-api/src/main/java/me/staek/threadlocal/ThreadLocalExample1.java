package me.staek.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Random;

public class ThreadLocalExample1 implements Runnable {

    /**
     * SimpleDateFormat 는 스레드세이프하지 않다.
     * - 여러개 스레드가 리소스를 동시에 변경했을 때, 다른 스레드로 리소스 변경내역이 전파가 됨.
     */
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmm");

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalExample1 obj = new ThreadLocalExample1();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(obj, "" + i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " default Formatter = " + formatter.toPattern());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat();

        System.out.println("Thread Name= " + Thread.currentThread().getName() + " formatter = " + formatter.toPattern());
    }

}
