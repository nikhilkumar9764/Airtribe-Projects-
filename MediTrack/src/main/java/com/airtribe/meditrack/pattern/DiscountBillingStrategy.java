package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.constants.Constants;

/**
 * Discount billing strategy with percentage discount.
 */
public class DiscountBillingStrategy implements BillingStrategy {
    private final double discountPercent;

    public DiscountBillingStrategy(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculateBill(double baseAmount) {
        double discountedAmount = baseAmount * (1 - discountPercent / 100.0);
        return discountedAmount * (1 + Constants.TAX_RATE);
    }
}

