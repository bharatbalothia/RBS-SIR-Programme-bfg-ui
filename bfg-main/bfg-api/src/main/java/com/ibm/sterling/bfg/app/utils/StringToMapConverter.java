package com.ibm.sterling.bfg.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StringToMapConverter implements AttributeConverter<Map<String, List<String>>, String> {

    private static final Logger LOGGER = LogManager.getLogger(StringToMapConverter.class);

    @Override
    public String convertToDatabaseColumn(Map<String, List<String>> attribute) {
        LOGGER.info("Convert map {} to string", attribute);
        return Optional.ofNullable(attribute).map(map -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                new ObjectMapper().writeValue(out, map);
            } catch (IOException e) {
                LOGGER.error("Cannot convert Map to string: {}", e.getMessage());
            }
            return new String(out.toByteArray());
        }).orElse("");
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(String dbData) {
        LOGGER.info("Convert string {} to map", dbData);
        return Optional.ofNullable(dbData).map(data -> {
                    Map<String, List<String>> map = new HashMap<>();
                    try {
                        map = new ObjectMapper().readValue(
                                data, new TypeReference<Map<String, List<String>>>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Cannot convert string to Map: {}", e.getOriginalMessage());
                    }
                    return map;
                }
        ).orElse(null);
    }

}
