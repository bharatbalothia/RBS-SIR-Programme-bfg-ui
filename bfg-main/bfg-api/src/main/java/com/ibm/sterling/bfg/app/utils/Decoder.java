package com.ibm.sterling.bfg.app.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Decoder {

    private static final Logger LOGGER = LogManager.getLogger(Decoder.class);

    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error decoding parameter: {}", e.getMessage());
        }
        return value;
    }

}
