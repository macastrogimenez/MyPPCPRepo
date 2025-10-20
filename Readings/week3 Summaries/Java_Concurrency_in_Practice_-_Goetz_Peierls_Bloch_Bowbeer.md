[[_7bpartIvAdvancedTopics_2025]]

# [7BPart IV: Advanced Topics - 28BChapter 16. The Java Memory Model 207 |]()

## Abstract
The Java Memory Model specifies when the actions of one thread on memory are guaranteed to be visible to another.The specifics involve ensuring that operations are ordered by a partial ordering called happens-before, which is specified at the level of individual memory and synchronization operations. In the absence of sufficient synchronization, some very strange things can happen when threads access shared data. However, the higher-level rules offered in Chapters 2 and 3, such as @GuardedBy and safe publication, can be used to ensure thread safety without resorting to the low-level details of happens-before.

## Key concepts
#double_checked_locking; #partial_ordering; #java_memory_model; #thread_safety

## Quote
> The text discusses the importance of ensuring happens-before ordering in multithreaded programs to prevent issues like partially constructed objects, and explores techniques like piggybacking, safe publication, and lazy initialization to achieve thread safety.

## Key points
- Throughout this book, we've mostly avoided the low-level details of the Java Memory Model (JMM) and instead focused on higher-level design issues such as safe publication, specification of, and adherence to synchronization policies
- The safe publication techniques described there derive their safety from guarantees provided by the JMM; the risks of improper publication are consequences of the absence of a happens-before ordering between publishing a shared object and accessing it from another thread
- Why did we focus so heavily on @GuardedBy and safe publication, when the JMM already provides us with the more powerful happens-before? Thinking in terms of handing off object ownership and publication fits better into most program designs than thinking in terms of visibility of individual memory writes
- The specifics involve ensuring that operations are ordered by a partial ordering called happens-before, which is specified at the level of individual memory and synchronization operations
- In the absence of sufficient synchronization, some very strange things can happen when threads access shared data


## Summary

### Java Memory Model
The Java Memory Model (JMM) is a specification that defines how multiple threads interact through shared memory.
In a multithreaded environment, the JMM provides minimal guarantees about when writes to variables become visible to other threads.
The JMM is designed to balance predictability and ease of program development with the need for high-performance implementations on various processor architectures.
The JMM defines a partial ordering called "happens-before" on all actions within a program, which includes reads and writes to variables, locks and unlocks of monitors, and starting and joining with threads.
To guarantee that one thread can see the results of another thread's actions, there must be a happens-before relationship between the two actions.
The rules for happens-before include the program order rule, monitor lock rule, volatile variable rule, thread start rule, thread termination rule, interruption rule, finalizer rule, and transitivity.
Synchronization actions, such as lock acquisition and release, and reads and writes of volatile variables, are totally ordered, making it possible to describe happens-before in terms of "subsequent" lock acquisitions and reads of volatile variables.
In the absence of synchronization, compilers, runtime, and hardware can reorder memory operations, leading to unexpected behavior.
The JMM provides a way to reason about the behavior of concurrent programs, ensuring that shared state is accessed correctly and that the program exhibits sequential consistency.
The Java Language Specification requires the JVM to maintain within-thread as-if-serial semantics, allowing for rearrangements that improve computing performance.
However, in a multithreaded environment, excessive inter-thread coordination would slow down the application, so the JVM relies on the program to identify when shared data is being accessed, using synchronization to maintain the illusion of sequentiality.
The JMM is designed to shield Java developers from the differences between memory models across architectures, providing its own memory model and inserting memory barriers at the appropriate places to ensure correct behavior.
By understanding the JMM and using synchronization effectively, developers can write concurrent programs that are both efficient and correct.

### Publication
The Java Memory Model (JMM) provides guarantees for safe publication of objects between threads.
Safe publication ensures that a thread sees the correct state of an object after it has been initialized by another thread.
The JMM guarantees that if a thread A publishes an object X and thread B subsequently retrieves it, B will see X in the state that A left it, provided that the publication happens-before the retrieval.
This happens-before guarantee is essential for ensuring visibility and ordering of memory writes.
The JMM provides several ways to achieve safe publication, including using a BlockingQueue, a shared variable guarded by a lock, or a shared volatile variable.
These methods ensure that the publication happens-before the consuming thread loads a reference to the published object.
Unsafe publication can occur when an object is published without adequate synchronization, allowing another thread to see a partially constructed object.
This can happen when using lazy initialization without proper synchronization.
To avoid unsafe publication, several safe initialization idioms can be used, including making the getInstance method synchronized, using eager initialization, or using the lazy initialization holder class idiom.
The double-checked locking (DCL) antipattern is an example of an incorrect approach to lazy initialization that can lead to unsafe publication.
Similarly, using a shared variable guarded by a lock or a shared volatile variable ensures that reads and writes of that variable are ordered by happens-before.
In summary, safe publication is essential for ensuring that threads see the correct state of objects, and the JMM provides several ways to achieve safe publication, including using BlockingQueues, locks, and volatile variables.
Unsafe publication can be avoided by using safe initialization idioms, such as synchronized getInstance methods, eager initialization, or the lazy initialization holder class idiom.

### Thread Safety
The Double-Checked-Locking (DCL) idiom is no longer effective as an optimization due to changes in the Java Memory Model (JMM) and improvements in JVM startup times.
Making a resource volatile can enable DCL to work, but the lazy initialization holder idiom is easier to understand and offers the same benefits.
Initialization safety guarantees that properly constructed immutable objects can be safely shared across threads without synchronization, and that all threads will see the correct values of final fields.
This applies to objects reachable only through final fields of the object under construction, and writes to final fields are "frozen" when the constructor completes.
However, initialization safety is not enough to ensure thread safety if an object has non-final fields or if its state can change after construction, and synchronization is required to ensure visibility in these cases.
The Java Memory Model specifies when actions of one thread on memory are guaranteed to be visible to another, and higher-level rules such as safe publication can be used to ensure thread safety without resorting to low-level details.


## Contributions
- The Java Memory Model specifies when the actions of one thread on memory are guaranteed to be visible to another.

## Limitations
- The limitations of the JMM are that it does not guarantee sequential consistency, and that synchronization is necessary to ensure that threads see the most up-to-date values for variables. The limitations of the text are that it assumes a basic understanding of multithreaded programming and the Java language.
- The limitations of the techniques discussed include the fragility of piggybacking, the need for proper synchronization in lazy initialization, and the potential pitfalls of techniques like double-checked locking.
- The limitations of initialization safety include the fact that it only applies to objects reachable through final fields, and that it does not provide guarantees for the visibility of non-final fields or values that may change after construction.

## Future work
- The future work in this area includes continuing to develop and refine the JMM to ensure that it remains relevant and effective in the face of evolving processor architectures and programming languages. The future work also includes educating programmers about the importance of synchronization and the JMM in multithreaded programming.
- The future work suggested by the text includes further exploration of techniques for achieving thread safety, such as the use of synchronization primitives and the development of new idioms for lazy initialization.
- The future work in this area may include further research on the Java Memory Model and its applications to thread safety, as well as the development of new techniques and tools for ensuring thread safety in concurrent programming.


## References
[

]

