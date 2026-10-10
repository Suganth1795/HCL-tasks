package com.hcl.order.exception;

/**
 * Custom checked exception indicating inventory shortage.
 * Extends Exception directly to enforce compile-time handling.
 */
public class InsufficientStockException extends Exception {

    private final String productId;
    private final int availableQuantity;
    private final int requestedQuantity;

    public InsufficientStockException(String productId, int availableQuantity, int requestedQuantity) {
        super(String.format("Insufficient stock for product [%s]! Available: %d units, Requested: %d units.",
                productId, availableQuantity, requestedQuantity));
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.requestedQuantity = requestedQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
