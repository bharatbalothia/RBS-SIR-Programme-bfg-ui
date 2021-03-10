package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {
    INBOUND("inbound"),
    OUTBOUND("outbound"),
    UNKNOWN("unknown");

    private String directionName;

    Direction(String directionName) {
        this.directionName = directionName;
    }

    public final String direction() {
        return directionName;
    }

    private static Map<String, Direction> FORMAT_MAP = Stream
            .of(Direction.values())
            .collect(Collectors.toMap(dir -> dir.directionName, Function.identity()));


    @JsonCreator
    public static Direction convertStringToDirection(String direction) {
        return Optional
                .ofNullable(FORMAT_MAP.get(direction))
                .orElseThrow(() -> new IllegalArgumentException(direction));
    }
}
