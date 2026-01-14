# Assignment 4

## Exercise 9

### Exercise 9.1

#### Exercise 9.1 – Thread Timing Measurements

The text files with the results are "resultsMark6.txt" and "resultsMark7.txt" 

##### Mark6 Measurements

When running the original code using **Mark6**, we observed that the results varied significantly, especially for the first few iteration counts ( 2, 4, 8).  
The reported times fluctuated by large margins across repetitions, and the standard deviations were often as large as or larger than the mean.

This variation is normal because:

- The JIT (Just-In-Time) compiler optimizes code at runtime, so early runs include compilation overhead.  
- Garbage collection and background OS activity can interfere with timing for very short code segments.  
- Thread scheduling introduces additional noise when measuring thread creation and execution.  

After a sufficient number of iterations (beyond 8192), the values stabilized and became more consistent.  
Overall, Mark6 provides useful insight into the warm-up behavior and variability of microbenchmarks,  
but it seems too noisy for reporting stable, final performance numbers.

##### Mark7 Measurements

After replacing all calls to `Benchmark.Mark6` with `Benchmark.Mark7`,  
the program reported only the final steady-state measurement for each case.

##### System Information

 OS:   Windows 11; 10.0; amd64
 JVM:  Eclipse Adoptium; 17.0.12
 CPU:  12th Gen Intel(R) Core(TM) i7-1260P, 2100 Mhz, 12 Core(s), 16 Logical Processor(s)


Mark 7 measurements
Point creation               23.9 ns
Thread's work             4,437.7 ns
Thread create                434.2 ns
Thread create start       175,177.5 ns
Thread create start join    94,219.6 ns
Uncontended lock              16.6 ns

##### Reflection

Overall, the measured results are plausible and follow the same trends as in the lecture slides:

- Object creation is fast (~20–30 ns), matching the lecture’s ~30 ns.  
- Thread creation itself is cheap (hundreds of nanoseconds).  
- Starting and joining threads are much more expensive (tens or hundreds of microseconds).  
  Most of this cost comes from OS-level thread scheduling and stack initialization.
- The uncontended lock result (~16 ns) matches typical synchronized block costs.

The only notable deviation is the thread create + start time, which in my system (~175 µs) is more than twice the lecture’s ~72 µs.  
This difference is probably because of:
- OS differences (I use Windows).
- Background load.
- Slight JVM or hardware architecture variations.

#### 9.1.3. Estimating the Cost of Thread Creation


1. Observation from Mark7 results:

 Thread create                434.2 ns       
 Thread create + start        175,177.5 ns   

2. Cost decomposition:

- Thread creation (`new Thread(...)`) = 434 ns  
  This is the cost of allocating the thread object and initializing its fields.

- Thread start (`t.start()`) adds most of the remaining cost:  
  175,177.5 ns − 434 ns ≈ 174,743 ns (~0.175 ms)  
  This accounts for JVM and OS overhead:
  - Allocating the thread stack  
  - Registering with the OS scheduler  
  - Context switching and thread startup routines

##### Estimated Thread Creation Cost on My System

 Thread object creation   ~0.43 µs       
 Thread start             ~174.7 µs      
 Total create + start     ~175 µs        

Observations:
- Creating and starting a thread is expensive compared to lightweight operations (object creation ~24 ns, uncontended lock ~16 ns).  
- The majority of the cost comes from starting the thread, not from constructing the object.  

---

## Exercise 9.2

OS: Mac OS X; 26.0.1; aarch64
JVM: Homebrew; 17.0.16
CPU: null; 10 "cores"

non-volatile inc 0.5 ns 0.03 536870912
volatile inc 1.7 ns 0.01 268435456

OS: Mac OS X; 26.0.1; aarch64
JVM: Homebrew; 17.0.16
CPU: null; 10 "cores"

non-volatile inc 0.5 ns 0.02 536870912
volatile inc 1.7 ns 0.05 268435456

OS: Mac OS X; 26.0.1; aarch64
JVM: Homebrew; 17.0.16
CPU: null; 10 "cores"

non-volatile inc 0.5 ns 0.00 536870912
volatile inc 1.7 ns 0.01 268435456

Are they plausible?
Yes. Volatile is 3.4x slower because it forces memory synchronization on every write,
preventing CPU caching and compiler optimizations.
Non-volatile variables can be cached in registers and heavily optimized by the JIT compiler.

Any surprises?
Only 3.4x slower, whereas on x86 architectures volatile is typically 10-20x slower.
Extremely fast, both operations complete in under 2ns

## Exercise 9.3

### Exercise 9.3.1 and 9.3.2

The test results are saved on PerformanceTestCountPrimesThreads.txt in the in /exercise09 folder.

Best measured time ~1.33 ms at 4 threads and degrading after ~4–8 threads, with clear slowdown when threadCount reaches 30–32 (oversubscription + contention). My laptop is aarch64 macOS with 10 cores. It has a mix of "performance" (P) and "efficiency" (E) cores (8P+2E) so the 10 reported cores are not identical.

Since I have few tabs open in VSC it is possible that there are only 4 high‑performance cores fully available, therefore, this may explain why we see peak throughput around that number rather than the full 10. When threadCount exceeds the number of fast hardware contexts (or when threads outnumber available hardware), the OS must time-slice threads. This increases context-switch overhead and cache miss rate (as Peter S. explained during his lecture in the Applied Algorithms course), hurting throughput.

The fact that many thread counts between 2..29 show similar good results (256 iterations) suggests moderate scaling up to some point - maybe as a consequence of the extra cost of creating and running yet another Thread; the sudden worsening above ~28–30 threads is likely a consequence of context switching.

The presence of 256 vs 128 counts is the benchmark deciding how many iterations it needs to reach its timing threshold, and it flips as measured throughput changes.

### Exercise 9.4.2

Occurences of ipsum: 1430

### Exercise 9.4.3

WordSearch 6314141.1 ns (runtime)   31749.71 (standard deviation)   64(iterations)

### Exercise 9.4.5

The test results are saved on PerformanceTestTimeSearch.txt in the in /exercise09 folder.

I measured the sequential run at about 6.31 ms and saw the parallel version drop to roughly 2.17 ms (about 3x speedup); Mark7’s iteration count changed from 64 to 128 for the faster runs because the harness adapts to reach its timing threshold. This shows the per-line string processing dominates the cost, so splitting the lines across threads yields real speedup and the shared counter does not seem to bottleneck the workload here — hence good scaling up toward the machine’s ~10 logical processors.

That contrasts with the benchmark on exercise 9.3, where counter contention and the difference performance of the cores seems to have pushed the optimum down to a few threads. Also, at no point does the Parallel counter reach running times anywhere close to those of the Sequential execution - unlike in exercise 9.3.
