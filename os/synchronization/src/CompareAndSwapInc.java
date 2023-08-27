import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


class OptimisticLockCounter{

    private AtomicLong count = new AtomicLong();

	
    public void inc() {

        boolean incSuccessful = false;
        while(!incSuccessful) {
            long value = this.count.get();
            long newValue = value + 1;

            incSuccessful = this.count.compareAndSet(value, newValue);
        }
    }

    public long getCount() {
        return this.count.get();
    }
}


class RunnableOne implements Runnable {

    OptimisticLockCounter counter;
    public RunnableOne(OptimisticLockCounter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++)
        {
            counter.inc();
        }
    }
}

public class CompareAndSwapInc {

    public static void main(String[] args) throws Exception {
        OptimisticLockCounter counter =new OptimisticLockCounter();
        RunnableOne run1 = new RunnableOne(counter);
		int len = 8;
		Thread t[] = new Thread[len];
		for (int i = 0 ; i < len ; i++) {
			t[i] = new Thread(run1);
			t[i].start();
		}
		for (int i = 0 ; i < len ; i++) {
			t[i].join();
		}
        System.out.println("Result: " + counter.getCount() );
    }
}


