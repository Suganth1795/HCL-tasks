package com.hcl.order.exception;

/**
 * High-level business exception wrapping low-level failures.
 * Demonstrates exception chaining via constructor preservation of the underlying Throwable cause.
 */
public class OrderProcessingException extends Exception {

    private final String orderId;

    public OrderProcessingException(String message, String orderId, Throwable cause) {
        super(message, cause);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
