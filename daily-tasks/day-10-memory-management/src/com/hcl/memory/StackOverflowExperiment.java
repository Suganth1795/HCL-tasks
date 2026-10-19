package com.hcl.memory;

/**
 * Experiment 1: StackOverflowError Depth Analysis
 * Tests stack frame consumption depth using unbounded recursion.
 * Can be run with different JVM stack size flags:
 * e.g., java -Xss256k, java -Xss512k, java -Xss1m
 */
public class StackOverflowExperiment {

    private static long recursionDepth = 0;

    public static void recursiveCall() {
        recursionDepth++;
        recursiveCall(); // Unbounded recursion causing stack frame exhaustion
    }

    public static long measureDepth() {
        recursionDepth = 0;
        try {
            recursiveCall();
        } catch (StackOverflowError error) {
            return recursionDepth;
        }
        return recursionDepth;
    }

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   EXPERIMENT 1: StackOverflowError DEPTH MEASUREMENT          ");
        System.out.println("===============================================================");

        long depth = measureDepth();
        System.out.printf("  Max Stack Frame Depth Reached: %,d calls before StackOverflowError\n", depth);
        System.out.println("===============================================================\n");
    }
}
