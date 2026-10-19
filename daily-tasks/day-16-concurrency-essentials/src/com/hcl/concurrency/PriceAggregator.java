package com.hcl.concurrency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Day 16: Concurrency Essentials
 * Price Aggregator comparing Sequential execution, ExecutorService, and CompletableFuture.allOf.
 */
public class PriceAggregator {

    private static final String TARGET_ITEM = "Logitech MX Master 3S";

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("DAY 16: CONCURRENCY ESSENTIALS - PRICE AGGREGATOR ENGINE");
        System.out.println("Target Product: " + TARGET_ITEM);
        System.out.println("================================================================================\n");

        List<SlowSupplier> suppliers = List.of(
                new SlowSupplier("Supplier-Alpha (Retail)", 1200, 499.00, false),
                new SlowSupplier("Supplier-Beta (Wholesale)", 1800, 465.50, false),
                new SlowSupplier("Supplier-Gamma (Direct)", 1500, 440.00, true) // Simulated failure
        );

        System.out.println("Registered Suppliers for Aggregation:");
        for (SlowSupplier s : suppliers) {
            System.out.printf(" - %-25s | Latency: %4d ms | Failing: %s\n",
                    s.getName(), s.getDelayMs(), s.isWillFail() ? "YES (Throws 503)" : "NO (Healthy)");
        }
        System.out.println();

        // -------------------------------------------------------------
        // 1. Sequential Execution
        // -------------------------------------------------------------
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("APPROACH 1: SEQUENTIAL SYNCHRONOUS EXECUTION");
        System.out.println("--------------------------------------------------------------------------------");
        long seqStart = System.currentTimeMillis();
        List<SupplierPrice> seqResults = new ArrayList<>();

        for (SlowSupplier supplier : suppliers) {
            long itemStart = System.currentTimeMillis();
            try {
                System.out.println("Fetching sequentially from " + supplier.getName() + "...");
                SupplierPrice quote = supplier.fetchPrice(TARGET_ITEM);
                seqResults.add(quote);
                System.out.println("  -> Received: " + quote);
            } catch (Exception e) {
                long elapsed = System.currentTimeMillis() - itemStart;
                SupplierPrice failedQuote = SupplierPrice.failure(supplier.getName(), TARGET_ITEM, elapsed, e.getMessage());
                seqResults.add(failedQuote);
                System.out.println("  -> Error caught: " + failedQuote);
            }
        }
        long seqTotalTime = System.currentTimeMillis() - seqStart;
        System.out.printf(">>> Total Sequential Time: %d ms (~sum of all latencies)\n\n", seqTotalTime);

        // -------------------------------------------------------------
        // 2. ExecutorService + Callable + Future
        // -------------------------------------------------------------
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("APPROACH 2: EXECUTOR SERVICE (THREAD POOL WITH CALLABLE & FUTURE)");
        System.out.println("--------------------------------------------------------------------------------");
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        long execStart = System.currentTimeMillis();
        List<Future<SupplierPrice>> futures = new ArrayList<>();

        for (SlowSupplier supplier : suppliers) {
            Callable<SupplierPrice> task = () -> supplier.fetchPrice(TARGET_ITEM);
            futures.add(threadPool.submit(task));
        }

        List<SupplierPrice> execResults = new ArrayList<>();
        for (int i = 0; i < suppliers.size(); i++) {
            SlowSupplier supplier = suppliers.get(i);
            try {
                SupplierPrice price = futures.get(i).get();
                execResults.add(price);
                System.out.println("  -> Future completed: " + price);
            } catch (ExecutionException e) {
                SupplierPrice failedQuote = SupplierPrice.failure(supplier.getName(), TARGET_ITEM,
                        supplier.getDelayMs(), e.getCause().getMessage());
                execResults.add(failedQuote);
                System.out.println("  -> ExecutionException caught: " + failedQuote);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted during future.get()");
            }
        }
        long execTotalTime = System.currentTimeMillis() - execStart;
        threadPool.shutdown();
        System.out.printf(">>> Total ExecutorService Time: %d ms (~max latency among suppliers)\n\n", execTotalTime);

        // -------------------------------------------------------------
        // 3. CompletableFuture.allOf + exceptionally
        // -------------------------------------------------------------
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("APPROACH 3: ASYNCHRONOUS NON-BLOCKING (CompletableFuture.allOf + exceptionally)");
        System.out.println("--------------------------------------------------------------------------------");
        ExecutorService customExecutor = Executors.newFixedThreadPool(3);
        long cfStart = System.currentTimeMillis();

        List<CompletableFuture<SupplierPrice>> cfList = suppliers.stream()
                .map(supplier -> CompletableFuture.supplyAsync(() -> supplier.fetchPrice(TARGET_ITEM), customExecutor)
                        .exceptionally(ex -> {
                            // Extract root cause if nested inside CompletionException
                            String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                            return SupplierPrice.failure(supplier.getName(), TARGET_ITEM, supplier.getDelayMs(), msg);
                        }))
                .toList();

        // Combine all futures into a single barrier
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                cfList.toArray(new CompletableFuture[0])
        );

        // Wait non-blockingly until all suppliers respond or fallback
        allFutures.join();
        long cfTotalTime = System.currentTimeMillis() - cfStart;

        // Collect processed quotes
        List<SupplierPrice> cfResults = cfList.stream()
                .map(CompletableFuture::join)
                .toList();

        System.out.println("Results retrieved via CompletableFuture:");
        cfResults.forEach(r -> System.out.println("  " + r));

        // Evaluate Best Price among successful quotes
        SupplierPrice bestQuote = cfResults.stream()
                .filter(SupplierPrice::isSuccessful)
                .min(Comparator.comparingDouble(SupplierPrice::getPrice))
                .orElse(null);

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("AGGREGATION DECISION SUMMARY");
        System.out.println("--------------------------------------------------------------------------------");
        if (bestQuote != null) {
            System.out.printf("[BEST OFFER] %s at $%.2f (Latency: %d ms)\n",
                    bestQuote.getSupplierName(), bestQuote.getPrice(), bestQuote.getResponseTimeMs());
        } else {
            System.out.println("No supplier returned an active quote.");
        }

        // -------------------------------------------------------------
        // Benchmark Comparison Table
        // -------------------------------------------------------------
        System.out.println("\n================================================================================");
        System.out.println("PERFORMANCE BENCHMARK & ARCHITECTURAL COMPARISON");
        System.out.println("================================================================================");
        System.out.printf("%-30s | %-16s | %-14s | %-18s\n", "Approach", "Time Taken", "Speedup vs Seq", "Failure Resiliency");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-30s | %8d ms      | %14s | %-18s\n",
                "1. Sequential Execution", seqTotalTime, "1.00x (Base)", "Fragile / Blocking");
        System.out.printf("%-30s | %8d ms      | %13.2fx | %-18s\n",
                "2. ExecutorService + Future", execTotalTime, (double) seqTotalTime / execTotalTime, "Try-catch per get()");
        System.out.printf("%-30s | %8d ms      | %13.2fx | %-18s\n",
                "3. CompletableFuture.allOf", cfTotalTime, (double) seqTotalTime / cfTotalTime, "exceptionally pipeline");
        System.out.println("================================================================================");

        customExecutor.shutdown();
        try {
            if (!customExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                customExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            customExecutor.shutdownNow();
        }
    }
}
