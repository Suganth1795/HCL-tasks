package com.hcl.bank.app;

import com.hcl.bank.model.BankAccount;
import com.hcl.bank.service.BankService;

/**
 * Driver Application for Day 4: OOP Concepts & Debugging Demonstration.
 * Demonstrates:
 * 1. Constructor chaining across all 3 constructors
 * 2. Strict encapsulation and validation
 * 3. Static counter verification
 * 4. equals() / hashCode() contract
 * 5. Simulation of business operations and debugging verification
 */
public class BankApplication {

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("         DAILY TASK 4: OOP CONCEPTS & BANK DOMAIN              ");
        System.out.println("===============================================================\n");

        BankService bankService = new BankService();

        // 1. Constructor Chaining Demonstration
        System.out.println("--- 1. CONSTRUCTOR CHAINING DEMONSTRATION ---");
        BankAccount acc1 = new BankAccount(); // Constructor 1 -> chains to 2 -> chains to 3
        BankAccount acc2 = new BankAccount("Alice Smith"); // Constructor 2 -> chains to 3
        BankAccount acc3 = new BankAccount("Bob Johnson", 5000.0, "CURRENT"); // Constructor 3

        System.out.println("Account 1 (Default): " + acc1);
        System.out.println("Account 2 (Chained): " + acc2);
        System.out.println("Account 3 (Full)   : " + acc3);
        System.out.println("Total Accounts Created (Static Counter): " + BankAccount.getTotalAccountsCreated());

        // 2. Encapsulation & Validated Operations
        System.out.println("\n--- 2. ENCAPSULATION & TRANSACTION VALIDATION ---");
        System.out.println("Depositing $1,200.00 into Account 2...");
        acc2.deposit(1200.0);
        System.out.println("Account 2 updated: " + acc2);

        System.out.println("\nAttempting invalid negative deposit (-$500.00)...");
        acc2.deposit(-500.0);

        System.out.println("\nAttempting valid withdrawal ($400.00) from Account 2...");
        acc2.withdraw(400.0);
        System.out.println("Account 2 after withdrawal: " + acc2);

        System.out.println("\nAttempting overdraft withdrawal ($2,000.00) from Account 2...");
        acc2.withdraw(2000.0);

        // 3. Equals and HashCode Contract
        System.out.println("\n--- 3. EQUALS & HASHCODE CONTRACT ---");
        BankAccount acc4 = bankService.createAccount("Alice Smith", 800.0, "SAVINGS");
        System.out.println("acc2 Number: " + acc2.getAccountNumber());
        System.out.println("acc4 Number: " + acc4.getAccountNumber());
        System.out.println("acc2.equals(acc4) [Different Account Numbers]: " + acc2.equals(acc4));
        System.out.println("acc2.equals(acc2) [Same Instance]: " + acc2.equals(acc2));

        // 4. Inter-Account Fund Transfer Service
        System.out.println("\n--- 4. INTER-ACCOUNT FUND TRANSFER ---");
        BankAccount accCharlie = bankService.createAccount("Charlie Brown", 10000.0, "SAVINGS");
        BankAccount accDiana = bankService.createAccount("Diana Prince", 2500.0, "SAVINGS");

        System.out.printf("Transferring $1,500.00 from %s (Charlie) to %s (Diana)...\n",
                accCharlie.getAccountNumber(), accDiana.getAccountNumber());
        bankService.transferFunds(accCharlie.getAccountNumber(), accDiana.getAccountNumber(), 1500.0);

        System.out.println("\n--- FINAL ACCOUNT REGISTRY (BANK SERVICE) ---");
        for (BankAccount acc : bankService.getAllAccounts()) {
            System.out.println("  • " + acc);
        }
    }
}
