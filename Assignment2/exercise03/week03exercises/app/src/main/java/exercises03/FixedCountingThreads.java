// For week 3 
// raup@itu.dk * 2025-09-03

package exercises03;

import java.util.concurrent.locks.ReentrantLock;

public class FixedCountingThreads {
    private int count;
    private ReentrantLock l = new ReentrantLock(); // init(l): the lock was added to the class so both CountingThread instances can use it

    public FixedCountingThreads() throws InterruptedException {
        count = 0;
        
        CountingThread t1 = new CountingThread();
        CountingThread t2 = new CountingThread();
        
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        
        System.out.println("count="+count);   
    }

    public class CountingThread extends Thread {

        public void run() {
            l.lock(); // lock before accessing the shared variable
            int temp = count;
            count = temp + 1;
            l.unlock();  // unlock after updating the shared variable
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new FixedCountingThreads();
    }
}
