package com.ibm.sterling.bfg.app.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static final String DEFAULT_DATE_FORMAT = "dd'/'MM'/'yyyy HH':'mm':'ss";

    public static Integer convertTimeToMinutes(String time) {
        LocalTime localTime = LocalTime.parse(time);
        Integer minutes = localTime.get(ChronoField.MINUTE_OF_DAY);
        return minutes;
    }

    public static String convertMinutesToTime(Integer minutes) {
        Duration duration = Duration.ofMinutes(minutes);
        String time = String.format("%02d:%02d", duration.toHours(), duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours()));
        return time;
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
        int hour = Integer.valueOf(array[0]);
        int min = Integer.valueOf(array[1]);
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateOfSchedule = LocalDateTime.now().withHour(hour).withMinute(min).withSecond(0);
        if (dateOfSchedule.isBefore(today)) {
            dateOfSchedule = dateOfSchedule.plusDays(1);
        }
        return dateOfSchedule;
    }

    public static String formatLocalDateTimeToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        String formatDateTime = date.format(formatter);
        return formatDateTime;
    }

    public static LocalDateTime formatStringToLocalDateTime(String date) {
        LocalDateTime formatDateTime = LocalDateTime.parse(date);
        return formatDateTime;
    }
}
