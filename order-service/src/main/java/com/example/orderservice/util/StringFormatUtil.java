package com.example.orderservice.util;

public final class StringFormatUtil {
    public static String formatBillId(String prefix, Long billId) {
        return prefix + String.format("%05d", billId);
    }
}
