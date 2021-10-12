package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringToIntegerConverterTest {
    private StringToIntegerConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StringToIntegerConverter();
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnInteger() {
        assertEquals(1, converter.convertToDatabaseColumn("1"));
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnString() {
        assertEquals("1", converter.convertToEntityAttribute(1));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }
}