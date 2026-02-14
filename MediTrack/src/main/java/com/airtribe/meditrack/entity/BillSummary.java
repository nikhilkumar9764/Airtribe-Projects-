package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable BillSummary class.
 * Demonstrates immutability: final fields, no setters, thread-safe.
 */
public final class BillSummary {
    private final String billId;
    private final String patientId;
    private final double totalAmount;
    private final LocalDateTime billDate;
    private final String paymentStatus;

    // Static block for initialization
    static {
        System.out.println("BillSummary class loaded - immutable class initialized");
    }

    /**
     * Constructor - all fields must be set at creation time
     */
    public BillSummary(String billId, String patientId, double totalAmount, 
                       LocalDateTime billDate, String paymentStatus) {
        this.billId = billId;
        this.patientId = patientId;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
        this.paymentStatus = paymentStatus;
    }

    // Only getters - no setters (immutability)
    public String getBillId() {
        return billId;
    }

    public String getPatientId() {
        return patientId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BillSummary that = (BillSummary) obj;
        return Double.compare(that.totalAmount, totalAmount) == 0 &&
               Objects.equals(billId, that.billId) &&
               Objects.equals(patientId, that.patientId) &&
               Objects.equals(billDate, that.billDate) &&
               Objects.equals(paymentStatus, that.paymentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, patientId, totalAmount, billDate, paymentStatus);
    }

    @Override
    public String toString() {
        return String.format("BillSummary{billId='%s', patientId='%s', totalAmount=%.2f, billDate=%s, status='%s'}", 
            billId, patientId, totalAmount, billDate, paymentStatus);
    }
}

