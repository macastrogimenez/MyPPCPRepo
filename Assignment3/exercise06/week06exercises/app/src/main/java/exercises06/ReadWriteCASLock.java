// For week 6
// raup@itu.dk * 2024-09-22

package exercises06;

import java.util.concurrent.atomic.AtomicReference;

class ReadWriteCASLock implements SimpleRWTryLockInterface {
    private final AtomicReference<Holders> holders = new AtomicReference<>(null);
    
    public boolean readerTryLock() {
        Thread current = Thread.currentThread();
        // We must loop because other threads may be concurrently acquiring or
        // releasing read locks. Each iteration reads the current value of
        // `holders` and, as long as it is either null or a ReaderList, attempts
        // to update it with a new ReaderList that includes the current thread.
        //
        // If `holders` holds a Writer, we fail fast and return false because a
        // writer currently owns the lock and readers cannot acquire it.
        //
        // Otherwise we create a new ReaderList node with the current thread
        // and set its `next` to the previously observed head. We then use a
        // CAS (compareAndSet) to atomically replace the old head with the
        // extended list. If the CAS succeeds we acquired the read lock. If it
        // fails, some other thread changed `holders` concurrently (either by
        // adding/removing readers or by installing a writer) so we retry.
        //
        // This loop therefore implements the required repeated-read-and-CAS
        // behavior: as long as `holders` is null or a ReaderList we attempt to
        // install an extended reader list containing the current thread.
        //
        // Note: the implementation allows the same thread to appear multiple
        // times in the list (reentrant acquires are not prevented) which is
        // acceptable for this exercise.
        while (true) {
            Holders h = holders.get();

            if (h instanceof Writer) {
                return false;
            }

            ReaderList newReaders = new ReaderList(current, (ReaderList) h);
            if (holders.compareAndSet(h, newReaders)) {
                return true;
            }
        }
    }
    
    public void readerUnlock() {
        Thread current = Thread.currentThread();
        // We must loop because other threads may concurrently add or remove
        // reader entries. The required behavior is:
        //  - Repeatedly read `holders` and, as long as:
        //      i)  it is non-null,
        //     ii)  it is a ReaderList, and
        //    iii)  the calling thread is present on that reader list,
        //    create a new ReaderList with the calling thread removed and try
        //    to atomically store that new list into `holders` using CAS.
        //
        // If the CAS succeeds we have successfully released our read lock and
        // can return. If the CAS fails we retry because some other thread
        // changed `holders` concurrently (either by adding/removing readers or
        // installing a writer). If at any point `holders` is null or is not a
        // ReaderList or the current thread is not on the reader list, we
        // throw an exception as required.
        //
        // This method relies on two auxiliary immutable operations on
        // ReaderList: `contains(Thread t)` to check membership and
        // `remove(Thread t)` to produce a new list without the given thread.
        while (true) {
            Holders h = holders.get();

            if (!(h instanceof ReaderList)) {
                // holders is null or a Writer -> current thread cannot hold a
                // read lock, so throwing an exception is correct.
                throw new RuntimeException("Current thread doesnt hold a read lock");
            }

            ReaderList readers = (ReaderList) h;
            if (!readers.contains(current)) {
                // The current thread is not on the reader list -> error.
                throw new RuntimeException("Current thread doesnt hold a read lock");
            }

            // Build a new reader list with the current thread removed.
            ReaderList newReaders = readers.remove(current);

            // Try to atomically replace the observed head with the new list.
            // If successful, we have released the read lock and can return.
            // If unsuccessful, loop and retry with the updated head.
            if (holders.compareAndSet(h, newReaders)) {
                return;
            }
        }
    }
    
    public boolean writerTryLock() {
        Thread current = Thread.currentThread();
        Holders h = holders.get();
        
        if (h != null) { // checks that the lock is unheld
            return false; 
        }
        
        Writer writer = new Writer(current);
        return holders.compareAndSet(null, writer); //it atomically sets holders to a Writer object
        // cause holders is AtomicReference<Holders>
    }
    
    public void writerUnlock() {
        Thread current = Thread.currentThread();
        Holders h = holders.get();
        
        if (!(h instanceof Writer)) {
            throw new RuntimeException("Current thread does not hold the write lock");
        }
        
        Writer writer = (Writer) h;
        if (writer.thread != current) {
            throw new RuntimeException("Current thread does not hold the write lock");
        }
        
        if (!holders.compareAndSet(h, null)) {
            throw new RuntimeException("Failed to release write lock");
        }
    }
    
    private static abstract class Holders { }
    
    private static class ReaderList extends Holders {
        private final Thread thread;
        private final ReaderList next;
        
        public ReaderList(Thread thread, ReaderList next) {
            this.thread = thread;
            this.next = next;
        }
        
        public boolean contains(Thread t) {
            ReaderList current = this;
            while (current != null) {
                if (current.thread == t) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }
        
        public ReaderList remove(Thread t) {
            if (this.thread == t) {
                return this.next;
            }
            
            ReaderList newNext = (next != null) ? next.remove(t) : null;
            return new ReaderList(this.thread, newNext);
        }
    }
    
    private static class Writer extends Holders {
        public final Thread thread;
        
        public Writer(Thread thread) {
            this.thread = thread;
        }
    }
}