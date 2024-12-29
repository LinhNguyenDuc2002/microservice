package com.example.productservice.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public final class StringUtil {
    public static final String DELIMITER = ",";
    public static String camelCaseToSnakeCase(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String joinDelimiter(List<String> array) {
        return String.join(DELIMITER, array);
    }

    public static List<String> splitDelimiter(String input) {
        if(StringUtils.hasText(input)) {
            String[] parts = input.split(DELIMITER);
            return Arrays.asList(parts);
        }

        return Arrays.asList();
    }
}
