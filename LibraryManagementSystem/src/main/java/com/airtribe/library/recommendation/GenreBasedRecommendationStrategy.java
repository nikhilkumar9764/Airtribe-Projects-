package com.airtribe.library.recommendation;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.Loan;
import com.airtribe.library.model.Patron;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple recommendation strategy that suggests books based on the patron's
 * preferred genres and borrowing history.
 */
public class GenreBasedRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Book> recommendBooks(Patron patron, Collection<Book> catalog) {
        Set<String> preferredGenres = new HashSet<>(patron.getPreferredGenres());
        if (preferredGenres.isEmpty()) {
            // Derive preferences from history if not explicitly set.
            for (Loan loan : patron.getBorrowingHistory()) {
                preferredGenres.addAll(loan.getBook().getGenres());
            }
        }

        if (preferredGenres.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Book, Integer> scores = new HashMap<>();
        for (Book book : catalog) {
            int score = 0;
            for (String genre : book.getGenres()) {
                if (preferredGenres.contains(genre)) {
                    score++;
                }
            }
            if (score > 0) {
                scores.put(book, score);
            }
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}


