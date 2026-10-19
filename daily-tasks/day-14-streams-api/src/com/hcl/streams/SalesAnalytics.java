package com.hcl.streams;

import com.hcl.streams.model.SalesOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Daily Task 14: Streams API
 * Topic: Sales Analytics & Performance Benchmarking
 *
 * Requirements:
 * 1. Process a dataset of 1,000 sales orders.
 * 2. Compute revenue by category using Collectors.groupingBy + Collectors.summingDouble.
 * 3. Identify Top 3 customers by spending using stream collectors and sorting.
 * 4. Partition orders by delivery status using Collectors.partitioningBy.
 * 5. Compute average basket size using Collectors.averagingDouble.
 * 6. Generate comma-joined distinct product catalog using flatMap and Collectors.joining(", ").
 * 7. Benchmark sequential vs parallel stream for a CPU-heavy mapping workload.
 * 8. Constraint: Every report uses Collectors; strictly NO external state mutation inside streams.
 */
public class SalesAnalytics {

    private static final String[] CATEGORIES = {"Electronics", "Furniture", "Audio", "Peripherals", "Accessories"};
    private static final String[] CUSTOMERS = {
            "Alice Walker", "Bob Martin", "Charlie Day", "Diana Prince", "Emma Watson",
            "Frank Castle", "Grace Hopper", "Henry Cavill", "Iris West", "John Doe"
    };
    private static final String[] STATUSES = {"DELIVERED", "PENDING", "CANCELLED"};
    private static final String[][] PRODUCTS_PER_CAT = {
            {"4K Monitor", "MacBook Air", "Gaming Laptop", "iPad Pro"},
            {"Standing Desk", "Ergonomic Chair", "Bookshelf", "Desk Lamp"},
            {"Noise-Cancelling Headphones", "Bluetooth Speaker", "Studio Mic", "Soundbar"},
            {"Mechanical Keyboard", "Wireless Mouse", "Webcam HD", "Trackpad"},
            {"USB-C Cable", "Thunderbolt Dock", "Laptop Stand", "Screen Cleaner"}
    };

    /**
     * Generates a deterministic dataset of 1,000 orders for benchmarking.
     */
    public static List<SalesOrder> generateSampleOrders(int count) {
        List<SalesOrder> orders = new ArrayList<>(count);
        Random rand = new Random(42);

        for (int i = 1; i <= count; i++) {
            String orderId = String.format("ORD-%04d", i);
            int custIdx = rand.nextInt(CUSTOMERS.length);
            String custId = "CUST-" + (100 + custIdx);
            String custName = CUSTOMERS[custIdx];

            int catIdx = rand.nextInt(CATEGORIES.length);
            String category = CATEGORIES[catIdx];

            int numProducts = 1 + rand.nextInt(3);
            List<String> products = new ArrayList<>();
            for (int p = 0; p < numProducts; p++) {
                String[] catProds = PRODUCTS_PER_CAT[catIdx];
                products.add(catProds[rand.nextInt(catProds.length)]);
            }

            double total = 25.0 + (rand.nextDouble() * 1475.0); // $25 to $1500
            total = Math.round(total * 100.0) / 100.0;

            String status = STATUSES[rand.nextInt(STATUSES.length)];

            orders.add(new SalesOrder(orderId, custId, custName, category, products, total, status));
        }

        return Collections.unmodifiableList(orders);
    }

    /**
     * Simulated CPU-intensive cryptographic / numerical workload for stream benchmarking.
     */
    public static double cpuHeavyTask(SalesOrder order) {
        double value = order.getOrderTotal();
        for (int i = 0; i < 20_000; i++) {
            value = Math.sin(value) * Math.cos(value) + Math.sqrt(Math.abs(value) + 1.0);
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("       DAILY TASK 14: STREAMS API - 1,000 ORDERS SALES ANALYTICS        ");
        System.out.println("========================================================================\n");

        List<SalesOrder> orders = generateSampleOrders(1000);
        System.out.println("Loaded " + orders.size() + " sales order transactions for processing.\n");

        // ---------------------------------------------------------------------
        // 1. REVENUE BY CATEGORY (groupingBy + summingDouble)
        // ---------------------------------------------------------------------
        System.out.println("--- 1. TOTAL REVENUE BY CATEGORY (Collectors.groupingBy + summingDouble) ---");
        Map<String, Double> revenueByCategory = orders.stream()
                .collect(Collectors.groupingBy(
                        SalesOrder::getCategory,
                        Collectors.summingDouble(SalesOrder::getOrderTotal)
                ));

        revenueByCategory.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("  • %-15s : $%,12.2f\n", entry.getKey(), entry.getValue()));

        // ---------------------------------------------------------------------
        // 2. TOP 3 CUSTOMERS BY TOTAL REVENUE
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. TOP 3 CUSTOMERS BY EXPENDITURE ---");
        Map<String, Double> customerSpend = orders.stream()
                .collect(Collectors.groupingBy(
                        SalesOrder::getCustomerName,
                        Collectors.summingDouble(SalesOrder::getOrderTotal)
                ));

        List<Map.Entry<String, Double>> top3Customers = customerSpend.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<String, Double> entry : top3Customers) {
            System.out.printf("  [#%d] %-16s : $%,12.2f in total orders\n", rank++, entry.getKey(), entry.getValue());
        }

        // ---------------------------------------------------------------------
        // 3. ORDERS PARTITIONED BY STATUS (DELIVERED vs PENDING/CANCELLED)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. ORDERS PARTITIONED BY STATUS (Delivered vs Others) ---");
        Map<Boolean, List<SalesOrder>> partitionedOrders = orders.stream()
                .collect(Collectors.partitioningBy(o -> "DELIVERED".equals(o.getStatus())));

        System.out.printf("  • DELIVERED Orders Count  : %d orders ($%,.2f)\n",
                partitionedOrders.get(true).size(),
                partitionedOrders.get(true).stream().mapToDouble(SalesOrder::getOrderTotal).sum());
        System.out.printf("  • NON-DELIVERED Count     : %d orders ($%,.2f)\n",
                partitionedOrders.get(false).size(),
                partitionedOrders.get(false).stream().mapToDouble(SalesOrder::getOrderTotal).sum());

        // ---------------------------------------------------------------------
        // 4. AVERAGE BASKET SIZE (Collectors.averagingDouble)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. AVERAGE BASKET SIZE ---");
        double averageBasket = orders.stream()
                .collect(Collectors.averagingDouble(SalesOrder::getOrderTotal));
        System.out.printf("  • Mean Transaction Value (Basket Size): $%,.2f\n", averageBasket);

        // ---------------------------------------------------------------------
        // 5. COMMA-JOINED DISTINCT PRODUCT LIST (flatMap + joining)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. DISTINCT PRODUCT CATALOG (flatMap + Collectors.joining) ---");
        String commaJoinedCatalog = orders.stream()
                .flatMap(o -> o.getProductNames().stream())
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        System.out.println("  • Active Catalog Items (" + orders.stream().flatMap(o -> o.getProductNames().stream()).distinct().count() + " unique products):");
        System.out.println("    " + commaJoinedCatalog);

        // ---------------------------------------------------------------------
        // 6. BENCHMARK: SEQUENTIAL VS PARALLEL STREAM (CPU-HEAVY MAPPING)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 6. PERFORMANCE BENCHMARK: SEQUENTIAL VS PARALLEL STREAM ---");
        System.out.println("  Workload: CPU-intensive non-linear trigonometric calculations across 1,000 orders.");

        // Warmup JIT
        orders.stream().limit(100).mapToDouble(SalesAnalytics::cpuHeavyTask).sum();

        // Sequential Execution
        long start = System.currentTimeMillis();
        double seqSum = orders.stream()
                .mapToDouble(SalesAnalytics::cpuHeavyTask)
                .sum();
        long seqTime = System.currentTimeMillis() - start;

        // Parallel Execution
        start = System.currentTimeMillis();
        double parSum = orders.parallelStream()
                .mapToDouble(SalesAnalytics::cpuHeavyTask)
                .sum();
        long parTime = System.currentTimeMillis() - start;

        double speedup = (double) seqTime / Math.max(1, parTime);

        System.out.println("\n========================================================================");
        System.out.println("                 SEQUENTIAL VS PARALLEL TIMING COMPARISON               ");
        System.out.println("========================================================================");
        System.out.printf("  Sequential Stream Execution Time : %,d ms\n", seqTime);
        System.out.printf("  Parallel Stream Execution Time   : %,d ms\n", parTime);
        System.out.printf("  Parallel Speedup Factor          : %.2fx faster (Utilizing ForkJoinPool.commonPool)\n", speedup);
        System.out.println("========================================================================\n");
    }
}
