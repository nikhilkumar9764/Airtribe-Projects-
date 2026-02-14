package com.airtribe.meditrack.exception;

/**
 * Custom exception for invalid data scenarios.
 * Demonstrates exception chaining.
 */
public class InvalidDataException extends Exception {
    private String fieldName;
    private Object invalidValue;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, String fieldName, Object invalidValue) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause); // Exception chaining
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }
}

