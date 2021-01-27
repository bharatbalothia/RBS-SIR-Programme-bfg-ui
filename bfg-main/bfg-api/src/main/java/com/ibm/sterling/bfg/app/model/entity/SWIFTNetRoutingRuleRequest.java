package com.ibm.sterling.bfg.app.model.entity;

import java.util.List;

public class SWIFTNetRoutingRuleRequest {
    private String entityName;
    private String entityType;
    private String requestorDN;
    private String responderDN;
    private String inboundRequestorDN;
    private String inboundResponderDN;
    private List<String> requestType;
    private String service;
    private String username;

    public SWIFTNetRoutingRuleRequest(ChangeControl changeControl) {
        EntityLog entityLog = changeControl.getEntityLog();
        entityName = entityLog.getEntity();
        entityType = entityLog.getService();
        requestorDN = entityLog.getRequestorDN();
        responderDN = entityLog.getResponderDN();
        inboundRequestorDN = entityLog.getInboundRequestorDN();
        inboundResponderDN = entityLog.getInboundResponderDN();
        requestType = entityLog.getInboundRequestType();
        service = entityLog.getInboundService();
        username = changeControl.getChanger();
    }

    public SWIFTNetRoutingRuleRequest(Entity entity, String changer) {
        entityName = entity.getEntity();
        entityType = entity.getService();
        requestorDN = entity.getRequestorDN();
        responderDN = entity.getResponderDN();
        inboundRequestorDN = entity.getInboundRequestorDN();
        inboundResponderDN = entity.getInboundResponderDN();
        requestType = entity.getInboundRequestType();
        service = entity.getInboundService();
        username = changer;
    }

    public SWIFTNetRoutingRuleRequest() {
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getRequestorDN() {
        return requestorDN;
    }

    public void setRequestorDN(String requestorDN) {
        this.requestorDN = requestorDN;
    }

    public String getResponderDN() {
        return responderDN;
    }

    public void setResponderDN(String responderDN) {
        this.responderDN = responderDN;
    }

    public String getInboundRequestorDN() {
        return inboundRequestorDN;
    }

    public void setInboundRequestorDN(String inboundRequestorDN) {
        this.inboundRequestorDN = inboundRequestorDN;
    }

    public String getInboundResponderDN() {
        return inboundResponderDN;
    }

    public void setInboundResponderDN(String inboundResponderDN) {
        this.inboundResponderDN = inboundResponderDN;
    }

    public List<String> getRequestType() {
        return requestType;
    }

    public void setRequestType(List<String> requestType) {
        this.requestType = requestType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
