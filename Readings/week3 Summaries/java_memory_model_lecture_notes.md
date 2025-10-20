[[_JavaMemoryModelPcpp_2025]]

# [The Java Memory Model (for PCPP)]()

## [[]]
## Key concepts
#java_virtual_machine; #programming_language; #claim/java_memory_model; #java_memory_model

## Quote
> The Java memory model ensures that concurrent Java programs behave predictably by defining well-formed executions, which are consistent with program order, happens-before order, and synchronization order, thereby preventing data races and ensuring visibility of variable accesses.

## Key points
- This chapter summarizes the Java memory model and adapts it to the purposes of the PCPP course
- The changes omit details on how the Java memory model must be enforced by implementations of the Java Virtual Machine
- The definitions and reasoning described in this note are a consistent subset of the complete Java memory model1 [^2]
- Programming languages used for concurrent programming strive to precisely define a memory model, as it allows programmers to predict the behavior of their programs and ensure their correctness
- We provide the definitions of Java memory model that we will use in the course as well as examples on how to use them to reason about concurrent Java programs


## Summary

### Java Memory Model
The Java memory model defines the set of valid executions of a concurrent Java program, guaranteeing that the Java Virtual Machine (JVM) and Just-In-Time (JIT) compiler only produce executions allowed by the model.
This guarantee is enforced across all hardware and operating systems supported by the Java runtime environment.
The memory model allows programmers to predict the behavior of their programs by determining the possible values observed when reading a variable.
A memory model is not unique to Java, as many programming languages, such as C/C++, C#, and Go, have their own memory models to ensure the correctness of concurrent programs.
The Java memory model was the first to be completely formalized, making it one of the most complete and mature memory models available.
The Java memory model is necessary because concurrent programs can exhibit unexpected behavior without synchronization primitives.
For example, a program with two threads can produce unexpected results due to compiler and hardware optimizations.
The concept of sequential consistency, introduced by Leslie Lamport, defines the expected behavior of a concurrent program, where operations appear to occur one at a time and preserve program order.
However, enforcing sequential consistency would require disabling compilation and CPU optimizations, which can impact performance.
The Java memory model provides basic definitions, including actions, executions, program order, happens-before order, and synchronization order.
Actions represent the execution of operations by threads, and executions are sequences of actions.
Program order defines the intra-thread execution order, while happens-before order establishes an order between actions of a concurrent program.
Synchronization order characterizes the possible executions of a program, ensuring that synchronization actions occur in a valid order.
These definitions enable programmers to reason about concurrent Java programs and predict their behavior, allowing for the creation of correct and efficient concurrent software.
The Java memory model defines what a valid execution is for a concurrent Java program.
The Java Virtual Machine (JVM) only produces well-formed executions.
The happens-before order ensures visibility between read and write accesses, and it is enforced by the JVM.
Programmers do not need to worry about how the hardware ensures data consistency in the different levels of the memory hierarchy.
To establish a happens-before order, programmers need to establish a happens-before order between variables of interest at the programming language level.
In a concurrent program, actions can be conflicting if they are accesses on the same non-volatile variable and at least one of them is a write access.
A data race occurs when conflicting actions are not ordered by happens-before.
A correctly synchronized program is one whose possible executions are data race free.
The Java memory model uses data race freedom as the notion of correctness for a concurrent Java program.
The Java memory model provides a way to reason about the behavior of concurrent Java programs.
It defines the possible executions of a program and provides a way to determine the visibility of variables.
By using the Java memory model, programmers can write correct concurrent Java programs that are free from data races.
The model provides a way to establish a happens-before order between variables, which ensures visibility and prevents data races.
The model provides a way to reason about the behavior of concurrent programs and to determine the correctness of a program.
It is an important tool for programmers who want to write correct and efficient concurrent Java programs.
The model is used to predict the value of program variables after the execution of a program.
It provides a way to derive the pairs of actions ordered by happens-before in the possible executions of a program.
By using the model, programmers can show that the only possible final values for variables are the expected ones, and that the program is free from data races.

### Correct Synchronization
A program is correctly synchronized if none of its executions contain data races.
To determine if a program is correctly synchronized, we must identify all conflicting actions in the executions of the program and show that they are ordered by happens-before.
The Java memory model ensures that all executions of correctly synchronized programs are sequentially consistent, which means that runtime, compiler, or CPU optimizations cannot produce unexpected executions.
In a correctly synchronized program, the happens-before order sets contain pairs of actions based on the program order rule and the synchronization actions from the thread start rule.
For example, in a program with two threads, the happens-before order sets for each thread and the main thread can be defined, and the transitive closure of these sets gives the happens-before order for all executions of the program.
The use of volatile variables can affect the happens-before order, as actions involving read/write accesses on volatile variables are synchronization actions.
The Java memory model can be used to argue why a program with volatile variables is correctly synchronized, and therefore, only sequentially consistent executions are valid.
The presence of conditional statements can impact the set of action pairs ordered by happens-before, and the evaluation of the predicate in the conditional statement can result in different happens-before order sets.
The happens-before order sets can be defined for each possible evaluation of the predicate, and the program can be shown to be correctly synchronized for each case.
In summary, a program is correctly synchronized if all conflicting actions are ordered by happens-before, and the Java memory model ensures that all executions of correctly synchronized programs are sequentially consistent.
The use of volatile variables and conditional statements can affect the happens-before order, but the program can still be shown to be correctly synchronized using the Java memory model.


## Study subjects

### 3 cases
- Due to the lack of synchronization, the value of (x,y) after the execution of the program can be (1,1), (0,1), (1,0), but also (0,0). ==Most programmers would consider the first three cases as expected behaviors (due to the program interleavings)==. The result (1,1) can be generated by interleavings (1),(3),(2),(4) or (1),(3),(4),(2) or (3),(1),(2),(4) or (3),(1),(4),(2)

### 3 cases
- There are only two nesting of synchronization operations consistent with mutual exclusion either t1 acquires the lock before t2 or vice-versa. ==The first 3 cases correspond to the situation when t1 acquires the lock before t2==. In these cases, it must hold that t1(4) → t2(5), and they must be consistent with program order, i.e., t1(1) → t1(4), t2(5) → t2(8) and m(start(t1)) → m(start(t2))

## Data analysis
- #method/java_program
- #method/java_software

##  Confirmation of earlier findings
- The changes simply omit details on how the Java memory model must be enforced by implementations of the Java Virtual Machine. ==The definitions and reasoning described in this note are a consistent subset of the complete Java memory model1== [^2].

## Contributions
- In summary, the set of happens-before pairs from program order rules is defined as: HB po = HB po m ∪ HB po t1 ∪ HB po t2 .

## Limitations
- The limitations of this chapter are that it only provides a subset of the definitions of the complete Java memory model, and that it omits details on how the Java memory model is enforced by the Java virtual machine.
- The limitations of the study are that it only considers a specific concurrent Java program, and that the analysis is based on the happens-before order, which may not be applicable to all concurrent programs. Another limitation is that the study assumes that the program is correctly synchronized, which may not always be the case.
- The limitations of the text include the complexity of analyzing happens-before orders and conflicting actions, and the need for careful consideration of conditional statements and their impact on happens-before orders.

## Future work
- The future work in this area is to continue studying the Java memory model and its applications in concurrent programming, and to explore new ways to reason about the behavior of concurrent Java programs.
- The future work could involve extending the analysis to other concurrent Java programs, and exploring the use of other tools and techniques for predicting the behavior of concurrent programs. Another area of future work could be to investigate the relationship between the happens-before order and other notions of correctness for concurrent programs.
- The future work suggested by the text includes further exploration of the Java memory model and its applications to concurrent programming, as well as the development of tools and techniques for analyzing and verifying correct synchronization.


## References
[^1]: Leslie Lamport. “How to Make a Multiprocessor Computer That Correctly Executes Multiprocess Programs”. In: IEEE Trans. Computers 28.9 (1979), pp. 690–691.  [OA](https://engine.scholarcy.com/oa_version?query=Lamport%2C%20Leslie%20How%20to%20Make%20a%20Multiprocessor%20Computer%20That%20Correctly%20Executes%20Multiprocess%20Programs%E2%80%9D%201979&author=Lamport&title=How%20to%20Make%20a%20Multiprocessor%20Computer%20That%20Correctly%20Executes%20Multiprocess%20Programs%E2%80%9D&year=1979) [GScholar](https://scholar.google.co.uk/scholar?q=Lamport%2C%20Leslie%20How%20to%20Make%20a%20Multiprocessor%20Computer%20That%20Correctly%20Executes%20Multiprocess%20Programs%E2%80%9D%201979) [Scite](/scite_tallies?query=author%3ALamport%2Ctitle%3AHow%20to%20Make%20a%20Multiprocessor%20Computer%20That%20Correctly%20Executes%20Multiprocess%20Programs%E2%80%9D%2Cyear%3A1979)

[^2]: Jeremy Manson, William W. Pugh, and Sarita V. Adve. “The Java memory model”. In: Proceedings of the 32nd ACM SIGPLAN-SIGACT Symposium on Principles of Programming Languages, POPL’05. ACM, 2005, pp. 378–391.   [OA](https://scholar.google.co.uk/scholar?q=Manson%2C%20Jeremy%20Pugh%2C%20William%20W.%20Adve%2C%20Sarita%20V.%20The%20Java%20memory%20model%E2%80%9D%202005) [GScholar](https://scholar.google.co.uk/scholar?q=Manson%2C%20Jeremy%20Pugh%2C%20William%20W.%20Adve%2C%20Sarita%20V.%20The%20Java%20memory%20model%E2%80%9D%202005) 

