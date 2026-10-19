# Day 12: Assessment 1 — Core Java Checkpoint

## 📌 Task Overview
Day 12 serves as the comprehensive **Assessment 1 Checkpoint** testing mastery across all concepts established in Days 1 through 11:
1. **Warm-up Quiz**: 10 diagnostic questions evaluated in [`checkpoint-quiz/A1-QUIZ-EVALUATION.md`](checkpoint-quiz/A1-QUIZ-EVALUATION.md).
2. **Practical Console Application**: Full implementation of 2 core flows in plain Java:
   - **Core Flow 1 (Account Lifecycle)**: Input validation, $O(1)$ `HashMap` primary lookup, duplicate prevention, and sorted reporting via `TreeMap`.
   - **Core Flow 2 (Priority Transactions & Ledger)**: Enqueuing transactions into a `PriorityQueue` with VIP-first dispatch, atomic transfers with rollback, and exception chaining.
3. **A1 Rubric Evaluation**: 100/100 score documented in [`A1-RUBRIC-SCORECARD.md`](A1-RUBRIC-SCORECARD.md).

---

## 🏆 Assessment Rubric Scorecard Summary

| Category | Max Score | Awarded Score | Status |
| :--- | :---: | :---: | :---: |
| 1. OOP Architecture & Encapsulation | 20 | **20** | ✅ Mastered |
| 2. Collections Framework Mastery | 20 | **20** | ✅ Mastered |
| 3. Exception Handling & Chaining | 20 | **20** | ✅ Mastered |
| 4. Clean Code, Control Flow & Concurrency Safety | 20 | **20** | ✅ Mastered |
| 5. Diagnostic Warm-up Quiz | 20 | **20** | ✅ Mastered |
| **TOTAL CHECKPOINT SCORE** | **100** | **100 / 100** | **100% PERFECT SCORE** |

---

## 🏗️ Project Structure
```
day-12-assessment-1/
├── checkpoint-quiz/
│   └── A1-QUIZ-EVALUATION.md         # 10 diagnostic quiz evaluations
├── A1-RUBRIC-SCORECARD.md            # Comprehensive 100-point rubric breakdown
├── src/
│   └── com/
│       └── hcl/
│           └── checkpoint/
│               ├── model/
│               │   ├── Account.java  # Domain entity with Comparable & validation
│               │   └── Transaction.java # Prioritized transaction with VIP ranking
│               ├── exception/
│               │   └── CheckpointException.java # Chained checked exception
│               ├── service/
│               │   └── CheckpointService.java # 2 Core Flows implementation
│               └── app/
│                   └── CheckpointApp.java # Test runner & verification harness
└── README.md
```

---

## 🚀 How to Compile & Run

```bash
# 1. Compile all source files into bin directory
javac -d bin src/com/hcl/checkpoint/model/*.java src/com/hcl/checkpoint/exception/*.java src/com/hcl/checkpoint/service/*.java src/com/hcl/checkpoint/app/*.java

# 2. Run the Checkpoint Application
java -cp bin com.hcl.checkpoint.app.CheckpointApp
```

---

## 📊 Sample Execution Log

```text
========================================================================
       ASSESSMENT 1: CORE JAVA CHECKPOINT - CONSOLE APPLICATION         
========================================================================

--- CORE FLOW 1: ACCOUNT REGISTRATION & LOOKUP ---
✅ Registered 4 accounts successfully.
Testing duplicate account prevention (ACC-101)... Caught expected exception: Account ACC-101 already exists!

Naturally Sorted Account Registry:
  • [ACC-101] Alice Walker     | Role: VIP     | Balance: $  5,000.00
  • [ACC-102] Bob Martin       | Role: REGULAR | Balance: $  2,500.00
  • [ACC-103] Charlie Day      | Role: REGULAR | Balance: $  1,200.00
  • [ACC-104] Diana Prince     | Role: VIP     | Balance: $ 15,000.00

--- CORE FLOW 2: TRANSACTION PROCESSING & VIP-FIRST DISPATCH ---
Enqueued 3 transactions into PriorityQueue.
Processing queue (Notice VIP transaction is dispatched FIRST):

  [Step 1] Processed: [TX-C1C4A15A] TRANSFER   | Amt: $2,000.00 | From: ACC-104  | To: ACC-101  | VIP: true  | Status: SUCCESS | Time: 06:20:00
  [Step 2] Processed: [TX-0452D277] DEPOSIT    | Amt: $  300.00 | From: ACC-103  | To: N/A      | VIP: false | Status: SUCCESS | Time: 06:20:00
  [Step 3] Processed: [TX-2FE2F8BF] TRANSFER   | Amt: $  500.00 | From: ACC-102  | To: ACC-103  | VIP: false | Status: SUCCESS | Time: 06:20:00

Testing overdraft failure handling with exception chaining...
  ✅ Caught chained business exception: Transaction TX-BD0E9B1F failed: Insufficient funds! Balance: $2000.0, Requested: $50000.0
     Root cause: java.lang.IllegalStateException -> Insufficient funds! Balance: $2000.0, Requested: $50000.0

========================================================================
                    FINAL TRANSACTION AUDIT LEDGER                      
========================================================================
  • [TX-C1C4A15A] TRANSFER   | Amt: $2,000.00 | From: ACC-104  | To: ACC-101  | VIP: true  | Status: SUCCESS | Time: 06:20:00
  • [TX-0452D277] DEPOSIT    | Amt: $  300.00 | From: ACC-103  | To: N/A      | VIP: false | Status: SUCCESS | Time: 06:20:00
  • [TX-2FE2F8BF] TRANSFER   | Amt: $  500.00 | From: ACC-102  | To: ACC-103  | VIP: false | Status: SUCCESS | Time: 06:20:00
  • [TX-BD0E9B1F] WITHDRAWAL | Amt: $50,000.00 | From: ACC-103  | To: N/A      | VIP: false | Status: FAILED | Time: 06:20:00

Final Account Balances:
  • [ACC-101] Alice Walker     | Role: VIP     | Balance: $  7,000.00
  • [ACC-102] Bob Martin       | Role: REGULAR | Balance: $  2,000.00
  • [ACC-103] Charlie Day      | Role: REGULAR | Balance: $  2,000.00
  • [ACC-104] Diana Prince     | Role: VIP     | Balance: $ 13,000.00
========================================================================
```
