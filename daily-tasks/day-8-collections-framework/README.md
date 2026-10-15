# Day 8: Collections Framework — Part 2

## 📌 Task Overview
The objective of Day 8 is to implement a high-performance **Employee Directory System** demonstrating advanced map operations in the Java Collections Framework: constant-time `HashMap` lookups, atomic frequency aggregation with `Map.merge()`, structured hierarchical reports via sorted `TreeMap`, natural sorting with `Comparable<Employee>`, multi-tier sorting with a `Comparator` chain, and an empirical **Missing-`hashCode()` Bug Demonstration and Fix**.

---

## 🎯 Key Design Features

| Feature | Data Structure / Technique | Complexity | Description & Benefit |
| :--- | :--- | :--- | :--- |
| **Primary Lookup** | `HashMap<Integer, Employee>` | $O(1)$ Average | Instant index lookup by unique employee ID. |
| **Department Aggregation** | `Map.merge(dept, 1, Integer::sum)` | $O(1)$ Average | Clean, atomic accumulation replacing verbose `containsKey` / `get-and-put` boilerplate. |
| **Department Report** | `TreeMap<String, List<Employee>>` | $O(\log N)$ Insert | Red-Black tree maintaining keys naturally in alphabetical order for structured reporting. |
| **Natural Ordering** | `Comparable<Employee>` | $O(N \log N)$ Sort | Enforces natural sort sequence ascending by employee ID. |
| **Top Earners Chain** | `Comparator.comparingDouble().reversed().thenComparing()` | $O(N \log N)$ Sort | Multi-level criteria: Salary (DESC) $\rightarrow$ Name (ASC) $\rightarrow$ ID (ASC). |
| **Object Contract** | `equals()` + `hashCode()` | $O(1)$ Bucket Hashing | Crucial for object lookup consistency when objects serve as map keys or set elements. |

---

## 🐞 Missing-`hashCode()` Bug Case Study

### The Contract
According to the Java Language Specification and `java.lang.Object` contract:
> *If two objects are equal according to the `equals(Object)` method, then calling the `hashCode()` method on each of the two objects **must produce the same integer result**.*

### What Happens When You Violate the Contract?
In `BrokenKeyEmployee`, `equals()` compares `id` and `name`, but `hashCode()` was omitted. Consequently, it inherited `Object.hashCode()`, which calculates a 32-bit identity integer derived from memory address references.

```java
// BrokenKeyEmployee:
BrokenKeyEmployee k1 = new BrokenKeyEmployee(101, "Alice Walker");
BrokenKeyEmployee k2 = new BrokenKeyEmployee(101, "Alice Walker");

map.put(k1, "DATA");
map.get(k2); // Returns NULL!
```

### Why Does It Return `null`?
1. `map.put(k1, ...)` calculates `k1.hashCode()` (e.g. `935044096`) and places the entry in bucket `index = hash & (table.length - 1)`.
2. `map.get(k2)` calculates `k2.hashCode()` (e.g. `396180261`) which points to an entirely **different hash bucket**.
3. The HashMap never even evaluates `equals()` because the bucket is either empty or contains unrelated keys, resulting in a silent, difficult-to-detect lookup failure.

### The Fix
In `Employee.java`, both `equals()` and `hashCode()` are overridden using consistent fields:
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return this.id == employee.id;
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```
With this fix, `valid1.hashCode() == valid2.hashCode()` evaluates to true (`132 == 132`), routing lookups directly to the same bucket and retrieving the stored value accurately.

---

## 🏗️ Project Structure
```
day-8-collections-framework/
├── src/
│   └── com/
│       └── hcl/
│           └── employee/
│               ├── model/
│               │   └── Employee.java               # Domain model with Comparable, equals/hashCode
│               ├── demo/
│               │   └── BrokenKeyEmployee.java      # Anti-pattern demonstration (missing hashCode)
│               └── EmployeeDirectory.java          # Core service & comprehensive driver harness
└── README.md
```

---

## 🚀 How to Compile & Run

```bash
# Compile all source files into bin directory
javac -d bin src/com/hcl/employee/model/*.java src/com/hcl/employee/demo/*.java src/com/hcl/employee/EmployeeDirectory.java

# Run Employee Directory
java -cp bin com.hcl.employee.EmployeeDirectory
```

---

## 📊 Sample Execution Log

```text
========================================================================
       DAILY TASK 8: COLLECTIONS FRAMEWORK - EMPLOYEE DIRECTORY         
========================================================================

--- 1. HASHMAP O(1) LOOKUP BY ID ---
Lookup ID 103: EMP#103  | Grace Hopper       | Research        | Chief Research Scientist | $195,000.00

--- 2. FREQUENCY COUNT PER DEPARTMENT (Map.merge) ---
  Department: Engineering        | Headcount: 3
  Department: Finance            | Headcount: 2
  Department: Research           | Headcount: 2
  Department: Security           | Headcount: 1
  Department: Human Resources    | Headcount: 2

--- 3. TREEMAP DEPARTMENT REPORT (Naturally Alphabetical) ---
📁 ENGINEERING DEPARTMENT (Headcount: 3):
    • EMP#105  | Sarah Connor       | Engineering     | Lead DevOps Engineer | $145,000.00
    • EMP#101  | Alice Walker       | Engineering     | Principal Architect  | $175,000.00
    • EMP#108  | Bob Martin         | Engineering     | Staff Software Engineer | $155,000.00
    Total Department Payroll: $475,000.00 | Average: $158,333.33

📁 FINANCE DEPARTMENT (Headcount: 2):
    • EMP#102  | David Miller       | Finance         | Senior Financial Analyst | $110,000.00
    • EMP#106  | Emma Watson        | Finance         | VP of Finance        | $185,000.00
    Total Department Payroll: $295,000.00 | Average: $147,500.00

📁 HUMAN RESOURCES DEPARTMENT (Headcount: 2):
    • EMP#104  | John Doe           | Human Resources | HR Generalist        | $ 85,000.00
    • EMP#107  | Clara Oswald       | Human Resources | HR Director          | $130,000.00
    Total Department Payroll: $215,000.00 | Average: $107,500.00

📁 RESEARCH DEPARTMENT (Headcount: 2):
    • EMP#103  | Grace Hopper       | Research        | Chief Research Scientist | $195,000.00
    • EMP#109  | Alan Turing        | Research        | AI Research Fellow   | $190,000.00
    Total Department Payroll: $385,000.00 | Average: $192,500.00

📁 SECURITY DEPARTMENT (Headcount: 1):
    • EMP#110  | James Bond         | Security        | Head of Information Security | $160,000.00
    Total Department Payroll: $160,000.00 | Average: $160,000.00

--- 4. COMPARABLE BY ID (Natural Ascending Order) ---
  EMP#101  | Alice Walker       | Engineering     | Principal Architect  | $175,000.00
  EMP#102  | David Miller       | Finance         | Senior Financial Analyst | $110,000.00
  EMP#103  | Grace Hopper       | Research        | Chief Research Scientist | $195,000.00
  EMP#104  | John Doe           | Human Resources | HR Generalist        | $ 85,000.00
  EMP#105  | Sarah Connor       | Engineering     | Lead DevOps Engineer | $145,000.00
  EMP#106  | Emma Watson        | Finance         | VP of Finance        | $185,000.00
  EMP#107  | Clara Oswald       | Human Resources | HR Director          | $130,000.00
  EMP#108  | Bob Martin         | Engineering     | Staff Software Engineer | $155,000.00
  EMP#109  | Alan Turing        | Research        | AI Research Fellow   | $190,000.00
  EMP#110  | James Bond         | Security        | Head of Information Security | $160,000.00

--- 5. TOP-5 SALARY COMPARATOR CHAIN ---
Comparator Chain: Salary (DESC) -> Name (ASC) -> ID (ASC)
  [Rank #1] EMP#103  | Grace Hopper       | Research        | Chief Research Scientist | $195,000.00
  [Rank #2] EMP#109  | Alan Turing        | Research        | AI Research Fellow   | $190,000.00
  [Rank #3] EMP#106  | Emma Watson        | Finance         | VP of Finance        | $185,000.00
  [Rank #4] EMP#101  | Alice Walker       | Engineering     | Principal Architect  | $175,000.00
  [Rank #5] EMP#110  | James Bond         | Security        | Head of Information Security | $160,000.00

========================================================================
       FEATURE 6: MISSING-HASHCODE BUG DEMO AND FIX VERIFICATION        
========================================================================

--- [PART A: THE BUG (BrokenKeyEmployee)] ---
Object 1: BrokenKeyEmployee{id=101, name='Alice Walker'}
Object 2: BrokenKeyEmployee{id=101, name='Alice Walker'}
broken1.equals(broken2) : true (Objects are conceptually equal!)
System.identityHashCode(broken1): 935044096
System.identityHashCode(broken2): 396180261
broken1.hashCode() == broken2.hashCode(): false
Attempting lookup with broken2 key: brokenMap.get(broken2) -> null
🚨 BUG CONFIRMED: HashMap returned null even though keys are equal according to equals()!
   Root Cause: BrokenKeyEmployee inherited Object.hashCode(), generating different hash buckets.

--- [PART B: THE FIX (Employee with overridden hashCode)] ---
valid1.equals(valid2) : true
valid1.hashCode()     : 132
valid2.hashCode()     : 132
valid1.hashCode() == valid2.hashCode(): true
Attempting lookup with valid2 key: validMap.get(valid2) -> "CONFIDENTIAL_PAYROLL_DATA_ALICE"
✅ FIX VERIFIED: Consistent hashCode() hashes to the identical bucket, allowing successful retrieval!
========================================================================
```
