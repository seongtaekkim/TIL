package me.staek.chapter09.item62;


import java.util.Random;

public class ThreadLocal2Test implements Runnable {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            ThreadLocal2Test test = new ThreadLocal2Test();
            Thread t = new Thread(test, i + "th thread");
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    /**
     * ThreadLocal2에서 inner class 에서 중복안되는 키를 생성해서 ThreadLocal 리소스를 관리한다.
     */
    @Override
    public void run() {
        ThreadLocal2.Key key = ThreadLocal2.getKey();
        ThreadLocal2.set(key, new Switch());

        System.out.println("Thread Name= " + Thread.currentThread().getName() + " before: " + ThreadLocal2.get(key));

        Switch account = (Switch) ThreadLocal2.get(key);
        account.setToggle(true);

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " result: " + ThreadLocal2.get(key));
    }
}
