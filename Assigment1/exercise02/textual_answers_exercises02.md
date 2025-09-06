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

