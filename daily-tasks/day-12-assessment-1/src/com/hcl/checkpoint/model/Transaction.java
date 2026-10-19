package com.hcl.checkpoint.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Domain entity representing a financial transaction.
 * Implements Comparable for priority transaction dispatching (VIP first, then FIFO).
 */
public class Transaction implements Comparable<Transaction> {

    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    private final String txnId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final Type type;
    private final double amount;
    private final boolean isVipPriority;
    private final LocalDateTime timestamp;
    private String status; // PENDING, SUCCESS, FAILED

    public Transaction(String sourceAccountId, String targetAccountId, Type type, double amount, boolean isVipPriority) {
        this.txnId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.type = type;
        this.amount = amount;
        this.isVipPriority = isVipPriority;
        this.timestamp = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getTxnId() {
        return txnId;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isVipPriority() {
        return isVipPriority;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(Transaction other) {
        // VIP transactions take precedence (true > false)
        if (this.isVipPriority != other.isVipPriority) {
            return this.isVipPriority ? -1 : 1;
        }
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s | Amt: $%,8.2f | From: %-8s | To: %-8s | VIP: %-5b | Status: %s | Time: %s",
                txnId, type, amount, sourceAccountId, (targetAccountId != null ? targetAccountId : "N/A"),
                isVipPriority, status, timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
