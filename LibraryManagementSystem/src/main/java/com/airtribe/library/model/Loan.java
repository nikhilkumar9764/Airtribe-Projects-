package com.airtribe.library.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a single loan (checkout) of a book by a patron from a branch.
 */
public class Loan {

    private final Book book;
    private final Patron patron;
    private final LibraryBranch branch;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Book book, Patron patron, LibraryBranch branch, LocalDate checkoutDate, LocalDate dueDate) {
        this.book = Objects.requireNonNull(book, "book must not be null");
        this.patron = Objects.requireNonNull(patron, "patron must not be null");
        this.branch = Objects.requireNonNull(branch, "branch must not be null");
        this.checkoutDate = Objects.requireNonNull(checkoutDate, "checkoutDate must not be null");
        this.dueDate = Objects.requireNonNull(dueDate, "dueDate must not be null");
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

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public void markReturned(LocalDate returnDate) {
        if (this.returnDate != null) {
            throw new IllegalStateException("Loan already returned");
        }
        this.returnDate = Objects.requireNonNull(returnDate, "returnDate must not be null");
    }
}


