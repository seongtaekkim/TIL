package me.staek.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.function.Supplier;

/**
 * ThreadLocal : 스레드 지역변수
 */
public class ThreadLocalExample2 implements Runnable {

    private static final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(new Supplier<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat get() {
            return new SimpleDateFormat("yyyyMMdd HHmm");
        }
    });

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalExample2 obj = new ThreadLocalExample2();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(obj, "" + i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " default Formatter = " + formatter.get().toPattern());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        formatter.set(new SimpleDateFormat());

        System.out.println("Thread Name= " + Thread.currentThread().getName() + " formatter = " + formatter.get().toPattern());
    }

}
