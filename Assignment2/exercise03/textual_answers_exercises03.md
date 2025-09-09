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
|  (1)            | int temp = count;                           | variable read and write action  |
|  (2)            | count = temp + 1;                           | variable read and write action  |
| (join(t1))      | t1.join();                                   | synchronization action          |
| (join(t2))      | t2.join();                                   | synchronization action          |
| (3)             | System.out.println("count="+count);          | variable read action            |

### Exercise 3.1.2

Definition:

- Program order rule: Actions a, b are executed by the same thread, and a appears before b in
program order (cf. section 3.3).

Solution: HB sets from the program order rule

The HB sets below are the same for all executions due to the program order. What can vary across executions is the relative order between t1 and t2 actions (there’s no HB relation between them), so the interleaving, and the resulting count is nondeterministic. There’s a data race on count, but the HB relation itself is invariant.

For HB = Happens-Before program order set; m = main thread; t1 = CountingThread t1; t2 = CountingThread t2:

- HB (m/po) = m(init(count)) -> m(init(t1)) -> m(init(t2)) -> m(start(t1)) -> m(start(t2)) -> m(join(t1)) -> m(join(t2)) -> m(3)
- HB (t1/po) = t1(1) -> t1(2)
- HB (t2/po) = t2(1) -> t2(2)

### Exercise 3.1.3

Definition:

- Thread start rule: Action a is a call to start() and b is the first action in the started thread.

- Thread termination rule: Action a is the last action executed by a thread and b is a call to
join(), by any other thread, to wait for the thread executing a.

Solution: HB sets from the thread start and termination rules.

The HB sets below are not the same for all executions due to the program order. As previously stated the relative order between t1 and t2 actions so HB (m/s-t) may vary, since the last action before printing depends on which thread (t1 or t2) dies and joins faster. It should be not the same for the first action since, t1 is started earlier - as per the program structure.

For HB = Happens-Before start-termination set; m = main thread; t1 = CountingThread t1; t2 = CountingThread t2:

- HB (m/s-t) = m(init(count)) -> m(init(t1)) -> ...  -> m(join(t2)) -> m(3) or HB (m/s-t) = m(init(count)) -> m(init(t1)) -> ... -> m(join(t1)) -> m(3)
- HB (t1/s-t) = m(start(t1)) -> t1(1) -> t1(2) -> m(join(t1))
- HB (t2/s-t) = m(start(t2)) -> t2(1) -> t2(2) -> m(join(t2))

### Exercise 3.1.4

The sets of all possible synchronization orders for this program:

- HB (m/sa) = m(start(t1)) -> m(start(t2)) -> m(join(t1)) -> m(join(t2))
- HB (t1/sa) = m(start(t1)) -> m(join(t1))
- HB (t2/sa) = m(start(t2)) -> m(join(t2))

### Exercise 3.1.5

Data Race Definition:

A data race occurs if: Two conflicting accesses (at least one is a write) to the same variable are performed by different threads and they are not ordered by the happens-before (HB) relation.

Conflicting Accesses on count

In our program we have:

- t1(1): int temp = count; → read of count
- t1(2): count = temp + 1; → write to count
- t2(1): int temp = count; → read of count
- t2(2): count = temp + 1; → write to count

Clearly, there are read–write and write–write conflicts on count.

Check Happens-Before Ordering:

From HB, we know:

- t1(1) → t1(2) (program order in t1)
- t2(1) → t2(2) (program order in t2)
- m(start(t1)) → t1(1)
- m(start(t2)) → t2(1)
- t1(2) → m(join(t1))
- t2(2) → m(join(t2))

There is no HB edge between t1 actions and t2 actions. For example, t1(2): write count and t2(1): read count are conflicting and not ordered by HB. Therefore this constitutes a data race on count.

### Exercise 3.1.6
