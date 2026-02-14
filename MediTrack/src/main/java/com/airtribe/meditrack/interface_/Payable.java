package com.airtribe.meditrack.interface_;

/**
 * Interface for payable entities.
 * Demonstrates polymorphism through interface implementation.
 */
public interface Payable {
    /**
     * Calculate the payment amount
     */
    double calculatePayment();

    /**
     * Default method to check if payment is due
     */
    default boolean isPaymentDue() {
        return calculatePayment() > 0;
    }
}

