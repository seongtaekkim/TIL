
public class Peterson1 {
    static int count = 0;
    static int turn = 0;
    static boolean[] flag = new boolean[2];

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
            for (int k = 0; k < 10000; k++) {
                /* entry section */
                flag[0] = true;
                turn = 1;
                while (flag[1] && turn == 1)
                    ;
                /* critical section */
                count++;
                /* exit section */
                flag[0] = false;
                /* remainder section */
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k++) {
                /* entry section */
                flag[1] = true;
                turn = 0;
                while (flag[0] && turn == 0)
                    ;
                /* critical section */
                count--;
                /* exit section */
                flag[1] = false;
                /* remainder section */
            }
        }
    }
}