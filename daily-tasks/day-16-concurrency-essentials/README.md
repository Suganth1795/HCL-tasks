# Day 16: Concurrency Essentials — Multi-Supplier Price Aggregator

## Objective
Build an asynchronous **Price Aggregator Engine** that queries multiple external slow suppliers (simulating 1–2 second network latencies), combines responses concurrently using `ExecutorService` and `CompletableFuture.allOf`, resiliently handles supplier failure using `.exceptionally()`, and analyzes performance speedups over synchronous sequential execution.

---

## Key Concepts Demonstrated

1. **Thread Latency & Blocking Pitfalls**:
   - Sequential execution sums the delays of all network calls ($T_{\text{total}} = t_1 + t_2 + t_3$).
   - A single slow or failed supplier blocks subsequent calls.
2. **`ExecutorService` & `Callable<T>` / `Future<T>`**:
   - Fixed thread pool executing worker threads in parallel.
   - Futures block upon calling `.get()`, requiring manual `try-catch` blocks per future for `ExecutionException`.
3. **`CompletableFuture` & Asynchronous Pipelines**:
   - Non-blocking supply via `CompletableFuture.supplyAsync(Supplier<U>, Executor)`.
   - Resilience through `.exceptionally(Function<Throwable, ? extends T>)` fallback mapping.
   - Coordination using `CompletableFuture.allOf(CompletableFuture<?>...)` barrier synchronization.
   - Best-price evaluation across responding nodes using Java Streams.

---

## Directory Structure

```
day-16-concurrency-essentials/
├── src/
│   └── com/hcl/concurrency/
│       ├── SupplierPrice.java          # Quote DTO with latency & error details
│       ├── SlowSupplier.java           # Supplier simulator with latency & failure mode
│       └── PriceAggregator.java        # Main benchmark & aggregation pipeline
└── README.md
```

---

## Benchmark Results

```
================================================================================
DAY 16: CONCURRENCY ESSENTIALS - PRICE AGGREGATOR ENGINE
Target Product: Logitech MX Master 3S
================================================================================

Registered Suppliers for Aggregation:
 - Supplier-Alpha (Retail)   | Latency: 1200 ms | Failing: NO (Healthy)
 - Supplier-Beta (Wholesale) | Latency: 1800 ms | Failing: NO (Healthy)
 - Supplier-Gamma (Direct)   | Latency: 1500 ms | Failing: YES (Throws 503)

--------------------------------------------------------------------------------
APPROACH 1: SEQUENTIAL SYNCHRONOUS EXECUTION
--------------------------------------------------------------------------------
Fetching sequentially from Supplier-Alpha (Retail)...
  -> Received: [SUCCESS] Supplier-Alpha (Retail) | Item: Logitech MX Master 3S | Price: $ 499.00 | Latency: 1203 ms
Fetching sequentially from Supplier-Beta (Wholesale)...
  -> Received: [SUCCESS] Supplier-Beta (Wholesale) | Item: Logitech MX Master 3S | Price: $ 465.50 | Latency: 1801 ms
Fetching sequentially from Supplier-Gamma (Direct)...
  -> Error caught: [FAILED]  Supplier-Gamma (Direct) | Item: Logitech MX Master 3S | Latency: 1501 ms | Error: HTTP 503: Supplier-Gamma (Direct) service unavailable / timeout
>>> Total Sequential Time: 4519 ms (~sum of all latencies)

--------------------------------------------------------------------------------
APPROACH 2: EXECUTOR SERVICE (THREAD POOL WITH CALLABLE & FUTURE)
--------------------------------------------------------------------------------
  -> Future completed: [SUCCESS] Supplier-Alpha (Retail) | Item: Logitech MX Master 3S | Price: $ 499.00 | Latency: 1200 ms
  -> Future completed: [SUCCESS] Supplier-Beta (Wholesale) | Item: Logitech MX Master 3S | Price: $ 465.50 | Latency: 1800 ms
  -> ExecutionException caught: [FAILED]  Supplier-Gamma (Direct) | Item: Logitech MX Master 3S | Latency: 1500 ms | Error: HTTP 503: Supplier-Gamma (Direct) service unavailable / timeout
>>> Total ExecutorService Time: 1808 ms (~max latency among suppliers)

--------------------------------------------------------------------------------
APPROACH 3: ASYNCHRONOUS NON-BLOCKING (CompletableFuture.allOf + exceptionally)
--------------------------------------------------------------------------------
Results retrieved via CompletableFuture:
  [SUCCESS] Supplier-Alpha (Retail) | Item: Logitech MX Master 3S | Price: $ 499.00 | Latency: 1201 ms
  [SUCCESS] Supplier-Beta (Wholesale) | Item: Logitech MX Master 3S | Price: $ 465.50 | Latency: 1801 ms
  [FAILED]  Supplier-Gamma (Direct) | Item: Logitech MX Master 3S | Latency: 1500 ms | Error: HTTP 503: Supplier-Gamma (Direct) service unavailable / timeout

--------------------------------------------------------------------------------
AGGREGATION DECISION SUMMARY
--------------------------------------------------------------------------------
[BEST OFFER] Supplier-Beta (Wholesale) at $465.50 (Latency: 1801 ms)

================================================================================
PERFORMANCE BENCHMARK & ARCHITECTURAL COMPARISON
================================================================================
Approach                       | Time Taken       | Speedup vs Seq | Failure Resiliency
--------------------------------------------------------------------------------
1. Sequential Execution        |     4519 ms      |   1.00x (Base) | Fragile / Blocking
2. ExecutorService + Future    |     1808 ms      |          2.50x | Try-catch per get()
3. CompletableFuture.allOf     |     1817 ms      |          2.49x | exceptionally pipeline
================================================================================
```

---

## Architectural Comparison

| Dimension | Sequential Execution | `ExecutorService` + `Future` | `CompletableFuture.allOf` |
| :--- | :--- | :--- | :--- |
| **Execution Model** | Synchronous, single-threaded | Multi-threaded pool | Asynchronous, non-blocking |
| **Total Wall Clock Time** | $\sum \text{latency} \approx 4519\text{ ms}$ | $\max(\text{latency}) \approx 1808\text{ ms}$ | $\max(\text{latency}) \approx 1817\text{ ms}$ |
| **Speedup** | $1.0\times$ (Baseline) | $2.50\times$ | $2.49\times$ |
| **Error Handling** | Aborts sequence or requires nested try-catches | Throws `ExecutionException` on `.get()` | Fluent `.exceptionally()` or `.handle()` |
| **Composition** | Easy but slow | Difficult to compose multiple stages | Declarative chaining (`thenCombine`, `allOf`) |

---

## How to Run

Compile and run via command line:
```bash
javac -d bin src/com/hcl/concurrency/*.java
java -cp bin com.hcl.concurrency.PriceAggregator
```
