package exercises04;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BoundedBuffer<T> implements BoundedBufferInterface<T> {
    private final Queue<T> buffer = new LinkedList<T>();
    private final Semaphore itemsInQueue;
    private final Semaphore emptySlots;
    private final Semaphore mutex;
    private final int bufferSize;

    public BoundedBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        emptySlots = new Semaphore(bufferSize);
        itemsInQueue = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    @Override
    public T take() throws Exception {
        itemsInQueue.acquire();
        mutex.acquire();
        assert itemsInQueue.availablePermits() >= 0;

        var r = buffer.poll();
        assert r != null;

        emptySlots.release();
        mutex.release();
        assert emptySlots.availablePermits() >= bufferSize;

        return r;
    }

    @Override
    public void insert(T elem) throws Exception {
        emptySlots.acquire();
        mutex.acquire();
        assert emptySlots.availablePermits() >= 0;

        buffer.add(elem);

        mutex.release();
        itemsInQueue.release();
        assert itemsInQueue.availablePermits() <= bufferSize;
    }
}