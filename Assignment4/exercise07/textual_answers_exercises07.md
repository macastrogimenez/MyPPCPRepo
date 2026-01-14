# Assignment 4

## Exercise 7.1

### 7.1.1

Yes, this execution is sequentially consistent. The order:
`<q.enq(x), q.enq(y), q.deq(x)>`

### 7.1.2

Its not linearizable, because before ` q.deq(x)` there should be `q.enq(x)`, and in the provided configuration it is not possible, because ` q.deq(x)` ends before `q.enq(x)` begins. So the reason is that it does not satisfy the real time ordering. (Its impossible to define linearization points within each method call, which map to
sequential execution that satisfy the specification of the object)

### 7.1.3

Yes, it is linearizable because we can provide linearization points within each method call, which map to sequential execution that satisfy the specification of the object.
We can for example choose point at the very beggining of method `q.enq(x)` call, and then choose a point at the end of method `q.deq(x)` call (importatnt part is to choose the point that is later than the point we have chosen for enqueue). So the linearization is:
`<q.enq(x), q.deq(x)>`

### 7.1.4

Its not linearizable, because its not sequentally consistant - There is no option to order these method calls so that program order is preserved and the execution satisfies the specification of the object. Possible options that would keep the real time ordering are:

```
<q.enq(x), q.deq(y), q.enq(y)>
<q.enq(x), q.enq(y), q.deq(y)>
<q.deq(y), q.enq(x), q.enq(y)>
```

But because of specification of FIFO queue, none of them is correct, because x is always enqueued first, so it has to get off the queue first (before q.deq(y) is called).

# Exercise 7.2

## Exercise 7.2.1

```java
class LockFreeStack<T> {
AtomicReference<Node<T>> top = new AtomicReference<Node<T>>();

    public void push(T value) {
        Node<T> newHead = new Node<>(value);
        Node<T> oldHead;
        do {
            oldHead = top.get();             // P1
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead)); // P2
    }

    public T pop() {
        Node<T> newHead;
        Node<T> oldHead;
        do {
            oldHead = top.get();             // R1
            if (oldHead == null)             // R2
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead)); // R3
        return oldHead.value;
    }

}
```

Push
Linearization point: P2 - successful compareAndSet(oldHead, newHead)
When P2 succeeds, the element becomes atomically visible on the stack.

Correctness: if two threads push concurrently, only one succeeds at P2, the other retries.
If a thread pushes after another updated top, the CAS fails and it retries.
Every successful CAS adds exactly one element in valid LIFO order.

Pop
Linearization points: R1 - when oldHead == null (empty stack, returns null)
R3 - successful compareAndSet(oldHead, newHead) (element removed)

Correctness: if two threads pop concurrently from non-empty stack,
only one succeeds at R3, the other retries.
If a thread pops after another removed top, the CAS fails and it retries.
If popping from empty stack, R1 observes null and returns safely.
Every successful CAS removes exactly one element in valid LIFO order.

Overall Correctness
Each operation has one atomic linearization point (successful CAS or empty-stack read). The order of these points defines a sequential history consistent with LIFO semantics. The Treiber Stack is linearizable

## Exercise 7.2.2 - 7.2.3

See TestLockFreeStack.java

## Exercise 7.2.4

No, the tests dont cover all linearization points.
The push test covers the successful compareAndSet(oldHead, newHead) in push(), which is the only linearization point for that method.
The pop test covers the successful compareAndSet(oldHead, newHead) in pop(), the linearization point where an element is actually removed from the stack.

However, the pop() method also has another linearization point:
when oldHead == null, the stack is empty and the method immediately returns null.
This case is not covered by the previous tests because they all start with a non-empty stack.

# Exercise 7.3

```java
public boolean writerTryLock() {
Thread current = Thread.currentThread();
Holders h = holders.get();

    if (h != null) {
        return false;
    }

    Writer writer = new Writer(current);
    return holders.compareAndSet(null, writer);

}
```

This method performs only a single get() operation and at most one compareAndSet(). There are no loops or retries involved, so because of that the number of steps it takes is always bounded, even if other threads are running at the same time. Thus, the thread will always finish quickly, returning either true or false without waiting on anyone else. Therefore, this operation is wait-free, since it completes in a fixed number of steps regardless of interference from other threads.

```java
public void writerUnlock() {
Thread current = Thread.currentThread();
Holders h = holders.get();

    if (!(h instanceof Writer)) { ... }
    Writer writer = (Writer) h;
    if (writer.thread != current) { ... }

    if (!holders.compareAndSet(h, null)) {
        throw new RuntimeException("Failed to release write lock");
    }

}
```

This method carries out only a small, fixed sequence of actions, including a single compareAndSet() operation, and it doesn’t use any retry loops. Because the number of steps is always bounded, the method will complete in a predictable amount of time regardless of what other threads are doing. As a result, it is wait-free, since every call finishes in a finite, fixed number of steps.

```java
public boolean readerTryLock() {
Thread current = Thread.currentThread();
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
```

This method uses a while(true) retry loop with CAS operations. When multiple readers compete the CAS can fail due to concurrent modifications, forcing retries. But at least one reader's CAS will succeed in each iteration, so some thread always makes progress even under contention. This fits the lock-free definition: "executing the method guarantees that some method call (including concurrent) finishes in a finite number of steps." It's not wait-free because individual threads might retry a bunch, but it's better than obstruction-free since progress happens even when threads are interfering with each other.

```java
public void readerUnlock() {
    Thread current = Thread.currentThread();
    while (true) {
        Holders h = holders.get();
        if (!(h instanceof ReaderList)) { ... }
        ReaderList readers = (ReaderList) h;
        if (!readers.contains(current)) { ... }
        ReaderList newReaders = readers.remove(current);
        if (holders.compareAndSet(h, newReaders)) {
            return;
        }
    }
}
```

This method also uses a while(true) retry loop with CAS. When multiple readers try to unlock at the same time, their CAS operations might fail and need retries. But at least one reader's CAS succeeds in each round, so the system keeps making progress despite the contention. It's not wait-free (individual threads could be stuck retrying for a while) but it's more than obstruction-free because progress happens even with interference.
