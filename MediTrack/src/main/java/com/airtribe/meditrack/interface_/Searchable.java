package com.airtribe.meditrack.interface_;

/**
 * Interface for searchable entities.
 * Demonstrates interface with default methods (Java 8+).
 */
public interface Searchable {
    /**
     * Check if entity matches the search query
     */
    boolean matches(String query);

    /**
     * Default method for case-insensitive search
     */
    default boolean matchesIgnoreCase(String query) {
        if (query == null) return false;
        return matches(query.toLowerCase());
    }
}

