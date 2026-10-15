# Day 7: Collections Framework — Part 1

## 📌 Task Overview
The objective of Day 7 is to implement a **Library Waitlist & Reservation System** leveraging specialized data structures from the Java Collections Framework (`ArrayList`, `LinkedHashSet`, `ArrayDeque`, `PriorityQueue`), demonstrate safe collection mutation with `Iterator.remove()`, and conduct an empirical **microbenchmark comparing `ArrayList` vs `LinkedList`**.

---

## 🎯 Collection Choice Rationale & Architectural Decisions

| Collection | Role in System | Algorithmic Complexity | Justification & Trade-offs |
| :--- | :--- | :--- | :--- |
| **`ArrayList<Book>`** | Book Catalogue | Read: $O(1)$<br>Append: Amortized $O(1)$<br>Shift: $O(N)$ | Chosen for rapid index-based lookups and cache-friendly contiguous memory layout. Ideal when reads heavily outnumber middle insertions. |
| **`LinkedHashSet<String>`** | Patron Deduplication | Add: $O(1)$<br>Lookup: $O(1)$<br>Iteration: $O(N)$ | Eliminates duplicate member registrations while maintaining exact chronological arrival/insertion sequence via doubly-linked bucket list. |
| **`ArrayDeque<BorrowRequest>`** | Regular FIFO Waitlist | Enqueue: $O(1)$<br>Dequeue: $O(1)$ | Superior double-ended queue. Unlike `LinkedList`, it has **no 24-byte per-node allocation overhead**, zero garbage collection pressure for node objects, and excellent CPU cache locality. |
| **`PriorityQueue<BorrowRequest>`** | Role-Based Waitlist | Insert: $O(\log N)$<br>Extract-Min: $O(\log N)$<br>Peek: $O(1)$ | Min-heap ordering requests by patron privileges (Faculty rank=1 before Student rank=2, with FIFO tie-breaking). Guarantees priority dispatch without sorting the whole list. |
| **`Iterator.remove()`** | Overdue Book Purging | Traversal: $O(N)$<br>Removal: In-place safe | Prevents `ConcurrentModificationException` when purging expired items during collection iteration. |

---

## 🏗️ Project Structure
```
day-7-collections-framework/
├── src/
│   └── com/
│       └── hcl/
│           └── library/
│               ├── model/
│               │   ├── Book.java
│               │   └── BorrowRequest.java
│               ├── service/
│               │   └── LibraryWaitlistService.java
│               ├── benchmark/
│               │   └── CollectionPerformanceBenchmark.java
│               └── app/
│                   └── LibraryApp.java
└── README.md
```

---

## ⚡ Empirical Performance Benchmark: ArrayList vs LinkedList

Benchmark executed with **40,000 insertions** and **20,000 random access reads** on Java 21 LTS:

| Operation Tested | Sample Size | ArrayList (ms) | LinkedList (ms) | Winner | Speedup | Architectural Reason |
| :--- | :---: | :---: | :---: | :---: | :---: | :--- |
| **Head Insert (`add(0, e)`)** | 40,000 | 214.611 ms | 12.093 ms | **LinkedList** | **17.7x** | `LinkedList` only updates head pointer ($O(1)$), whereas `ArrayList` must `System.arraycopy` shift all existing elements ($O(N)$). |
| **Tail Insert (`add(e)`)** | 40,000 | 5.222 ms | 4.841 ms | **LinkedList** | **1.1x** | Both achieve $O(1)$ tail appends; `ArrayList` incurs occasional dynamic array resize copy. |
| **Middle Insert (`add(mid, e)`)** | 2,000 | 2.894 ms | 48.861 ms | **ArrayList** | **16.9x** | `LinkedList` must traverse $N/2$ node pointers ($O(N)$ seek) before inserting; `ArrayList` shifts contiguous memory rapidly with native memory copy. |
| **Random Access (`get(i)`)** | 20,000 | 0.715 ms | 496.193 ms | **ArrayList** | **693.6x** | `ArrayList` computes offset directly ($O(1)$ direct array index lookup); `LinkedList` must sequentially traverse pointers ($O(N)$) from nearest end. |

---

## 🚀 How to Compile & Run

```bash
# Compile all source files into bin directory
javac -d bin src/com/hcl/library/model/*.java src/com/hcl/library/service/*.java src/com/hcl/library/benchmark/*.java src/com/hcl/library/app/*.java

# Run Driver Application & Benchmark
java -cp bin com.hcl.library.app.LibraryApp
```

---

## 📊 Sample Execution Log

```text
========================================================================
     DAILY TASK 7: COLLECTIONS FRAMEWORK - LIBRARY WAITLIST & BENCHMARK 
========================================================================

--- 1. ARRAYLIST BOOK CATALOGUE ---
Total catalogue items: 5
  Index [0] -> [978-0134685991] "Effective Java (3rd Edition)" by Joshua Bloch
  Index [1] -> [978-0596009205] "Head First Design Patterns" by Eric Freeman
  Index [2] -> [978-0132350884] "Clean Code" by Robert C. Martin (OVERDUE by 14 days)
  Index [3] -> [978-0321356680] "Java Concurrency in Practice" by Brian Goetz (OVERDUE by 35 days)
  Index [4] -> [978-0201633610] "Design Patterns (GoF)" by Erich Gamma

--- 2. LINKEDHASHSET PATRON DEDUPLICATION ---
Raw registration submissions (with duplicates): [PATRON-101, PATRON-102, PATRON-103, PATRON-101, PATRON-104, PATRON-102, PATRON-105, PATRON-103]
Deduplicated unique patrons registered: 5 (New unique: 5)
Patrons preserved in strict arrival order: [PATRON-101, PATRON-102, PATRON-103, PATRON-104, PATRON-105]

--- 3. ARRAYDEQUE REGULAR BORROW WAITLIST (FIFO) ---
Enqueued 3 requests. Serving from ArrayDeque in FIFO order:
  • Dispatched request: [REQ-101] Bob Smith      (STUDENT) | Book: 978-0134685991 | Time: 10:34:58.774
  • Dispatched request: [REQ-102] Charlie Day    (STUDENT) | Book: 978-0596009205 | Time: 10:34:58.774
  • Dispatched request: [REQ-103] David Kim      (GUEST) | Book: 978-0201633610 | Time: 10:34:58.774

--- 4. PRIORITYQUEUE FACULTY-FIRST BORROW DISPATCH ---
Dispatched from PriorityQueue (Faculty prioritized regardless of arrival):
  [1] Served: [REQ-203] Prof. Alan Turing (FACULTY) | Book: 978-0134685991 | Time: 10:34:58.791
  [2] Served: [REQ-204] Dr. Grace Hopper (FACULTY) | Book: 978-0134685991 | Time: 10:35:28.791
  [3] Served: [REQ-201] Student Alice  (STUDENT) | Book: 978-0134685991 | Time: 10:24:58.791
  [4] Served: [REQ-202] Guest Peter    (GUEST) | Book: 978-0134685991 | Time: 10:29:58.791

--- 5. ITERATOR.REMOVE OVERDUE BOOK PURGING ---
Catalogue size before purge: 5
Purged books overdue beyond 30 days:
  ❌ Removed: [978-0321356680] "Java Concurrency in Practice" by Brian Goetz (OVERDUE by 35 days)
Catalogue size after safe Iterator.remove: 4

--- 6. EXECUTION TIMING BENCHMARK (ARRAYLIST VS LINKEDLIST) ---
========================================================================================================
                         PERFORMANCE BENCHMARK: ARRAYLIST VS LINKEDLIST                                 
========================================================================================================
Operation Tested               | Sample Size  | ArrayList (ms)     | LinkedList (ms)    | Winner       | Speedup   
--------------------------------------------------------------------------------------------------------
Head Insert (add(0, e))        | 40000        |         214.611 ms |          12.093 ms | LinkedList   |     17.7x
Tail Insert (add(e))           | 40000        |           5.222 ms |           4.841 ms | LinkedList   |      1.1x
Middle Insert (add(mid, e))    | 2000         |           2.894 ms |          48.861 ms | ArrayList    |     16.9x
Random Access (get(i))         | 20000        |           0.715 ms |         496.193 ms | ArrayList    |    693.6x
========================================================================================================
```
