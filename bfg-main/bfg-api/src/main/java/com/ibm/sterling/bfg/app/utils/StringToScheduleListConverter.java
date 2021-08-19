package com.ibm.sterling.bfg.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.entity.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Converter
public class StringToScheduleListConverter implements AttributeConverter<List<Schedule>, String> {

    private static final Logger LOGGER = LogManager.getLogger(StringToScheduleListConverter.class);

    @Override
    public String convertToDatabaseColumn(List<Schedule> list) {
        LOGGER.info("Convert list of schedules {} to string", list);
        return Optional.ofNullable(list).map(schedules -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                new ObjectMapper().writeValue(out, schedules);
            } catch (IOException e) {
                LOGGER.error("Cannot convert schedule list to string: {}", e.getMessage());
            }
            return new String(out.toByteArray());
        }).orElse("");
    }

    @Override
    public List<Schedule> convertToEntityAttribute(String schedules) {
        LOGGER.info("Convert string of schedules {} to list of schedules", schedules);
        return Optional.ofNullable(schedules).map(schedulesStr -> {
                    List<Schedule> schedulesList = new ArrayList<>();
                    try {
                        schedulesList = new ObjectMapper().readValue(
                                schedulesStr, new TypeReference<List<Schedule>>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Cannot convert string to schedule list: {}", e.getOriginalMessage());
                    }
                    return schedulesList;
                }
        ).orElse(new ArrayList<>());
    }

}
