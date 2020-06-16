package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter
public class StringToIntegerConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        return Optional.ofNullable(attribute)
                .map(Integer::valueOf)
                .orElse(null);
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        return Optional.ofNullable(dbData)
                .map(String::valueOf)
                .orElse("");
    }
}
