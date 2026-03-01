package com.airtribe.library.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a physical library branch that holds inventory for books.
 */
public class LibraryBranch {

    private final String id;
    private String name;
    private String address;

    // Book inventory: available and borrowed copy counts per book.
    private final Map<Book, Integer> availableCopies = new HashMap<>();
    private final Map<Book, Integer> borrowedCopies = new HashMap<>();

    public LibraryBranch(String id, String name, String address) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Branch id must not be blank");
        }
        this.id = id;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.address = Objects.requireNonNull(address, "address must not be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Objects.requireNonNull(address, "address must not be null");
    }

    public Map<Book, Integer> getAvailableCopiesSnapshot() {
        return Collections.unmodifiableMap(new HashMap<>(availableCopies));
    }

    public int getAvailableCopies(Book book) {
        return availableCopies.getOrDefault(book, 0);
    }

    public int getBorrowedCopies(Book book) {
        return borrowedCopies.getOrDefault(book, 0);
    }

    public void addCopies(Book book, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        availableCopies.merge(book, count, Integer::sum);
    }

    public void removeCopies(Book book, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        int current = availableCopies.getOrDefault(book, 0);
        if (count > current) {
            throw new IllegalStateException("Cannot remove more copies than available");
        }
        int remaining = current - count;
        if (remaining == 0) {
            availableCopies.remove(book);
        } else {
            availableCopies.put(book, remaining);
        }
    }

    public void checkoutCopy(Book book) {
        int currentAvailable = availableCopies.getOrDefault(book, 0);
        if (currentAvailable <= 0) {
            throw new IllegalStateException("No available copies for book: " + book.getTitle());
        }
        availableCopies.put(book, currentAvailable - 1);
        borrowedCopies.merge(book, 1, Integer::sum);
    }

    public void returnCopy(Book book) {
        int currentBorrowed = borrowedCopies.getOrDefault(book, 0);
        if (currentBorrowed <= 0) {
            throw new IllegalStateException("No borrowed copies to return for book: " + book.getTitle());
        }
        borrowedCopies.put(book, currentBorrowed - 1);
        availableCopies.merge(book, 1, Integer::sum);
    }

    @Override
    public String toString() {
        return "LibraryBranch{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}


