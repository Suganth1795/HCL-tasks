package com.hcl.bank.service;

import com.hcl.bank.model.BankAccount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service layer orchestrating banking operations and business rules.
 */
public class BankService {

    private final List<BankAccount> accounts;

    public BankService() {
        this.accounts = new ArrayList<>();
    }

    public BankAccount createAccount(String holderName, double initialBalance, String accountType) {
        BankAccount account = new BankAccount(holderName, initialBalance, accountType);
        accounts.add(account);
        return account;
    }

    public BankAccount createAccount(String holderName) {
        BankAccount account = new BankAccount(holderName);
        accounts.add(account);
        return account;
    }

    public BankAccount findAccountByNumber(String accountNumber) {
        if (accountNumber == null) return null;
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equalsIgnoreCase(accountNumber.trim())) {
                return acc;
            }
        }
        return null;
    }

    public boolean transferFunds(String sourceAccNo, String targetAccNo, double amount) {
        BankAccount source = findAccountByNumber(sourceAccNo);
        BankAccount target = findAccountByNumber(targetAccNo);

        if (source == null) {
            System.out.println("❌ Source account not found: " + sourceAccNo);
            return false;
        }
        if (target == null) {
            System.out.println("❌ Target account not found: " + targetAccNo);
            return false;
        }
        if (source.equals(target)) {
            System.out.println("❌ Cannot transfer funds to the same account.");
            return false;
        }

        if (source.withdraw(amount)) {
            if (target.deposit(amount)) {
                System.out.printf("✅ Successfully transferred $%,.2f from %s to %s\n", amount, sourceAccNo, targetAccNo);
                return true;
            } else {
                // Rollback withdrawal on deposit failure
                source.deposit(amount);
                System.out.println("❌ Transfer failed during credit. Rolled back debit.");
                return false;
            }
        }
        return false;
    }

    public List<BankAccount> getAllAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}
