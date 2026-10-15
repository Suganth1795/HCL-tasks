package com.hcl.library.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Domain model representing a patron's borrow reservation request.
 * Implements Comparable to enable natural ordering in PriorityQueue:
 * Faculty requests precede Student/Guest requests; ties are broken by request timestamp (FIFO).
 */
public class BorrowRequest implements Comparable<BorrowRequest> {

    public enum PatronType {
        FACULTY(1),
        STUDENT(2),
        GUEST(3);

        private final int priorityRank;

        PatronType(int priorityRank) {
            this.priorityRank = priorityRank;
        }

        public int getPriorityRank() {
            return priorityRank;
        }
    }

    private final String requestId;
    private final String patronId;
    private final String patronName;
    private final PatronType patronType;
    private final String requestedBookIsbn;
    private final LocalDateTime requestTimestamp;

    public BorrowRequest(String requestId, String patronId, String patronName,
                         PatronType patronType, String requestedBookIsbn) {
        this.requestId = requestId;
        this.patronId = patronId;
        this.patronName = patronName;
        this.patronType = patronType;
        this.requestedBookIsbn = requestedBookIsbn;
        this.requestTimestamp = LocalDateTime.now();
    }

    public BorrowRequest(String requestId, String patronId, String patronName,
                         PatronType patronType, String requestedBookIsbn, LocalDateTime timestamp) {
        this.requestId = requestId;
        this.patronId = patronId;
        this.patronName = patronName;
        this.patronType = patronType;
        this.requestedBookIsbn = requestedBookIsbn;
        this.requestTimestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getPatronName() {
        return patronName;
    }

    public PatronType getPatronType() {
        return patronType;
    }

    public String getRequestedBookIsbn() {
        return requestedBookIsbn;
    }

    public LocalDateTime getRequestTimestamp() {
        return requestTimestamp;
    }

    /**
     * Comparable contract:
     * 1. Lower priorityRank comes first (FACULTY=1 < STUDENT=2 < GUEST=3).
     * 2. If same patronType, earlier requestTimestamp comes first (FIFO).
     */
    @Override
    public int compareTo(BorrowRequest other) {
        int rankDiff = Integer.compare(this.patronType.getPriorityRank(), other.patronType.getPriorityRank());
        if (rankDiff != 0) {
            return rankDiff;
        }
        return this.requestTimestamp.compareTo(other.requestTimestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-14s (%s) | Book: %s | Time: %s",
                requestId, patronName, patronType, requestedBookIsbn,
                requestTimestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    }
}
