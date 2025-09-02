# Answers Exericse 1

## Exercise 1.1

### Exercise 1.1.1

The answers I got from running it 3 times were as below:
19326139, 19326139, 19167248. Neither of them amount to 20000000 because of interleaving. The code from TestLongCounterExperiments is not thread-safe, so there are racing conditions generating the results to be incorrect, since the tasks executed by every thread are not atomic enough, not to be interrupted half-way through (therefore leaving them incomplete) by other Threads.

### Exercise 1.1.2

Yes, I get the expected output: 200. However, this is possible because interleaving is less likely to happen when the number of tasks to run is lower, but it is not guaranteed that it will not happen. Actually it is basically guaranteed that the result will be incorrect if you run it sufficient times.

### Exercise 1.1.3

All three expressions are equally susceptible to interleaving problems when executed inside a while loop with multiple threads.

#### with count += 1

The results are very similar to "count = count + 1" : 19165454, 19412820, 19212987

#### with count++

Very similar: 19411636, 19264517, 19082172

// Explain why your solution is correct, and why no other output is possible.
Note: In your explanation, please use the concepts and vocabulary introduced during the lecture, e.g., critical
sections, interleavings, race conditions, mutual exclusion, etc.

### Exercise 1.1.4

The idea of mutual exclusion is to prevent two or more threads from modifying or reading shared data in a way that causes race conditions or inconsistent states.
No other output is possible because the section of the code that has been edited is the critical section that makes the program as a whole be thread-safe (or not - as it was in its original state).

I changed the count type from long to AtomicLong so that "count++" would be replaced by "count.getAndIncrement()" which should execute in a single step. I also added an instance of a lock to the LongCounter class and used it inside the LongCounter.increment() method so the lock would be acquired by any thread that is using the method increment() and not released until the very last line of the increment() method. Also, using ReentrantLock is particularly safe since it allows the Thread that currently holds the lock to request for it once more if it needs it (as it would be in the case of incrementing the counter which consists of several steps).

Finally, since no interleaving is possible the program returns '20000000' as expected.

### Exercise 1.1.5

Yes, my critical section contains the least number of lines of code since "count++" which executed in multiple steps has been replaced with "count.getAndIncrement()" which executes atomically.

## Exercise 1.2

### Exercise 1.2.1

Done - the name of the file is UnsafeTestDashBarPrinter.java and executes as required.

### Exercise 1.2.2

Possible interleavings would be:

1. Thread 1 prints "-" then Thread 2 prints "-|", then Thread 1 prints "|" resulting in "--||"
2. Thread 1 prints "-" then Thread 2 prints "-|-", then Thread 1 prints "|" resulting in "--|-|"

The problem arises from - and | being printed by different threads without proper synchronization, leading to inconsistent output.

### Exercise 1.2.3

Done - the name of the file is SafeTestDashBarPrinter.java and executes as required.

The program is correct because the critical section of the code is properly synchronized, ensuring that the printing of "-" and "|" is done in a thread-safe manner. This prevents any interleaving issues and guarantees the expected output.

## Exercise 1.3

### Exercise 1.3.1

Done - the name of the file is SafeCounterThreads2Covid.java and executes as required.

### Exercise 1.3.2

The program runs in the following way:

- The counter is an AtomicInteger, which guarantees that each increment (getAndIncrement()) is atomic—no increments are lost, even if two threads try to increment at the same time.
- The ReentrantLock ensures that only one thread at a time can enter the critical section where the counter is incremented and printed. This prevents race conditions and guarantees that the counter never exceeds MAX_PEOPLE_COVID.
- Both threads (turnstile1 and turnstile2) run a loop, each trying to increment the counter up to PEOPLE times, but the lock and atomic counter ensure that the total never exceeds 15000.

The combination of atomic operations and locking guarantees that all increments are counted and the final result is always correct, regardless of thread scheduling or interleaving.

## Exercise 1.4

### Exercise 1.4.1

From Goetz resource utilization seems to be very closely related to the Inherent point from the concurrency note, like exploitation and convenience. However, there is a difference when it comes to the understanding of fairness. Goetz emphasizes the importance of fairness in resource allocation, ensuring that all users and programs have equitable access to the machine's resources, while the concurrency note touches on fairness more as a byproduct of efficient resource utilization and exploitation.
Also, it is particularly interesting the point on the concurrency note that highlights the use of concurrency for enabling several programs to share some resources in a manner where each can act as if they had sole ownership (hiding the fact that they are actually sharing those resources).

### Exercise 1.4.2

#### Inherent (I/O, UI, real-world events):

- iOS apps with multiple UI threads → UI thread handles user input; background thread loads data.

- Web servers → Each HTTP request handled independently.

#### Exploitation (Parallel hardware, distributed computing):

- Java → Parallel computations on multicore CPUs.

- Whatsapp on iOS/ Android → Background threads for message handling, UI updates.

#### Hidden (Illusion of exclusive resources):

- Unix multitasking → Many processes “run” simultaneously via time-slicing.

- Java Virtual Machine (JVM) → Many threads appear to run at once, but OS schedules them on available CPUs.

- Web browsers → Multiple tabs “run” independently while sharing the same system resources.

## Exercise 1.5

### Exercise 1.5.1

I am running MacOS. It is not emulated.

### Exercise 1.5.2

- operating system: MacOS 15.6.1 (24G90)
- number of cores: 10 (4 performance and 6 efficiency)
- size of main memory: 16 GB
- cache architecture (including sizes of the caches): 
    - L1 :192k+128k cache per performance core and a 128k+64k cache per efficiency core.
    - L2: 16 MB cache per performance core and 4 MB cache per efficiency core.
    - L3: no L3 cache

### Exercise 1.5.3

Measured the time using the program SumOneToOnehundred.java which is part of the Exercise01 package.

My computer spent 2542 nanoseconds adding the numbers from 1 to 100.