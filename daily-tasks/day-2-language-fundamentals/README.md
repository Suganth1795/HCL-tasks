# Day 2: Language Fundamentals & Git Fundamentals

## 📌 Task Overview
The objective of Day 2 is to build a **Monthly Usage Analyser** in Java demonstrating core language fundamentals, data types, arithmetic operators, explicit type casting, control structures, and multi-dimensional arrays.

---

## 🎯 Key Requirements & Implementation

1. **1-D Array for 12 Months**:
   - Stores monthly electricity/utility usage units for a 12-month calendar year.
2. **Slab Constants**:
   - Clean constants defined for rate slabs and efficiency thresholds (`SLAB1_LIMIT`, `SLAB2_LIMIT`, `SLAB1_RATE`, `SLAB2_RATE`, `SLAB3_RATE`), eliminating magic numbers.
3. **Statistical Aggregation**:
   - Calculates **Total Units**, **Average Units**, **Maximum Unit (Peak Month)**, and **Minimum Unit (Lowest Month)**.
4. **Explicit Type Casting**:
   - Average monthly usage is computed using explicit cast `(double) totalUnits / monthlyUnits.length`.
5. **Overflow Prevention**:
   - Accumulators utilize 64-bit `long` primitive type to prevent integer overflow.
6. **Efficiency Grade via Ternary Operator**:
   - Nested ternary operator determines rating:
     - `'A'` for Average < 150 kWh
     - `'B'` for Average between 150 and 300 kWh
     - `'C'` for Average > 300 kWh
7. **2-D Array Analysis (Multi-House)**:
   - Evaluates a 2-D array representing 3 houses across 12 months (`3 x 12`), calculating per-house metrics and community grand totals.

---

## 🚀 How to Run

### 1. Compile
```bash
javac MonthlyUsageAnalyzer.java
```

### 2. Execute
```bash
java MonthlyUsageAnalyzer
```

---

## 📊 Sample Execution Output

```text
===============================================================
         DAILY TASK 2: MONTHLY USAGE ANALYZER                  
===============================================================

--- PART 1: SINGLE HOUSE ANNUAL USAGE ANALYSIS ---
Monthly Breakdown:
  January    :  120 kWh
  February   :  145 kWh
  March      :  180 kWh
  April      :  290 kWh
  May        :  340 kWh
  June       :  420 kWh
  July       :  380 kWh
  August     :  310 kWh
  September  :  220 kWh
  October    :  190 kWh
  November   :  130 kWh
  December   :  115 kWh
---------------------------------------------------------------
Total Annual Usage     : 2840 kWh (Computed using long to prevent overflow)
Average Monthly Usage  : 236.67 kWh (Explicit cast: (double) total / length)
Peak Month (Max)       : June with 420 kWh
Lowest Month (Min)     : December with 115 kWh
Efficiency Grade       : 'B' (via Ternary: A < 150, B <= 300, C > 300)
Estimated Annual Bill  : $13450.00 (Based on slab constants)

--- PART 2: MULTI-HOUSE (2-D ARRAY) COMPARATIVE ANALYSIS ---
House      | Total Units  | Avg Monthly  | Min Month  | Max Month  | Grade 
-------------------------------------------------------------------------
House #1    |     2840 kWh  |   236.67 kWh  |    115 kWh  |    420 kWh  |   B  
House #2    |     1835 kWh  |   152.92 kWh  |     90 kWh  |    250 kWh  |   B  
House #3    |     5340 kWh  |   445.00 kWh  |    290 kWh  |    620 kWh  |   C  
-------------------------------------------------------------------------
Combined Grand Total for 3 Houses : 10015 kWh
Overall Community Average Monthly : 278.19 kWh
```
