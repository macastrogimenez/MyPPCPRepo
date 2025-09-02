# PRACTICAL PARALLEL AND CONCURRENT PROGRAMMING

Repo for PPCP exercises, assignments and thoughts.

## EXERCISE 1 - 01/09/2025

About:

- Race Conditions & Interleavings: Experiment with multiple threads incrementing/decrementing shared counters to observe race conditions, incorrect results, and learn about critical sections and mutual exclusion using ReentrantLock.

- Synchronization & Correctness: Practice ensuring correct program outputs by minimizing critical sections, enforcing locks only where necessary, and analyzing interleavings that break correctness.

- Fairness & Concurrency Motivations: Compare concurrency motivations in Goetz and the Concurrency Note; find real-life examples of systems for each concurrency motivation.

- Practical Concurrency Issues: Handle visibility problems, race conditions, and fairness in shared resources using locking mechanisms and proper synchronization.

- Hardware & Timing Awareness: Collect information about CPU cores, caches, and measure program runtime to connect concurrency concepts with hardware performance realities.

## EXERCISE 2 - 02/09/2025

About:

- Readers-Writers Problem: Implement monitors using synchronized and later ReentrantLock with condition variables to ensure readers/writers access shared resources safely and fairly, avoiding starvation and deadlocks.

- Visibility Issues: Work with a MutableInteger example to understand how missing synchronization or volatile can cause threads to miss updates (visibility problems).

- Locks and Synchronization: Explore the difference between intrinsic locks (synchronized) and explicit locks (ReentrantLock) and when to use each.

- Race Conditions in Synchronized Methods: Investigate why race conditions can still occur even if methods are marked synchronized (due to static vs instance method locking differences).

- Safe Fixes: Modify implementations to eliminate race conditions, ensure visibility, and guarantee thread termination in concurrent programs.