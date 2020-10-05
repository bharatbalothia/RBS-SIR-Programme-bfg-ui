package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

public enum RouteFieldName {

    ROUTE_REQUESTORDN("inboundRequestorDN"),
    ROUTE_RESPONDERDN("inboundResponderDN"),
    ROUTE_SERVICE("inboundService"),
    ROUTE_REQUESTTYPE("inboundRequestType");

    RouteFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String fieldName() {
        return fieldName;
    }

}
