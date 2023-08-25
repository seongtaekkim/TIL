import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

// atomic 
class CompareAndSwapLock {

    private AtomicBoolean locked = new AtomicBoolean(false);

    public void unlock() {
        this.locked.set(false);
    }

    public void lock() {
        while(!this.locked.compareAndSet(false, true)) {
            // busy wait - until compareAndSet() succeeds
        }
    }
}

// synchronized
 class ProblematicLock {

    private volatile boolean locked = false;

    public synchronized void lock() {

        while(this.locked) {
            // busy wait - until this.locked == false
        }

        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

}

class RunnableTwo implements Runnable {
    static int count = 0;
    CompareAndSwapLock lock;
    public RunnableTwo(CompareAndSwapLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++)
        {
            lock.lock();
            count++;
            lock.unlock();
        }
    }
}

public class CompareAndSwap {

    public static void main(String[] args) throws Exception {
        CompareAndSwapLock lock =new CompareAndSwapLock();
        RunnableTwo run1 = new RunnableTwo(lock);
        RunnableTwo run2 = new RunnableTwo(lock);
        Thread t1 = new Thread(run1);
        Thread t2 = new Thread(run2);
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Result: " + run1.count );
    }
}

