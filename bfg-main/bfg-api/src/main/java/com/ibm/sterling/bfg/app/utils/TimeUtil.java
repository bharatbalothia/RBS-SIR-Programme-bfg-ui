package com.ibm.sterling.bfg.app.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

import static com.ibm.sterling.bfg.app.utils.FieldCheckUtil.checkStringEmptyOrNull;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;

public class TimeUtil {
    public static final String    DEFAULT_DATE_FORMAT = "dd'/'MM'/'yyyy HH':'mm':'ss";
    private static final Logger LOG = LogManager.getLogger(TimeUtil.class);

    public static Integer convertTimeToMinutes(String time) {
        LOG.info("Convert {} to minutes count", time);
        if (time == null) return 0;
        return LocalTime.parse(formatToHHmm(time)).get(MINUTE_OF_DAY);
    }

    private static String formatToHHmm(String time) {
        LOG.info("Convert {} to HH:mm format", time);
        if (time == null) return null;
        String[] array = time.split(":", 2);
        int hour = Integer.parseInt(array[0]);
        int min = Integer.parseInt(array[1]);
        time = String.format("%02d:%02d", hour, min);
        return time;
    }

    public static String convertMinutesToTime(Integer minutes) {
        LOG.info("Convert count {} minutes to HH:mm format of time", minutes);
        Duration duration = Duration.ofMinutes(minutes);
        return String.format("%02d:%02d", duration.toHours(),
                duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours()));
    }

    public static LocalDateTime convertTimeToLocalDateTimeForSchedule(String time) {
        LOG.info("Convert time {} to LocalDateTime", time);
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
        LOG.info("Convert LocalDateTime {} to {} format", date, DEFAULT_DATE_FORMAT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return date.format(formatter);
    }

    public static LocalDateTime formatStringToLocalDateTime(String date) {
        LOG.info("Convert date {} to LocalDateTime", date);
        return LocalDateTime.parse(date);
    }

    public static boolean isStringDateBefore(String from, String to) {
        LOG.info("Check is {} before {}", from, to);
        try {
            if (checkStringEmptyOrNull(from) || checkStringEmptyOrNull(to)) return true;
            LocalDateTime dateFrom = LocalDateTime.parse(from);
            LocalDateTime dateTo = LocalDateTime.parse(to);
            return !dateFrom.isAfter(dateTo);
        } catch (DateTimeParseException exception) {
            return false;
        }
    }
}
