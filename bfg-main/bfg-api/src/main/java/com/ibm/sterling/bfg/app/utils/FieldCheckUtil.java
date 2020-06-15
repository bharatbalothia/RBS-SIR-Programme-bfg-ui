package com.ibm.sterling.bfg.app.utils;

import org.springframework.util.StringUtils;

import java.util.Optional;

public class FieldCheckUtil {
    public static boolean checkStringEmptyOrNull(String field) {
        return Optional.ofNullable(field).map(StringUtils::isEmpty).orElse(true);
    }
}
