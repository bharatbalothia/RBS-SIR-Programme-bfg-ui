package com.ibm.sterling.bfg.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StringToMapConverter implements AttributeConverter<Map<String, List<String>>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, List<String>> attribute) {
        return Optional.ofNullable(attribute).map(map -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                new ObjectMapper().writeValue(out, map);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(out.toByteArray());
        }).orElse("");
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData).map(data -> {
            Map<String, List<String>> map = new HashMap<>();
                    try {
                        map = new ObjectMapper().readValue(
                                data, new TypeReference<Map<String, List<String>>>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return map;
                }
        ).orElse(null);
    }
}
