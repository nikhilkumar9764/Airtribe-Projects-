package com.airtribe.library.recommendation;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.Patron;
import com.airtribe.library.service.BookCatalogService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Context class for the Strategy pattern that delegates recommendation
 * generation to a pluggable RecommendationStrategy implementation.
 */
public class RecommendationService {

    private static final Logger LOGGER = Logger.getLogger(RecommendationService.class.getName());

    private final BookCatalogService catalogService;
    private RecommendationStrategy strategy;

    public RecommendationService(BookCatalogService catalogService, RecommendationStrategy strategy) {
        this.catalogService = Objects.requireNonNull(catalogService, "catalogService must not be null");
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
    }

    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
        LOGGER.log(Level.INFO, "Recommendation strategy set to {0}", strategy.getClass().getSimpleName());
    }

    public List<Book> recommendBooksFor(Patron patron) {
        if (patron == null) {
            return Collections.emptyList();
        }
        List<Book> recommendations = strategy.recommendBooks(patron, catalogService.getAllBooks());
        LOGGER.log(Level.INFO,
                "Generated {0} recommendations for patron {1}",
                new Object[]{recommendations.size(), patron.getId()});
        return recommendations;
    }
}


