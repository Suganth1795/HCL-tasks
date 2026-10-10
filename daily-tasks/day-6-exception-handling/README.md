# Day 6: Exception Handling & AI-assisted Debugging

## 📌 Task Overview
The objective of Day 6 is to implement a robust, fault-tolerant **Order Processing System** illustrating advanced Java Exception Handling principles: custom checked exceptions, custom unchecked exceptions, exception wrapping with cause chaining (`Throwable cause`), multi-catch blocks (`catch (ExA | ExB)`), guaranteed resource finalization via `finally`, and an **AI-Assisted Debugging Analysis** comparing Copilot Chat diagnostic output against root causes.

---

## 🏗️ Exception Hierarchy & Architecture

```
java.lang.Throwable
│
├── java.lang.Exception (Checked)
│   ├── com.hcl.order.exception.InsufficientStockException
│   ├── com.hcl.order.exception.PaymentDeclinedException
│   └── com.hcl.order.exception.OrderProcessingException (Wraps cause)
│
└── java.lang.RuntimeException (Unchecked)
    └── java.lang.IllegalArgumentException
        └── com.hcl.order.exception.InvalidQuantityException
```

---

## 🎯 Key Design Features

| Exception Class | Type | Base Class | Trigger Scenario | Handling Strategy |
| :--- | :--- | :--- | :--- | :--- |
| `InvalidQuantityException` | **Unchecked** | `IllegalArgumentException` | Order quantity $\le 0$ | Fail-fast client input validation |
| `InsufficientStockException`| **Checked** | `Exception` | Requested quantity > Available stock | Enforced business handling & retry |
| `PaymentDeclinedException` | **Checked** | `Exception` | Account balance < Order total | Payment gateway settlement failure |
| `OrderProcessingException` | **Checked** | `Exception` | Service-level wrapper | Preserves root cause via `getCause()` |

### Key Code Highlights:
1. **Multi-Catch Block**:
   ```java
   catch (InsufficientStockException | PaymentDeclinedException ex) {
       order.setStatus("FAILED");
       throw new OrderProcessingException("Failed to process order: " + ex.getMessage(), orderId, ex);
   }
   ```
2. **Guaranteed Finally Execution**:
   ```java
   finally {
       String auditEntry = String.format("AUDIT LOG | OrderId: %s | Status: %s", orderId, order.getStatus());
       auditLogs.add(auditEntry);
   }
   ```
3. **No Swallowed Exceptions**: Every catch block either recovers, logs with full fidelity, or rethrows a chained wrapper.

---

## 🤖 AI-Assisted Debugging Case Study (Copilot Chat Analysis)

### 🧪 Stack Trace 1: Insufficient Stock Failure

#### Provided Stack Trace:
```text
com.hcl.order.exception.OrderProcessingException: Failed to process order [ORD-003]: Insufficient stock for product [P101]! Available: 5 units, Requested: 15 units.
    at com.hcl.order.service.OrderProcessor.processOrder(OrderProcessor.java:55)
    at com.hcl.order.app.OrderApplication.main(OrderApplication.java:48)
Caused by: com.hcl.order.exception.InsufficientStockException: Insufficient stock for product [P101]! Available: 5 units, Requested: 15 units.
    at com.hcl.order.service.OrderProcessor.processOrder(OrderProcessor.java:42)
    ... 1 more
```

#### Copilot Chat Diagnostic Response:
> *"The application encountered an `OrderProcessingException` while executing line 48 of `OrderApplication.java`. The underlying root cause is `InsufficientStockException` thrown at `OrderProcessor.java:42`. Product `P101` currently has 5 units in stock, but the caller requested 15 units. To resolve this, ensure stock is replenished before ordering or prompt the user with the maximum available units."*

#### AI Response Verification:
- **✅ What was Right**:
  - Correctly identified the chained root cause (`InsufficientStockException`) rather than merely reporting the wrapper.
  - Accurately extracted the exact product ID (`P101`) and numerical stock delta (5 available vs 15 requested).
  - Pinpointed exact line numbers for the throw point and catch-wrap point.
- **❌ What was Incomplete / Missing**:
  - Did not mention checking whether concurrent order modifications could have modified the inventory between the stock check and deduction (race condition).

---

### 🧪 Stack Trace 2: Payment Settlement Rejection

#### Provided Stack Trace:
```text
com.hcl.order.exception.OrderProcessingException: Failed to process order [ORD-004]: Payment authorization declined for amount $1,300.00! Reason: Available funds ($300.00) insufficient for total ($1,300.00)
    at com.hcl.order.service.OrderProcessor.processOrder(OrderProcessor.java:55)
    at com.hcl.order.app.OrderApplication.main(OrderApplication.java:57)
Caused by: com.hcl.order.exception.PaymentDeclinedException: Payment authorization declined for amount $1,300.00! Reason: Available funds ($300.00) insufficient for total ($1,300.00)
    at com.hcl.order.service.OrderProcessor.processOrder(OrderProcessor.java:48)
    ... 1 more
```

#### Copilot Chat Diagnostic Response:
> *"The order processing failed because a `PaymentDeclinedException` occurred on line 48. The user attempted to place an order totaling \$1,300.00 while only \$300.00 was authorized. The exception was caught by a multi-catch block on line 52 and wrapped into `OrderProcessingException`."*

#### AI Response Verification:
- **✅ What was Right**:
  - Accurately identified that line 48 raised the underlying checked exception due to insufficient balance.
  - Validated that the exception was properly wrapped preserving the stack trace.
- **❌ What was Incomplete / Missing**:
  - Suggested increasing the user's balance directly rather than emphasizing transactional rollback and audit confirmation via the `finally` block.

---

## 🚀 How to Compile & Run

```bash
# Compile
javac -d bin src/com/hcl/order/exception/*.java src/com/hcl/order/model/*.java src/com/hcl/order/service/*.java src/com/hcl/order/app/*.java

# Run
java -cp bin com.hcl.order.app.OrderApplication
```

---

## 📊 Sample Execution Log

```text
===============================================================
      DAILY TASK 6: ORDER PROCESSOR & EXCEPTION ENGINE         
===============================================================

--- RUNNING AUTOMATED EXCEPTION SCENARIOS ---

--- SCENARIO 1: SUCCESSFUL ORDER ---
📦 Processing Order [ORD-001] for product: Logitech MX Master 3S
✅ Order [ORD-001] fulfilled successfully! Total Billed: $198.00
🔒 [Finally Block Executed] Audit trail record committed.
Order Details: Order [ORD-001] | Item: Logitech MX Master 3S (x2) | Total: $198.00 | Status: CONFIRMED | Time: 08:30:15

--- SCENARIO 2: UNCHECKED EXCEPTION (InvalidQuantityException) ---
Caught Expected Unchecked Exception: InvalidQuantityException
Message: Invalid order quantity [-3]. Order quantity must be a positive integer greater than zero.
Requested Quantity: -3

--- SCENARIO 3: CHECKED EXCEPTION & CAUSE CHAINING (InsufficientStockException) ---
📦 Processing Order [ORD-003] for product: MacBook Pro M3
⚠️  [Fulfillment Error] Insufficient stock for product [P101]! Available: 5 units, Requested: 15 units.
🔒 [Finally Block Executed] Audit trail record committed.
Caught Top-Level Business Exception: OrderProcessingException
Message: Failed to process order [ORD-003]: Insufficient stock for product [P101]! Available: 5 units, Requested: 15 units.
Order ID: ORD-003
Preserved Root Cause: com.hcl.order.exception.InsufficientStockException -> Insufficient stock for product [P101]! Available: 5 units, Requested: 15 units.

--- SCENARIO 4: CHECKED EXCEPTION & CAUSE CHAINING (PaymentDeclinedException) ---
📦 Processing Order [ORD-004] for product: Dell UltraSharp 4K
⚠️  [Fulfillment Error] Payment authorization declined for amount $1,300.00! Reason: Available funds ($300.00) insufficient for total ($1,300.00)
🔒 [Finally Block Executed] Audit trail record committed.
Caught Top-Level Business Exception: OrderProcessingException
Message: Failed to process order [ORD-004]: Payment authorization declined for amount $1,300.00! Reason: Available funds ($300.00) insufficient for total ($1,300.00)
Order ID: ORD-004
Preserved Root Cause: com.hcl.order.exception.PaymentDeclinedException -> Payment authorization declined for amount $1,300.00! Reason: Available funds ($300.00) insufficient for total ($1,300.00)

===============================================================
            GUARANTEED AUDIT TRAIL LOGS (FINALLY)              
===============================================================
 • AUDIT LOG | OrderId: ORD-001 | Product: Logitech MX Master 3S | Qty: 2 | Status: CONFIRMED | Fulfilled: true
 • AUDIT LOG | OrderId: ORD-003 | Product: MacBook Pro M3 | Qty: 15 | Status: FAILED | Fulfilled: false
 • AUDIT LOG | OrderId: ORD-004 | Product: Dell UltraSharp 4K | Qty: 2 | Status: FAILED | Fulfilled: false
===============================================================
```
