# Day 3: Control Flow & Maven Project Setup

## 📌 Task Overview
The objective of Day 3 is to construct an **ATM Simulator** demonstrating core Java control flow constructs (`do-while`, `switch-case`, `break`, `continue`, and enhanced `for-each` loop) along with input validation and a standard Apache Maven project structure featuring environment-specific build profiles (`dev` and `prod`).

---

## 🎯 Key Requirements & Implementation Details

1. **Robust Input Validation**:
   - Program validates token types using `Scanner.hasNextInt()` and `hasNextDouble()`.
   - Never crashes on malformed inputs (e.g., text, invalid numbers, symbols).
2. **PIN Security with Attempt Limit**:
   - Limits users to **3 PIN attempts**.
   - Uses `break` to exit upon successful verification and card lockout upon 3 failed attempts.
3. **Interactive Menu via `do-while` & `switch`**:
   - `do-while` loop renders menu and re-prompts until the user chooses Exit (Option 5).
   - `continue` skips current cycle and re-renders menu on invalid selection.
4. **Enhanced-For Loop for Mini-Statement**:
   - Uses `for (String tx : transactionHistory)` to iterate and display transaction log.
5. **Maven Build Profiles**:
   - `dev` profile (active by default): Sets environment to `DEVELOPMENT`, max withdrawal to `$10,000`.
   - `prod` profile: Sets environment to `PRODUCTION`, max withdrawal to `$50,000`.
   - Maven resource filtering injects properties into `application.properties` during packaging.

---

## 🏗️ Project Structure
```
day-3-control-flow/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── hcl/
│   │   │           └── atm/
│   │   │               └── ATMSimulator.java
│   │   └── resources/
│   │       └── application.properties
└── README.md
```

---

## 🚀 How to Build & Run with Maven

### 1. Build & Package (Dev Profile - Default)
```bash
mvn clean package
```

### 2. Build & Package (Production Profile)
```bash
mvn clean package -Pprod
```

### 3. Run Packaged JAR
```bash
java -jar target/day-3-control-flow-1.0.0.jar
```

---

## 📊 Sample Execution Log

```text
===============================================================
            WELCOME TO HCL BANK - DEV SANDBOX
            Environment: [DEVELOPMENT]
===============================================================

Enter your 4-digit security PIN (Attempt 1/3): 1234
✅ PIN Verified Successfully! Access Granted.

---------------- ATM MAIN MENU ----------------
1. Check Account Balance
2. Deposit Funds
3. Withdraw Cash
4. Mini-Statement (Recent Transactions)
5. Exit & Eject Card
-----------------------------------------------
Select an option (1-5): 1

💰 Current Available Balance: $15,000.00

---------------- ATM MAIN MENU ----------------
1. Check Account Balance
2. Deposit Funds
3. Withdraw Cash
4. Mini-Statement (Recent Transactions)
5. Exit & Eject Card
-----------------------------------------------
Select an option (1-5): 2

Enter deposit amount ($): 2500
✅ Successfully deposited $2,500.00. New Balance: $17,500.00

---------------- ATM MAIN MENU ----------------
1. Check Account Balance
2. Deposit Funds
3. Withdraw Cash
4. Mini-Statement (Recent Transactions)
5. Exit & Eject Card
-----------------------------------------------
Select an option (1-5): 3

Enter withdrawal amount ($) [Max Limit per Tx: $10,000.00]: 1200
✅ Successfully dispensed $1,200.00. Remaining Balance: $16,300.00

---------------- ATM MAIN MENU ----------------
1. Check Account Balance
2. Deposit Funds
3. Withdraw Cash
4. Mini-Statement (Recent Transactions)
5. Exit & Eject Card
-----------------------------------------------
Select an option (1-5): 4

============ MINI-STATEMENT (TRANSACTION HISTORY) ============
[01] ACCOUNT OPENED: Initial Balance = $15000.00
[02] DEPOSIT : +$2500.00 | Balance = $17500.00
[03] WITHDRAW: -$1200.00 | Balance = $16300.00
==============================================================

---------------- ATM MAIN MENU ----------------
1. Check Account Balance
2. Deposit Funds
3. Withdraw Cash
4. Mini-Statement (Recent Transactions)
5. Exit & Eject Card
-----------------------------------------------
Select an option (1-5): 5

👋 Thank you for banking with HCL Bank - Dev Sandbox. Have a great day!
```
