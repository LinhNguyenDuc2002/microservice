package com.example.productservice.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtil {
    public static Date convertStringToDate(String date) throws DateTimeParseException {
        try {
            Instant instant = Instant.parse(date);
            return Date.from(instant);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Error parsing date string", date, 0);
        }
    }

    public static Date add(Date now, int unit, Integer number) throws Exception {
        if(!Calendar.getAvailableCalendarTypes().contains(unit)) {
            throw new Exception("Invalid unit");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(unit, number);

        return calendar.getTime();
    }
}
