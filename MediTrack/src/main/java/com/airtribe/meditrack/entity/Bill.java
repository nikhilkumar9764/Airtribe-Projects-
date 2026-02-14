package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interface_.Payable;
import com.airtribe.meditrack.constants.Constants;
import java.time.LocalDateTime;

/**
 * Bill entity representing a medical bill.
 * Demonstrates polymorphism through Payable interface.
 */
public class Bill implements Payable {
    private String billId;
    private String appointmentId;
    private String patientId;
    private double baseAmount;
    private double taxAmount;
    private double totalAmount;
    private LocalDateTime billDate;
    private String paymentStatus;

    public Bill(String billId, String appointmentId, String patientId, double baseAmount) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.baseAmount = baseAmount;
        this.taxAmount = baseAmount * Constants.TAX_RATE;
        this.totalAmount = baseAmount + this.taxAmount;
        this.billDate = LocalDateTime.now();
        this.paymentStatus = "PENDING";
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
        this.taxAmount = baseAmount * Constants.TAX_RATE;
        this.totalAmount = baseAmount + this.taxAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public double calculatePayment() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return String.format("Bill{id='%s', appointmentId='%s', baseAmount=%.2f, taxAmount=%.2f, totalAmount=%.2f, status='%s'}", 
            billId, appointmentId, baseAmount, taxAmount, totalAmount, paymentStatus);
    }
}

