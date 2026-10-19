package com.hcl.memory;

/**
 * Experiment 3: String Concatenation vs StringBuilder Benchmark
 * Compares cumulative string mutation timing and explains heap garbage generation.
 */
public class StringConcatBenchmark {

    public static class ConcatTimingResult {
        public final int iterations;
        public final long stringConcatMs;
        public final long stringBuilderMs;
        public final double speedup;

        public ConcatTimingResult(int iterations, long stringConcatMs, long stringBuilderMs) {
            this.iterations = iterations;
            this.stringConcatMs = stringConcatMs;
            this.stringBuilderMs = stringBuilderMs;
            this.speedup = (double) stringConcatMs / Math.max(1, stringBuilderMs);
        }
    }

    public static ConcatTimingResult runBenchmark(int iterations) {
        // 1. String += Concatenation
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < iterations; i++) {
            str += "A"; // Creates a new intermediate String object in heap every loop cycle
        }
        long stringConcatTime = System.currentTimeMillis() - start;

        // 2. StringBuilder append()
        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("A"); // Reuses expandable internal char/byte buffer
        }
        String sbResult = sb.toString();
        long stringBuilderTime = System.currentTimeMillis() - start;

        // Prevent dead-code elimination
        if (str.length() != sbResult.length()) {
            throw new IllegalStateException("Lengths must match!");
        }

        return new ConcatTimingResult(iterations, stringConcatTime, stringBuilderTime);
    }

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   EXPERIMENT 3: String += VS StringBuilder BENCHMARK          ");
        System.out.println("===============================================================");

        int iterations = 40000;
        ConcatTimingResult result = runBenchmark(iterations);

        System.out.printf("  Iterations Tested       : %,d iterations\n", result.iterations);
        System.out.printf("  String += Elapsed Time  : %,d ms\n", result.stringConcatMs);
        System.out.printf("  StringBuilder Time      : %,d ms\n", result.stringBuilderMs);
        System.out.printf("  StringBuilder Speedup   : %.1fx FASTER\n", result.speedup);
        System.out.println("===============================================================\n");
    }
}
