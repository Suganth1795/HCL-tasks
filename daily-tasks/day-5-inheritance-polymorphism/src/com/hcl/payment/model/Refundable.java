package com.hcl.payment.model;

/**
 * Interface defining refund behavior for compatible payment instruments.
 * Demonstrates abstraction and interface-driven design.
 */
public interface Refundable {

    /**
     * Issues a refund for a specified amount.
     * @param amount The refund amount requested.
     * @return true if refund was successfully initiated, false otherwise.
     */
    boolean issueRefund(double amount);

    /**
     * Calculates the processing fee applicable to this refund.
     * @param amount The base refund amount.
     * @return Processing fee deduction.
     */
    double calculateRefundFee(double amount);
}
