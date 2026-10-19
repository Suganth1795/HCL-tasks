# Day 13: Functional Programming — Product Filter Engine

## 📌 Task Overview
The objective of Day 13 is to build an extensible, declarative **Product Filter Engine** applying core functional programming paradigms in Java:
1. **Composed Predicates**: Combining atomic filters using `.and()`, `.or()`, and `.negate()`.
2. **Function Transformation Chains**: Composing multi-stage data pipelines using `Function.andThen()`.
3. **Consumer & Side Effects**: Formatting and streaming output using `Consumer` and method references.
4. **Supplier Fallback Generator**: Supplying lazily evaluated default objects when search criteria yield no match.
5. **Modern Syntax Guarantee**: Zero anonymous inner classes; all lambdas are rewritten as method references wherever applicable.

---

## 🎯 Functional Interfaces Architecture

| Functional Interface | Target Operation in Engine | Lambda / Method Reference Form | Functional Composition |
| :--- | :--- | :--- | :--- |
| **`Predicate<Product>`** | Stock Verification | `Product::isInStock` (Method Ref) | `isElectronics.and(under800).and(isInStock)` |
| **`Predicate<Product>`** | Out-of-Stock Filter | `isInStock.negate()` | Logical inverse of stock predicate |
| **`Function<Product, String>`** | Product Tag Generation | `p -> String.format(...)` | Combined via `.andThen(toUpperBanner).andThen(addBadge)` |
| **`Function<String, String>`** | Text Transformation | `String::toUpperCase` (Method Ref) | Pipeline intermediate stage |
| **`Consumer<Product>`** | Record Output | `System.out::println` (Method Ref) | Eliminates boilerplate loop logic |
| **`Supplier<Product>`** | Default Workstation Bundle | `() -> new Product("DEF-999", ...)` | Evaluated lazily on fallback |

---

## 🏗️ Project Structure
```
day-13-functional-programming/
├── src/
│   └── com/
│       └── hcl/
│           └── functional/
│               ├── model/
│               │   └── Product.java       # Product entity
│               └── FilterEngine.java      # Master functional engine & test harness
└── README.md
```

---

## 🚀 How to Compile & Run

```bash
# 1. Compile all source files into bin directory
javac -d bin src/com/hcl/functional/model/*.java src/com/hcl/functional/FilterEngine.java

# 2. Run the Filter Engine
java -cp bin com.hcl.functional.FilterEngine
```

---

## 📊 Sample Execution Log

```text
========================================================================
     DAILY TASK 13: FUNCTIONAL PROGRAMMING - PRODUCT FILTER ENGINE      
========================================================================

--- TOTAL CATALOGUE (8 items) ---
[P101] Dell UltraSharp 32-inch 4K | Electronics  | $ 749.99 | InStock: true  | Rating: 4.8★
[P102] Apple MacBook Air M3       | Electronics  | $1,099.00 | InStock: true  | Rating: 4.9★
[P103] Ergonomic Mesh Chair       | Furniture    | $ 299.50 | InStock: false | Rating: 4.3★
[P104] Solid Oak Standing Desk    | Furniture    | $ 550.00 | InStock: true  | Rating: 4.7★
[P105] Logitech MX Master 3S Mouse | Peripherals  | $  99.00 | InStock: true  | Rating: 4.9★
[P106] Keychron Q1 Pro Mechanical | Peripherals  | $ 199.00 | InStock: false | Rating: 4.6★
[P107] Sony WH-1000XM5 Headphones | Audio        | $ 399.00 | InStock: true  | Rating: 4.7★
[P108] Budget USB-C Earbuds       | Audio        | $  19.99 | InStock: true  | Rating: 3.8★

--- 1. COMPOSED PREDICATES FILTERING ---

Filter: [Electronics AND Price < $800 AND In-Stock]:
[P101] Dell UltraSharp 32-inch 4K | Electronics  | $ 749.99 | InStock: true  | Rating: 4.8★

Filter: [(Furniture OR Audio) AND In-Stock]:
[P104] Solid Oak Standing Desk    | Furniture    | $ 550.00 | InStock: true  | Rating: 4.7★
[P107] Sony WH-1000XM5 Headphones | Audio        | $ 399.00 | InStock: true  | Rating: 4.7★
[P108] Budget USB-C Earbuds       | Audio        | $  19.99 | InStock: true  | Rating: 3.8★

Filter: [Out of Stock (isInStock.negate())]:
[P103] Ergonomic Mesh Chair       | Furniture    | $ 299.50 | InStock: false | Rating: 4.3★
[P106] Keychron Q1 Pro Mechanical | Peripherals  | $ 199.00 | InStock: false | Rating: 4.6★

--- 2. FUNCTION CHAINS (andThen PIPELINE) ---
🏷️ FEATURED ITEM: [P101: DELL ULTRASHARP 32-INCH 4K ($749.99)]

--- 3. SUPPLIER FOR FALLBACK GENERATION ---
Query for Product < $10.00 (Not found, fallback triggered):
  • [DEF-999] Standard Ergonomic Workstation Bundle | Bundle       | $ 499.00 | InStock: true  | Rating: 4.5★
========================================================================
```
