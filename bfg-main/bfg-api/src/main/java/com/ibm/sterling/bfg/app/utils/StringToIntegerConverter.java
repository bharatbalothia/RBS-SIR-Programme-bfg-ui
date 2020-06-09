package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class StringToIntegerConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        return TimeUtil.convertTimeToMinutes(attribute);
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        return TimeUtil.convertMinutesToTime(dbData);
    }
}
