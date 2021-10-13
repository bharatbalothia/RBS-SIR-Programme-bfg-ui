package com.ibm.sterling.bfg.app.model.event;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EventTypePermission {
    ENTITY_APPROVE("ENTITY", "SFG_UI_SCT_APPROVE_ENTITY"),
    TRUSTED_CERT_APPROVE("TRUSTED_CERTIFICATE", "FB_UI_TRUSTED_CERTS_APPROVE"),
    ENTITY_TRANSMIT("TRANSMIT", "SFG_UI_SCT_ENTITY_TRANSMIT");

    private String eventType;
    private String neededPermission;

    EventTypePermission(String eventType, String neededPermission) {
        this.eventType = eventType;
        this.neededPermission = neededPermission;
    }

    public String getEventType() {
        return eventType;
    }

    public String getNeededPermission() {
        return neededPermission;
    }

    private static Map<String, String> EVENT_TYPE_MAP = Stream
            .of(EventTypePermission.values())
            .collect(Collectors.toMap(
                    EventTypePermission::getNeededPermission, EventTypePermission::getEventType));

    public static EventType convertNeededPermissionToEventType(String permission) {
        return Optional
                .ofNullable(EVENT_TYPE_MAP.get(permission))
                .map(EventType::valueOf)
                .orElseThrow(() -> new IllegalArgumentException(permission));
    }
}
