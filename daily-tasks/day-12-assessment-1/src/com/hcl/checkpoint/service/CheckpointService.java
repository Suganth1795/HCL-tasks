package com.hcl.checkpoint.service;

import com.hcl.checkpoint.exception.CheckpointException;
import com.hcl.checkpoint.model.Account;
import com.hcl.checkpoint.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Service implementing the 2 Core Flows for Assessment 1 Checkpoint:
 * Flow 1: Account Lifecycle & Indexing (HashMap + TreeMap)
 * Flow 2: Priority Transaction Dispatch & Ledger (PriorityQueue + Atomic Transfer Safety)
 */
public class CheckpointService {

    // Flow 1 Storage: HashMap for O(1) account lookups
    private final Map<String, Account> accountRepository;

    // Flow 2 Storage: PriorityQueue for VIP-first transaction execution
    private final Queue<Transaction> pendingTransactions;

    // Completed Transaction Ledger
    private final List<Transaction> completedLedger;

    public CheckpointService() {
        this.accountRepository = new HashMap<>();
        this.pendingTransactions = new PriorityQueue<>();
        this.completedLedger = new ArrayList<>();
    }

    // =========================================================================
    // CORE FLOW 1: ACCOUNT LIFECYCLE MANAGEMENT
    // =========================================================================

    public void registerAccount(Account account) throws CheckpointException {
        if (account == null) {
            throw new CheckpointException("INVALID_INPUT", "Account cannot be null.");
        }
        if (accountRepository.containsKey(account.getAccountId())) {
            throw new CheckpointException("DUPLICATE_ACCOUNT", "Account " + account.getAccountId() + " already exists!");
        }
        accountRepository.put(account.getAccountId(), account);
    }

    public Optional<Account> getAccountById(String accountId) {
        if (accountId == null) return Optional.empty();
        return Optional.ofNullable(accountRepository.get(accountId.trim().toUpperCase()));
    }

    public Map<String, Account> getSortedAccountsReport() {
        return new TreeMap<>(accountRepository); // Naturally sorted by Account ID
    }

    // =========================================================================
    // CORE FLOW 2: TRANSACTION PROCESSING & PRIORITY DISPATCH
    // =========================================================================

    public void submitTransaction(Transaction transaction) throws CheckpointException {
        if (transaction == null) {
            throw new CheckpointException("INVALID_TRANSACTION", "Transaction cannot be null.");
        }
        pendingTransactions.offer(transaction);
    }

    public Transaction processNextTransaction() throws CheckpointException {
        Transaction txn = pendingTransactions.poll();
        if (txn == null) {
            throw new CheckpointException("QUEUE_EMPTY", "No pending transactions in queue.");
        }

        try {
            switch (txn.getType()) {
                case DEPOSIT:
                    Account dest = getAccountById(txn.getSourceAccountId())
                            .orElseThrow(() -> new CheckpointException("ACCOUNT_NOT_FOUND", "Account not found: " + txn.getSourceAccountId()));
                    dest.credit(txn.getAmount());
                    txn.setStatus("SUCCESS");
                    break;

                case WITHDRAWAL:
                    Account src = getAccountById(txn.getSourceAccountId())
                            .orElseThrow(() -> new CheckpointException("ACCOUNT_NOT_FOUND", "Account not found: " + txn.getSourceAccountId()));
                    src.debit(txn.getAmount());
                    txn.setStatus("SUCCESS");
                    break;

                case TRANSFER:
                    Account from = getAccountById(txn.getSourceAccountId())
                            .orElseThrow(() -> new CheckpointException("SOURCE_NOT_FOUND", "Source account not found: " + txn.getSourceAccountId()));
                    Account to = getAccountById(txn.getTargetAccountId())
                            .orElseThrow(() -> new CheckpointException("TARGET_NOT_FOUND", "Target account not found: " + txn.getTargetAccountId()));

                    // Atomic transfer with rollback
                    from.debit(txn.getAmount());
                    try {
                        to.credit(txn.getAmount());
                    } catch (Exception ex) {
                        from.credit(txn.getAmount()); // Rollback debit
                        throw new CheckpointException("CREDIT_FAILED", "Transfer credit failed. Rolled back.", ex);
                    }
                    txn.setStatus("SUCCESS");
                    break;
            }
        } catch (Exception ex) {
            txn.setStatus("FAILED");
            completedLedger.add(txn);
            throw new CheckpointException("TXN_FAILED", "Transaction " + txn.getTxnId() + " failed: " + ex.getMessage(), ex);
        }

        completedLedger.add(txn);
        return txn;
    }

    public int getPendingQueueSize() {
        return pendingTransactions.size();
    }

    public List<Transaction> getCompletedLedger() {
        return Collections.unmodifiableList(completedLedger);
    }
}
