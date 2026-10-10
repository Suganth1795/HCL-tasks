package com.hcl.order.exception;

/**
 * Custom unchecked exception indicating invalid ordering parameters (e.g. quantity <= 0).
 * Extends IllegalArgumentException to signify programmatic/input validation faults.
 */
public class InvalidQuantityException extends IllegalArgumentException {

    private final int requestedQuantity;

    public InvalidQuantityException(int requestedQuantity) {
        super(String.format("Invalid order quantity [%d]. Order quantity must be a positive integer greater than zero.", requestedQuantity));
        this.requestedQuantity = requestedQuantity;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
