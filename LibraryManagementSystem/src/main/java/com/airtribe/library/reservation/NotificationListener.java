package com.airtribe.library.reservation;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.LibraryBranch;

/**
 * Observer in the Observer pattern for reservation notifications.
 * Implementations will be notified when a reserved book becomes available.
 */
public interface NotificationListener {

    void onBookAvailable(Book book, LibraryBranch branch);
}


