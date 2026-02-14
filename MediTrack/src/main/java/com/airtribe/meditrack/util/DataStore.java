package com.airtribe.meditrack.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generic DataStore class for storing entities.
 * Demonstrates generics, collections, and Java 8+ streams.
 */
public class DataStore<T> {
    private final Map<String, T> store;
    private final String entityType;

    public DataStore(String entityType) {
        this.store = new HashMap<>();
        this.entityType = entityType;
    }

    public void add(String id, T entity) {
        if (id == null || entity == null) {
            throw new IllegalArgumentException("ID and entity cannot be null");
        }
        store.put(id, entity);
    }

    public T get(String id) {
        return store.get(id);
    }

    public boolean contains(String id) {
        return store.containsKey(id);
    }

    public T remove(String id) {
        return store.remove(id);
    }

    public List<T> getAll() {
        return new ArrayList<>(store.values());
    }

    public int size() {
        return store.size();
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }

    public void clear() {
        store.clear();
    }

    /**
     * Filter entities using a predicate (Java 8+ streams)
     */
    public List<T> filter(java.util.function.Predicate<T> predicate) {
        return store.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    /**
     * Get all IDs
     */
    public Set<String> getAllIds() {
        return new HashSet<>(store.keySet());
    }

    @Override
    public String toString() {
        return String.format("DataStore[%s] - Size: %d", entityType, store.size());
    }
}

