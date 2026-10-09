package com.hcl.payment.service;

import com.hcl.payment.model.Payment;
import com.hcl.payment.model.Refundable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service orchestrator demonstrating Dependency Inversion Principle (DIP):
 * High-level business logic depends exclusively on abstract Payment and Refundable interfaces.
 */
public class PaymentProcessor {

    private final List<Payment> processedPayments;

    public PaymentProcessor() {
        this.processedPayments = new ArrayList<>();
    }

    /**
     * Executes payment generically via polymorphic method dispatch.
     */
    public boolean execute(Payment payment) {
        if (payment == null) {
            System.out.println("❌ Cannot process null payment.");
            return false;
        }

        System.out.println("\n---------------------------------------------------------------");
        System.out.println("Initiating Payment Transaction: " + payment.getTransactionId());
        System.out.println("---------------------------------------------------------------");

        boolean isSuccess = payment.pay();
        if (isSuccess) {
            processedPayments.add(payment);
        }
        return isSuccess;
    }

    /**
     * Executes refund specifically for instances implementing the Refundable interface.
     */
    public boolean refund(Payment payment, double refundAmount) {
        if (payment == null) {
            System.out.println("❌ Invalid payment reference for refund.");
            return false;
        }

        System.out.println("\n---------------------------------------------------------------");
        System.out.println("Initiating Refund Request for: " + payment.getTransactionId());
        System.out.println("---------------------------------------------------------------");

        if (payment instanceof Refundable) {
            Refundable refundablePayment = (Refundable) payment;
            return refundablePayment.issueRefund(refundAmount);
        } else {
            System.out.println("⚠️  Refund Not Supported: Payment type [" + payment.getClass().getSimpleName() + 
                               "] does not implement the Refundable interface (e.g. Cash purchases must be settled manually at the counter).");
            return false;
        }
    }

    public List<Payment> getAuditLog() {
        return Collections.unmodifiableList(processedPayments);
    }
}
