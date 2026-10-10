package com.hcl.order.exception;

/**
 * Custom checked exception representing a payment authorization rejection.
 */
public class PaymentDeclinedException extends Exception {

    private final double attemptedAmount;
    private final String reason;

    public PaymentDeclinedException(double attemptedAmount, String reason) {
        super(String.format("Payment authorization declined for amount $%,.2f! Reason: %s", attemptedAmount, reason));
        this.attemptedAmount = attemptedAmount;
        this.reason = reason;
    }

    public double getAttemptedAmount() {
        return attemptedAmount;
    }

    public String getReason() {
        return reason;
    }
}
