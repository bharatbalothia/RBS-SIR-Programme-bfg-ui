package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilTest {

    @Test
    void convertTimeToMinutes() {
        String time = "10:20";
        Integer minutes = TimeUtil.convertTimeToMinutes(time);
        assertEquals(620, minutes);
    }

    @Test
    void convertMinutesToTime() {
        Integer minutes = 620;
        assertEquals("10:20", TimeUtil.convertMinutesToTime(minutes));
    }
}