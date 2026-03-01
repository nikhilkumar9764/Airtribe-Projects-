package com.airtribe.library.service;

import com.airtribe.library.model.Book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service responsible for managing the book catalog.
 * Supports adding, removing, updating and searching books.
 */
public class BookCatalogService {

    private static final Logger LOGGER = Logger.getLogger(BookCatalogService.class.getName());

    private final Map<String, Book> booksByIsbn = new ConcurrentHashMap<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }
        String isbn = book.getIsbn();
        if (booksByIsbn.containsKey(isbn)) {
            LOGGER.log(Level.WARNING, "Attempted to add duplicate book with ISBN: {0}", isbn);
            throw new IllegalStateException("Book with ISBN already exists: " + isbn);
        }
        booksByIsbn.put(isbn, book);
        LOGGER.log(Level.INFO, "Added book to catalog: {0}", book);
    }

    public void removeBook(String isbn) {
        Book removed = booksByIsbn.remove(isbn);
        if (removed == null) {
            LOGGER.log(Level.WARNING, "Attempted to remove non-existing book with ISBN: {0}", isbn);
        } else {
            LOGGER.log(Level.INFO, "Removed book from catalog: {0}", removed);
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(booksByIsbn.get(isbn));
    }

    public List<Book> searchByTitle(String titleFragment) {
        if (titleFragment == null || titleFragment.isBlank()) {
            return Collections.emptyList();
        }
        String query = titleFragment.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String authorFragment) {
        if (authorFragment == null || authorFragment.isBlank()) {
            return Collections.emptyList();
        }
        String query = authorFragment.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public Collection<Book> getAllBooks() {
        return Collections.unmodifiableCollection(new ArrayList<>(booksByIsbn.values()));
    }
}


