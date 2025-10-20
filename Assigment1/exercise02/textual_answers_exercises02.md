# Answers Exercise 2

## Exercise 2.1

### Exercise 2.1.1

Done - the file is ReadersWriters.java

### Exercise 2.1.2

No, the solution is not fair. If there is a continuous stream of readers, a writer may starve forever.
A new fair solution is implemented in FairReadersWriters.java. By setting writer = true before waiting for readers to finish, our method blocks both new readers and writers, giving the waiting writer a fair chance to proceed as soon as all current readers are done. This prevents writer starvation and balances access between readers and writers.

### Exercise 2.1.3

Our monitor implementation does not contain any condition varibles because Java's intrinsic monitor mechanism (using synchronized methods and wait/notify) inherently manages condition variables. Each synchronized method acts as a critical section, and the wait() and notify()/notifyAll() methods allow threads to wait for certain conditions to be met and to signal other threads when those conditions change. This built-in mechanism simplifies the implementation by eliminating the need for explicit condition variable management.

## Exercise 2.2

### Exercise 2.2.1

Yes, the program will loop forever because thread t does not terminate since it is unable to see the change made by the main thread to the variable mi, as this variable has not been set to volatile, therefore there is no visibility of the changes made to it.

### Exercise 2.2.2

Done - the name of the file is FixedTestMutableInteger.java. Implemented synchronized in the get method of MutableInteger, to ensure visibility. It is necessary to add synchronized() to the set() method so that the new value is set in main memory, not only in cache.

### Exercise 2.2.3

Another option for the Thread t would to terminate if it was not defined as synchronized would be to declare the field value of MutableInteger as volatile, so any read operations involving it would need to be checked directly from main memory. However, if we were to remove synchronized and not add any alternative that would ensure visibility, Thread t would never terminate.

### Exercise 2.2.4

As explained in point 2.2.3, in this case the Thread would finish because using volatile on value would mean that any read or write operations done on this variable would have to be performed directly on main memory, which would slow down the program but would ensure visibility - even though not mutual exclusion.

## Exercise 2.3

### Exercise 2.3.1 and 2.3.2

The results we get are the following:

- Sum is 1311683.000000 and should be 2000000.000000
- Sum is 1271500.000000 and should be 2000000.000000
- Sum is 1133633.000000 and should be 2000000.000000
- Sum is 1256599.000000 and should be 2000000.000000

The answer is yes: there are race conditions - both threads t1 and t2 compete for access to the class (static) value of the field sum from the Mystery class, which causes the result to be an unpredictable outcome of the interleaving of both threads. Even though the methods that both threads are using are both synchronized (therefore, should be thread-safe) since they are both using different methods which due to the keyword static on addStatic() use different monitors. addStatic() uses the Mystery class as a monitor while addInstance() uses the object m as a monitor, therefore, they are not synchronizing correctly. 
To sum up, even if the methods implemented are "thread-safe", the monitor these use is critical, if two threads don't share a monitor, they won't be mutually exclusive.

### Exercise 2.3.3

Done - implemented in the file SafeTestLocking.java

This solution does not have race conditions because both Threads share the same monitor, therefore, one would not be able to acquire a lock if it is being used by the other. We decided to use only addInstance(), since we think that generally it is a better practice to increment the value of the field of an object, than the value of its Class, however, it could have been done the other way round. If both Threads were using addStatic() the result would also be correct since they are sharing monitor (the Mistery class itself in this case).

### Exercise 2.3.4

The use of 'synchronized' in sum is unnecessary since before reaching the line where sum() is executed "try { t1.join(); t2.join(); } catch (InterruptedException exn) { }" is executed, which waits for the threads to die, meaning that there are no race conditions to guard against, since only the main thread is left running the program.
