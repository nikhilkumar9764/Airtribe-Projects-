package com.airtribe.library.model;

import com.airtribe.library.reservation.NotificationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents a library member.
 */
public class Patron implements NotificationListener {

    private static final Logger LOGGER = Logger.getLogger(Patron.class.getName());

    private final String id;
    private String name;
    private String email;

    private final List<Loan> borrowingHistory = new ArrayList<>();
    private final Set<String> preferredGenres = new HashSet<>();

    public Patron(String id, String name, String email) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Patron id must not be blank");
        }
        this.id = id;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "email must not be null");
    }

    public List<Loan> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public void addLoanToHistory(Loan loan) {
        borrowingHistory.add(Objects.requireNonNull(loan, "loan must not be null"));
    }

    public Set<String> getPreferredGenres() {
        return Collections.unmodifiableSet(preferredGenres);
    }

    public void addPreferredGenre(String genre) {
        if (genre != null && !genre.isBlank()) {
            preferredGenres.add(genre.toLowerCase());
        }
    }

    @Override
    public void onBookAvailable(Book book, LibraryBranch branch) {
        // In a real system, this might send an email / SMS.
        // For this assignment we log a notification.
        String message = String.format(
                "Notification for patron %s (%s): Book '%s' is now available at branch '%s'",
                name, email, book.getTitle(), branch.getName()
        );
        LOGGER.info(message);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", preferredGenres=" + preferredGenres +
                '}';
    }
}


