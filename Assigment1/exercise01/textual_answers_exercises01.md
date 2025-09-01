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
No other output is possible because the section of the code that has been edited is that part of it which 
makes use of shared memory (reading and/or writing to it), therefore, it is the critical section that makes
the program as a whole be thread-safe or not.
Added an instance of a lock to the LongCounter class and used it inside the LongCounter.increment() method so
the lock would be acquired by any thread that is using the method increment()

### Exercise 1.1.5
