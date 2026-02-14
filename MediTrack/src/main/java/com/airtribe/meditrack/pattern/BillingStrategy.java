package com.airtribe.meditrack.pattern;

/**
 * Strategy interface for billing strategies.
 * Demonstrates Strategy design pattern.
 */
public interface BillingStrategy {
    double calculateBill(double baseAmount);
}

