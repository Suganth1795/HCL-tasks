package com.hcl.order.service;

import com.hcl.order.exception.InsufficientStockException;
import com.hcl.order.exception.InvalidQuantityException;
import com.hcl.order.exception.OrderProcessingException;
import com.hcl.order.exception.PaymentDeclinedException;
import com.hcl.order.model.Order;
import com.hcl.order.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core business processor for customer order fulfillment.
 * Demonstrates:
 * - Checked vs Unchecked custom exceptions
 * - Multi-catch block (Java 7+)
 * - Exception chaining (preservation of root causes)
 * - Guaranteed audit execution via finally block
 * - Zero empty/swallowed catches
 */
public class OrderProcessor {

    private final List<String> auditLogs;
    private final List<Order> processedOrders;

    public OrderProcessor() {
        this.auditLogs = new ArrayList<>();
        this.processedOrders = new ArrayList<>();
    }

    /**
     * Processes an order end-to-end with comprehensive exception safety.
     */
    public Order processOrder(String orderId, Product product, int requestedQuantity, double availableFunds)
            throws OrderProcessingException {

        // 1. Validation Rule (Unchecked Exception)
        if (requestedQuantity <= 0) {
            throw new InvalidQuantityException(requestedQuantity);
        }

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        Order order = new Order(orderId, product, requestedQuantity);
        boolean isFulfilled = false;

        try {
            System.out.println("📦 Processing Order [" + orderId + "] for product: " + product.getName());

            // 2. Inventory Check (Custom Checked Exception)
            if (requestedQuantity > product.getStockQuantity()) {
                throw new InsufficientStockException(product.getId(), product.getStockQuantity(), requestedQuantity);
            }

            // 3. Payment Settlement Check (Custom Checked Exception)
            double requiredAmount = order.getTotalAmount();
            if (availableFunds < requiredAmount) {
                throw new PaymentDeclinedException(requiredAmount, 
                        String.format("Available funds ($%,.2f) insufficient for total ($%,.2f)", availableFunds, requiredAmount));
            }

            // Deduct stock and finalize order
            product.reduceStock(requestedQuantity);
            order.setStatus("CONFIRMED");
            processedOrders.add(order);
            isFulfilled = true;

            System.out.printf("✅ Order [%s] fulfilled successfully! Total Billed: $%,.2f\n", orderId, requiredAmount);
            return order;

        } catch (InsufficientStockException | PaymentDeclinedException ex) {
            // 4. Multi-Catch: Captures both business failures without code duplication
            order.setStatus("FAILED");
            System.err.println("⚠️  [Fulfillment Error] " + ex.getMessage());

            // 5. Exception Chaining: Preserves the root cause in the wrapper exception
            throw new OrderProcessingException("Failed to process order [" + orderId + "]: " + ex.getMessage(), orderId, ex);

        } finally {
            // 6. Finally Block: Guaranteed execution for audit trail & resource logging
            String auditEntry = String.format("AUDIT LOG | OrderId: %s | Product: %s | Qty: %d | Status: %s | Fulfilled: %b",
                    orderId, product.getName(), requestedQuantity, order.getStatus(), isFulfilled);
            auditLogs.add(auditEntry);
            System.out.println("🔒 [Finally Block Executed] Audit trail record committed.");
        }
    }

    public List<String> getAuditLogs() {
        return Collections.unmodifiableList(auditLogs);
    }

    public List<Order> getProcessedOrders() {
        return Collections.unmodifiableList(processedOrders);
    }
}
