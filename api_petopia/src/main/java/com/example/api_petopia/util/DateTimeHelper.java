package com.example.api_petopia.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DateTimeHelper {

    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static Random randomObj = new Random();

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        return LocalDate.parse(date, formatter).atStartOfDay();
    }

    public static String convertLocalDateTimeToString(LocalDateTime date) {
        return date.format(formatter);
    }

    public static Month getRandomMonth() {
        Month[] months = Month.values();
        return months[randomObj.nextInt(months.length)];
    }
}
