package me.staek.issue._volatile;

/**
 *  TODO static boolean flag를 정의한다.
 *       thread1: flag가 false가 될 때까지 반복한다.
 *       thread2: sleep 이후 flag를 false 로 세팅한다.
 *       -> non volatile: thread1은 cache 에서 flag 를 읽으므로 무한반복한다.
 *          volatile:     thread1은 main memory 에서 flag를 읽으므로 thread를 종료한다.
 *                        thread2는 main memory에 flag를 변경한다.
 */
public class BoolTest {
    static boolean flag = true;
//    static volatile boolean running = true;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (flag) {
                    count++;
                }
                System.out.println("[Thread1] end, count: " + count);
            }
        }
        ).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                flag = false;
                System.out.println("[Thread2] end");
            }
        }
        ).start();
    }
}
