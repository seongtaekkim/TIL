package me.staek.chapter09.item62;


import java.util.Random;

public class ThreadLocal4Test implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        Switch aSwitch = new Switch();
        System.out.println(aSwitch);

        for (int i = 0; i < 10; i++) {
            ThreadLocal4Test test = new ThreadLocal4Test();
            Thread t = new Thread(test, i + "th thread");
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }

    }

    /**
     * 기존의 ThreadLocal 하고 비슷하게 동작하는걸 볼 수있다.
     */
    @Override
    public void run() {
        SwitchContext.setAccount(new Switch());
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " before: " + SwitchContext.getAccount());

        Switch account = SwitchContext.getAccount();
        account.setToggle(true);

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " result: " + SwitchContext.getAccount());
    }
}
