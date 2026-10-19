# Day 15: Optional & Modern Date/Time — Subscription Calculator

## 📌 Task Overview
The objective of Day 15 is to master modern Java date/time arithmetic and null-safety paradigms using the **`java.time` (JSR-310)** and **`java.util.Optional`** frameworks:
1. **Subscription Validity & Expiry Engine**: Calculate activation, duration, exact expiry dates, and remaining days using `LocalDate` and `ChronoUnit`.
2. **Grace Period Management**: Calculate post-expiration grace intervals before subscription termination.
3. **Cross-Timezone Normalization**: Perform timezone conversions across **UTC**, **IST (`Asia/Kolkata`)**, and **US EST (`America/New_York`)** using `ZonedDateTime` and `ZoneId`.
4. **`Optional` Refactoring**: Eliminate null-returning APIs across the lookup service and demonstrate `.orElseThrow()` with a custom exception.
5. **Strict Constraints**: **Zero usage of legacy `java.util.Date` or `Calendar`**; **strictly zero methods return `null`**.

---

## 🎯 Architectural Highlights & Contract Compliance

| Feature | Legacy Anti-Pattern (Eliminated) | Modern Java 21 Standard (Implemented) |
| :--- | :--- | :--- |
| **Date Modeling** | Mutable, thread-unsafe `java.util.Date` | Immutable, thread-safe `LocalDate` and `ZonedDateTime` |
| **Date Arithmetic** | Error-prone `Calendar.add()` | Declarative `startDate.plusDays(validityDays)` |
| **Duration Between Dates** | Raw millisecond subtraction `(t2 - t1) / 86400000` | `ChronoUnit.DAYS.between(today, expiryDate)` & `Period.between(...)` |
| **Timezone Conversions** | Legacy `SimpleDateFormat` | `utcTimestamp.withZoneSameInstant(ZoneId.of("Asia/Kolkata"))` |
| **Missing Values** | Returning `null` causing unexpected NPEs | Returning `Optional<Subscription>` and handling with `.orElseThrow()` |

---

## 🏗️ Project Structure
```
day-15-optional-date-time/
├── src/
│   └── com/
│       └── hcl/
│           └── datetime/
│               ├── model/
│               │   └── Subscription.java       # Immutable subscription entity
│               ├── exception/
│               │   └── SubscriptionNotFoundException.java # Custom checked exception
│               ├── service/
│               │   └── SubscriptionService.java # Refactored Optional service
│               └── SubscriptionCalculator.java  # Master calculator & timezone engine
└── README.md
```

---

## 🚀 How to Compile & Run

```bash
# 1. Compile all source files into bin directory
javac -d bin src/com/hcl/datetime/model/*.java src/com/hcl/datetime/exception/*.java src/com/hcl/datetime/service/*.java src/com/hcl/datetime/SubscriptionCalculator.java

# 2. Run the Subscription Calculator
java -cp bin com.hcl.datetime.SubscriptionCalculator
```

---

## 📊 Sample Execution Log

```text
========================================================================
   DAILY TASK 15: OPTIONAL & MODERN DATE/TIME (Subscription Calculator) 
========================================================================

System Evaluation Date: 2026-10-19

--- 1. SUBSCRIPTION VALIDITY, DAYS REMAINING & GRACE PERIOD ---
Active / Grace Subscriptions as of 2026-10-19 (3 found):

  • [SUB-101] User: alice@example.com    | Plan: MONTHLY   | Start: 2026-09-20 | Expiry: 2026-10-20 | Grace: 2026-10-25
    - Exact Expiry Date          : 2026-10-20
    - Days Remaining Until Expiry: 1 days
    - Remaining Period (Y-M-D)   : 0 year(s), 0 month(s), 1 day(s)
    - Is Active Today?           : true
    - Is in Grace Period?        : false (Grace ends 2026-10-25)

  • [SUB-102] User: bob@example.com      | Plan: QUARTERLY | Start: 2026-08-01 | Expiry: 2026-10-30 | Grace: 2026-11-06
    - Exact Expiry Date          : 2026-10-30
    - Days Remaining Until Expiry: 11 days
    - Remaining Period (Y-M-D)   : 0 year(s), 0 month(s), 11 day(s)
    - Is Active Today?           : true
    - Is in Grace Period?        : false (Grace ends 2026-11-06)

  • [SUB-103] User: charlie@example.com  | Plan: ANNUAL    | Start: 2026-01-01 | Expiry: 2027-01-01 | Grace: 2027-01-11
    - Exact Expiry Date          : 2027-01-01
    - Days Remaining Until Expiry: 74 days
    - Remaining Period (Y-M-D)   : 0 year(s), 2 month(s), 13 day(s)
    - Is Active Today?           : true
    - Is in Grace Period?        : false (Grace ends 2027-01-11)

--- 2. TIMEZONE CONVERSION ENGINE (UTC <-> IST <-> EST) ---
Converting Subscription Purchase Timestamp for SUB-101:
  🌐 Original UTC Timestamp : 2026-09-20 14:30:00 UTC
  🇮🇳 Converted to IST       : 2026-09-20 20:00:00 IST
  🇺🇸 Converted to US EST    : 2026-09-20 10:30:00 GMT-04:00

--- 3. REFACTORED LOOKUP: Optional & orElseThrow ---
Searching for existing ID 'SUB-102' via Optional:
  ✅ Found via Optional.map(): bob@example.com on QUARTERLY
  ✅ Masked Email via Optional.map(): al***@example.com

Attempting to query non-existent ID 'SUB-999' with orElseThrow():
  ✅ Caught Expected Custom Exception: SubscriptionNotFoundException
     Error Message: "Subscription with ID 'SUB-999' was not found in registry."

========================================================================
```
