package me.staek.issue._volatile;


/**
 *
 *
 * TODO static int count 변수가 정의되어 있다.
 *      thread1: count가 변경될 때 localCount를 count로 변경한다.
 *      thread2: count를 1씩 증가시킨다.
 *      -> non volatile: thread1은 count를 cache에서 읽기 때문에 값이 변경됨을 감지 못한다.
 *                       thread2는 count를 cache에 저장한다.
 *         volatile:     thread1은 count를 main memory 에서 읽기 때문에 변경됨을 감지한다.
 *                       thread2는 count를 main memory에 저장한다.
 *
 */
public class CounterTest {

//    private volatile static int count = 0;
    private static int count = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int localCount = count;
                while (localCount < 10) {
                    if (localCount != count) {
                        System.out.println("[Thread1] static count has changed");
                        localCount = count;
                    }
                }
            }
        });

        Thread t2 = new Thread( new Runnable() {
            @Override
            public void run() {
                while (count < 10) {
                    System.out.println("[Thread2] static value is " + (count + 1));
                    count++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }
}