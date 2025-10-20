# Answers Exercise 3

## Exercise 3.1

### Exercise 3.1.1

List of all the possible actions in the executions of the program:

| Step             | Operation                                    | Action Type                     |
|------------------|----------------------------------------------|---------------------------------|
| (init(count))   | count = 0;                                   | variable write action           |
| (init(t1))      | CountingThread t1 = new CountingThread();    | other action                    |
| (init(t2))      | CountingThread t2 = new CountingThread();    | other action                    |
| (start(t1))     | t1.start();                                  | synchronization action          |
| (start(t2))     | t2.start();                                  | synchronization action          |
|  (1)            | int temp = count;                            | variable read and write action  |
|  (2)            | count = temp + 1;                            | variable read and write action  |
| (join(t1))      | t1.join();                                   | synchronization action          |
| (join(t2))      | t2.join();                                   | synchronization action          |
| (3)             | System.out.println("count="+count);          | variable read action            |

### Exercise 3.1.2

Definition:

- Program order rule: Actions a, b are executed by the same thread, and a appears before b in program order (cf. section 3.3).

Solution - HB sets from the program order rule:

The HB sets below are the same for all executions due to the program order. What can vary across executions is the relative order between t1 and t2 actions (there’s no HB relation between them), so the interleaving, and the resulting count is nondeterministic. There’s a data race on count, but the HB relation itself is invariant.

For HB = Happens-Before program order set; m = main thread; t1 = CountingThread t1; t2 = CountingThread t2:

HB (po) = {
  m(init(count)) → m(init(t1)),
  m(init(t1)) → m(init(t2)),
  m(init(t2)) → m(start(t1)),
  m(start(t1)) → m(start(t2)),
  m(start(t2)) → m(join(t1)),
  m(join(t1)) → m(join(t2)),
  m(join(t2)) → m(3),
  t1(1) → t1(2),
  t2(1) → t2(2)
}

### Exercise 3.1.3

Definitions:

- Thread start rule: Action a is a call to start() and b is the first action in the started thread.

- Thread termination rule: Action a is the last action executed by a thread and b is a call to
join(), by any other thread, to wait for the thread executing a.

Solution -  HB sets from the thread start and termination rules:

The HB sets below are not the same for all executions due to the program order. As previously stated the relative order between t1 and t2 actions so HB (m/s-t) may vary, since the last action before printing depends on which thread (t1 or t2) dies and joins faster. It should be not the same for the first action since t1 is started earlier - as per the program structure.

For HB = Happens-Before start-termination set; m = main thread; t1 = CountingThread t1; t2 = CountingThread t2:

- HB (init-term) = {
        m(start(t1)) → t1(1),
        m(start(t2)) → t2(1),
        t1(2) → m(join(t1)),
        t2(2) → m(join(t2))
    }

### Exercise 3.1.4

The sets of all possible synchronization orders for this program:

SO = {
  t1(1) -> t1(2) -> t2(1) -> t2(2),
  t2(1) -> t2(2) -> t1(1) -> t1(2),
  t1(1) -> t2(1) -> t1(2) -> t2(2),
  t2(1) -> t1(1) -> t2(2) -> t1(1),
  t1(1) -> t2(1) -> t2(2) -> t1(2),
  t2(1) -> t1(1) -> t1(2) -> t2(2)
}

### Exercise 3.1.5

Definition:

- Data race: A data race occurs if two threads access the same variable, and all of the following hold:

1. At least one of the accesses is a write.
2. The accesses are not ordered by the happens-before (HB) relation.

If a program has a data race, then its behavior is not guaranteed to be sequentially consistent.

Solution - In our program we have:

- t1(1): int temp = count; -> read of count
- t1(2): count = temp + 1; -> write to count
- t2(1): int temp = count; -> read of count
- t2(2): count = temp + 1; -> write to count

Clearly, there are read–write and write–write conflicts on count.

Check Happens-Before Ordering - From HB, we know:

- t1(1) -> t1(2) (program order in t1)
- t2(1) -> t2(2) (program order in t2)
- m(start(t1)) -> t1(1)
- m(start(t2)) -> t2(1)
- t1(2) -> m(join(t1))
- t2(2) -> m(join(t2))

There is no HB edge between t1 actions and t2 actions. For example, t1(2): write count and t2(1): read count are conflicting and not ordered by HB. Therefore this constitutes a data race on count.

### Exercise 3.1.6

The solution is implemented in the file named FixedCountingThreads.java.

When it comes to HB - program order, the changes would be at the beginning of HB (m/po) since we would also initialize the lock (m(init(l))) and then at the beginning and end of HB (t1/po) and HB (t1/po), where we would add an action for each thread to take the lock l (t1/2(lock())) and release it (t1/2(unlock())) after (2) - former (1) and (3) - former (2)  are executed.

With regards to synchronization orders, we would have to introduce new 4 new actions (in shorthand: t1lock, t1unlock, t2lock and t2unlock), which would give way to two different combinations of synchronization orders, depending on which thread manages to acquire the lock first after initializing.

### Exercise 3.1.7

In our program we have:

- t1(1): l.lock(); -> synchronization action
- t1(2): int temp = count; -> read of count
- t1(3): count = temp + 1; -> write to count
- t1(4): l.unlock(); -> synchronization action
- t2(1): l.lock(); -> synchronization action
- t2(2): int temp = count; -> read of count
- t2(3): count = temp + 1; -> write to count
- t2(4): l.unlock(); -> synchronization action

Clearly, there could be read–write and write–write conflicts on count, however if we check Happens-Before Ordering, we know:

- t1(1) -> t1(4) (program order by transitivity in t1)
- t2(1) -> t2(4) (program order by transitivity in t2)
- m(start(t1)) -> t1(1)
- m(start(t2)) -> t2(1)
- t1(4) -> m(join(t1))
- t2(4) -> m(join(t2))
- t1(4) -> t2(1) or t2(4) -> t1(1) (by monitor rule - depending on which thread acquires the lock first)

Conclusion:

Now, in opposition to our answer to 3.1.5 there is an HB edge between t1 actions and t2 actions (and vice-versa). For example, t1(2): write count and t2(1): read count are now ordered by HB. Therefore there is no data race on count.

### Exercise 3.1.8

Definitions:

- Data race: A data race occurs if two threads access the same variable, and all of the following hold:

1. At least one of the accesses is a write.
2. The accesses are not ordered by the happens-before (HB) relation.

If a program has a data race, then its behavior is not guaranteed to be sequentially consistent.

- Volatile rule: Action a is a write access on a volatile variable, and b is a subsequent read access on the same volatile variable.

Solution:

Due to the addition of the volatile word to count we have the following additions to the HB (m) set:

- t2(2)->t1(1)
- OR t1(2)->t2(1)

As all these actions would be performed on a volatile variable, therefore if we defined count as volatile, all executions of this program would be data race free since the accesses to variable count would be ordered by a HB relation.

## Exercise 3.2

### Exercise 3.2.1

Definitions:

- Conflicting actions: Given an execution e ∈ E, we say that actions a, b ∈e are conflicting iff they are accesses on the same (non-volatile) variable and at least one of them is a write access.

- Correctly synchronized programs: A program is correctly synchronized iff none of its executions contains data races.

- Data race: A data race occurs if two threads access the same variable, and all of the following hold:

1. At least one of the accesses is a write.
2. The accesses are not ordered by the happens-before (HB) relation.

If a program has a data race, then its behavior is not guaranteed to be sequentially consistent.

Solution:

As per JMM, the program is not correctly synchronized because both threads are trying to access the StringSet s to either write or read the string "PCPP" on/from it and there is not HB relation between them. More specifically, in the case of t2 executing before t1, it may be reading from s while t1 acquires the lock to write to it, therefore, the information it will read may no longer be correct, hence  there is still a data race on s.

### Exercise 3.2.2

Add the modifier synchronized to the definition of the method find(...) in the class StringSet.
Show, using the Java memory model, whether the program is correctly synchronized. The notes for 3.2.1
also apply in this exercise.

For HB = Happens-Before set; m = main thread; t1 = Thread t1; t2 = Thread t2

| Step             | Operation                                       |
|------------------|----------------------------------------------   |
| (init(s))        | s = new StringSet();                            |
| (init(t1))       | Thread t1 = new Thread()                        |
| (init(t2))       | Thread t2 = new Thread()                        |
| (start(t1))      | t1.start();                                     |
| (start(t2))      | t2.start();                                     |
|  (1)             | int ret = list.indexOf(s);                      |
|  (2)             | list.add(s);                                    |
|  (3)             | return list.indexOf(s);                         |

Before adding synchronized to the method s.find() we had the following HB relation (as findOrAdd() was already synchronized):

For the synchronization order : m(start(t1)) -> t1(lock()) -> t1(1) -> t1(2) -> t1(unlock()) -> t2(3)

The edge t1(unlock()) -> t2(3) already existed, meaning a HB relation between the read and write from t1 and read from t2.

However, since adding synchronized to the program we have a new HB edge connecting the completion of t2 and unlock() of s, with the start() and run of t1 to read and from s:

- m(start(t2)) -> t2(lock()) -> t2(3) -> t2(unlock()) -> t1(lock()) ...

So, by transitivity we have that t2(lock())->t1(lock()), therefore a HB relation is established also for the interleaving in which t2 would execute first.

## Exercise 3.3

### Exercise 3.3.1

The following actions should have a HB order:

- while(x==0) // reading x
- x=42; // writing x

### Exercise 3.3.2

Definitions:

- A data race occurs if two threads access the same variable, at least one access is a write, and the accesses are not ordered by happens-before (HB).
- The program order rule only orders actions within the same thread.
- The thread start and termination rules only order the start of a thread before its first action, and the last action of a thread before a join.

Solution:

However, in this program there is no synchronization (no lock, no volatile, no explicit HB edge) between the thread writing x=42 and the thread reading x in the loop. Therefore, the write to x and the read of x are not ordered by happens-before in all executions.

Demonstration:

Let m be the thread that writes x=42.
Let t1 be the thread that reads x in while(x==0).
The only HB pairs are those from program order within main and t1, and from t1 start/termination, but there is no HB edge from m's write to t2's read (or vice versa).
As a result, the read and write actions on x are not ordered by HB in all executions.
