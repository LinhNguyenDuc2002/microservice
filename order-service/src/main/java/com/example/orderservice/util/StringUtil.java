package com.example.orderservice.util;

public final class StringUtil {
    public static String formatBillId(String prefix, Long billId) {
        return prefix + String.format("%05d", billId);
    }
}
