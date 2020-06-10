package com.ibm.sterling.bfg.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.Schedule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Converter
public class StringToScheduleListConverter implements AttributeConverter<List<Schedule>, String> {

    @Override
    public String convertToDatabaseColumn(List<Schedule> list) {
        return Optional.ofNullable(list).map(schedules -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                new ObjectMapper().writeValue(out, schedules);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(out.toByteArray());
        }).orElse(null);
    }

    @Override
    public List<Schedule> convertToEntityAttribute(String schedules) {
        return Optional.ofNullable(schedules).map(schedulesStr -> {
                    List<Schedule> schedulesList = new ArrayList<>();
                    try {
                        schedulesList = new ObjectMapper().readValue(
                                schedulesStr, new TypeReference<List<Schedule>>() {
                                }
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return schedulesList;
                }
        ).orElse(null);
    }

}
