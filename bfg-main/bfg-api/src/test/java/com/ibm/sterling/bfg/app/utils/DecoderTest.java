package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoderTest {

    @Test
    void decodeValue_ShouldDecodeURLEncodedValue() {
        assertEquals("NatWest Group", Decoder.decodeValue("NatWest%20Group"));
    }
}