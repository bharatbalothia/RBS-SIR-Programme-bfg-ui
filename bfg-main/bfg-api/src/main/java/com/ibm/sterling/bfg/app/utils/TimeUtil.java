package com.ibm.sterling.bfg.app.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoField.MINUTE_OF_DAY;

public class TimeUtil {
    public static final String DEFAULT_DATE_FORMAT = "dd'/'MM'/'yyyy HH':'mm':'ss";

    public static Integer convertTimeToMinutes(String time) {
        if (time == null) return 0;
        return LocalTime.parse(formatToHHmm(time)).get(MINUTE_OF_DAY);
    }

    private static String formatToHHmm(String time) {
        if (time == null) return null;
        String[] array = time.split(":", 2);
        int hour = Integer.parseInt(array[0]);
        int min = Integer.parseInt(array[1]);
        time = String.format("%02d:%02d", hour, min);
        return time;
    }

    public static String convertMinutesToTime(Integer minutes) {
        Duration duration = Duration.ofMinutes(minutes);
        return String.format("%02d:%02d", duration.toHours(),
                duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours()));
    }

    public static Date convertTimeToDate(String time) {
        if (time == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(time, ":");
        int hrs = Integer.parseInt(tokenizer.nextToken());
        int mins = Integer.parseInt(tokenizer.nextToken());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hrs);
        cal.set(Calendar.MINUTE, mins);

        Date scheduleDate = cal.getTime();
        Date nowDate = new Date();

        if (nowDate.compareTo(scheduleDate)>=0){
            cal.roll(Calendar.DAY_OF_MONTH, true);
            return cal.getTime();
        }
        else {
            return scheduleDate;
        }
    }

    public static LocalDateTime convertTimeToLocalDateTime(String time) {
        if (time == null) return null;
        String[] array = time.split(":", 2);
        int hour = Integer.parseInt(array[0]);
        int min = Integer.parseInt(array[1]);
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateOfSchedule = LocalDateTime.now().withHour(hour).withMinute(min).withSecond(0);
        if (dateOfSchedule.isBefore(today)) {
            dateOfSchedule = dateOfSchedule.plusDays(1);
        }
        return dateOfSchedule;
    }

    public static String formatLocalDateTimeToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return date.format(formatter);
    }

    public static LocalDateTime formatStringToLocalDateTime(String date) {
        return LocalDateTime.parse(date);
    }

    public static boolean isStringDateBefore(String from, String to) {
        try {
            LocalDateTime dateFrom = LocalDateTime.parse(from);
            LocalDateTime dateTo = LocalDateTime.parse(to);
            return !dateFrom.isAfter(dateTo);
        } catch (DateTimeParseException exception) {
            return false;
        }
    }
}
