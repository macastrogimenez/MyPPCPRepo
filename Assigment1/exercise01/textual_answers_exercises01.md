# Answers Exericse 1

## Exercise 1.1

### Exercise 1.1.1

The answers I got from running it 3 times were as below:
19326139, 19326139, 19167248. Neither of them amount to 20000000 because of interleaving. The output is non-deterministic. With this large number of computations, it’s very possible that two threads will want to increment `count` at exactly the same time. As a result, the final `count` will be less than 20 million, because when one thread tries to increase `count` from 1 to 2, the other might also try to do this simultaneously.

### Exercise 1.1.2

Yes, I get the expected output: 200. However, this is possible because interleaving is less likely to happen when the number of tasks to run is lower, but it is not guaranteed that it will not happen. Actually it is basically guaranteed that the result will be incorrect if you run it sufficient times.

### Exercise 1.1.3

`count += 1`, `count = count + 1`, and `count++` (or `++count`) all perform the same underlying sequence:  

1. Read the value of `count`.  
2. Add one.  
3. Write the new value back.  
 
The only difference is in their expresion result (e.g., `count++` returns the old value, `++count` and `count += 1` return the new value).  
In a multithreaded context, they are all equally unsafe because the increment is not atomic — race conditions can occur regardless of which form is used.

### Exercise 1.1.4

The name of the file is TestLongCounterExperiments.java 

The idea of mutual exclusion is to prevent two or more threads from modifying or reading shared data in a way that causes race conditions or inconsistent states.

No other output is possible because the section of the code that has been edited is the critical section that makes the program as a whole be thread-safe (or not - as it was in its original state).

Using ReentrantLock is particularly safe since it allows the Thread that currently holds the lock to request for it once more if it needs it (as it would be in the case of incrementing the counter which consists of several steps).

Finally, since no interleaving is possible the program returns '20000000' as expected.

### Exercise 1.1.5

Yes, my critical section contains the least number of lines of code since "count++" is the only operation that needs to be thread-safe.

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

- The ReentrantLock ensures that only one thread at a time can enter the critical section where the counter is incremented and printed. This prevents race conditions and guarantees that the counter never exceeds MAX_PEOPLE_COVID.
- Both threads (turnstile1 and turnstile2) run a loop, each trying to increment the counter up to PEOPLE, but the lock and counter ensure that the total never exceeds 15000.

Locking guarantees that all increments are counted and the final result is always correct, regardless of thread scheduling or interleaving.

## Exercise 1.4

### Exercise 1.4.1

Goetz highlights fairness as a motivation, which doesn’t really appear in the concurrency note. For him fairness means ensuring programs and users get a fair share of the system’s resources, while the note’s idea of “hidden concurrency” is more about creating the illusion of sole oewnership.

On the other hand, the concurrency note talks about inherent concurrency, focusing on external events and independent input streams—something Goetz doesn’t separate out as its own category.

What they do have in common are the motivations of parallelism (taking advantage of multiple cores) and better resource utilization (keeping the CPU busy instead of sitting idle).

### Exercise 1.4.2

#### Inherent (I/O, UI, real-world events):

- iOS apps with multiple UI threads → UI thread handles user input; background thread loads data.

- Web servers → Each HTTP request handled independently.

- Web browsers → Multiple tabs run independently while sharing the same system resources.

#### Exploitation (Parallel hardware, distributed computing):

- Java → Parallel computations on multicore CPUs.

- Whatsapp on iOS/ Android → Background threads for message handling, UI updates.

- Zoom or Teams video call

#### Hidden (Illusion of exclusive resources):

- Unix multitasking → Many processes “run” simultaneously via time-slicing.

- Java Virtual Machine (JVM) → Many threads appear to run at once, but OS schedules them on available CPUs.

- ⁠Any webapp which interacts with databases

## Exercise 1.5

### Exercise 1.5.1

I am running MacOS. It is not emulated.

### Exercise 1.5.2

Miguel:

- operating system: MacOS 15.6.1 (24G90)
- number of cores: 10 (4 performance and 6 efficiency)
- size of main memory: 16 GB
- cache architecture (including sizes of the caches): 
    - L1 :192k+128k cache per performance core and a 128k+64k cache per efficiency core.
    - L2: 16 MB cache per performance core and 4 MB cache per efficiency core.
    - L3: no L3 cache

Urszula: 

- operating system: MacOS 15.6.1 (24G90)
- number of cores: 10 (4 performance and 6 efficiency)
- size of main memory: 16 GB
- cache architecture (including sizes of the caches): 
    - L1 :192k+128k cache per performance core and a 128k+64k cache per efficiency core.
    - L2: 16 MB cache per performance core and 4 MB cache per efficiency core.
    - L3: no L3 cache

Blanka: 

- operating system: macOS 15.6.1
- number of cores (physical/logical):10, 10
- size of main memory: 16 GB
- cache architecture (including sizes of the caches): 
    - 131072 bytes
    - 65536 bytes 
    - 4194304 bytes

### Exercise 1.5.3

Measured the time using the program SumOneToOnehundred.java which is part of the Exercise01 package.

Miguel and Urszula:
My computer spent 2542 nanoseconds adding the numbers from 1 to 100.

Blanka:
Time spent: 833 nanoseconds
