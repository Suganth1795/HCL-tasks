# Assessment 1: Core Java Checkpoint — Warm-up Quiz Evaluation

**Candidate**: Suganth  
**Program**: HCL Core Java & Engineering Track  
**Checkpoint**: Module 1 (Days 1–11)  
**Total Questions**: 10  
**Score**: 10 / 10 (100%)

---

### Q1. JVM Memory Model & Stack Size
**Question**: What happens when you decrease the JVM stack size parameter using `-Xss256k`?  
**Answer**: Each thread is allocated a smaller 256 KB execution call stack. This limits the maximum recursive method call depth before a `StackOverflowError` occurs, but allows the OS to spawn a higher number of concurrent threads within native virtual memory boundaries.

---

### Q2. Primitive Overflow Prevention
**Question**: In Java, why does multiplying large `int` values produce negative results, and how is it resolved?  
**Answer**: Java `int` is a signed 32-bit two's complement integer with max value $2^{31}-1$ (2,147,483,647). When an arithmetic operation exceeds this boundary, the sign bit flips, producing negative numbers without raising an exception. To prevent overflow, use 64-bit `long` literals or explicit widening casting `(long) a * b`.

---

### Q3. Control Flow & Scanner Input Safety
**Question**: How do you prevent a console application from crashing when a user types characters instead of an integer into `Scanner`?  
**Answer**: Pre-validate tokens using `scanner.hasNextInt()` before calling `scanner.nextInt()`. If `hasNextInt()` returns false, flush the invalid token with `scanner.nextLine()` and continue the loop cycle cleanly.

---

### Q4. Constructor Chaining
**Question**: What keyword is used to chain constructors within the same class, and where must it reside?  
**Answer**: `this(...)` must be the absolute first statement in the constructor body.

---

### Q5. equals() and hashCode() Contract
**Question**: If two objects are equal according to `a.equals(b)`, must `a.hashCode() == b.hashCode()`? What happens if you violate this?  
**Answer**: Yes, equal objects MUST return identical hash codes. Violating this contract breaks hash-based collections (`HashMap`, `HashSet`, `ConcurrentHashMap`), resulting in stored objects becoming unreachable via `.get(key)` because queries land in different hash buckets.

---

### Q6. Abstract Class vs. Interface in Modern Java
**Question**: What is the core semantic difference between an `abstract class` and an `interface` in Java 21?  
**Answer**: An abstract class represents an "IS-A" identity relationship and can hold mutable instance state (fields) and constructor chaining. An interface represents a "CAN-DO" behavioral capability contract with multiple inheritance support.

---

### Q7. Exception Chaining
**Question**: Why should lower-level exceptions be passed to the constructor of high-level business exceptions?  
**Answer**: Passing the original `Throwable cause` preserves the underlying root-cause stack trace (accessible via `.getCause()`), preventing lost diagnostics while maintaining clean architectural boundaries.

---

### Q8. ArrayDeque vs LinkedList
**Question**: Why is `ArrayDeque` generally preferred over `LinkedList` for FIFO waitlists?  
**Answer**: `ArrayDeque` uses a circular array buffer that requires zero separate node allocations, eliminates the 24-byte per-node pointer overhead of `LinkedList`, reduces garbage collection pressure, and leverages CPU cache locality.

---

### Q9. PECS Rule in Generics
**Question**: Explain the PECS mnemonic for generic wildcards.  
**Answer**: **Producer Extends, Consumer Super**. Use `? extends T` when reading/getting data out of a collection (covariance). Use `? super T` when writing/putting data into a collection (contravariance).

---

### Q10. Integer Cache Range
**Question**: Why does `Integer a = 127, b = 127; (a == b)` evaluate to `true`, but `Integer c = 128, d = 128; (c == d)` evaluate to `false`?  
**Answer**: `Integer.valueOf()` caches instances from -128 to 127. Values within this range share a cached reference. Values $\ge 128$ instantiate new heap objects with different memory addresses, failing reference equality `==`.
