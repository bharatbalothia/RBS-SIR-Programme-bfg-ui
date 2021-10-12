package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebugStringToIntegerConverterTest {
    private DebugStringToIntegerConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DebugStringToIntegerConverter();
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnOne() {
        assertEquals(1, converter.convertToDatabaseColumn("YES"));
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnZero() {
        assertEquals(0, converter.convertToDatabaseColumn("No"));
    }

    @Test
    void convertToDatabaseColumn_ShouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnYes() {
        assertEquals("YES", converter.convertToEntityAttribute(1));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnNo() {
        assertEquals("NO", converter.convertToEntityAttribute(0));
    }

    @Test
    void convertToEntityAttribute_ShouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }
}