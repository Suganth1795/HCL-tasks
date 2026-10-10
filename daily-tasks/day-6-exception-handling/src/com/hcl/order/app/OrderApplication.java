package com.hcl.order.app;

import com.hcl.order.exception.InvalidQuantityException;
import com.hcl.order.exception.OrderProcessingException;
import com.hcl.order.model.Order;
import com.hcl.order.model.Product;
import com.hcl.order.service.OrderProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Driver Application for Day 6: Exception Handling & AI-assisted Debugging.
 * Demonstrates:
 * 1. Handling checked vs unchecked exceptions cleanly
 * 2. Unwrapping chained causes (getCause())
 * 3. Guaranteed finally execution for audit records
 * 4. Interactive menu loop resilience (recovers safely after any exception)
 */
public class OrderApplication {

    private static final Map<String, Product> inventory = new HashMap<>();

    static {
        inventory.put("P101", new Product("P101", "MacBook Pro M3", 1999.00, 5));
        inventory.put("P102", new Product("P102", "Dell UltraSharp 4K", 650.00, 10));
        inventory.put("P103", new Product("P103", "Logitech MX Master 3S", 99.00, 20));
    }

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("      DAILY TASK 6: ORDER PROCESSOR & EXCEPTION ENGINE         ");
        System.out.println("===============================================================\n");

        OrderProcessor processor = new OrderProcessor();

        // 1. Automated Scenarios Testing All Exception Paths & Chained Causes
        System.out.println("--- RUNNING AUTOMATED EXCEPTION SCENARIOS ---\n");

        // Scenario A: Valid Successful Order
        System.out.println("--- SCENARIO 1: SUCCESSFUL ORDER ---");
        try {
            Order order1 = processor.processOrder("ORD-001", inventory.get("P103"), 2, 500.00);
            System.out.println("Order Details: " + order1 + "\n");
        } catch (Exception e) {
            System.err.println("Unexpected failure: " + e.getMessage());
        }

        // Scenario B: Unchecked Exception (Invalid Quantity <= 0)
        System.out.println("--- SCENARIO 2: UNCHECKED EXCEPTION (InvalidQuantityException) ---");
        try {
            processor.processOrder("ORD-002", inventory.get("P101"), -3, 5000.00);
        } catch (InvalidQuantityException e) {
            System.out.println("Caught Expected Unchecked Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Requested Quantity: " + e.getRequestedQuantity() + "\n");
        } catch (Exception e) {
            System.err.println("Other Exception: " + e.getMessage());
        }

        // Scenario C: Checked Exception with Chaining (Insufficient Stock)
        System.out.println("--- SCENARIO 3: CHECKED EXCEPTION & CAUSE CHAINING (InsufficientStockException) ---");
        try {
            processor.processOrder("ORD-003", inventory.get("P101"), 15, 50000.00);
        } catch (OrderProcessingException e) {
            System.out.println("Caught Top-Level Business Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Order ID: " + e.getOrderId());
            System.out.println("Preserved Root Cause: " + e.getCause().getClass().getName() + " -> " + e.getCause().getMessage() + "\n");
        }

        // Scenario D: Checked Exception with Chaining (Payment Declined)
        System.out.println("--- SCENARIO 4: CHECKED EXCEPTION & CAUSE CHAINING (PaymentDeclinedException) ---");
        try {
            processor.processOrder("ORD-004", inventory.get("P102"), 2, 300.00); // 2 * $650 = $1300 > $300
        } catch (OrderProcessingException e) {
            System.out.println("Caught Top-Level Business Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Order ID: " + e.getOrderId());
            System.out.println("Preserved Root Cause: " + e.getCause().getClass().getName() + " -> " + e.getCause().getMessage() + "\n");
        }

        // 2. Audit Trail Inspection
        System.out.println("===============================================================");
        System.out.println("            GUARANTEED AUDIT TRAIL LOGS (FINALLY)              ");
        System.out.println("===============================================================");
        for (String log : processor.getAuditLogs()) {
            System.out.println(" • " + log);
        }
        System.out.println("===============================================================\n");
    }
}
