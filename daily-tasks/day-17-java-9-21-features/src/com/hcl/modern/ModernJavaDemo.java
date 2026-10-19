package com.hcl.modern;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Day 17: Java 9–21 Features + Immutable Collections & Modern APIs
 * Interactive laboratory demonstrating:
 * 1. Sealed interface & records
 * 2. Exhaustive pattern-matching switch for Shape area calculation
 * 3. Text block invoice email template
 * 4. Immutable collections (List.of) & UnsupportedOperationException
 * 5. String modern methods (strip, isBlank, lines, repeat)
 */
public class ModernJavaDemo {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("DAY 17: JAVA 9-21 MODERN FEATURES & MODERN APIS DRILL");
        System.out.println("================================================================================\n");

        runSealedAndPatternMatchingDemo();
        runTextBlockDemo();
        runImmutableCollectionsDemo();
        runStringModernApisDemo();
    }

    /**
     * Feature 1: Sealed Types + Pattern Matching Switch (Java 17/21)
     */
    private static void runSealedAndPatternMatchingDemo() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. SEALED TYPES & EXHAUSTIVE PATTERN MATCHING SWITCH");
        System.out.println("--------------------------------------------------------------------------------");

        List<Shape> shapes = List.of(
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Rectangle(5.0, 5.0) // Square instance
        );

        for (Shape shape : shapes) {
            // Pattern matching switch expression with guarded pattern (when clause)
            String description = switch (shape) {
                case Circle c -> String.format("Circle(radius=%.1f) -> Area = %.2f", c.radius(), Math.PI * c.radius() * c.radius());
                case Rectangle r when r.isSquare() -> String.format("Square(side=%.1f) -> Area = %.2f (Detected via when-guard)", r.width(), r.calculateArea());
                case Rectangle r -> String.format("Rectangle(w=%.1f, h=%.1f) -> Area = %.2f", r.width(), r.height(), r.calculateArea());
                // No default clause needed! The compiler verifies exhaustiveness because Shape is sealed.
            };
            System.out.println("  " + description);
        }
        System.out.println();
    }

    /**
     * Feature 2: Text Blocks (Java 15)
     */
    private static void runTextBlockDemo() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("2. TEXT BLOCKS & MULTI-LINE TEMPLATE GENERATION");
        System.out.println("--------------------------------------------------------------------------------");

        Invoice invoice = new Invoice(
                "INV-2026-HCL-091",
                "Global Logistics Corp",
                "Cloud Architecture Consulting (Sprint 12)",
                40,
                125.00,
                LocalDate.now(),
                LocalDate.now().plusDays(15)
        );

        String email = invoice.generateEmailTemplate();
        System.out.println(email);
    }

    /**
     * Feature 3: Immutable Collections (Java 9) & UnsupportedOperationException
     */
    private static void runImmutableCollectionsDemo() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("3. IMMUTABLE COLLECTIONS (List.of, Set.of, Map.of) & INTEGRITY");
        System.out.println("--------------------------------------------------------------------------------");

        List<String> frameworks = List.of("Spring Boot", "Micronaut", "Quarkus");
        System.out.println("Created Immutable List: " + frameworks);
        System.out.println("Class Type: " + frameworks.getClass().getName());

        System.out.print("Attempting to mutate List.of via .add(\"Helidon\"): ");
        try {
            frameworks.add("Helidon");
            System.out.println("Mutation succeeded (unexpected!)");
        } catch (UnsupportedOperationException e) {
            System.out.println("SUCCESSFULLY BLOCKED!");
            System.out.println("  -> Caught expected java.lang.UnsupportedOperationException.");
            System.out.println("  -> Reason: List.of creates an unmodifiable, space-efficient, null-hostile list.");
        }

        Set<String> cloudProviders = Set.of("AWS", "Azure", "GCP");
        Map<String, Integer> portMappings = Map.of("HTTP", 80, "HTTPS", 443, "SSH", 22);
        System.out.println("Immutable Set: " + cloudProviders);
        System.out.println("Immutable Map: " + portMappings);
        System.out.println();
    }

    /**
     * Feature 4: Modern String Methods (Java 11+)
     */
    private static void runStringModernApisDemo() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("4. MODERN STRING APIS (strip, isBlank, lines, repeat)");
        System.out.println("--------------------------------------------------------------------------------");

        // Unicode whitespace (en-quad \u2000 and em-space \u2003)
        String rawInput = "\u2000  \t  HCL Modern Java Suite  \u2003\n";
        System.out.println("Raw Input String length: " + rawInput.length());

        // trim() vs strip() comparison
        System.out.println(" - trim() result:  [" + rawInput.trim() + "] (length: " + rawInput.trim().length() + ", misses unicode spaces)");
        System.out.println(" - strip() result: [" + rawInput.strip() + "] (length: " + rawInput.strip().length() + ", cleans ALL unicode spaces)");

        // isBlank() vs isEmpty()
        String emptyStr = "";
        String whitespaceOnly = "   \t\n  ";
        System.out.printf(" - String \"%s\": isEmpty()=%b, isBlank()=%b\n", emptyStr, emptyStr.isEmpty(), emptyStr.isBlank());
        System.out.printf(" - String \"   \\t\\n  \": isEmpty()=%b, isBlank()=%b\n", whitespaceOnly.isEmpty(), whitespaceOnly.isBlank());

        // lines() stream
        String multiLine = "Line 1: Requirement Analysis\nLine 2: Test Automation\nLine 3: Delivery";
        long lineCount = multiLine.lines().count();
        System.out.println(" - lines() stream count: " + lineCount + " lines");

        // repeat()
        String separator = "=".repeat(40);
        System.out.println(" - repeat(40): " + separator);
        System.out.println();
    }
}
