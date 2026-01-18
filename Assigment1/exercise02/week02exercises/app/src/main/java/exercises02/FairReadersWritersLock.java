package exercises02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FairReadersWritersLock {
    private int sharedResource;

    public int getSharedResource() {
        return sharedResource;
    }

    public class ReadWriteMonitor {
        private int readers = 0;
        private boolean writer = false;
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        //////////////////////////
        // Read lock operations //
        //////////////////////////

        public void readLock() throws InterruptedException {
            lock.lock();
            try {
                while (writer)
                    condition.await();
                readers++;
            } finally {
                lock.unlock();
            }
        }

        public void readUnlock() {
            lock.lock();
            try {
                readers--;
                if (readers == 0)
                    condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        ///////////////////////////
        // Write lock operations //
        ///////////////////////////

        public void writeLock() throws InterruptedException {
            lock.lock();
            try {
                while (writer)
                    condition.await();
                writer = true;
                while (readers > 0)
                    condition.await();
            } finally {
                lock.unlock();
            }
        }

        public void writeUnlock() {
            lock.lock();
            try {
                writer = false;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public FairReadersWritersLock() {
        ReadWriteMonitor m = new ReadWriteMonitor();
        this.sharedResource = 0;
        final int numReadersWriters = 10;

        for (int i = 0; i < numReadersWriters; i++) {

            // start a reader
            new Thread(() -> {
                try {
                    m.readLock();
                    System.out.println(" Reader " + Thread.currentThread().getId() + " started reading");
                    int currentValueSR = this.sharedResource;
                    System.out.println(" Reader " + Thread.currentThread().getId() + " sharedResource is currently at " + currentValueSR);
                    System.out.println(" Reader " + Thread.currentThread().getId() + " stopped reading");
                    m.readUnlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

            // start a writer
            new Thread(() -> {
                try {
                    m.writeLock();
                    System.out.println(" Writer " + Thread.currentThread().getId() + " started writing");
                    if (sharedResource < 100) {
                        sharedResource++;
                        System.out.println("count: " + sharedResource);
                    }
                    System.out.println(" Writer " + Thread.currentThread().getId() + " stopped writing");
                    m.writeUnlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

        }
    }

    public static void main(String[] args) {
        new FairReadersWritersLock();
    }

}
