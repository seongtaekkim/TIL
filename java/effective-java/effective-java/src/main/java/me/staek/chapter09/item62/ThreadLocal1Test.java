package me.staek.chapter09.item62;


import java.util.Random;

/**
 * 키로 활용되는 문자열이 같다면, 리소스를 독립적으로 관리하지 못한다.
 */
public class ThreadLocal1Test implements Runnable {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            ThreadLocal1Test test = new ThreadLocal1Test();
            Thread t = new Thread(test, i + "th thread");
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run() {
//        String key = "key1"; // ThreadLocal의 키가 같으면 중복문제가 발생
        String key = Thread.currentThread().getName(); // 키가 모두 달라야 독립적인 리소스 관리가 가능
        ThreadLocal1.set(key, new Switch());

        System.out.println("Thread Name= " + Thread.currentThread().getName() + " before: " + ThreadLocal1.get(key));

        Switch account = (Switch) ThreadLocal1.get(key);
        account.setToggle(true);

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " result: " + ThreadLocal1.get(key));
    }
}
