package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringTimeToIntegerMinuteConverterTest {
    private StringTimeToIntegerMinuteConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StringTimeToIntegerMinuteConverter();
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnCountOfMinutes() {
        assertEquals(620, converter.convertToDatabaseColumn("10:20"));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnTimeHHmm() {
        assertEquals("10:20", converter.convertToEntityAttribute(620));
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }
}