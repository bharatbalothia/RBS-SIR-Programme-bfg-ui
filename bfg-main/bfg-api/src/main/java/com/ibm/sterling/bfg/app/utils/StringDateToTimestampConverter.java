package com.ibm.sterling.bfg.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Converter
public class StringDateToTimestampConverter implements AttributeConverter<String, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(String attribute) {
        LocalDateTime time = Optional.ofNullable(attribute)
                .map(TimeUtil::formatStringToLocalDateTime)
                .orElse(null);
        return Optional.ofNullable(time).map(Timestamp::valueOf).orElse(null);
    }

    @Override
    public String convertToEntityAttribute(Timestamp dbData) {
        if (dbData == null) return null;
        return Optional.of(dbData.toLocalDateTime())
                .map(TimeUtil::formatLocalDateTimeToString)
                .orElse("");
    }

}
