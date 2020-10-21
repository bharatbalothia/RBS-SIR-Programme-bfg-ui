package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Converter
public class StringToListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return Optional.ofNullable(list)
                .map(strList -> String.join("|", strList))
                .orElse("");
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData)
                .map(data -> Arrays.asList(data.split("\\|")))
                .orElse(new ArrayList<>());
    }

}
