package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringToListConverterTest {
    private StringToListConverter converter;
    private String dbColumn = "PROPT.PM|MSS01.PM";
    private List<String> entityAttributes = new ArrayList();

    @BeforeEach
    void setUp() {
        converter = new StringToListConverter();
        entityAttributes.add("PROPT.PM");
        entityAttributes.add("MSS01.PM");
    }

    @Test
    void convertToDatabaseColumn() {
        assertEquals(dbColumn, converter.convertToDatabaseColumn(entityAttributes));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(entityAttributes, converter.convertToEntityAttribute(dbColumn));
    }
}