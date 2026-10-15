package com.hcl.generics;

import com.hcl.generics.model.Box;
import com.hcl.generics.model.Pair;
import com.hcl.generics.resource.CustomResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Daily Task 9: Generics
 * Topic: Generic Toolkit
 *
 * Requirements:
 * 1. Pair<K,V> type-safe tuple
 * 2. Box<T extends Number> bounded generic container
 * 3. <T extends Comparable<T>> max() generic bounded methods
 * 4. Wildcard methods demonstrating PECS (Producer Extends, Consumer Super)
 * 5. Read products.csv with try-with-resources
 * 6. Custom AutoCloseable demonstrating reverse LIFO close order
 * 7. Strictly zero raw types or compiler warnings
 */
public class GenericToolkit {

    // =========================================================================
    // 1. GENERIC BOUNDED METHODS (<T extends Comparable<T>>)
    // =========================================================================

    /**
     * Generic bounded method returning the maximum of two comparable elements.
     *
     * @param <T> Any type implementing Comparable<T>
     * @param a   First item
     * @param b   Second item
     * @return The greater item
     */
    public static <T extends Comparable<T>> T max(T a, T b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) >= 0 ? a : b;
    }

    /**
     * Generic bounded method returning the maximum element from a list.
     */
    public static <T extends Comparable<T>> T max(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty.");
        }
        T maxElement = list.get(0);
        for (T item : list) {
            if (item != null && item.compareTo(maxElement) > 0) {
                maxElement = item;
            }
        }
        return maxElement;
    }

    // =========================================================================
    // 2. WILDCARD METHODS (PECS: Producer Extends, Consumer Super)
    // =========================================================================

    /**
     * Upper Bounded Wildcard (? extends Number): PRODUCER.
     * Reads numbers out of the list and calculates their cumulative sum.
     * Accepts List<Integer>, List<Double>, List<Float>, List<Long>, etc. Covariant read-only.
     */
    public static double sumOfList(List<? extends Number> list) {
        double sum = 0.0;
        for (Number number : list) {
            if (number != null) {
                sum += number.doubleValue();
            }
        }
        return sum;
    }

    /**
     * Lower Bounded Wildcard (? super Integer): CONSUMER.
     * Writes integer values into the list safely.
     * Accepts List<Integer>, List<Number>, List<Object>. Contravariant write-supported.
     */
    public static void addIntegers(List<? super Integer> list, int count) {
        for (int i = 1; i <= count; i++) {
            list.add(i * 10); // Safe write: Integer satisfies ? super Integer
        }
    }

    /**
     * Unbounded Wildcard (List<?>):
     * Safely reads and formats arbitrary typed collections without unsafe raw casts.
     */
    public static void printCollection(String label, Collection<?> collection) {
        System.out.printf("  %s (size=%d): [", label, collection.size());
        boolean first = true;
        for (Object item : collection) {
            if (!first) System.out.print(", ");
            System.out.print(item);
            first = false;
        }
        System.out.println("]");
    }

    // =========================================================================
    // 3. TRY-WITH-RESOURCES: CSV READER
    // =========================================================================

    /**
     * Reads products.csv safely using try-with-resources.
     * Guarantees automatic file handle disposal even if an IOException occurs.
     *
     * @param csvPath Path to CSV file
     * @return List of Pair<String, Double> mapping Product Name -> Price
     */
    public static List<Pair<String, Double>> readProductsCsv(String csvPath) {
        List<Pair<String, Double>> products = new ArrayList<>();
        File file = new File(csvPath);

        if (!file.exists()) {
            System.err.println("⚠️  CSV file not found at: " + csvPath);
            return products;
        }

        // Try-with-resources with BufferedReader ensures deterministic stream closure
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine(); // Consume header
            System.out.println("  📄 [CSV Header Read] -> " + headerLine);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    String name = tokens[1].trim();
                    double price = Double.parseDouble(tokens[3].trim());
                    products.add(Pair.of(name, price));
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading CSV file: " + e.getMessage());
        }

        return products;
    }

    // =========================================================================
    // 4. CUSTOM AUTOCLOSEABLE DEMONSTRATION (LIFO CLOSE ORDER)
    // =========================================================================

    /**
     * Demonstrates multiple AutoCloseable resources inside try-with-resources.
     * Java guarantees that resources are closed in the REVERSE order of their declaration (LIFO).
     */
    public static void demonstrateAutoCloseableLIFO() {
        System.out.println("--- 4. AUTOCLOSEABLE DEMONSTRATION (LIFO CLOSE ORDER) ---");
        System.out.println("Declaring: R1 (DatabaseConnection) -> R2 (TransactionScope) -> R3 (AuditOutputStream)...");

        try (CustomResource r1 = new CustomResource("DatabaseConnection");
             CustomResource r2 = new CustomResource("TransactionScope");
             CustomResource r3 = new CustomResource("AuditOutputStream")) {

            System.out.println("\n  >> Inside try-block: Executing coordinated business transaction...");
            r1.doWork();
            r2.doWork();
            r3.doWork();
            System.out.println("  >> Exiting try-block now. Observe closing order below:\n");

        } // <-- Automatic closing happens here in reverse declaration order: r3 -> r2 -> r1

        System.out.println("✅ All resources finalized without manual finally blocks!\n");
    }

    // =========================================================================
    // MAIN DRIVER HARNESS
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("           DAILY TASK 9: GENERICS & TYPE-SAFE TOOLKIT                   ");
        System.out.println("========================================================================\n");

        // 1. Pair<K, V> Demonstration
        System.out.println("--- 1. GENERIC Pair<K, V> DEMONSTRATION ---");
        Pair<String, Integer> studentScore = Pair.of("Alice Walker", 98);
        Pair<Integer, String> httpStatus = Pair.of(200, "OK");
        Pair<String, List<Double>> metrics = Pair.of("CPU_LOAD", Arrays.asList(0.42, 0.55, 0.61));

        System.out.println("Pair 1: " + studentScore + " | Key=" + studentScore.getKey() + ", Val=" + studentScore.getValue());
        System.out.println("Pair 2: " + httpStatus);
        System.out.println("Pair 3: " + metrics);

        // 2. Box<T extends Number> Demonstration
        System.out.println("\n--- 2. BOUNDED GENERIC Box<T extends Number> ---");
        Box<Integer> intBox = new Box<>(500);
        Box<Double> doubleBox = new Box<>(750.85);
        Box<Long> longBox = new Box<>(1000L);

        System.out.println("Int Box   : " + intBox + " -> doubleValue: " + intBox.doubleValue());
        System.out.println("Double Box: " + doubleBox + " -> doubleValue: " + doubleBox.doubleValue());
        System.out.println("Long Box  : " + longBox + " -> doubleValue: " + longBox.doubleValue());

        System.out.println("Is doubleBox > intBox? " + doubleBox.isGreaterThan(intBox));
        System.out.printf("Sum of intBox + doubleBox = $%,.2f\n", intBox.sumWith(doubleBox));

        // 3. Generic Bounded max() Method
        System.out.println("\n--- 3. <T extends Comparable<T>> max() METHOD ---");
        Integer maxInt = max(100, 250);
        String maxStr = max("Apple", "Zebra");
        Double maxDouble = max(Arrays.asList(19.99, 49.95, 120.00, 5.50));

        System.out.println("max(100, 250)         : " + maxInt);
        System.out.println("max(\"Apple\", \"Zebra\") : " + maxStr);
        System.out.println("max([19.99, 49.95, 120.00, 5.50]): " + maxDouble);

        // 4. Wildcards (PECS) Demonstration
        System.out.println("\n--- 4. WILDCARDS DEMONSTRATION (PECS: Producer Extends, Consumer Super) ---");
        List<Integer> integers = Arrays.asList(10, 20, 30, 40);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);

        // Producer Extends: Reading numbers
        System.out.printf("Sum of List<Integer> via (? extends Number): %.2f\n", sumOfList(integers));
        System.out.printf("Sum of List<Double>  via (? extends Number): %.2f\n", sumOfList(doubles));

        // Consumer Super: Writing integers into List<Number> or List<Object>
        List<Number> numberList = new ArrayList<>();
        addIntegers(numberList, 4); // Writes into List<? super Integer>
        printCollection("numberList after addIntegers()", numberList);

        // 5. Try-with-resources CSV Reader
        System.out.println("\n--- 5. CSV READER WITH TRY-WITH-RESOURCES (products.csv) ---");
        String csvPath = "data/products.csv";
        // Check relative path fallback
        if (!new File(csvPath).exists()) {
            csvPath = "day-9-generics/data/products.csv";
        }
        List<Pair<String, Double>> products = readProductsCsv(csvPath);
        System.out.println("Successfully parsed " + products.size() + " product catalog entries:");
        for (Pair<String, Double> prod : products) {
            System.out.printf("  • %-30s -> $%,.2f\n", prod.getKey(), prod.getValue());
        }

        // 6. Custom AutoCloseable LIFO demonstration
        System.out.println();
        demonstrateAutoCloseableLIFO();
    }
}
