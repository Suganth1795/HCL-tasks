package com.hcl.datetime.exception;

/**
 * Custom checked exception thrown when a requested subscription is not found.
 */
public class SubscriptionNotFoundException extends Exception {

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
