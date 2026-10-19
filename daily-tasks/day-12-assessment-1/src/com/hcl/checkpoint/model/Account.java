package com.hcl.checkpoint.model;

import java.util.Objects;

/**
 * Domain entity representing an Account for Assessment 1 Checkpoint.
 */
public class Account implements Comparable<Account> {

    private final String accountId;
    private final String holderName;
    private final String role; // "VIP", "REGULAR"
    private double balance;

    public Account(String accountId, String holderName, String role, double initialBalance) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty.");
        }
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be null or empty.");
        }
        if (initialBalance < 0.0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }

        this.accountId = accountId.trim().toUpperCase();
        this.holderName = holderName.trim();
        this.role = (role == null || role.trim().isEmpty()) ? "REGULAR" : role.trim().toUpperCase();
        this.balance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getRole() {
        return role;
    }

    public double getBalance() {
        return balance;
    }

    public synchronized void credit(double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Credit amount must be positive.");
        }
        this.balance += amount;
    }

    public synchronized void debit(double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Debit amount must be positive.");
        }
        if (amount > this.balance) {
            throw new IllegalStateException("Insufficient funds! Balance: $" + balance + ", Requested: $" + amount);
        }
        this.balance -= amount;
    }

    @Override
    public int compareTo(Account other) {
        return this.accountId.compareTo(other.accountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-16s | Role: %-7s | Balance: $%,10.2f",
                accountId, holderName, role, balance);
    }
}
