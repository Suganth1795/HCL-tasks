package com.hcl.memory;

/**
 * Experiment 5: Integer Cache Equality Pitfall
 * Demonstrates the subtle equality pitfall caused by JVM's IntegerCache (-128 to 127).
 */
public class IntegerCachePitfall {

    public static void runDemonstration() {
        System.out.println("===============================================================");
        System.out.println("   EXPERIMENT 5: INTEGER CACHE '==' EQUALITY PITFALL          ");
        System.out.println("===============================================================");

        // Test within cache range [-128, 127]
        Integer a = 100;
        Integer b = 100;

        System.out.println("Test Case 1: Values inside Integer Cache (100):");
        System.out.println("  Integer a = 100; Integer b = 100;");
        System.out.printf("  Identity Comparison (a == b)    : %b  (Points to SAME cached object)\n", (a == b));
        System.out.printf("  Value Comparison (a.equals(b))  : %b  (Values are equal)\n", a.equals(b));
        System.out.printf("  System.identityHashCode(a)      : %d\n", System.identityHashCode(a));
        System.out.printf("  System.identityHashCode(b)      : %d\n\n", System.identityHashCode(b));

        // Test outside cache range [> 127]
        Integer c = 200;
        Integer d = 200;

        System.out.println("Test Case 2: Values outside Integer Cache (200):");
        System.out.println("  Integer c = 200; Integer d = 200;");
        System.out.printf("  Identity Comparison (c == d)    : %b  (PITFALL! Separate heap objects)\n", (c == d));
        System.out.printf("  Value Comparison (c.equals(d))  : %b  (Values are equal)\n", c.equals(d));
        System.out.printf("  System.identityHashCode(c)      : %d\n", System.identityHashCode(c));
        System.out.printf("  System.identityHashCode(d)      : %d\n\n", System.identityHashCode(d));

        System.out.println("  🔍 Architectural Explanation:");
        System.out.println("  - Java caches Integer objects in range [-128, 127] via Integer.valueOf().");
        System.out.println("  - For values > 127, autoboxing allocates a new Integer heap instance every time.");
        System.out.println("  - Using '==' compares memory references, leading to intermittent bugs!");
        System.out.println("  - Always use .equals() or primitive int for value comparisons.");
        System.out.println("===============================================================\n");
    }

    public static void main(String[] args) {
        runDemonstration();
    }
}
