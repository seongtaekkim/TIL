public class RaceCondition1 {
    public static void main(String[] args) throws Exception {
        RunnableOne run1 = new RunnableOne();
        RunnableOne run2 = new RunnableOne();
        Thread t1 = new Thread(run1);
        Thread t2 = new Thread(run2);
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Result: " + run1.count + ", " + run2.count);
        } 
}

class RunnableOne implements Runnable {
    int count = 0;
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++)
            count++;
    }
}