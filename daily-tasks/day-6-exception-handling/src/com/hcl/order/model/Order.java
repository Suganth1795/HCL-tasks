package com.hcl.order.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Order domain entity.
 */
public class Order {

    private final String orderId;
    private final Product product;
    private final int quantity;
    private final double totalAmount;
    private final LocalDateTime orderTime;
    private String status; // CREATED, CONFIRMED, FAILED

    public Order(String orderId, Product product, int quantity) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = product.getPrice() * quantity;
        this.orderTime = LocalDateTime.now();
        this.status = "CREATED";
    }

    public String getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Order [%s] | Item: %s (x%d) | Total: $%,.2f | Status: %s | Time: %s",
                orderId, product.getName(), quantity, totalAmount, status,
                orderTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
