package com.ibm.sterling.bfg.app.utils;

import com.ibm.sterling.bfg.app.model.entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringToScheduleListConverterTest {
    private StringToScheduleListConverter converter;
    private List<Schedule> scheduleList = new ArrayList<>();
    private String schedules;

    @BeforeEach
    void setUp() {
        converter = new StringToScheduleListConverter();
        scheduleList.add(new Schedule());
        schedules = "[{\"scheduleId\":null,\"isWindow\":true,\"timeStart\":\"00:00\",\"windowEnd\":null," +
                "\"windowInterval\":null,\"transThreshold\":null,\"active\":true,\"nextRun\":null,\"lastRun\":null," +
                "\"fileType\":null}]";
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnEmptyString() {
        assertEquals("", converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnConvertedToStringListOfSchedules() {
        assertEquals(schedules, converter.convertToDatabaseColumn(scheduleList));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnListOfSchedules() {
        assertEquals(scheduleList.toString(),
                converter.convertToEntityAttribute(schedules).toString());
    }

    @Test
    void convertToEntityAttribute_ShouldReturnNull() {
        assertEquals(new ArrayList<Schedule>(), converter.convertToEntityAttribute(null));
    }
}