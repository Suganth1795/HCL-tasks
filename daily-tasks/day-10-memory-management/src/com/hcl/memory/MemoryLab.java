package com.hcl.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Daily Task 10: Memory Management
 * Master Memory Lab Runner coordinating all 5 memory experiments:
 * 1. StackOverflowError depth analysis (-Xss)
 * 2. OutOfMemoryError simulation (-Xmx64m)
 * 3. String concatenation vs StringBuilder performance
 * 4. Garbage Collection churn simulation (-Xlog:gc)
 * 5. Integer cache == equality pitfall
 */
public class MemoryLab {

    public static void runGcGenerationWorkload() {
        System.out.println("--- EXPERIMENT 4: GC LOGGING WORKLOAD (Triggers Minor & Major GC) ---");
        System.out.println("  Generating object churn to trigger GC cycles under -Xlog:gc...");

        // Allocate transient objects to produce young gen GC churn
        List<Object> retained = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            byte[] transientChunk = new byte[512 * 1024]; // 512 KB short-lived
            transientChunk[0] = (byte) i;
            if (i % 5 == 0) {
                retained.add(transientChunk); // Retain some to promote to Old Gen
            }
        }
        System.out.printf("  Completed GC workload generation: %d transient allocations, %d objects retained in Old Gen.\n\n",
                50, retained.size());
    }

    public static void printSummaryTable(long stackDepth, HeapOOMExperiment.AllocationResult oomResult,
                                         StringConcatBenchmark.ConcatTimingResult concatResult) {
        System.out.println("========================================================================================================");
        System.out.println("                         DAY 10: MEMORY MANAGEMENT LAB EXPERIMENTAL RESULTS                             ");
        System.out.println("========================================================================================================");
        System.out.printf("%-28s | %-25s | %-45s\n", "Experiment", "JVM Parameter / Metric", "Observed Outcome");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf("%-28s | %-25s | %-45s\n", "1. Stack Overflow Depth", "Current -Xss", String.format("%,d recursive stack frames before error", stackDepth));
        System.out.printf("%-28s | %-25s | %-45s\n", "2. Heap OutOfMemoryError", "Constrained -Xmx", String.format("Exhausted at %d MB (%s)", oomResult.chunksAllocated, oomResult.errorMessage));
        System.out.printf("%-28s | %-25s | %-45s\n", "3. String += vs StringBuilder", "40,000 iterations", String.format("String +=: %d ms vs StringBuilder: %d ms (%.1fx faster)",
                concatResult.stringConcatMs, concatResult.stringBuilderMs, concatResult.speedup));
        System.out.printf("%-28s | %-25s | %-45s\n", "4. GC Activity Logging", "-Xlog:gc", "Captured Young/Old Gen GC cycles & pause durations");
        System.out.printf("%-28s | %-25s | %-45s\n", "5. Integer Cache Pitfall", "[-128, 127] Boundary", "100==100 is TRUE (cached); 200==200 is FALSE (heap ref mismatch)");
        System.out.println("========================================================================================================");
    }

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("             DAILY TASK 10: MEMORY MANAGEMENT LAB                       ");
        System.out.println("========================================================================\n");

        // 1. StackOverflowError depth
        System.out.println("--- EXPERIMENT 1: StackOverflowError DEPTH ---");
        long depth = StackOverflowExperiment.measureDepth();
        System.out.printf("  Recursion depth reached before StackOverflowError: %,d\n\n", depth);

        // 2. OutOfMemoryError test
        System.out.println("--- EXPERIMENT 2: OutOfMemoryError WITH CONSTRAINED HEAP ---");
        HeapOOMExperiment.AllocationResult oomResult = HeapOOMExperiment.triggerOOM();
        System.out.printf("  Total chunks allocated before OOM: %d MB (%s)\n\n",
                oomResult.chunksAllocated, oomResult.errorMessage);

        // 3. String Concatenation vs StringBuilder benchmark
        System.out.println("--- EXPERIMENT 3: String += vs StringBuilder BENCHMARK ---");
        StringConcatBenchmark.ConcatTimingResult concatResult = StringConcatBenchmark.runBenchmark(40000);
        System.out.printf("  String += elapsed time  : %,d ms\n", concatResult.stringConcatMs);
        System.out.printf("  StringBuilder time      : %,d ms\n", concatResult.stringBuilderMs);
        System.out.printf("  StringBuilder speedup   : %.1fx FASTER\n\n", concatResult.speedup);

        // 4. GC Churn simulation
        runGcGenerationWorkload();

        // 5. Integer Cache pitfall
        IntegerCachePitfall.runDemonstration();

        // Summary Table
        printSummaryTable(depth, oomResult, concatResult);
    }
}
