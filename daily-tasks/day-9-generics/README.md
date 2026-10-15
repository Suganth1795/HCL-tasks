# Day 9: Generics & Type-Safe Toolkit

## 📌 Task Overview
The objective of Day 9 is to implement a robust, type-safe **Generic Toolkit** in Java demonstrating advanced generic programming constructs: multi-type generic tuples (`Pair<K,V>`), bounded type parameters (`Box<T extends Number>`), generic bounded comparison methods (`<T extends Comparable<T>> max()`), wildcard variance under the **PECS** principle (*Producer Extends, Consumer Super*), robust IO with `try-with-resources` reading `products.csv`, and a custom `AutoCloseable` validating reverse LIFO close order.

---

## 🎯 Key Design Features

| Generic Feature | Implementation Signature | Principle & Purpose |
| :--- | :--- | :--- |
| **Generic Tuple** | `Pair<K, V>` | Multi-parameter container for type-safe key-value associations without raw Object casting. |
| **Bounded Class** | `Box<T extends Number>` | Restricts generic type to `Number` subclasses, enabling direct access to methods like `doubleValue()`. |
| **Bounded Method** | `<T extends Comparable<T>> T max(T a, T b)` | Restricts type parameter to mutually comparable types using self-referential bounds. |
| **Upper Bounded Wildcard** | `sumOfList(List<? extends Number>)` | **PECS (Producer Extends)**: Covariant, read-only collection traversal across `List<Integer>`, `List<Double>`, etc. |
| **Lower Bounded Wildcard** | `addIntegers(List<? super Integer>)` | **PECS (Consumer Super)**: Contravariant, write-supported collection modification across `List<Integer>`, `List<Number>`, `List<Object>`. |
| **Try-With-Resources** | `try (BufferedReader reader = ...)` | Deterministic resource management preventing socket/file descriptor leaks. |
| **AutoCloseable LIFO** | `try (R1 r1 = ...; R2 r2 = ...)` | Enforces reverse declaration close order ($R_3 \rightarrow R_2 \rightarrow R_1$) during block exit. |

---

## 💡 The PECS Principle (Producer Extends, Consumer Super)

1. **Producer Extends (`? extends T`)**:
   - Use `? extends` when your method only **reads/consumes** data produced by the collection.
   - Example:
     ```java
     public static double sumOfList(List<? extends Number> list) {
         double sum = 0.0;
         for (Number n : list) sum += n.doubleValue(); // Reading is safe!
         return sum;
     }
     ```
   - *Attempting to call `list.add(10)` causes a compile error because the exact subtype is unknown.*

2. **Consumer Super (`? super T`)**:
   - Use `? super` when your method only **writes/inserts** elements into the collection.
   - Example:
     ```java
     public static void addIntegers(List<? super Integer> list, int count) {
         for (int i = 1; i <= count; i++) {
             list.add(i * 10); // Writing an Integer is always safe!
         }
     }
     ```

---

## 🔄 Custom AutoCloseable & Deterministic LIFO Close Order

In Java's `try-with-resources` specification, resources declared in the header are closed in **reverse declaration order (LIFO)**. This prevents resource dependency faults (e.g., closing a database connection before closing the transaction stream running on that connection):

```java
try (CustomResource r1 = new CustomResource("DatabaseConnection");
     CustomResource r2 = new CustomResource("TransactionScope");
     CustomResource r3 = new CustomResource("AuditOutputStream")) {
    // operations...
}
```

### Observed Runtime Log:
```text
🟢 [OPENED] Resource: 'DatabaseConnection'
🟢 [OPENED] Resource: 'TransactionScope'
🟢 [OPENED] Resource: 'AuditOutputStream'

⚡ [WORKING] Executing transactional operations...

🔴 [CLOSED] Resource: 'AuditOutputStream' has been safely finalized and released.
🔴 [CLOSED] Resource: 'TransactionScope' has been safely finalized and released.
🔴 [CLOSED] Resource: 'DatabaseConnection' has been safely finalized and released.
```

---

## 🏗️ Project Structure
```
day-9-generics/
├── data/
│   └── products.csv                     # Sample product inventory dataset
├── src/
│   └── com/
│       └── hcl/
│           └── generics/
│               ├── model/
│               │   ├── Pair.java        # Generic Pair<K,V>
│               │   └── Box.java         # Bounded Box<T extends Number>
│               ├── resource/
│               │   └── CustomResource.java # Custom AutoCloseable implementation
│               └── GenericToolkit.java  # Bounded methods, wildcards, CSV reader & harness
└── README.md
```

---

## 🚀 How to Compile & Run (Strict No-Warning Compliance)

Compiled with strict compiler flags to guarantee **zero raw types and zero compiler warnings**:

```bash
# Compile with strict compiler warnings-as-errors
javac -Xlint:all -Werror -d bin src/com/hcl/generics/model/*.java src/com/hcl/generics/resource/*.java src/com/hcl/generics/GenericToolkit.java

# Run Generic Toolkit
java -cp bin com.hcl.generics.GenericToolkit
```

---

## 📊 Sample Execution Log

```text
========================================================================
           DAILY TASK 9: GENERICS & TYPE-SAFE TOOLKIT                   
========================================================================

--- 1. GENERIC Pair<K, V> DEMONSTRATION ---
Pair 1: Pair(key=Alice Walker, value=98) | Key=Alice Walker, Val=98
Pair 2: Pair(key=200, value=OK)
Pair 3: Pair(key=CPU_LOAD, value=[0.42, 0.55, 0.61])

--- 2. BOUNDED GENERIC Box<T extends Number> ---
Int Box   : Box[500 (Integer)] -> doubleValue: 500.0
Double Box: Box[750.85 (Double)] -> doubleValue: 750.85
Long Box  : Box[1000 (Long)] -> doubleValue: 1000.0
Is doubleBox > intBox? true
Sum of intBox + doubleBox = $1,250.85

--- 3. <T extends Comparable<T>> max() METHOD ---
max(100, 250)         : 250
max("Apple", "Zebra") : Zebra
max([19.99, 49.95, 120.00, 5.50]): 120.0

--- 4. WILDCARDS DEMONSTRATION (PECS: Producer Extends, Consumer Super) ---
Sum of List<Integer> via (? extends Number): 100.00
Sum of List<Double>  via (? extends Number): 7.50
  numberList after addIntegers() (size=4): [10, 20, 30, 40]

--- 5. CSV READER WITH TRY-WITH-RESOURCES (products.csv) ---
  📄 [CSV Header Read] -> productId,name,category,price,stock
Successfully parsed 5 product catalog entries:
  • UltraWide Gaming Monitor       -> $499.99
  • Mechanical Keyboard            -> $129.50
  • Ergonomic Office Chair         -> $349.00
  • Noise-Cancelling Headphones    -> $279.95
  • USB-C Thunderbolt Dock         -> $189.00

--- 4. AUTOCLOSEABLE DEMONSTRATION (LIFO CLOSE ORDER) ---
Declaring: R1 (DatabaseConnection) -> R2 (TransactionScope) -> R3 (AuditOutputStream)...
  🟢 [OPENED] Resource: 'DatabaseConnection'
  🟢 [OPENED] Resource: 'TransactionScope'
  🟢 [OPENED] Resource: 'AuditOutputStream'

  >> Inside try-block: Executing coordinated business transaction...
  ⚡ [WORKING] Executing transactional operations on 'DatabaseConnection'
  ⚡ [WORKING] Executing transactional operations on 'TransactionScope'
  ⚡ [WORKING] Executing transactional operations on 'AuditOutputStream'
  >> Exiting try-block now. Observe closing order below:

  🔴 [CLOSED] Resource: 'AuditOutputStream' has been safely finalized and released.
  🔴 [CLOSED] Resource: 'TransactionScope' has been safely finalized and released.
  🔴 [CLOSED] Resource: 'DatabaseConnection' has been safely finalized and released.
✅ All resources finalized without manual finally blocks!
```
