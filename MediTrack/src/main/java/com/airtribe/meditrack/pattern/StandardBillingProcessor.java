package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.constants.Constants;

/**
 * Concrete implementation of TemplateBillingProcessor.
 */
public class StandardBillingProcessor extends TemplateBillingProcessor {
    @Override
    protected double calculateAmount(double baseAmount) {
        return baseAmount * (1 + Constants.TAX_RATE);
    }
}

