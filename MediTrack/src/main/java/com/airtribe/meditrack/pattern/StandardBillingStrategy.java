package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.constants.Constants;

/**
 * Standard billing strategy with tax.
 */
public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public double calculateBill(double baseAmount) {
        return baseAmount * (1 + Constants.TAX_RATE);
    }
}

