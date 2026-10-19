package com.hcl.streams.model;

import java.util.Collections;
import java.util.List;

/**
 * Domain entity representing a Sales Order for Streams API analytics.
 */
public class SalesOrder {

    private final String orderId;
    private final String customerId;
    private final String customerName;
    private final String category;
    private final List<String> productNames;
    private final double orderTotal;
    private final String status; // "DELIVERED", "PENDING", "CANCELLED"

    public SalesOrder(String orderId, String customerId, String customerName,
                      String category, List<String> productNames, double orderTotal, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.category = category;
        this.productNames = productNames;
        this.orderTotal = orderTotal;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getProductNames() {
        return Collections.unmodifiableList(productNames);
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("[%s] Customer: %-15s | Cat: %-12s | Items: %d | Total: $%,7.2f | Status: %s",
                orderId, customerName, category, productNames.size(), orderTotal, status);
    }
}
