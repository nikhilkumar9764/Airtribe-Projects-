package com.airtribe.library.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a book in the library catalog.
 * Immutable identifier (ISBN), mutable descriptive fields.
 */
public class Book {

    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private final Set<String> genres = new HashSet<>();

    public Book(String isbn, String title, String author, int publicationYear) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN must not be blank");
        }
        this.isbn = isbn;
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "title must not be null");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = Objects.requireNonNull(author, "author must not be null");
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Set<String> getGenres() {
        return Collections.unmodifiableSet(genres);
    }

    public void addGenre(String genre) {
        if (genre != null && !genre.isBlank()) {
            genres.add(genre.toLowerCase());
        }
    }

    public void removeGenre(String genre) {
        if (genre != null) {
            genres.remove(genre.toLowerCase());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", genres=" + genres +
                '}';
    }
}


