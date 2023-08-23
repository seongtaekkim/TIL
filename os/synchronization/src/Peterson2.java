import java.util.concurrent.atomic.AtomicBoolean;

public class Peterson2 {
    static int count = 0;
    static int turn = 0;
    static AtomicBoolean[] flag;

    static {
        flag = new AtomicBoolean[2];
        for (int i = 0; i < flag.length; i++) {
            flag[i] = new AtomicBoolean();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Producer());
        Thread t2 = new Thread(new Consumer());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(Peterson1.count);
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 100000; k++) {
                /* entry section */
                flag[0].set(true);
                turn = 1;
                while (flag[1].get() && turn == 1)
                    ;
                /* critical section */
                count++;
                /* exit section */
                flag[0].set(false);
                /* remainder section */
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 100000; k++) {
                /* entry section */
                flag[1].set(true);
                turn = 0;
                while (flag[0].get() && turn == 0)
                    ;
                /* critical section */
                count--;
                /* exit section */
                flag[1].set(false);
                /* remainder section */
            }
        }
    }
}