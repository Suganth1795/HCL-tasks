package com.hcl.library.model;

import java.util.Objects;

/**
 * Domain model representing a library Book.
 */
public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private boolean overdue;
    private int daysOverdue;

    public Book(String isbn, String title, String author) {
        this(isbn, title, author, false, 0);
    }

    public Book(String isbn, String title, String author, boolean overdue, int daysOverdue) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.overdue = overdue;
        this.daysOverdue = daysOverdue;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(int daysOverdue) {
        this.daysOverdue = daysOverdue;
        this.overdue = daysOverdue > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" by %s%s",
                isbn, title, author, (overdue ? " (OVERDUE by " + daysOverdue + " days)" : ""));
    }
}
