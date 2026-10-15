package com.hcl.library.benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Performance benchmarking utility measuring and contrasting ArrayList vs LinkedList
 * across fundamental operations:
 * 1. Head Insertions (add(0, item))
 * 2. Tail Insertions (add(item))
 * 3. Middle Insertions (add(size / 2, item))
 * 4. Positional Access (get(i))
 */
public class CollectionPerformanceBenchmark {

    public static class BenchmarkResult {
        public final String operation;
        public final int sampleSize;
        public final long arrayListNanos;
        public final long linkedListNanos;
        public final String fasterCollection;
        public final double speedFactor;

        public BenchmarkResult(String operation, int sampleSize, long arrayListNanos, long linkedListNanos) {
            this.operation = operation;
            this.sampleSize = sampleSize;
            this.arrayListNanos = arrayListNanos;
            this.linkedListNanos = linkedListNanos;

            if (arrayListNanos < linkedListNanos) {
                this.fasterCollection = "ArrayList";
                this.speedFactor = (double) linkedListNanos / Math.max(1, arrayListNanos);
            } else {
                this.fasterCollection = "LinkedList";
                this.speedFactor = (double) arrayListNanos / Math.max(1, linkedListNanos);
            }
        }
    }

    public static List<BenchmarkResult> runBenchmark(int insertCount, int readCount) {
        List<BenchmarkResult> results = new ArrayList<>();

        // Warmup JVM JIT
        warmup();

        // 1. Head Insertions (add at index 0)
        long start = System.nanoTime();
        List<Integer> alHead = new ArrayList<>();
        for (int i = 0; i < insertCount; i++) {
            alHead.add(0, i); // O(N) array shifts
        }
        long alHeadTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<Integer> llHead = new LinkedList<>();
        for (int i = 0; i < insertCount; i++) {
            llHead.add(0, i); // O(1) pointer updates
        }
        long llHeadTime = System.nanoTime() - start;
        results.add(new BenchmarkResult("Head Insert (add(0, e))", insertCount, alHeadTime, llHeadTime));

        // 2. Tail Insertions (add at end)
        start = System.nanoTime();
        List<Integer> alTail = new ArrayList<>();
        for (int i = 0; i < insertCount; i++) {
            alTail.add(i); // Amortized O(1)
        }
        long alTailTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<Integer> llTail = new LinkedList<>();
        for (int i = 0; i < insertCount; i++) {
            llTail.add(i); // O(1) tail pointer
        }
        long llTailTime = System.nanoTime() - start;
        results.add(new BenchmarkResult("Tail Insert (add(e))", insertCount, alTailTime, llTailTime));

        // 3. Middle Insertions (add at middle)
        int midSample = Math.min(insertCount, 15000); // capped to complete in reasonable time
        List<Integer> alMid = new ArrayList<>();
        for (int i = 0; i < midSample; i++) alMid.add(i);
        start = System.nanoTime();
        for (int i = 0; i < 2000; i++) {
            alMid.add(alMid.size() / 2, i);
        }
        long alMidTime = System.nanoTime() - start;

        List<Integer> llMid = new LinkedList<>();
        for (int i = 0; i < midSample; i++) llMid.add(i);
        start = System.nanoTime();
        for (int i = 0; i < 2000; i++) {
            llMid.add(llMid.size() / 2, i);
        }
        long llMidTime = System.nanoTime() - start;
        results.add(new BenchmarkResult("Middle Insert (add(mid, e))", 2000, alMidTime, llMidTime));

        // 4. Positional Random Access (get(i))
        List<Integer> alRead = new ArrayList<>(alTail);
        List<Integer> llRead = new LinkedList<>(llTail);
        Random rand = new Random(42);
        int[] indices = new int[readCount];
        for (int i = 0; i < readCount; i++) {
            indices[i] = rand.nextInt(alRead.size());
        }

        start = System.nanoTime();
        long alDummy = 0;
        for (int idx : indices) {
            alDummy += alRead.get(idx); // O(1) indexed access
        }
        long alGetTime = System.nanoTime() - start;

        start = System.nanoTime();
        long llDummy = 0;
        for (int idx : indices) {
            llDummy += llRead.get(idx); // O(N) linear pointer traversal
        }
        long llGetTime = System.nanoTime() - start;
        results.add(new BenchmarkResult("Random Access (get(i))", readCount, alGetTime, llGetTime));

        // Prevent dead code elimination
        if (alDummy == llDummy && alDummy == -999) {
            System.out.println("Anti-optimization check");
        }

        return results;
    }

    private static void warmup() {
        List<Integer> w1 = new ArrayList<>();
        List<Integer> w2 = new LinkedList<>();
        for (int i = 0; i < 5000; i++) {
            w1.add(i);
            w2.add(i);
        }
        for (int i = 0; i < 2000; i++) {
            w1.get(i);
            w2.get(i);
        }
    }

    public static void printBenchmarkTable(List<BenchmarkResult> results) {
        System.out.println("========================================================================================================");
        System.out.println("                         PERFORMANCE BENCHMARK: ARRAYLIST VS LINKEDLIST                                 ");
        System.out.println("========================================================================================================");
        System.out.printf("%-30s | %-12s | %-18s | %-18s | %-12s | %-10s\n",
                "Operation Tested", "Sample Size", "ArrayList (ms)", "LinkedList (ms)", "Winner", "Speedup");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        for (BenchmarkResult res : results) {
            double alMs = res.arrayListNanos / 1_000_000.0;
            double llMs = res.linkedListNanos / 1_000_000.0;
            System.out.printf("%-30s | %-12d | %15.3f ms | %15.3f ms | %-12s | %8.1fx\n",
                    res.operation, res.sampleSize, alMs, llMs, res.fasterCollection, res.speedFactor);
        }
        System.out.println("========================================================================================================");
    }
}
