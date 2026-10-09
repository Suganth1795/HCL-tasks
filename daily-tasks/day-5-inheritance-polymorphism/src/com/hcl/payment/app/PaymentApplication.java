package com.hcl.payment.app;

import com.hcl.payment.model.*;
import com.hcl.payment.service.PaymentProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 * Driver Application for Day 5: Inheritance, Polymorphism & Interface Design.
 * Demonstrates:
 * 1. Polymorphic collection processing (Payment hierarchy)
 * 2. Overloaded pay() method variations (promo codes, notes)
 * 3. Dynamic method dispatch across Card, UPI, and Cash
 * 4. Refundable interface contract invocation
 */
public class PaymentApplication {

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  DAILY TASK 5: PAYMENT HIERARCHY & POLYMORPHISM ENGINE        ");
        System.out.println("===============================================================");

        PaymentProcessor processor = new PaymentProcessor();

        // 1. Instantiating polymorphic Payment objects
        Payment cardPayment = new CreditCardPayment(2500.00, "4111222233334444", "John Doe", "12/28");
        Payment upiPayment = new UPIPayment(850.00, "john.doe@okhclbank", "Google Pay");
        Payment cashPayment = new CashPayment(320.00, "COUNTER-POS-04", 350.00);

        // 2. Overloaded pay() demonstration
        System.out.println("\n--- DEMONSTRATING OVERLOADED pay() METHODS ---");
        System.out.println("\n[Test 1: Standard pay()]");
        cardPayment.pay();

        System.out.println("\n[Test 2: Overloaded pay(note)]");
        upiPayment.pay("Monthly utility subscription fee");

        System.out.println("\n[Test 3: Overloaded pay(discount, promo)]");
        Payment discountedCardPayment = new CreditCardPayment(1000.00, "5200111122223333", "Sarah Connor", "08/29");
        discountedCardPayment.pay(15.0, "FESTIVE15");

        // 3. Polymorphic Batch Processing via Interface-Driven Service
        System.out.println("\n\n--- DEMONSTRATING POLYMORPHIC SERVICE DISPATCH ---");
        List<Payment> transactions = new ArrayList<>();
        transactions.add(cardPayment);
        transactions.add(upiPayment);
        transactions.add(cashPayment);

        for (Payment payment : transactions) {
            processor.execute(payment);
        }

        // 4. Interface Refund Processing
        System.out.println("\n\n--- DEMONSTRATING REFUNDABLE INTERFACE DISPATCH ---");
        // Card Refund (Applies 2% processing fee)
        processor.refund(cardPayment, 500.00);

        // UPI Refund (Applies 0% fee)
        processor.refund(upiPayment, 850.00);

        // Cash Payment Refund Attempt (Non-refundable digital instrument)
        processor.refund(cashPayment, 100.00);

        // 5. Final Audit Log
        System.out.println("\n===============================================================");
        System.out.println("                  PAYMENT AUDIT TRAIL LOG                      ");
        System.out.println("===============================================================");
        for (Payment p : processor.getAuditLog()) {
            System.out.println(" • " + p);
        }
        System.out.println("===============================================================\n");
    }
}
