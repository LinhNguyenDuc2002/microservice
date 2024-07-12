package com.example.orderservice.util;

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateUtilTest {
    @Test
    void test_convertStringToDate() {
        String input = "2024-10-12T00:00:00Z";

        ZonedDateTime expectedDateTime = ZonedDateTime.of(2024, 10, 12, 0, 0, 0, 0, ZoneOffset.UTC);
        Date expectedDate = Date.from(expectedDateTime.toInstant());

        assertEquals(expectedDate, DateUtil.convertStringToDate(input));
    }

    @Test
    void test_convertStringToDate_throwException() {
        String input = "2024-10-12";

        assertThrows(DateTimeParseException.class, () -> DateUtil.convertStringToDate(input));
    }
}
