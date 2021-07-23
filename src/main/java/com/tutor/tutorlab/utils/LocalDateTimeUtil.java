package com.tutor.tutorlab.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    public static String getNowToString(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

}
