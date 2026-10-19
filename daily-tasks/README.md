# 🚀 HCL Core Java & Engineering Track - Daily Hands-on Tasks

This repository tracks the sequential daily hands-on coding tasks for the **HCL Java Full Stack Training Program (Weeks 1 to 4 / Sprints 1 & 2)**.

---

## 🎯 Program Objective
The daily tasks build progressive, real-world mastery in modern Java development (Java 21 LTS), software engineering best practices, Maven project structures, object-oriented design patterns, the Java Collections Framework, Generics, JVM memory management, AI-assisted development auditing, functional programming & streams, modern Java 9–21 features, concurrency, and relational database systems.

---

## 🛠️ Technologies & Tools Used
- **Language**: Java 21 (LTS)
- **Build Tool**: Apache Maven (with multi-profile builds)
- **Database**: MySQL / Relational Database Design (3NF)
- **Version Control**: Git & GitHub
- **IDE / Environment**: Antigravity / VS Code / IntelliJ / Terminal CLI
- **Key Concepts**: JVM Architecture, Memory Management (-Xss, -Xmx, GC Logging), Collections, Generics, AI Code Auditing & Prompt Engineering, Lambdas, Functional Interfaces, Streams API, java.time (JSR-310), Optional, CompletableFuture, Modern Java (Records, Pattern Matching, Text Blocks), SQL CRUD

---

## 📅 Daily Progress Tracker

| Day | Task | Topic | Status | Short Description | Git Commit Message |
| :--- | :--- | :--- | :---: | :--- | :--- |
| **Day 1** | Daily Task 1 | Java Platform Basics + Agile/Scrum | ✅ Completed | JDK 21 installation, JVM PlatformInfo inspector, bytecode disassembly with `javap -c` | `day-1: java-platform-basics` |
| **Day 2** | Daily Task 2 | Language Fundamentals + Git Fundamentals | ✅ Completed | Monthly Usage Analyser with 1D/2D arrays, slab billing constants, overflow prevention, ternary grades | `day-2: language-fundamentals` |
| **Day 3** | Daily Task 3 | Control Flow + Maven | ✅ Completed | ATM Simulator console application with PIN verification, switch-menu, transaction statement, Maven packaging & dev/prod profiles | `day-3: control-flow` |
| **Day 4** | Daily Task 4 | OOP Concepts + IDE & Debugging | ✅ Completed | BankAccount domain model with constructor chaining, encapsulation, debugger watchpoints & hot code replace | `day-4: oop-concepts` |
| **Day 5** | Daily Task 5 | Inheritance & Polymorphism + Branching | ✅ Completed | Payment hierarchy (Card/UPI/Cash), Refundable interface, Git conflict resolution simulation & rebase workflow | `day-5: inheritance-polymorphism` |
| **Day 6** | Daily Task 6 | Exception Handling + AI Debugging | ✅ Completed | Custom checked/unchecked exceptions, chained causes, multi-catch, guaranteed finally audit, and AI error analysis | `day-6: exception-handling` |
| **Day 7** | Daily Task 7 | Collections Framework — Part 1 | ✅ Completed | Library Waitlist (ArrayList, LinkedHashSet, ArrayDeque, PriorityQueue, Iterator.remove) + timing benchmark table | `day-7: collections-framework` |
| **Day 8** | Daily Task 8 | Collections Framework — Part 2 | ✅ Completed | Employee Directory (HashMap, Map.merge, TreeMap report, Comparable, Comparator chain, equals/hashCode demo) | `day-8: collections-framework` |
| **Day 9** | Daily Task 9 | Generics | ✅ Completed | Generic Toolkit (Pair, Box<T extends Number>, Comparable max(), wildcards PECS, products.csv try-with-resources, AutoCloseable) | `day-9: generics` |
| **Day 10** | Daily Task 10 | Memory Management | ✅ Completed | Memory Lab: StackOverflowError depth with -Xss, OutOfMemoryError with -Xmx64m, String += vs StringBuilder, -Xlog:gc, Integer cache pitfall | `day-10: memory-management` |
| **Day 11** | Daily Task 11 | AI Integration (Core Java) | ✅ Completed | Prompt & Review Drill: Student POJO comparison, StudentService review with 5 AI-generated issues identified & fixed | `day-11: ai-integration-core-java` |
| **Day 12** | Daily Task 12 | Assessment 1 — Core Java Checkpoint | ✅ Completed | Core Java 10-question warm-up quiz & practical console app implementing 2 core flows (100/100 on A1 rubric) | `day-12: assessment-1` |
| **Day 13** | Daily Task 13 | Functional Programming | ✅ Completed | Product Filter Engine: composed Predicates, Function chains, Consumer, Supplier, and method references | `day-13: functional-programming` |
| **Day 14** | Daily Task 14 | Streams API | ✅ Completed | Sales Analytics: revenue by category (groupingBy + summingDouble), top 3 customers, partitioned orders, sequential vs parallel benchmark | `day-14: streams-api` |
| **Day 15** | Daily Task 15 | Optional & Date/Time | ✅ Completed | Subscription Calculator: expiry, days remaining, grace periods, IST/UTC conversions, Optional refactoring | `day-15: optional-date-time` |
| **Day 16** | Daily Task 16 | Concurrency Essentials | ⏳ Pending | Price Aggregator: ExecutorService + Callable, CompletableFuture.allOf, exceptionally handling, timing comparison | `day-16: concurrency-essentials` |
| **Day 17** | Daily Task 17 | Java 9–21 Features + Modern APIs | ⏳ Pending | Shape & Invoice Lab: sealed interface, records, pattern-matching switch, text blocks, List.of immutability, strip/isBlank | `day-17: java-9-21-features` |
| **Day 18** | Daily Task 18 | Relational Database Concepts | ⏳ Pending | Normalisation Workshop: college_registrations spreadsheet normalized step-by-step to 3NF + ER diagram | `day-18: relational-database-concepts` |
| **Day 19** | Daily Task 19 | CRUD Operations | ⏳ Pending | MySQL employees/departments database, 30 rows, 12 filtering/sorting queries, safe transaction updates | `day-19: crud-operations` |

---

## 📁 Repository Directory Structure

```
daily-tasks/
│
├── README.md                                 # Overall Daily Tasks Tracker & Master Documentation
│
├── day-1-java-platform-basics/               # Day 1: JVM & Platform Introspection
├── day-2-language-fundamentals/              # Day 2: Array Manipulation & Slab Calculation
├── day-3-control-flow/                       # Day 3: ATM Simulator & Maven Build (dev/prod profiles)
├── day-4-oop-concepts/                       # Day 4: Bank Account Domain & Debugging Walkthrough
├── day-5-inheritance-polymorphism/           # Day 5: Payment Processing & Polymorphism Hierarchy
├── day-6-exception-handling/                 # Day 6: Order Processor & Robust Exceptions
├── day-7-collections-framework/              # Day 7: Library Waitlist & Collection Performance
├── day-8-collections-framework/              # Day 8: Employee Directory & Map Architectures
├── day-9-generics/                           # Day 9: Type-safe Generic Toolkit & IO
├── day-10-memory-management/                 # Day 10: JVM Memory Lab (-Xss, -Xmx, GC logs, Integer Cache)
├── day-11-ai-integration-core-java/          # Day 11: AI Prompting & Code Review Drill
├── day-12-assessment-1/                      # Day 12: Core Java Checkpoint & Practical App
├── day-13-functional-programming/            # Day 13: Functional Interfaces & Predicate Engine
├── day-14-streams-api/                       # Day 14: Sales Analytics & Parallel Streams
│
├── day-15-optional-date-time/                # Day 15: Subscription Calculator & java.time
│   ├── src/com/hcl/datetime/
│   │   ├── model/Subscription.java
│   │   ├── exception/SubscriptionNotFoundException.java
│   │   ├── service/SubscriptionService.java
│   │   └── SubscriptionCalculator.java
│   └── README.md
│
├── day-16-concurrency-essentials/            # Day 16: Price Aggregator & CompletableFuture (Upcoming)
├── day-17-java-9-21-features/                # Day 17: Modern Java Records, Pattern Matching & Text Blocks (Upcoming)
├── day-18-relational-database-concepts/      # Day 18: Normalization Workshop to 3NF (Upcoming)
└── day-19-crud-operations/                   # Day 19: MySQL Schema & Filtering Queries (Upcoming)
```
