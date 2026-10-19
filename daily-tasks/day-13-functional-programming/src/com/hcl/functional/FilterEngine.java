package com.hcl.functional;

import com.hcl.functional.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Daily Task 13: Functional Programming
 * Topic: Product Filter Engine
 *
 * Requirements:
 * 1. Filter a product list with composed Predicates (price, category, in-stock)
 * 2. Transform with Function chains (andThen / compose)
 * 3. Print with Consumer (and method references)
 * 4. Create defaults with Supplier
 * 5. Rewrite every eligible lambda as a method reference
 * 6. Strictly ZERO anonymous inner classes left where a lambda fits
 */
public class FilterEngine {

    // =========================================================================
    // 1. FILTERING WITH COMPOSED PREDICATES
    // =========================================================================

    public static List<Product> filterProducts(List<Product> products, Predicate<Product> condition) {
        List<Product> matched = new ArrayList<>();
        for (Product p : products) {
            if (condition.test(p)) {
                matched.add(p);
            }
        }
        return matched;
    }

    // =========================================================================
    // 2. TRANSFORMING WITH FUNCTION CHAINS
    // =========================================================================

    public static <R> List<R> transformProducts(List<Product> products, Function<Product, R> transformer) {
        List<R> results = new ArrayList<>();
        for (Product p : products) {
            results.add(transformer.apply(p));
        }
        return results;
    }

    // =========================================================================
    // 3. PROCESSING WITH CONSUMER
    // =========================================================================

    public static void processProducts(List<Product> products, Consumer<Product> action) {
        for (Product p : products) {
            action.accept(p);
        }
    }

    // =========================================================================
    // 4. DEFAULT FALLBACK WITH SUPPLIER
    // =========================================================================

    public static Product getOrFallback(List<Product> products, Predicate<Product> condition, Supplier<Product> fallbackSupplier) {
        for (Product p : products) {
            if (condition.test(p)) {
                return p;
            }
        }
        return fallbackSupplier.get();
    }

    // =========================================================================
    // MAIN DRIVER & HARNESS
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("     DAILY TASK 13: FUNCTIONAL PROGRAMMING - PRODUCT FILTER ENGINE      ");
        System.out.println("========================================================================\n");

        List<Product> catalog = Arrays.asList(
                new Product("P101", "Dell UltraSharp 32-inch 4K", "Electronics", 749.99, true, 4.8),
                new Product("P102", "Apple MacBook Air M3", "Electronics", 1099.00, true, 4.9),
                new Product("P103", "Ergonomic Mesh Chair", "Furniture", 299.50, false, 4.3),
                new Product("P104", "Solid Oak Standing Desk", "Furniture", 550.00, true, 4.7),
                new Product("P105", "Logitech MX Master 3S Mouse", "Peripherals", 99.00, true, 4.9),
                new Product("P106", "Keychron Q1 Pro Mechanical", "Peripherals", 199.00, false, 4.6),
                new Product("P107", "Sony WH-1000XM5 Headphones", "Audio", 399.00, true, 4.7),
                new Product("P108", "Budget USB-C Earbuds", "Audio", 19.99, true, 3.8)
        );

        System.out.println("--- TOTAL CATALOGUE (" + catalog.size() + " items) ---");
        // Consumer with Method Reference
        catalog.forEach(System.out::println);

        // ---------------------------------------------------------------------
        // PART 1: COMPOSED PREDICATES (AND, OR, NEGATE)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 1. COMPOSED PREDICATES FILTERING ---");

        // Atomic predicates
        Predicate<Product> isElectronics = p -> "Electronics".equalsIgnoreCase(p.getCategory());
        Predicate<Product> under800 = p -> p.getPrice() < 800.0;
        Predicate<Product> isInStock = Product::isInStock; // Method Reference!
        Predicate<Product> isTopRated = p -> p.getRating() >= 4.7;

        // Composed: In-stock Electronics under $800
        Predicate<Product> budgetInStockElectronics = isElectronics.and(under800).and(isInStock);

        System.out.println("\nFilter: [Electronics AND Price < $800 AND In-Stock]:");
        List<Product> filteredBudget = filterProducts(catalog, budgetInStockElectronics);
        filteredBudget.forEach(System.out::println);

        // Composed with OR and NEGATE: (Furniture OR Audio) AND In-Stock
        Predicate<Product> isFurnitureOrAudio = ((Predicate<Product>) p -> "Furniture".equals(p.getCategory()))
                .or(p -> "Audio".equals(p.getCategory()))
                .and(isInStock);

        System.out.println("\nFilter: [(Furniture OR Audio) AND In-Stock]:");
        List<Product> furnitureOrAudio = filterProducts(catalog, isFurnitureOrAudio);
        furnitureOrAudio.forEach(System.out::println);

        // Composed with NEGATE: Out-of-Stock items
        Predicate<Product> isOutOfStock = isInStock.negate();
        System.out.println("\nFilter: [Out of Stock (isInStock.negate())]:");
        filterProducts(catalog, isOutOfStock).forEach(System.out::println);

        // ---------------------------------------------------------------------
        // PART 2: FUNCTION CHAINS (andThen)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. FUNCTION CHAINS (andThen PIPELINE) ---");

        // Function 1: Product -> Clean Tag String
        Function<Product, String> toTag = p -> String.format("[%s: %s ($%.2f)]", p.getId(), p.getName(), p.getPrice());

        // Function 2: String -> UpperCase Banner
        Function<String, String> toUpperBanner = String::toUpperCase; // Method Reference!

        // Function 3: String -> Formatted Badge
        Function<String, String> addBadge = s -> "🏷️ FEATURED ITEM: " + s;

        // Chained Pipeline: Product -> Tag -> UpperCase -> Badge
        Function<Product, String> promoBadgePipeline = toTag.andThen(toUpperBanner).andThen(addBadge);

        List<String> promoBadges = transformProducts(filteredBudget, promoBadgePipeline);
        promoBadges.forEach(System.out::println);

        // ---------------------------------------------------------------------
        // PART 3: SUPPLIER FOR DEFAULT VALUES
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. SUPPLIER FOR FALLBACK GENERATION ---");

        Supplier<Product> defaultOfficeBundleSupplier = () ->
                new Product("DEF-999", "Standard Ergonomic Workstation Bundle", "Bundle", 499.00, true, 4.5);

        // Search for non-existent item (Price < $10)
        Predicate<Product> ultraCheapFilter = p -> p.getPrice() < 10.0;
        Product resultOrFallback = getOrFallback(catalog, ultraCheapFilter, defaultOfficeBundleSupplier);

        System.out.println("Query for Product < $10.00 (Not found, fallback triggered):");
        System.out.println("  • " + resultOrFallback);

        System.out.println("========================================================================\n");
    }
}
