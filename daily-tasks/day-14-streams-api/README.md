# Day 14: Streams API — 1,000 Orders Sales Analytics

## 📌 Task Overview
The objective of Day 14 is to harness the power of the Java **Streams API** to process, aggregate, and analyze a dataset of **1,000 sales transactions** using advanced collector strategies and evaluate multicore performance with parallel streams:
1. **Category Revenue Reduction**: Multi-level reduction via `Collectors.groupingBy` and `Collectors.summingDouble`.
2. **Top Spenders Identification**: Grouping by customer, sorting in descending order, and extracting the top 3 spenders.
3. **Delivery Partitioning**: Boolean partitioning via `Collectors.partitioningBy` separating delivered transactions from pending/cancelled orders.
4. **Mean Basket Size**: Calculating average transaction value via `Collectors.averagingDouble`.
5. **Flattened Catalog Listing**: Flattening nested product lists using `.flatMap()` and joining unique entries into a comma-delimited string with `Collectors.joining(", ")`.
6. **Parallel Stream Benchmarking**: Measuring speedup under a CPU-bound mathematical transformation using `ForkJoinPool.commonPool()`.
7. **Purity Guarantee**: Pure functional pipeline with **strictly zero external state mutation** inside stream lambdas.

---

## 🎯 Stream Pipeline Architecture & Collectors

| Analytical Metric | Collector Expression | Output Type |
| :--- | :--- | :--- |
| **Category Revenue** | `Collectors.groupingBy(SalesOrder::getCategory, Collectors.summingDouble(SalesOrder::getOrderTotal))` | `Map<String, Double>` |
| **Customer Spend** | `Collectors.groupingBy(SalesOrder::getCustomerName, Collectors.summingDouble(SalesOrder::getOrderTotal))` | `Map<String, Double>` |
| **Status Partition** | `Collectors.partitioningBy(o -> "DELIVERED".equals(o.getStatus()))` | `Map<Boolean, List<SalesOrder>>` |
| **Average Basket** | `Collectors.averagingDouble(SalesOrder::getOrderTotal)` | `Double` |
| **Unique Catalog** | `orders.stream().flatMap(o -> o.getProductNames().stream()).distinct().collect(Collectors.joining(", "))` | `String` |

---

## ⚡ Sequential vs. Parallel Performance Benchmark

Benchmark executed on 1,000 orders applying an intensive non-linear mathematical transformation per record:

| Execution Mode | Threading Strategy | Elapsed Time | Speedup Factor | Architectural Reason |
| :--- | :--- | :---: | :---: | :--- |
| **Sequential Stream** | Single calling thread | **804 ms** | 1.0x (Baseline) | Processed one element at a time on a single CPU core without thread coordination overhead. |
| **Parallel Stream** | `ForkJoinPool.commonPool()` | **136 ms** | **5.91x FASTER** | Work-stealing ForkJoinPool partitioned the dataset across all available CPU cores, maximizing processor utilization. |

> [!TIP]
> Parallel streams yield dramatic speedups for **CPU-bound** workloads on large datasets ($N \times Q > 10,000$). For trivial operations or I/O-bound tasks, the thread context-switching and join overhead may degrade performance compared to sequential processing.

---

## 🏗️ Project Structure
```
day-14-streams-api/
├── src/
│   └── com/
│       └── hcl/
│           └── streams/
│               ├── model/
│               │   └── SalesOrder.java    # Sales Order domain model
│               └── SalesAnalytics.java    # Master analytics pipeline & benchmark
└── README.md
```

---

## 🚀 How to Compile & Run

```bash
# 1. Compile all source files into bin directory
javac -d bin src/com/hcl/streams/model/*.java src/com/hcl/streams/SalesAnalytics.java

# 2. Run Sales Analytics Application
java -cp bin com.hcl.streams.SalesAnalytics
```

---

## 📊 Sample Execution Log

```text
========================================================================
       DAILY TASK 14: STREAMS API - 1,000 ORDERS SALES ANALYTICS        
========================================================================

Loaded 1000 sales order transactions for processing.

--- 1. TOTAL REVENUE BY CATEGORY (Collectors.groupingBy + summingDouble) ---
  • Peripherals     : $  154,608.90
  • Audio           : $  145,963.63
  • Furniture       : $  145,332.80
  • Accessories     : $  144,016.66
  • Electronics     : $  143,806.29

--- 2. TOP 3 CUSTOMERS BY EXPENDITURE ---
  [#1] Iris West        : $   80,059.59 in total orders
  [#2] John Doe         : $   79,786.14 in total orders
  [#3] Henry Cavill     : $   78,099.65 in total orders

--- 3. ORDERS PARTITIONED BY STATUS (Delivered vs Others) ---
  • DELIVERED Orders Count  : 317 orders ($236,768.15)
  • NON-DELIVERED Count     : 683 orders ($496,960.13)

--- 4. AVERAGE BASKET SIZE ---
  • Mean Transaction Value (Basket Size): $733.73

--- 5. DISTINCT PRODUCT CATALOG (flatMap + Collectors.joining) ---
  • Active Catalog Items (20 unique products):
    4K Monitor, Bluetooth Speaker, Bookshelf, Desk Lamp, Ergonomic Chair, Gaming Laptop, Laptop Stand, MacBook Air, Mechanical Keyboard, Noise-Cancelling Headphones, Screen Cleaner, Soundbar, Standing Desk, Studio Mic, Thunderbolt Dock, Trackpad, USB-C Cable, Webcam HD, Wireless Mouse, iPad Pro

--- 6. PERFORMANCE BENCHMARK: SEQUENTIAL VS PARALLEL STREAM ---
  Workload: CPU-intensive non-linear trigonometric calculations across 1,000 orders.

========================================================================
                 SEQUENTIAL VS PARALLEL TIMING COMPARISON               
========================================================================
  Sequential Stream Execution Time : 804 ms
  Parallel Stream Execution Time   : 136 ms
  Parallel Speedup Factor          : 5.91x faster (Utilizing ForkJoinPool.commonPool)
========================================================================
```
