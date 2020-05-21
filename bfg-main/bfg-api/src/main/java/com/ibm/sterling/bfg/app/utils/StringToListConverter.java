package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringToListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return (list == null) ? "" : String.join(";", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) dbData = "";
        return new ArrayList<>(Arrays.asList(dbData.split(";")));
    }
}
