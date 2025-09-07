# Answers Exercise 2

## Exercise 2.1

### Exercise 2.1.1

Done - the file is ReadersWriters.java

### Exercise 2.1.2

No, the solution is not fair. If there is a continuous stream of readers, a writer may starve forever.
A new fair solution is implemented in FairReadersWriters.java. By setting writer = true before waiting for readers to finish, my method blocks both new readers and writers, giving the waiting writer a fair chance to proceed as soon as all current readers are done. This prevents writer starvation and balances access between readers and writers.

### Exercise 2.1.3

My monitor implementation does not contain any condition varibles because Java's intrinsic monitor mechanism (using synchronized methods and wait/notify) inherently manages condition variables. Each synchronized method acts as a critical section, and the wait() and notify()/notifyAll() methods allow threads to wait for certain conditions to be met and to signal other threads when those conditions change. This built-in mechanism simplifies the implementation by eliminating the need for explicit condition variable management.

## Exercise 2.2

### Exercise 2.2.1

Yes, the program will loop forever because thread t does not terminate since it is unable to see the change made by the main thread to the variable mi, as this variable has not been set to volatile, therefore there is no visibility of the changes made to it.

### Exercise 2.2.2

Done - the name of the file is FixedTestMutableInteger.java. Implemented synchronized in the get method of MutableInteger, to ensure visibility. It is not necessary to add it to set since MutableInteger is initialized as final.

### Exercise 2.2.3

Another option for the Thread t would to terminate if it was not defined as synchronized would be to declare the field value of MutableInteger as volatile, so any read operations involving it would need to be checked directly from main memory. However, if we were to remove synchronized and not add any alternative that would ensure visibility, Thread t would never terminate.

### Exercise 2.2.4

As explained in point 2.2.3, in this case the Thread would finish because using volatile on value would mean that any read or write operations done on this variable would have to be performed directly on main memory, which would slow down the program but would ensure visibility - even though not mutual exclusion.