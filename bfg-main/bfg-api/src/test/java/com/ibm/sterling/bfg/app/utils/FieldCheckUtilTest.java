package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldCheckUtilTest {

    @Test
    void checkStringEmptyOrNull_ShouldReturnTrueWhenFieldIsNull() {
        assertTrue(FieldCheckUtil.checkStringEmptyOrNull(null));
    }

    @Test
    void checkStringEmptyOrNull_ShouldReturnTrueWhenFieldIsEmpty() {
        assertTrue(FieldCheckUtil.checkStringEmptyOrNull(""));
    }

    @Test
    void checkStringEmptyOrNull_ShouldReturnFalse() {
        assertFalse(FieldCheckUtil.checkStringEmptyOrNull("field"));
    }
}