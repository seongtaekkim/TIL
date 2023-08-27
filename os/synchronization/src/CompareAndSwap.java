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
        }
    }
}

// synchronized
 class ProblematicLock {

    private volatile boolean locked = false;

    public synchronized void lock() {

        while(this.locked) {
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

/**
 * TODO thread 2개일 경우 synchronized 와 compareAndSwap 완료시간을 비교.
 *      thread 100개일 경우 같은테스느 진행
 *      1번케이스 : compareAndSwap 빠름
 *      2번케이스 : synchronized 빠름
 */
public class CompareAndSwap {

    public static void main(String[] args) throws Exception {

		CompareAndSwapLock lock =new CompareAndSwapLock();
        RunnableTwo run1 = new RunnableTwo(lock);
		int len = 8;
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

