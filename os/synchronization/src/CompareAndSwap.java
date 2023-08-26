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
    ProblematicLock lock;
    public RunnableTwo(ProblematicLock lock) {
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

		ProblematicLock lock =new ProblematicLock();
        RunnableTwo run1 = new RunnableTwo(lock);
		int len = 2;
		Thread t[] = new Thread[len];
		for (int i = 0 ; i < len ; i++) {
			t[i] = new Thread(run1);
			
		}
		for (int i = 0 ; i < len ; i++) 
			t[i].start();
		for (int i = 0 ; i < len ; i++) {
			t[i].join();
		}
        System.out.println("Result: " + run1.count );
    }
}

