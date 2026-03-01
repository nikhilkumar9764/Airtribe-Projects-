package com.airtribe.library.recommendation;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.Patron;

import java.util.Collection;
import java.util.List;

/**
 * Strategy interface for generating book recommendations.
 */
public interface RecommendationStrategy {

    List<Book> recommendBooks(Patron patron, Collection<Book> catalog);
}


