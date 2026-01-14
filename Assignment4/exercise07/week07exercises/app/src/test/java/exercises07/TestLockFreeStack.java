// raup@itu.dk * 2023-10-20
package exercises07;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


class TestLockFreeStack {

    // TODO: 7.2.2 - Test push

    @Test
    @DisplayName("Concurrent push test - sum verification")
    public void testConcurrentPush() throws InterruptedException {
        final int NUM_THREADS = 10;
        final LockFreeStack<Integer> stack = new LockFreeStack<>();
        final CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS);
        final Thread[] threads = new Thread[NUM_THREADS];
        
        long expectedSum = 0;
        for (int i = 1; i <= NUM_THREADS; i++) {
            expectedSum += i;
        }
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int value = i + 1;
            threads[i] = new Thread(() -> {
                try {
                    barrier.await(); 
                    stack.push(value);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        long actualSum = 0;
        Integer value;
        while ((value = stack.pop()) != null) {
            actualSum += value;
        }
        
        assertEquals(expectedSum, actualSum, 
            "Sum of elements in stack should equal sum of pushed values");
    }

    // TODO: 7.2.3 - Test pop
    @Test
    @DisplayName("Concurrent pop test - sum verification")
    public void testConcurrentPop() throws InterruptedException {
        final int NUM_THREADS = 10;
        final LockFreeStack<Integer> stack = new LockFreeStack<>();
        final CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS);
        final Thread[] threads = new Thread[NUM_THREADS];
        final AtomicInteger sumOfPopped = new AtomicInteger(0);
        
        long expectedSum = 0;
        for (int i = 1; i <= NUM_THREADS; i++) {
            stack.push(i);
            expectedSum += i;
        }
        
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await(); 
                    Integer value = stack.pop();
                    if (value != null) {
                        sumOfPopped.addAndGet(value);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        assertEquals(expectedSum, sumOfPopped.get(), 
            "Sum of popped elements should equal sum of original elements");
        
        assertNull(stack.pop(), "Stack should be empty after all pops");
    }


    @Test
    @DisplayName("Concurrent pop from empty stack")
    public void testConcurrentPopEmpty() throws InterruptedException {
        final int NUM_THREADS = 10;
        final LockFreeStack<Integer> stack = new LockFreeStack<>();
        final CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS);
        final Thread[] threads = new Thread[NUM_THREADS];
        final AtomicInteger nullCount = new AtomicInteger(0);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    Integer value = stack.pop();
                    if (value == null) {
                        nullCount.incrementAndGet();
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        assertEquals(NUM_THREADS, nullCount.get());
    }
}
