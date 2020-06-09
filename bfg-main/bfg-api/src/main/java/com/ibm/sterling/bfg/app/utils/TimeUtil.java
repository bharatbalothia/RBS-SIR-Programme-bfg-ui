package com.ibm.sterling.bfg.app.utils;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
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
}
