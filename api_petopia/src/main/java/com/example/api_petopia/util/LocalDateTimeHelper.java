package com.example.api_petopia.util;

import com.github.javafaker.Faker;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LocalDateTimeHelper {
    public static LocalDateTime generateLocalDate() {
        Faker faker = new Faker();
        long minDay = LocalDate.of(2021, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2022, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate localDate = LocalDate.ofEpochDay(randomDay);
        LocalTime localTime = LocalTime.of(faker.number().numberBetween(1,12), faker.number().numberBetween(1,60));
        return LocalDateTime.of(localDate, localTime);
    }
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
