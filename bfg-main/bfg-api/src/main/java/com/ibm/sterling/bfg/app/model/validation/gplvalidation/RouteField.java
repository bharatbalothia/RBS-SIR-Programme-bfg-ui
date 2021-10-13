package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

import com.ibm.sterling.bfg.app.model.validation.Field;

public enum RouteField implements Field {
    ROUTE_INBOUND("routeInbound"),
    ROUTE_REQUESTORDN("inboundRequestorDN"),
    ROUTE_RESPONDERDN("inboundResponderDN"),
    ROUTE_SERVICE("inboundService"),
    ROUTE_REQUESTTYPE("inboundRequestType");

    RouteField(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String fieldName() {
        return fieldName;
    }

}
