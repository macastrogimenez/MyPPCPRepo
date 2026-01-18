# Exercise 4.1

## 1
See `BoundedBuffer.java`.


## 2
For the class to be thread-safe, we must consider

**Class state does not escape**
All class fields are declared private and no methods return a reference to the class fields. So, state does not escape.

**Safe publication**
All fields are declared as final. So, we are publishing safely.

**Mutual exclusion**
The `mutex` semaphor ensures mutual exclusion.

## 3
No.

A barrier allows us to halt execution until all threads reach a certain point in the program.

I don't see how "gathering up" all threads at certain points in the BoundedBuffer would allow us to use the class concurrently.

# Exercise 4.2

## 1
See `Person.java`.

## 2
Because it is synchronized via `synchronized(Person.class)`.

## 3
See `PersonDemo.java`.

## 4
No. Although it is arguably less likely that the program is incorrect if we observe it acting correctly many times, it is not enough to prove its correctness.