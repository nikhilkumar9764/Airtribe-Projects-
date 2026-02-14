package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;

/**
 * Factory pattern for creating different types of bills.
 * Demonstrates Factory design pattern.
 */
public class BillFactory {
    // Private constructor to prevent instantiation
    private BillFactory() {
        throw new AssertionError("Cannot instantiate BillFactory class");
    }

    /**
     * Factory method to create a standard bill
     */
    public static Bill createStandardBill(String billId, String appointmentId, String patientId, double baseAmount) {
        return new Bill(billId, appointmentId, patientId, baseAmount);
    }

    /**
     * Factory method to create a bill from doctor consultation fee
     */
    public static Bill createBillFromDoctor(String billId, String appointmentId, String patientId, Doctor doctor) {
        return new Bill(billId, appointmentId, patientId, doctor.getConsultationFee());
    }

    /**
     * Factory method to create a bill with discount
     */
    public static Bill createBillWithDiscount(String billId, String appointmentId, String patientId, 
                                              double baseAmount, double discountPercent) {
        double discountedAmount = baseAmount * (1 - discountPercent / 100.0);
        return new Bill(billId, appointmentId, patientId, discountedAmount);
    }
}

