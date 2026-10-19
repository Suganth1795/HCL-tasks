package com.hcl.checkpoint.app;

import com.hcl.checkpoint.exception.CheckpointException;
import com.hcl.checkpoint.model.Account;
import com.hcl.checkpoint.model.Transaction;
import com.hcl.checkpoint.service.CheckpointService;

import java.util.Map;

/**
 * Driver Application for Assessment 1: Core Java Checkpoint.
 * Demonstrates:
 * Flow 1: Account registration, validation, O(1) retrieval, and naturally sorted report.
 * Flow 2: VIP-first priority transaction processing, safe atomic transfers, and ledger auditing.
 */
public class CheckpointApp {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("       ASSESSMENT 1: CORE JAVA CHECKPOINT - CONSOLE APPLICATION         ");
        System.out.println("========================================================================\n");

        CheckpointService service = new CheckpointService();

        // =====================================================================
        // CORE FLOW 1: ACCOUNT LIFECYCLE MANAGEMENT
        // =====================================================================
        System.out.println("--- CORE FLOW 1: ACCOUNT REGISTRATION & LOOKUP ---");
        try {
            service.registerAccount(new Account("ACC-101", "Alice Walker", "VIP", 5000.00));
            service.registerAccount(new Account("ACC-103", "Charlie Day", "REGULAR", 1200.00));
            service.registerAccount(new Account("ACC-102", "Bob Martin", "REGULAR", 2500.00));
            service.registerAccount(new Account("ACC-104", "Diana Prince", "VIP", 15000.00));
            System.out.println("✅ Registered 4 accounts successfully.");

            // Test duplicate prevention
            System.out.print("Testing duplicate account prevention (ACC-101)... ");
            try {
                service.registerAccount(new Account("ACC-101", "Duplicate Alice", "REGULAR", 100.0));
            } catch (CheckpointException e) {
                System.out.println("Caught expected exception: " + e.getMessage());
            }

            // Print Naturally Sorted Accounts Report (TreeMap)
            System.out.println("\nNaturally Sorted Account Registry:");
            for (Map.Entry<String, Account> entry : service.getSortedAccountsReport().entrySet()) {
                System.out.println("  • " + entry.getValue());
            }

        } catch (CheckpointException e) {
            System.err.println("Fatal setup error: " + e.getMessage());
        }

        // =====================================================================
        // CORE FLOW 2: TRANSACTION PROCESSING & VIP DISPATCH
        // =====================================================================
        System.out.println("\n--- CORE FLOW 2: TRANSACTION PROCESSING & VIP-FIRST DISPATCH ---");
        try {
            // Regular submitted first
            service.submitTransaction(new Transaction("ACC-103", null, Transaction.Type.DEPOSIT, 300.00, false));
            // Regular transfer submitted second
            service.submitTransaction(new Transaction("ACC-102", "ACC-103", Transaction.Type.TRANSFER, 500.00, false));
            // VIP submitted LAST (Should jump to the front of the queue!)
            service.submitTransaction(new Transaction("ACC-104", "ACC-101", Transaction.Type.TRANSFER, 2000.00, true));

            System.out.printf("Enqueued %d transactions into PriorityQueue.\n", service.getPendingQueueSize());
            System.out.println("Processing queue (Notice VIP transaction is dispatched FIRST):\n");

            int step = 1;
            while (service.getPendingQueueSize() > 0) {
                try {
                    Transaction processed = service.processNextTransaction();
                    System.out.printf("  [Step %d] Processed: %s\n", step++, processed);
                } catch (CheckpointException ex) {
                    System.out.printf("  [Step %d] Failed: %s\n", step++, ex.getMessage());
                }
            }

            // Test Overdraft Failure Handling
            System.out.println("\nTesting overdraft failure handling with exception chaining...");
            service.submitTransaction(new Transaction("ACC-103", null, Transaction.Type.WITHDRAWAL, 50000.00, false));
            try {
                service.processNextTransaction();
            } catch (CheckpointException ex) {
                System.out.println("  ✅ Caught chained business exception: " + ex.getMessage());
                System.out.println("     Root cause: " + ex.getCause().getClass().getName() + " -> " + ex.getCause().getMessage());
            }

        } catch (CheckpointException e) {
            System.err.println("Transaction submission error: " + e.getMessage());
        }

        // Final Audit Ledger
        System.out.println("\n========================================================================");
        System.out.println("                    FINAL TRANSACTION AUDIT LEDGER                      ");
        System.out.println("========================================================================");
        for (Transaction t : service.getCompletedLedger()) {
            System.out.println("  • " + t);
        }

        System.out.println("\nFinal Account Balances:");
        for (Map.Entry<String, Account> entry : service.getSortedAccountsReport().entrySet()) {
            System.out.println("  • " + entry.getValue());
        }
        System.out.println("========================================================================\n");
    }
}
