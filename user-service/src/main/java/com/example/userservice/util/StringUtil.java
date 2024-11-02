package com.example.userservice.util;

public final class StringUtil {
    public static String camelCaseToSnakeCase(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
