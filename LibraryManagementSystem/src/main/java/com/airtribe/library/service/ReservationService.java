package com.airtribe.library.service;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.LibraryBranch;
import com.airtribe.library.model.Patron;
import com.airtribe.library.model.Reservation;
import com.airtribe.library.model.ReservationStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for creating and managing reservations.
 * Acts as the Subject in the Observer pattern: when a reserved book
 * becomes available, the corresponding patron observer is notified.
 */
public class ReservationService {

    private static final Logger LOGGER = Logger.getLogger(ReservationService.class.getName());

    private final List<Reservation> reservations = new ArrayList<>();

    public Reservation createReservation(Book book, Patron patron, LibraryBranch branch) {
        if (branch.getAvailableCopies(book) > 0) {
            LOGGER.log(Level.WARNING,
                    "Reservation created while copies are available for book {0} at branch {1}",
                    new Object[]{book.getTitle(), branch.getName()});
        }
        Reservation reservation = new Reservation(book, patron, branch);
        reservations.add(reservation);
        LOGGER.log(Level.INFO, "Created reservation for book {0} by patron {1} at branch {2}",
                new Object[]{book.getTitle(), patron.getId(), branch.getName()});
        return reservation;
    }

    public List<Reservation> getActiveReservations() {
        List<Reservation> active = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.ACTIVE) {
                active.add(reservation);
            }
        }
        return Collections.unmodifiableList(active);
    }

    /**
     * Should be called when a book is returned to a branch.
     * Notifies the earliest active reservation (if any).
     */
    public void onBookReturned(Book book, LibraryBranch branch) {
        Reservation next = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .filter(r -> r.getBook().equals(book) && r.getBranch().equals(branch))
                .min(Comparator.comparing(Reservation::getCreatedAt))
                .orElse(null);

        if (next == null) {
            LOGGER.log(Level.INFO,
                    "No active reservations for book {0} at branch {1}",
                    new Object[]{book.getTitle(), branch.getName()});
            return;
        }

        next.markFulfilled();
        LOGGER.log(Level.INFO,
                "Fulfilling reservation for book {0} at branch {1} for patron {2}",
                new Object[]{book.getTitle(), branch.getName(), next.getPatron().getId()});

        // Notify the observer (Patron implements NotificationListener).
        next.getPatron().onBookAvailable(book, branch);
    }
}


