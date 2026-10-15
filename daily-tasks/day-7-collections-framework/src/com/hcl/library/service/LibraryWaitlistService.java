package com.hcl.library.service;

import com.hcl.library.model.Book;
import com.hcl.library.model.BorrowRequest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Service managing library operations and demonstrating specialized Collections Framework components.
 *
 * COLLECTION CHOICE RATIONALE:
 * 1. ArrayList<Book>:
 *    - Why chosen: Catalogue lookup requires frequent random positional access get(i) and cache locality.
 *    - Trade-off: Fast O(1) random reads, amortized O(1) append at tail; slow O(N) arbitrary middle insertions.
 *
 * 2. LinkedHashSet<String>:
 *    - Why chosen: Deduplication of patron registrations while strictly preserving original arrival/registration order.
 *    - Trade-off: Constant-time O(1) contains/add operations like HashSet, but with predictable insertion-order iteration.
 *
 * 3. ArrayDeque<BorrowRequest>:
 *    - Why chosen: Waitlist buffer implemented as a double-ended queue (FIFO).
 *    - Trade-off: Significantly faster than LinkedList (no per-node allocation overhead, superior cache locality, no 24-byte node pointers).
 *
 * 4. PriorityQueue<BorrowRequest>:
 *    - Why chosen: Unbounded binary min-heap where faculty requests are elevated to the front of the queue regardless of arrival order.
 *    - Trade-off: O(log N) insertion and removal, O(1) peek.
 *
 * 5. Iterator.remove():
 *    - Why chosen: Safe in-place removal of overdue items while iterating over collections, preventing ConcurrentModificationException.
 */
public class LibraryWaitlistService {

    // 1. ArrayList for catalogue (fast O(1) indexed reads)
    private final List<Book> catalogue;

    // 2. LinkedHashSet for deduplicating patron IDs while maintaining insertion sequence
    private final Set<String> registeredPatronIds;

    // 3. ArrayDeque for general FIFO waitlist (zero node overhead compared to LinkedList)
    private final Deque<BorrowRequest> regularWaitlist;

    // 4. PriorityQueue for tiered role-based reservation (Faculty-first)
    private final Queue<BorrowRequest> prioritizedWaitlist;

    public LibraryWaitlistService() {
        this.catalogue = new ArrayList<>();
        this.registeredPatronIds = new LinkedHashSet<>();
        this.regularWaitlist = new ArrayDeque<>();
        this.prioritizedWaitlist = new PriorityQueue<>();
    }

    // --- CATALOGUE OPERATIONS (ArrayList) ---
    public void addBookToCatalogue(Book book) {
        catalogue.add(book);
    }

    public List<Book> getCatalogue() {
        return Collections.unmodifiableList(catalogue);
    }

    public Book getBookByIndex(int index) {
        if (index >= 0 && index < catalogue.size()) {
            return catalogue.get(index); // O(1) random access
        }
        return null;
    }

    // --- DEDUPLICATION (LinkedHashSet) ---
    /**
     * Registers a batch of incoming patron ID applications.
     * LinkedHashSet automatically filters out duplicates while preserving original registration sequence.
     *
     * @param incomingIds List containing raw patron IDs (may contain duplicates)
     * @return count of newly registered unique patrons
     */
    public int registerPatrons(List<String> incomingIds) {
        int initialSize = registeredPatronIds.size();
        registeredPatronIds.addAll(incomingIds); // LinkedHashSet strips duplicates
        return registeredPatronIds.size() - initialSize;
    }

    public Set<String> getRegisteredPatronIds() {
        return Collections.unmodifiableSet(registeredPatronIds);
    }

    // --- GENERAL WAITLIST (ArrayDeque FIFO) ---
    public void enqueueRegularWaitlist(BorrowRequest request) {
        regularWaitlist.offerLast(request); // Enqueue at tail
    }

    public BorrowRequest serveNextRegularPatron() {
        return regularWaitlist.pollFirst(); // Dequeue from head (FIFO)
    }

    public int getRegularWaitlistSize() {
        return regularWaitlist.size();
    }

    // --- PRIORITIZED WAITLIST (PriorityQueue - Faculty First) ---
    public void enqueuePriorityRequest(BorrowRequest request) {
        prioritizedWaitlist.offer(request); // Enters binary min-heap
    }

    public BorrowRequest serveNextPriorityPatron() {
        return prioritizedWaitlist.poll(); // Returns highest priority (Faculty first, then FIFO)
    }

    public int getPriorityWaitlistSize() {
        return prioritizedWaitlist.size();
    }

    // --- SAFE ITERATION REMOVAL (Iterator.remove) ---
    /**
     * Purges books overdue past the threshold days using Iterator.remove().
     * This avoids ConcurrentModificationException that occurs with foreach modification.
     *
     * @param overdueDaysThreshold Threshold in days
     * @return List of removed overdue books
     */
    public List<Book> purgeOverdueBooks(int overdueDaysThreshold) {
        List<Book> purgedBooks = new ArrayList<>();
        Iterator<Book> iterator = catalogue.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.isOverdue() && book.getDaysOverdue() > overdueDaysThreshold) {
                purgedBooks.add(book);
                iterator.remove(); // Safe removal without throwing ConcurrentModificationException
            }
        }
        return purgedBooks;
    }
}
