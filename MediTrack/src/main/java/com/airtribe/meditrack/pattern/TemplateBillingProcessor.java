package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.entity.Bill;

/**
 * Template Method pattern for billing processing.
 * Defines the algorithm structure with customizable steps.
 */
public abstract class TemplateBillingProcessor {
    /**
     * Template method - defines the algorithm structure
     */
    public final Bill processBill(String billId, String appointmentId, String patientId, double baseAmount) {
        // Step 1: Validate
        validateBillData(billId, appointmentId, patientId, baseAmount);
        
        // Step 2: Calculate amount (delegated to subclass)
        double calculatedAmount = calculateAmount(baseAmount);
        
        // Step 3: Create bill
        Bill bill = createBill(billId, appointmentId, patientId, calculatedAmount);
        
        // Step 4: Apply additional processing (hook method)
        applyAdditionalProcessing(bill);
        
        return bill;
    }

    /**
     * Hook method - can be overridden by subclasses
     */
    protected void validateBillData(String billId, String appointmentId, String patientId, double baseAmount) {
        if (billId == null || appointmentId == null || patientId == null) {
            throw new IllegalArgumentException("Bill data cannot be null");
        }
        if (baseAmount < 0) {
            throw new IllegalArgumentException("Base amount cannot be negative");
        }
    }

    /**
     * Abstract method - must be implemented by subclasses
     */
    protected abstract double calculateAmount(double baseAmount);

    /**
     * Hook method - can be overridden by subclasses
     */
    protected Bill createBill(String billId, String appointmentId, String patientId, double amount) {
        return new Bill(billId, appointmentId, patientId, amount);
    }

    /**
     * Hook method - can be overridden by subclasses for additional processing
     */
    protected void applyAdditionalProcessing(Bill bill) {
        // Default: no additional processing
    }
}

