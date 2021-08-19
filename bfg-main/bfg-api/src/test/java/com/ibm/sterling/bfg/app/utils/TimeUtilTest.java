package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {
    private LocalDateTime localDateTime;
    private String date;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.of(2021, 3, 23, 15, 1, 20);
        date = "2021-03-23T15:01:20";
    }

    @Test
    void convertTimeToMinutes_ShouldReturnCountOfMinutes() {
        String time = "10:20";
        Integer minutes = TimeUtil.convertTimeToMinutes(time);
        assertEquals(620, minutes);
    }

    @Test
    void convertMinutesToTime_ShouldReturnTimeHHmm() {
        Integer minutes = 620;
        assertEquals("10:20", TimeUtil.convertMinutesToTime(minutes));
    }

    @Test
    void convertTimeToLocalDateTimeForSchedule_ShouldReturnLocalDateTime() {
        assertNotNull(TimeUtil.convertTimeToLocalDateTimeForSchedule("00:00"));
        }

    @Test
    void formatLocalDateTimeToString_ShouldReturnFormattedString() {
        assertEquals("23/03/2021 15:01:20", TimeUtil.formatLocalDateTimeToString(localDateTime));
    }

    @Test
    void formatStringToLocalDateTime_ShouldReturnLocalDateTime() {
        assertEquals(localDateTime, TimeUtil.formatStringToLocalDateTime(date));
    }

    @Test
    void isStringDateBeforeShouldReturnTrue_ShouldReturnTrue() {
        String to = "2021-03-23T15:01:21";
        assertTrue(TimeUtil.isStringDateBefore(date, to));
    }

    @Test
    void isStringDateBefore_ShouldReturnFalse() {
        String to = "2021-02-23T15:01:20";
        assertFalse(TimeUtil.isStringDateBefore(date, to));
    }
}