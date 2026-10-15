package com.hcl.library.app;

import com.hcl.library.benchmark.CollectionPerformanceBenchmark;
import com.hcl.library.model.Book;
import com.hcl.library.model.BorrowRequest;
import com.hcl.library.service.LibraryWaitlistService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Driver Application for Day 7: Collections Framework - Part 1
 * Demonstrates:
 * 1. ArrayList catalogue
 * 2. LinkedHashSet deduplication
 * 3. ArrayDeque FIFO waitlist
 * 4. PriorityQueue faculty-first priority waitlist
 * 5. Iterator.remove() for safe overdue item purging
 * 6. Benchmark: ArrayList vs LinkedList insert and get(i) execution
 */
public class LibraryApp {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("     DAILY TASK 7: COLLECTIONS FRAMEWORK - LIBRARY WAITLIST & BENCHMARK ");
        System.out.println("========================================================================\n");

        LibraryWaitlistService service = new LibraryWaitlistService();

        // 1. ARRAYLIST CATALOGUE DEMO
        System.out.println("--- 1. ARRAYLIST BOOK CATALOGUE ---");
        service.addBookToCatalogue(new Book("978-0134685991", "Effective Java (3rd Edition)", "Joshua Bloch"));
        service.addBookToCatalogue(new Book("978-0596009205", "Head First Design Patterns", "Eric Freeman"));
        service.addBookToCatalogue(new Book("978-0132350884", "Clean Code", "Robert C. Martin", true, 14));
        service.addBookToCatalogue(new Book("978-0321356680", "Java Concurrency in Practice", "Brian Goetz", true, 35));
        service.addBookToCatalogue(new Book("978-0201633610", "Design Patterns (GoF)", "Erich Gamma"));

        System.out.println("Total catalogue items: " + service.getCatalogue().size());
        for (int i = 0; i < service.getCatalogue().size(); i++) {
            System.out.printf("  Index [%d] -> %s\n", i, service.getBookByIndex(i));
        }

        // 2. LINKEDHASHSET DEDUPLICATION DEMO
        System.out.println("\n--- 2. LINKEDHASHSET PATRON DEDUPLICATION ---");
        List<String> rawPatronRegistrations = Arrays.asList(
                "PATRON-101", "PATRON-102", "PATRON-103",
                "PATRON-101", // Duplicate!
                "PATRON-104",
                "PATRON-102", // Duplicate!
                "PATRON-105",
                "PATRON-103"  // Duplicate!
        );
        System.out.println("Raw registration submissions (with duplicates): " + rawPatronRegistrations);
        int newAdded = service.registerPatrons(rawPatronRegistrations);
        System.out.printf("Deduplicated unique patrons registered: %d (New unique: %d)\n",
                service.getRegisteredPatronIds().size(), newAdded);
        System.out.println("Patrons preserved in strict arrival order: " + service.getRegisteredPatronIds());

        // 3. ARRAYDEQUE REGULAR WAITLIST (FIFO) DEMO
        System.out.println("\n--- 3. ARRAYDEQUE REGULAR BORROW WAITLIST (FIFO) ---");
        service.enqueueRegularWaitlist(new BorrowRequest("REQ-101", "P-101", "Bob Smith",
                BorrowRequest.PatronType.STUDENT, "978-0134685991"));
        service.enqueueRegularWaitlist(new BorrowRequest("REQ-102", "P-102", "Charlie Day",
                BorrowRequest.PatronType.STUDENT, "978-0596009205"));
        service.enqueueRegularWaitlist(new BorrowRequest("REQ-103", "P-103", "David Kim",
                BorrowRequest.PatronType.GUEST, "978-0201633610"));

        System.out.println("Enqueued 3 requests. Serving from ArrayDeque in FIFO order:");
        while (service.getRegularWaitlistSize() > 0) {
            BorrowRequest req = service.serveNextRegularPatron();
            System.out.println("  • Dispatched request: " + req);
        }

        // 4. PRIORITYQUEUE FACULTY-FIRST DEMO
        System.out.println("\n--- 4. PRIORITYQUEUE FACULTY-FIRST BORROW DISPATCH ---");
        LocalDateTime baseTime = LocalDateTime.now();
        // Student arrived first
        service.enqueuePriorityRequest(new BorrowRequest("REQ-201", "S-201", "Student Alice",
                BorrowRequest.PatronType.STUDENT, "978-0134685991", baseTime.minusMinutes(10)));
        // Guest arrived second
        service.enqueuePriorityRequest(new BorrowRequest("REQ-202", "G-202", "Guest Peter",
                BorrowRequest.PatronType.GUEST, "978-0134685991", baseTime.minusMinutes(5)));
        // Faculty arrived last!
        service.enqueuePriorityRequest(new BorrowRequest("REQ-203", "F-203", "Prof. Alan Turing",
                BorrowRequest.PatronType.FACULTY, "978-0134685991", baseTime));
        // Another Faculty arrived slightly later
        service.enqueuePriorityRequest(new BorrowRequest("REQ-204", "F-204", "Dr. Grace Hopper",
                BorrowRequest.PatronType.FACULTY, "978-0134685991", baseTime.plusSeconds(30)));

        System.out.println("Dispatched from PriorityQueue (Faculty prioritized regardless of arrival):");
        int priorityOrder = 1;
        while (service.getPriorityWaitlistSize() > 0) {
            BorrowRequest req = service.serveNextPriorityPatron();
            System.out.printf("  [%d] Served: %s\n", priorityOrder++, req);
        }

        // 5. ITERATOR.REMOVE OVERDUE PURGING DEMO
        System.out.println("\n--- 5. ITERATOR.REMOVE OVERDUE BOOK PURGING ---");
        System.out.println("Catalogue size before purge: " + service.getCatalogue().size());
        int overdueThreshold = 30; // Purge books overdue > 30 days
        List<Book> purged = service.purgeOverdueBooks(overdueThreshold);
        System.out.println("Purged books overdue beyond " + overdueThreshold + " days:");
        for (Book b : purged) {
            System.out.println("  ❌ Removed: " + b);
        }
        System.out.println("Catalogue size after safe Iterator.remove: " + service.getCatalogue().size());

        // 6. BENCHMARK ARRAYLIST VS LINKEDLIST
        System.out.println("\n--- 6. EXECUTION TIMING BENCHMARK (ARRAYLIST VS LINKEDLIST) ---");
        List<CollectionPerformanceBenchmark.BenchmarkResult> benchmarkResults =
                CollectionPerformanceBenchmark.runBenchmark(40000, 20000);
        CollectionPerformanceBenchmark.printBenchmarkTable(benchmarkResults);
    }
}
