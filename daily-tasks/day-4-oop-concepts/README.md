# Day 4: OOP Concepts + IDE & Debugging

## 📌 Task Overview
The objective of Day 4 is to build a robust **Bank Account Domain Model** adhering strictly to Object-Oriented Programming (OOP) principles, clean multi-package architecture, constructor chaining, encapsulation, and an in-depth IDE debugging walkthrough using conditional breakpoints, variable watches, and Hot Code Replace.

---

## 🎯 Key Requirements & OOP Implementation

### 1. Multi-Package Architecture
```
day-4-oop-concepts/
├── src/
│   └── com/
│       └── hcl/
│           └── bank/
│               ├── model/
│               │   └── BankAccount.java     # Encapsulated domain entity
│               ├── service/
│               │   └── BankService.java     # Business logic & fund transfer orchestration
│               └── app/
│                   └── BankApplication.java # Application runner & test harness
└── README.md
```

### 2. Encapsulation & Field Modifiers
- All member fields (`accountNumber`, `accountHolderName`, `balance`, `accountType`) are strictly `private`.
- Immutable unique identifier `accountNumber` marked `final`.
- Public getters and validated setters enforcing business boundaries.

### 3. Static Counter & Unique Account Number Generator
- `private static long accountCounter = 100001L;`
- Every new instance automatically receives a formatted sequence ID: `"HCL-" + (accountCounter++)`.

### 4. Constructor Chaining (`this(...)`)
1. **`BankAccount()`**: Default constructor chaining to `this("Guest Customer")`.
2. **`BankAccount(String name)`**: Single-argument constructor chaining to `this(name, 0.0, "SAVINGS")`.
3. **`BankAccount(String name, double balance, String type)`**: Master constructor performing argument validation, counter increments, and field assignment.

### 5. `equals()` and `hashCode()` Contract
- Overridden to evaluate identity based purely on unique `accountNumber` using `java.util.Objects.equals()` and `Objects.hash()`.

---

## 🐞 IDE Debugging & Bug-Fix Case Study

### Scenario: Planted Bug in `withdraw()`
During initial testing, the `withdraw()` method contained a subtle operator logic defect:
```java
// BUGGY IMPLEMENTATION:
public boolean withdraw(double amount) {
    if (amount > this.balance) {
        return false;
    }
    this.balance = amount - this.balance; // BUG: Inverted subtraction yielding negative balance!
    return true;
}
```

### Debugging Procedure with IDE Tools:
1. **Conditional Breakpoint**:
   - Set on `BankAccount.java` inside `withdraw()`.
   - Condition configured: `amount > 300.0 && this.balance > 0`.
2. **Watch Expressions Added**:
   - `this.balance`
   - `amount`
   - `this.balance - amount` (Expected) vs `amount - this.balance` (Actual)
3. **Root Cause Analysis via Watcher**:
   - When withdrawing `$400.00` from an initial balance of `$1,200.00`, the watch expression evaluated `400.0 - 1200.0 = -800.0` instead of `+800.0`.
4. **Fix via Hot Code Replace / Source Patch**:
   ```java
   // FIXED IMPLEMENTATION:
   this.balance -= amount; // Correct balance deduction
   ```
5. **Validation**:
   - Verified that balance decreases accurately to `$800.00` and overdraft attempts are rejected cleanly.

---

## 🚀 How to Compile & Run

```bash
# Compile all packages
javac -d bin src/com/hcl/bank/model/*.java src/com/hcl/bank/service/*.java src/com/hcl/bank/app/*.java

# Run Driver Application
java -cp bin com.hcl.bank.app.BankApplication
```

---

## 📊 Sample Execution Output

```text
===============================================================
         DAILY TASK 4: OOP CONCEPTS & BANK DOMAIN              
===============================================================

--- 1. CONSTRUCTOR CHAINING DEMONSTRATION ---
Account 1 (Default): BankAccount [Account No: HCL-100001 | Holder: Guest Customer  | Type: SAVINGS | Balance: $0.00]
Account 2 (Chained): BankAccount [Account No: HCL-100002 | Holder: Alice Smith     | Type: SAVINGS | Balance: $0.00]
Account 3 (Full)   : BankAccount [Account No: HCL-100003 | Holder: Bob Johnson     | Type: CURRENT | Balance: $5,000.00]
Total Accounts Created (Static Counter): 3

--- 2. ENCAPSULATION & TRANSACTION VALIDATION ---
Depositing $1,200.00 into Account 2...
Account 2 updated: BankAccount [Account No: HCL-100002 | Holder: Alice Smith     | Type: SAVINGS | Balance: $1,200.00]

Attempting invalid negative deposit (-$500.00)...
❌ [Validation Error] Deposit amount must be greater than $0.00.

Attempting valid withdrawal ($400.00) from Account 2...
Account 2 after withdrawal: BankAccount [Account No: HCL-100002 | Holder: Alice Smith     | Type: SAVINGS | Balance: $800.00]

Attempting overdraft withdrawal ($2,000.00) from Account 2...
❌ [Transaction Error] Insufficient funds! Current: $800.00, Requested: $2000.00

--- 3. EQUALS & HASHCODE CONTRACT ---
acc2: HCL-100002
acc2DuplicateRef: HCL-100004
acc2.equals(acc2DuplicateRef) [Different Account Numbers]: false
acc2.equals(acc2) [Same Instance]: true

--- 4. INTER-ACCOUNT FUND TRANSFER ---
✅ Successfully transferred $1,500.00 from HCL-100005 to HCL-100006

--- FINAL ACCOUNT REGISTRY ---
  • BankAccount [Account No: HCL-100004 | Holder: Alice Smith     | Type: SAVINGS | Balance: $800.00]
  • BankAccount [Account No: HCL-100005 | Holder: Charlie Brown   | Type: SAVINGS | Balance: $8,500.00]
  • BankAccount [Account No: HCL-100006 | Holder: Diana Prince    | Type: SAVINGS | Balance: $4,000.00]
```
