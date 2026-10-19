package com.hcl.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Experiment 2: OutOfMemoryError Trigger
 * Rapidly allocates memory in the Java Heap until exhaustion.
 * Demonstrates heap allocation tracking under constrained heap (-Xmx64m).
 */
public class HeapOOMExperiment {

    public static class AllocationResult {
        public final int chunksAllocated;
        public final long totalMegabytesAllocated;
        public final String errorMessage;

        public AllocationResult(int chunksAllocated, long totalMegabytesAllocated, String errorMessage) {
            this.chunksAllocated = chunksAllocated;
            this.totalMegabytesAllocated = totalMegabytesAllocated;
            this.errorMessage = errorMessage;
        }
    }

    public static AllocationResult triggerOOM() {
        List<byte[]> memoryHog = new ArrayList<>();
        int chunks = 0;
        try {
            while (true) {
                // Allocate 1 MB chunks
                byte[] chunk = new byte[1024 * 1024];
                // Touch the memory so OS/JVM commits it
                chunk[0] = 1;
                chunk[chunk.length - 1] = 1;
                memoryHog.add(chunk);
                chunks++;
            }
        } catch (OutOfMemoryError oom) {
            long totalMb = chunks;
            return new AllocationResult(chunks, totalMb, oom.toString());
        }
    }

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   EXPERIMENT 2: OutOfMemoryError WITH CONSTRAINED HEAP        ");
        System.out.println("===============================================================");

        Runtime rt = Runtime.getRuntime();
        System.out.printf("  Configured Max Heap Memory: %,d MB\n", rt.maxMemory() / (1024 * 1024));

        AllocationResult result = triggerOOM();
        System.out.printf("  Allocated Chunks (1 MB each): %d MB\n", result.chunksAllocated);
        System.out.printf("  Caught Expected Error: %s\n", result.errorMessage);
        System.out.println("===============================================================\n");
    }
}
