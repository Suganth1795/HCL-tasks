# Day 10: Memory Management Lab

## 📌 Task Overview
The objective of Day 10 is to explore JVM memory architecture (Stack vs Heap, Garbage Collection dynamics, and object lifecycle pitfalls) through five hands-on empirical experiments:
1. Measuring **`StackOverflowError`** recursion depth under varied thread stack sizes (`-Xss256k`, `-Xss1m`, default).
2. Forcing a **`java.lang.OutOfMemoryError: Java heap space`** under constrained heap limits (`-Xmx64m`).
3. Benchmarking **`String +=` concatenation** against **`StringBuilder.append()`** and analyzing object allocation churn.
4. Inspecting **Garbage Collection (GC) logs** (`-Xlog:gc`) to observe Young generation evacuation pauses and Old generation promotions.
5. Diagnosing the **Integer Cache (`[-128, 127]`) reference equality (`==`) pitfall**.

---

## 📊 Experimental Results Table

| Experiment | JVM Parameter / Setting | Sample / Scale | Observed Outcome | Architectural Explanation |
| :--- | :--- | :--- | :--- | :--- |
| **1. Stack Overflow Depth** | `-Xss256k` | Unbounded recursion | **2,714 calls** | Constrained stack per thread allocates fewer stack frames before exhausting available thread stack memory. |
| **1. Stack Overflow Depth** | `-Xss1m` | Unbounded recursion | **16,347 calls** | A 4x increase in stack allocation allows approximately 6x deeper stack frame invocation. |
| **1. Stack Overflow Depth** | Default (~2MB) | Unbounded recursion | **27,879 calls** | Default 64-bit JVM thread stack allocates the highest frame capacity for local variable tables and operand stacks. |
| **2. Heap OutOfMemoryError** | `-Xmx64m` | 1 MB byte arrays | **OOM at 31 MB** | Configured heap of 64MB holds ~31MB of live humongous byte arrays before G1 GC exhausted all evacuation regions and threw `OutOfMemoryError: Java heap space`. |
| **3. String vs StringBuilder** | Default heap | 40,000 iterations | **String +=: 306 ms<br>StringBuilder: 6 ms<br>(51.0x faster)** | `String +=` creates 40,000 immutable intermediate `String` objects, causing high heap churn and GC pressure. `StringBuilder` reuses an expandable contiguous array buffer. |
| **4. GC Activity Logging** | `-Xlog:gc` | High churn workload | **Recorded 45 GC events** | Logged Young Generation evacuation pauses (`Pause Young`), humongous allocation markers, and compaction pauses. |
| **5. Integer Cache Pitfall** | `Integer.valueOf()` | `100` vs `200` | **`100 == 100` $\rightarrow$ `true`<br>`200 == 200` $\rightarrow$ `false`** | Values between -128 and 127 point to identical pre-allocated flyweight cache instances. Values $\ge 128$ instantiate distinct heap objects, failing reference equality `==`. |

---

## 🔬 In-Depth Analysis of All 5 Experiments (In Own Words)

### 1. StackOverflowError Depth with Different `-Xss` Flags
- **Concept**: In Java, every thread has its own call stack dedicated to executing methods. Each method invocation pushes a new **Stack Frame** onto this stack containing the method's local variables, parameters, return address, and operand stack.
- **Observations**:
  - When `-Xss256k` was configured, recursion terminated early at **2,714 frames**.
  - When `-Xss1m` was configured, recursion reached **16,347 frames**.
  - Without flags (default stack), recursion reached **27,879 frames**.
- **Takeaway**: `-Xss` controls memory per thread stack, not heap size. Increasing `-Xss` enables deeper recursion but lowers the total number of concurrent threads a JVM can spawn before exhausting native OS memory.

---

### 2. OutOfMemoryError with Constrained Heap (`-Xmx64m`)
- **Concept**: The Java Heap is shared across all threads and holds object instances and arrays. When new allocations exceed the heap capacity defined by `-Xmx` and Garbage Collection cannot reclaim sufficient memory, the JVM terminates with `java.lang.OutOfMemoryError: Java heap space`.
- **Observations**: With `-Xmx64m`, the loop allocated 1 MB byte chunks continuously. The JVM threw `OutOfMemoryError` after allocating **31 chunks (31 MB)**. The remaining ~33 MB was occupied by JVM internal classes, metadata, thread stacks, and G1 GC region overhead.
- **Takeaway**: Heap limits must be sized according to peak concurrent traffic. Retaining references in static collections or unclosed data structures guarantees eventual heap exhaustion regardless of total physical RAM.

---

### 3. `String +=` Concatenation vs `StringBuilder` Timing
- **Concept**: Java `String` instances are immutable. Executing `str += "A"` inside a loop creates a new `String` object on the heap on every single iteration, copying all previous characters into the new object ($O(N^2)$ memory copying overhead) and abandoning the old instance as garbage.
- **Observations**: Over 40,000 iterations:
  - `String +=`: **306 ms** and generated tens of thousands of discarded intermediate objects.
  - `StringBuilder`: **6 ms** (51x faster), mutating its internal buffer in-place with amortized $O(1)$ appends.
- **Takeaway**: Never use `String +=` inside tight loops or streaming algorithms. Always use `StringBuilder` or pre-sized buffers.

---

### 4. Garbage Collection Activity Logging (`-Xlog:gc`)
- **Concept**: Java 9+ introduced unified JVM logging via `-Xlog`. Running with `-Xlog:gc` prints live diagnostics whenever the Garbage Collector triggers pauses or memory reclamation.
- **Observations**:
  ```text
  [0.444s][info][gc] GC(14) Pause Young (Normal) (G1 Evacuation Pause) 20M->0M(36M) 0.952ms
  [0.715s][info][gc] GC(42) Pause Young (Concurrent Start) (G1 Humongous Allocation) 33M->6M(54M) 1.442ms
  ```
  - The G1 collector triggered frequent **Young Evacuation Pauses**, collecting short-lived strings in less than 1 millisecond.
  - Byte arrays exceeding half of a G1 region size were logged as **Humongous Allocations** directly bypassing Eden and landing in Old Generation regions.
- **Takeaway**: GC logging provides visibility into application latency bottlenecks and helps differentiate between healthy short-lived object churn and dangerous heap leakage.

---

### 5. Integer Cache `==` Pitfall
- **Concept**: To conserve memory, the Java Language Specification mandates caching for `Byte`, `Short`, `Integer`, and `Long` within the range of `[-128, 127]`. When autoboxing integer literals (e.g. `Integer a = 100`), the compiler calls `Integer.valueOf(100)`, returning a shared singleton instance from `IntegerCache.cache`.
- **Observations**:
  - `Integer a = 100; Integer b = 100; a == b` is **`true`** because both references hold the identical memory address (`identityHashCode = 1915910607`).
  - `Integer c = 200; Integer d = 200; c == d` is **`false`** because 200 falls outside the cache, forcing the JVM to instantiate two distinct heap objects (`identityHashCode = 284720968` vs `189568618`).
- **Takeaway**: Never use `==` to compare boxed primitives or object wrappers. Always use `.equals()` or unbox to primitive types (`int`) to evaluate numerical equivalence safely.

---

## 🏗️ Project Structure
```
day-10-memory-management/
├── src/
│   └── com/
│       └── hcl/
│           └── memory/
│               ├── StackOverflowExperiment.java # Depth tester with -Xss
│               ├── HeapOOMExperiment.java       # Heap exhauster with -Xmx64m
│               ├── StringConcatBenchmark.java   # String vs StringBuilder timing
│               ├── IntegerCachePitfall.java     # Autoboxing cache demonstration
│               └── MemoryLab.java               # Master runner & GC workload orchestrator
└── README.md
```

---

## 🚀 How to Compile & Run

```bash
# 1. Compile all classes
javac -d bin src/com/hcl/memory/*.java

# 2. Run Stack Overflow tests with custom stack sizes
java -Xss256k -cp bin com.hcl.memory.StackOverflowExperiment
java -Xss1m -cp bin com.hcl.memory.StackOverflowExperiment

# 3. Run Heap OOM test with 64MB constraint
java -Xmx64m -cp bin com.hcl.memory.HeapOOMExperiment

# 4. Run full Master MemoryLab with GC logging enabled
java -Xmx64m -Xlog:gc -cp bin com.hcl.memory.MemoryLab
```

---

## 📊 Sample Output Log

```text
========================================================================
             DAILY TASK 10: MEMORY MANAGEMENT LAB                       
========================================================================

--- EXPERIMENT 1: StackOverflowError DEPTH ---
  Recursion depth reached before StackOverflowError: 27,879

--- EXPERIMENT 2: OutOfMemoryError WITH CONSTRAINED HEAP ---
[0.343s][info][gc] GC(0) Pause Young (Concurrent Start) (G1 Humongous Allocation) 30M->29M(64M) 16.113ms
[0.377s][info][gc] GC(6) Pause Full (G1 Compaction Pause) 62M->62M(64M) 6.253ms
  Total chunks allocated before OOM: 31 MB (java.lang.OutOfMemoryError: Java heap space)

--- EXPERIMENT 3: String += vs StringBuilder BENCHMARK ---
  String += elapsed time  : 306 ms
  StringBuilder time      : 6 ms
  StringBuilder speedup   : 51.0x FASTER

--- EXPERIMENT 4: GC LOGGING WORKLOAD (Triggers Minor & Major GC) ---
  Generating object churn to trigger GC cycles under -Xlog:gc...
[0.715s][info][gc] GC(42) Pause Young (Concurrent Start) (G1 Humongous Allocation) 33M->6M(54M) 1.442ms
  Completed GC workload generation: 50 transient allocations, 10 objects retained in Old Gen.

===============================================================
   EXPERIMENT 5: INTEGER CACHE '==' EQUALITY PITFALL          
===============================================================
Test Case 1: Values inside Integer Cache (100):
  Integer a = 100; Integer b = 100;
  Identity Comparison (a == b)    : true  (Points to SAME cached object)
  Value Comparison (a.equals(b))  : true  (Values are equal)

Test Case 2: Values outside Integer Cache (200):
  Integer c = 200; Integer d = 200;
  Identity Comparison (c == d)    : false  (PITFALL! Separate heap objects)
  Value Comparison (c.equals(d))  : true  (Values are equal)

========================================================================================================
                         DAY 10: MEMORY MANAGEMENT LAB EXPERIMENTAL RESULTS                             
========================================================================================================
Experiment                   | JVM Parameter / Metric    | Observed Outcome                             
--------------------------------------------------------------------------------------------------------
1. Stack Overflow Depth      | Current -Xss              | 27,879 recursive stack frames before error   
2. Heap OutOfMemoryError     | Constrained -Xmx          | Exhausted at 31 MB (java.lang.OutOfMemoryError: Java heap space)
3. String += vs StringBuilder | 40,000 iterations         | String +=: 306 ms vs StringBuilder: 6 ms (51.0x faster)
4. GC Activity Logging       | -Xlog:gc                  | Captured Young/Old Gen GC cycles & pause durations
5. Integer Cache Pitfall     | [-128, 127] Boundary      | 100==100 is TRUE (cached); 200==200 is FALSE (heap ref mismatch)
========================================================================================================
```
