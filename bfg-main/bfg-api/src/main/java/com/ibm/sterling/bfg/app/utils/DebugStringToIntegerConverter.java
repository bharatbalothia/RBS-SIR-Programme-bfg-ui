package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter
public class DebugStringToIntegerConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String debugString) {
        return Optional.ofNullable(debugString)
                .map(attribute -> attribute.equals("YES") ? 1 : 0)
                .orElse(null);
    }

    @Override
    public String convertToEntityAttribute(Integer debugInt) {
        return Optional.ofNullable(debugInt)
                .map(attribute -> debugInt == 1 ? "YES" : "NO")
                .orElse(null);
    }

}
