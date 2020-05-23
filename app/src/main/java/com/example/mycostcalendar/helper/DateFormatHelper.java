package com.example.mycostcalendar.helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatHelper {

    private static final String DATE_FORMAT_1 = "dd/MM/yyyy";
    private static final String DATE_FORMAT_2 = "MMMM yyyy";
    private static final String DATE_FORMAT_3 = "MMMM dd yyyy HH:mm:ss";

    public static String formatDate(int day, int month, int year) {
        String dateString = String.format("%d/%d/%d", day, month, year);
        Date date = formatStringToDate(dateString);
        return formatDate(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        return dateFormat.format(date);
    }

    public static String formatDate2(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        return dateFormat.format(date);
    }

    public static String formatDate3(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_3);
        return dateFormat.format(date);
    }

    public static Date formatStringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_1);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Calendar calendarFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        } else {
            calendar.setTime(new Date());
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

        return calendar;
    }

    public static Calendar calendarLastDayOfMonth(Date date) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        } else {
            calendar.setTime(new Date());
        }
        calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));

        return calendar;
    }

    public static long getFirstDayOfMonthTmp(Date date) {
        return calendarFirstDayOfMonth(date).getTime().getTime();
    }

    public static long getLastDayOfMonthTmp(Date date) {
        return calendarLastDayOfMonth(date).getTime().getTime();
    }

    public static String getYearFromDate(Date date) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(java.util.Calendar.YEAR));
    }

    public static int getMonthFromDate(Date date) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
}
