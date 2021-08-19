package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringToMapConverterTest {
    private String field;
    private StringToMapConverter converter = new StringToMapConverter();
    private Map<String, List<String>> attribute = new HashMap<>();

    @BeforeEach
    void setUp() {
        field = "{\"C\":[\"IE\"],\"O\":[\"Baltimore\"]}";
        attribute.put("C", Arrays.asList("IE"));
        attribute.put("O", Arrays.asList("Baltimore"));
    }

    @Test
    void convertToDatabaseColumn() {
        assertEquals(field, converter.convertToDatabaseColumn(attribute));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(attribute, converter.convertToEntityAttribute(field));
    }
}