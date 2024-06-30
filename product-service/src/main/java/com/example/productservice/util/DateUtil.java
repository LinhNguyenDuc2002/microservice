package com.example.productservice.util;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {
    public static Date convertStringToDate(String date) {
        Instant instant = Instant.parse(date);
        return Date.from(instant);
    }

    public static Date add(Date now, int unit, Integer number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(unit, number);

        return calendar.getTime();
    }
}
