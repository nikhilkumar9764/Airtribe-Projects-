package com.airtribe.library.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a reservation (hold) that a patron places on a book at a branch.
 */
public class Reservation {

    private final Book book;
    private final Patron patron;
    private final LibraryBranch branch;
    private final LocalDateTime createdAt;
    private ReservationStatus status;

    public Reservation(Book book, Patron patron, LibraryBranch branch) {
        this.book = Objects.requireNonNull(book, "book must not be null");
        this.patron = Objects.requireNonNull(patron, "patron must not be null");
        this.branch = Objects.requireNonNull(branch, "branch must not be null");
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.ACTIVE;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LibraryBranch getBranch() {
        return branch;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void markFulfilled() {
        this.status = ReservationStatus.FULFILLED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}


