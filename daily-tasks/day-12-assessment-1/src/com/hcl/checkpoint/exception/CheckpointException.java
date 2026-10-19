package com.hcl.checkpoint.exception;

/**
 * Custom application checked exception for Core Java Checkpoint.
 * Demonstrates exception chaining with root-cause preservation.
 */
public class CheckpointException extends Exception {

    private final String errorCode;

    public CheckpointException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CheckpointException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
