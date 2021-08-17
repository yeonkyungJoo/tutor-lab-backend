package com.tutor.tutorlab.utils;

import org.apache.commons.lang3.StringUtils;
import jdk.internal.joptsimple.internal.Strings;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    public static String getNowToString(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getDateToString(LocalDate date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    // parse
    public static LocalDate getStringToDate(String localDate) {
        if (StringUtils.isBlank(localDate)) {
            return null;
        }
        return LocalDate.parse(localDate);
    }

}
