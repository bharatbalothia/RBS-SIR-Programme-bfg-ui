package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter
public class StringTimeToIntegerMinuteConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        return Optional.ofNullable(attribute)
                .map(TimeUtil::convertTimeToMinutes)
                .orElse(null);
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        return Optional.ofNullable(dbData)
                .map(TimeUtil::convertMinutesToTime)
                .orElse(null);
    }
}
