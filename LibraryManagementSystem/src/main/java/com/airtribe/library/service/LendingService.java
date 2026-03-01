package com.airtribe.library.service;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.LibraryBranch;
import com.airtribe.library.model.Loan;
import com.airtribe.library.model.Patron;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for lending operations: checkouts and returns.
 * Delegates inventory changes to LibraryBranch and reservation handling
 * to ReservationService.
 */
public class LendingService {

    private static final Logger LOGGER = Logger.getLogger(LendingService.class.getName());

    private final ReservationService reservationService;
    private final List<Loan> activeLoans = new ArrayList<>();

    public LendingService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Loan checkoutBook(Patron patron, Book book, LibraryBranch branch, int loanDays) {
        if (loanDays <= 0) {
            throw new IllegalArgumentException("Loan days must be positive");
        }

        branch.checkoutCopy(book);
        LocalDate checkoutDate = LocalDate.now();
        LocalDate dueDate = checkoutDate.plusDays(loanDays);
        Loan loan = new Loan(book, patron, branch, checkoutDate, dueDate);
        activeLoans.add(loan);

        LOGGER.log(Level.INFO,
                "Checked out book {0} from branch {1} to patron {2} due on {3}",
                new Object[]{book.getTitle(), branch.getName(), patron.getId(), dueDate});

        return loan;
    }

    public void returnBook(Patron patron, Book book, LibraryBranch branch) {
        Iterator<Loan> iterator = activeLoans.iterator();
        while (iterator.hasNext()) {
            Loan loan = iterator.next();
            if (loan.getBook().equals(book)
                    && loan.getPatron().equals(patron)
                    && loan.getBranch().equals(branch)
                    && !loan.isReturned()) {

                loan.markReturned(LocalDate.now());
                iterator.remove();
                branch.returnCopy(book);
                patron.addLoanToHistory(loan);

                LOGGER.log(Level.INFO,
                        "Returned book {0} to branch {1} from patron {2}",
                        new Object[]{book.getTitle(), branch.getName(), patron.getId()});

                // Notify reservation system that a copy is now available.
                reservationService.onBookReturned(book, branch);
                return;
            }
        }

        LOGGER.log(Level.WARNING,
                "No active loan found to return for book {0}, branch {1}, patron {2}",
                new Object[]{book.getTitle(), branch.getName(), patron.getId()});
        throw new IllegalStateException("No active loan found for this patron/book/branch combination");
    }

    public List<Loan> getActiveLoans() {
        return Collections.unmodifiableList(new ArrayList<>(activeLoans));
    }
}


