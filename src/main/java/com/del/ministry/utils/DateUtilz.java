package com.del.ministry.utils;

import org.apache.commons.lang.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

final public class DateUtilz {

    private DateUtilz() {
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static boolean between(Date tested, Date from, Date to) {
        return from.compareTo(tested) * tested.compareTo(to) >= 0;
    }

    public static Date today() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static Date fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return java.sql.Date.valueOf(localDate);
    }

    public static String formatTime(Date date) {
        return format(date, TIME_FORMAT);
    }

    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    public static String formatDateTime(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    private static String format(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }


}
