# Day 17: Modern Java (9–21) Features & Clean APIs

## Objective
Implement and verify modern Java platform advancements introduced from Java 9 through Java 21, including:
1. **Sealed Types & Records**: Enforcing restricted domain hierarchies with compile-time type exhaustiveness.
2. **Pattern Matching for Switch**: Deconstructing records with type patterns and guarded `when` clauses.
3. **Text Blocks (`"""..."""`)**: Clean multi-line string templates with `.formatted()`.
4. **Immutable Collections (`List.of`, `Set.of`, `Map.of`)**: Zero-overhead unmodifiable collections and validating `UnsupportedOperationException`.
5. **Modern String Enhancements**: Unicode-aware `strip()`, whitespace detection via `isBlank()`, lazy line streaming via `lines()`, and `repeat()`.

---

## Directory Structure

```
day-17-java-9-21-features/
├── src/
│   └── com/hcl/modern/
│       ├── Shape.java                 # Sealed interface permitting Circle, Rectangle
│       ├── Circle.java                # Immutable record implementing Shape
│       ├── Rectangle.java             # Immutable record with isSquare() helper
│       ├── Invoice.java               # Record using Text Block for email notification
│       └── ModernJavaDemo.java        # Comprehensive feature demonstration suite
└── README.md
```

---

## Key Java Platform Features Demonstrated

### 1. Sealed Interface & Pattern Matching Switch (Java 17/21)
```java
public sealed interface Shape permits Circle, Rectangle {
    double calculateArea();
}

public record Circle(double radius) implements Shape { ... }
public record Rectangle(double width, double height) implements Shape { ... }

// Exhaustive pattern matching switch with when guards
String description = switch (shape) {
    case Circle c -> String.format("Circle(radius=%.1f) -> Area = %.2f", c.radius(), Math.PI * c.radius() * c.radius());
    case Rectangle r when r.isSquare() -> String.format("Square(side=%.1f) -> Area = %.2f (Detected via when-guard)", r.width(), r.calculateArea());
    case Rectangle r -> String.format("Rectangle(w=%.1f, h=%.1f) -> Area = %.2f", r.width(), r.height(), r.calculateArea());
    // No default branch needed: sealed hierarchy guarantees exhaustiveness
};
```

### 2. Multi-line Text Blocks (Java 15)
Text blocks eliminate noisy string concatenations (`+ "\n"`) and escape characters, preserving indentation cleanly:
```java
public String generateEmailTemplate() {
    return """
    ========================================================================
                         OFFICIAL TAX INVOICE
    ========================================================================
    Invoice ID   : %s
    Issue Date   : %s
    Due Date     : %s
    ...
    """.formatted(invoiceNumber, issueDate, dueDate, ...);
}
```

### 3. Immutable Collections & Defensive Design (Java 9)
```java
List<String> frameworks = List.of("Spring Boot", "Micronaut", "Quarkus");
// Invoking frameworks.add("Helidon") throws java.lang.UnsupportedOperationException
```
- Guarantees thread-safety without external synchronization.
- Space-efficient implementation avoiding overhead of `ArrayList` resizing buffers.
- Null-hostile: Throws `NullPointerException` immediately upon creation if any element is null.

### 4. Modern String Operations (Java 11+)
- **`strip()` vs `trim()`**: `trim()` only removes ASCII spaces ($\le \text{U+0020}$); `strip()` uses `Character.isWhitespace()` to strip full Unicode spaces (`\u2000`, `\u2003`, etc.).
- **`isBlank()`**: Returns `true` if string is empty or contains only whitespace codepoints.
- **`lines()`**: Returns a `Stream<String>` extracted lazily by line terminators (`\n`, `\r\n`).
- **`repeat(n)`**: Duplicates string content $n$ times efficiently.

---

## Execution Output

```
================================================================================
DAY 17: JAVA 9-21 MODERN FEATURES & MODERN APIS DRILL
================================================================================

--------------------------------------------------------------------------------
1. SEALED TYPES & EXHAUSTIVE PATTERN MATCHING SWITCH
--------------------------------------------------------------------------------
  Circle(radius=5.0) -> Area = 78.54
  Rectangle(w=4.0, h=6.0) -> Area = 24.00
  Square(side=5.0) -> Area = 25.00 (Detected via when-guard)

--------------------------------------------------------------------------------
2. TEXT BLOCKS & MULTI-LINE TEMPLATE GENERATION
--------------------------------------------------------------------------------
========================================================================
                     OFFICIAL TAX INVOICE
========================================================================
Invoice ID   : INV-2026-HCL-091
Issue Date   : 2026-10-19
Due Date     : 2026-11-03
------------------------------------------------------------------------
Billed To:
  Client Name: Global Logistics Corp
------------------------------------------------------------------------
Line Items:
  Service    : Cloud Architecture Consulting (Sprint 12)
  Quantity   : 40 units
  Unit Rate  : $125.00
------------------------------------------------------------------------
TOTAL AMOUNT DUE: $5000.00
------------------------------------------------------------------------
Payment Instructions:
  Please transfer payment before 2026-11-03 to:
  Bank   : HCL Corporate Treasury
  A/C    : 987654321098
  SWIFT  : HCLTINBBXXX

For billing discrepancies, contact support-finance@hcltech.com.
========================================================================

--------------------------------------------------------------------------------
3. IMMUTABLE COLLECTIONS (List.of, Set.of, Map.of) & INTEGRITY
--------------------------------------------------------------------------------
Created Immutable List: [Spring Boot, Micronaut, Quarkus]
Class Type: java.util.ImmutableCollections$ListN
Attempting to mutate List.of via .add("Helidon"): SUCCESSFULLY BLOCKED!
  -> Caught expected java.lang.UnsupportedOperationException.
  -> Reason: List.of creates an unmodifiable, space-efficient, null-hostile list.
Immutable Set: [Azure, GCP, AWS]
Immutable Map: {SSH=22, HTTP=80, HTTPS=443}

--------------------------------------------------------------------------------
4. MODERN STRING APIS (strip, isBlank, lines, repeat)
--------------------------------------------------------------------------------
Raw Input String length: 31
 - trim() result:  [?  	  HCL Modern Java Suite  ?] (length: 30, misses unicode spaces)
 - strip() result: [HCL Modern Java Suite] (length: 21, cleans ALL unicode spaces)
 - String "": isEmpty()=true, isBlank()=true
 - String "   \t\n  ": isEmpty()=false, isBlank()=true
 - lines() stream count: 3 lines
 - repeat(40): ========================================
```

---

## How to Run

```bash
javac -d bin src/com/hcl/modern/*.java
java -cp bin com.hcl.modern.ModernJavaDemo
```
