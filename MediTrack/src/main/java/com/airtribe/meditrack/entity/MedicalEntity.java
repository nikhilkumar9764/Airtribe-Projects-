package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

/**
 * Abstract class for medical entities.
 * Demonstrates abstraction and template method pattern.
 */
public abstract class MedicalEntity {
    protected String id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public MedicalEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    protected void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Abstract method - must be implemented by subclasses
     */
    public abstract String getEntityType();

    /**
     * Template method - defines the algorithm structure
     */
    public final String getEntityInfo() {
        return String.format("%s [ID: %s, Created: %s, Updated: %s]", 
            getEntityType(), id, createdAt, updatedAt);
    }
}

