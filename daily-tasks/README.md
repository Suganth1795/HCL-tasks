# 🚀 HCL Core Java & Engineering Track - Daily Hands-on Tasks

This repository tracks the sequential daily hands-on coding tasks for the **HCL Java Full Stack Training Program (Week 1 / Sprint 1)**.

---

## 🎯 Program Objective
The daily tasks are designed to build progressive competency in modern Java development (Java 21 LTS), software engineering best practices, Maven project structures, object-oriented design patterns, Git workflow management, clean code principles, and debugging methodologies.

---

## 🛠️ Technologies & Tools Used
- **Language**: Java 21 (LTS)
- **Build Tool**: Apache Maven (with multi-profile builds)
- **Version Control**: Git & GitHub
- **IDE / Environment**: Antigravity / VS Code / IntelliJ / Terminal CLI
- **Key Concepts**: JVM Architecture, Language Fundamentals, Control Flow, OOP & Design Patterns, Exception Handling, AI-Assisted Debugging

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

---

## 📁 Repository Directory Structure

```
daily-tasks/
│
├── README.md                                 # Overall Daily Tasks Tracker & Master Documentation
│
├── day-1-java-platform-basics/               # Day 1: JVM & Platform Introspection
│   ├── PlatformInfo.java
│   ├── README.md
│   └── screenshots/
│
├── day-2-language-fundamentals/              # Day 2: Array Manipulation & Slab Calculation
│   ├── MonthlyUsageAnalyzer.java
│   └── README.md
│
├── day-3-control-flow/                       # Day 3: ATM Simulator & Maven Build (dev/prod profiles)
│   ├── pom.xml
│   ├── src/main/java/com/hcl/atm/ATMSimulator.java
│   ├── src/main/resources/application.properties
│   └── README.md
│
├── day-4-oop-concepts/                       # Day 4: Bank Account Domain & Debugging Walkthrough
│   ├── src/com/hcl/bank/
│   │   ├── model/BankAccount.java
│   │   ├── service/BankService.java
│   │   └── app/BankApplication.java
│   └── README.md
│
├── day-5-inheritance-polymorphism/           # Day 5: Payment Processing & Polymorphism Hierarchy
│   ├── src/com/hcl/payment/
│   │   ├── model/
│   │   │   ├── Payment.java (Abstract)
│   │   │   ├── Refundable.java (Interface)
│   │   │   ├── CreditCardPayment.java
│   │   │   ├── UPIPayment.java
│   │   │   └── CashPayment.java
│   │   ├── service/PaymentProcessor.java
│   │   └── app/PaymentApplication.java
│   └── README.md
│
└── day-6-exception-handling/                 # Day 6: Order Processor & Robust Exceptions
    ├── src/com/hcl/order/
    │   ├── exception/
    │   │   ├── InsufficientStockException.java (Checked)
    │   │   ├── InvalidQuantityException.java (Unchecked)
    │   │   ├── PaymentDeclinedException.java (Checked)
    │   │   └── OrderProcessingException.java (Chained wrapper)
    │   ├── model/
    │   │   ├── Product.java
    │   │   └── Order.java
    │   ├── service/OrderProcessor.java
    │   └── app/OrderApplication.java
    └── README.md
```
