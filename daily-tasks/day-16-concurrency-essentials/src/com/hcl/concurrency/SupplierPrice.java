package com.hcl.concurrency;

/**
 * Model representing the price quote received from an upstream supplier.
 */
public class SupplierPrice {
    private final String supplierName;
    private final String item;
    private final double price;
    private final long responseTimeMs;
    private final boolean successful;
    private final String errorMessage;

    public SupplierPrice(String supplierName, String item, double price, long responseTimeMs) {
        this(supplierName, item, price, responseTimeMs, true, null);
    }

    public SupplierPrice(String supplierName, String item, double price, long responseTimeMs, boolean successful, String errorMessage) {
        this.supplierName = supplierName;
        this.item = item;
        this.price = price;
        this.responseTimeMs = responseTimeMs;
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    public static SupplierPrice failure(String supplierName, String item, long responseTimeMs, String errorMessage) {
        return new SupplierPrice(supplierName, item, 0.0, responseTimeMs, false, errorMessage);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (successful) {
            return String.format("[SUCCESS] %-18s | Item: %-15s | Price: $%7.2f | Latency: %4d ms",
                    supplierName, item, price, responseTimeMs);
        } else {
            return String.format("[FAILED]  %-18s | Item: %-15s | Latency: %4d ms | Error: %s",
                    supplierName, item, responseTimeMs, errorMessage);
        }
    }
}
