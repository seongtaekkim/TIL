package me.staek.chapter09.item62;


import java.util.Random;

public class ThreadLocal3Test implements Runnable {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            ThreadLocal3Test test = new ThreadLocal3Test();
            Thread t = new Thread(test, i + "th thread");
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    /**
     * ThreadLocal 에서 set,get 을 호출하는게 아니라, Key 에서 호출하게 되었다.
     */
    @Override
    public void run() {
        ThreadLocal3.Key key = ThreadLocal3.getKey();
        key.set(key, new Switch());

        System.out.println("Thread Name= " + Thread.currentThread().getName() + " before: " + key.get(key));

        Switch account = (Switch) key.get(key);
        account.setToggle(true);

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " result: " + key.get(key));
    }
}
