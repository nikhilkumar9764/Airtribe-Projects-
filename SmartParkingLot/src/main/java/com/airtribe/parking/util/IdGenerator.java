package com.airtribe.parking.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating unique IDs for various entities.
 */
public class IdGenerator {
    private static final AtomicInteger spotCounter = new AtomicInteger(1);
    private static final AtomicInteger transactionCounter = new AtomicInteger(1);

    /**
     * Generates a unique spot ID.
     */
    public static String generateSpotId(int floor, int spotNumber) {
        return String.format("F%d-S%03d", floor, spotNumber);
    }

    /**
     * Generates a unique transaction ID.
     */
    public static String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + transactionCounter.getAndIncrement();
    }
}

