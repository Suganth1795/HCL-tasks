package com.hcl.bank.model;

import java.util.Objects;

/**
 * Domain model representing a Bank Account.
 * Demonstrates:
 * - Strict encapsulation with private fields and controlled accessors
 * - Static counter for auto-generating unique account numbers
 * - 3 Chained constructors using this(...)
 * - Input validation in business methods (deposit, withdraw)
 * - Overridden equals() and hashCode() conforming to Java contract
 */
public class BankAccount {

    // Static counter shared across all instances
    private static long accountCounter = 100001L;

    // Instance fields with strict private encapsulation
    private final String accountNumber;
    private String accountHolderName;
    private double balance;
    private String accountType; // "SAVINGS", "CURRENT", "CHECKING"

    /**
     * Primary / No-Arg Constructor (Constructor 1)
     * Chains to Constructor 2 with default holder name.
     */
    public BankAccount() {
        this("Guest Customer");
    }

    /**
     * Single-parameter Constructor (Constructor 2)
     * Chains to Constructor 3 with default initial balance and default type.
     */
    public BankAccount(String accountHolderName) {
        this(accountHolderName, 0.0, "SAVINGS");
    }

    /**
     * Master Parameterized Constructor (Constructor 3)
     * Initializes all fields and increments the static counter.
     */
    public BankAccount(String accountHolderName, double initialBalance, String accountType) {
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }

        this.accountNumber = "HCL-" + (accountCounter++);
        this.accountHolderName = accountHolderName.trim();
        this.balance = initialBalance;
        this.accountType = (accountType == null || accountType.trim().isEmpty()) ? "SAVINGS" : accountType.toUpperCase();
    }

    // Static accessor for account counter
    public static long getTotalAccountsCreated() {
        return accountCounter - 100001L;
    }

    // Getters and validated setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        this.accountHolderName = accountHolderName.trim();
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        if (accountType != null && !accountType.trim().isEmpty()) {
            this.accountType = accountType.toUpperCase();
        }
    }

    /**
     * Deposits validated funds into the account.
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ [Validation Error] Deposit amount must be greater than $0.00.");
            return false;
        }
        this.balance += amount;
        return true;
    }

    /**
     * Withdraws validated funds from the account.
     * Fixed implementation (Free from faulty deduction bugs).
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ [Validation Error] Withdrawal amount must be greater than $0.00.");
            return false;
        }
        if (amount > this.balance) {
            System.out.printf("❌ [Transaction Error] Insufficient funds! Current: $%.2f, Requested: $%.2f\n", this.balance, amount);
            return false;
        }

        this.balance -= amount;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("BankAccount [Account No: %s | Holder: %-15s | Type: %-7s | Balance: $%,.2f]",
                accountNumber, accountHolderName, accountType, balance);
    }
}
