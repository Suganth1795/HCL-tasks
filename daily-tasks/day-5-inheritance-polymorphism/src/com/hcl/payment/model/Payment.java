package com.hcl.payment.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Abstract base class representing a generic Payment transaction.
 * Demonstrates:
 * - Abstract classes and template method patterns
 * - Encapsulation of common transaction metadata
 * - Method overloading for pay()
 */
public abstract class Payment {

    private final String transactionId;
    private double amount;
    private final LocalDateTime timestamp;
    private String status; // PENDING, SUCCESS, FAILED, REFUNDED

    public Payment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    protected void setAmount(double amount) {
        this.amount = amount;
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

    /**
     * Abstract method enforcing polymorphic payment execution in subclasses.
     */
    public abstract boolean processPayment();

    /**
     * Overloaded method 1: standard payment trigger.
     */
    public boolean pay() {
        return processPayment();
    }

    /**
     * Overloaded method 2: payment trigger with custom memo/notes.
     */
    public boolean pay(String transactionNote) {
        System.out.println("📝 Transaction Note attached: \"" + transactionNote + "\"");
        return processPayment();
    }

    /**
     * Overloaded method 3: payment trigger with dynamic discount percentage.
     */
    public boolean pay(double discountPercentage, String discountCode) {
        if (discountPercentage > 0 && discountPercentage <= 100) {
            double discountAmount = (this.amount * discountPercentage) / 100.0;
            this.amount -= discountAmount;
            System.out.printf("🏷️  Promo Applied [%s]: Saved $%.2f (%.1f%% off). New Total: $%.2f\n",
                    discountCode, discountAmount, discountPercentage, this.amount);
        }
        return processPayment();
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Amount: $%,.2f | Status: %s",
                transactionId, getFormattedTimestamp(), amount, status);
    }
}
