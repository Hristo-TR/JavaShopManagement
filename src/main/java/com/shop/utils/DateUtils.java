package com.shop.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public static boolean isExpired(LocalDate expirationDate) {
        return expirationDate.isBefore(LocalDate.now());
    }

    public static boolean isExpiringSoon(LocalDate expirationDate, int daysThreshold) {
        return !isExpired(expirationDate) && isWithinDays(expirationDate, daysThreshold);
    }

    private static boolean isWithinDays(LocalDate date, int days) {
        LocalDate now = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(now, date);
        return daysBetween >= 0 && daysBetween <= days;
    }
} 