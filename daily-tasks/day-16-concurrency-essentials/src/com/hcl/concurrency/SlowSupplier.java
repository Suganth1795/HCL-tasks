package com.hcl.concurrency;

/**
 * Simulates a third-party supplier service with network latency and potential failures.
 */
public class SlowSupplier {
    private final String name;
    private final int delayMs;
    private final double fixedPrice;
    private final boolean willFail;

    public SlowSupplier(String name, int delayMs, double fixedPrice, boolean willFail) {
        this.name = name;
        this.delayMs = delayMs;
        this.fixedPrice = fixedPrice;
        this.willFail = willFail;
    }

    /**
     * Fetches price quote with simulated network latency (1-2 seconds) and simulated fault.
     */
    public SupplierPrice fetchPrice(String item) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while awaiting supplier " + name, e);
        }
        long elapsed = System.currentTimeMillis() - start;

        if (willFail) {
            throw new RuntimeException("HTTP 503: " + name + " service unavailable / timeout");
        }

        return new SupplierPrice(name, item, fixedPrice, elapsed);
    }

    public String getName() {
        return name;
    }

    public int getDelayMs() {
        return delayMs;
    }

    public boolean isWillFail() {
        return willFail;
    }
}
