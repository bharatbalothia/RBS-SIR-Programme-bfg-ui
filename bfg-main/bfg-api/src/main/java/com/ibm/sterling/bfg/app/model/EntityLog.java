package com.ibm.sterling.bfg.app.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SCT_ENTITY_LOG")
public class EntityLog {
    @Id
    @Column(name = "ENTITY_LOG_ID")

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_ENTITY_LOG_IDSEQ")
    @SequenceGenerator(sequenceName = "SCT_ENTITY_LOG_IDSEQ", name = "SCT_ENTITY_LOG_IDSEQ", allocationSize = 1)
    private Integer entityLogId;

    private Integer entityId;

    private String entity;
    private String service;

    @Embedded
    private EntityFields entityFields;

    @Transient
    private List schedules = new ArrayList();
    @Transient
    private List deletedSchedules = new ArrayList();

//    @Column
//    private Boolean routeInbound = Boolean.TRUE;
//    @Column
//    private Boolean routeOutbound = Boolean.TRUE;
//    @Column
//    private Boolean inboundDir = Boolean.FALSE;
//    @Column
//    private Boolean inboundRoutingRule = Boolean.FALSE;
//
//    @Column(name = "ROUTE_REQUESTORDN")
//    private String inboundRequestorDN = "";
//    @Column(name = "ROUTE_RESPONDERDN")
//    private String inboundResponderDN = "";
//    @Column(name = "ROUTE_SERVICE")
//    private String inboundService = "";
//    @Column(name = "ROUTE_REQUESTTYPE")
//    private String inboundType = "";
//    @Transient
//    private String[] inboundRequestType = new String[0];
//    @Column(name = "NONREPUDIATION")
//    private Boolean nonRepudiation = Boolean.FALSE;
//    @Column(name = "PAUSE_INBOUND")
//    private Boolean pauseInbound = Boolean.FALSE;
//    @Column(name = "PAUSE_OUTBOUND")
//    private Boolean pauseOutbound = Boolean.FALSE;
//    @Column(name = "ISDELETED")
//    private Boolean deleted = Boolean.FALSE;
//    @Column
//    private String changeID = "";
//    @Column
//    private String changerComments = "";
//    @Column
//    private Boolean irishStep2 = Boolean.FALSE;

    @Column(name = "E2ESIGNING")
    private String e2eSigning;

    public EntityLog() {

    }
    public EntityLog(com.ibm.sterling.bfg.app.model.Entity entity) {
        this.entity = entity.getEntity();
        this.entityId = entity.getEntityId();
        this.entityFields = entity.getEntityFields();
    }

    public Integer getEntityLogId() {
        return entityLogId;
    }

    public void setEntityLogId(Integer entityLogId) {
        this.entityLogId = entityLogId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public EntityFields getEntityFields() {
        return entityFields;
    }

    public void setEntityFields(EntityFields entityFields) {
        this.entityFields = entityFields;
    }

    public List getSchedules() {
        return schedules;
    }

    public void setSchedules(List schedules) {
        this.schedules = schedules;
    }

    public List getDeletedSchedules() {
        return deletedSchedules;
    }

    public void setDeletedSchedules(List deletedSchedules) {
        this.deletedSchedules = deletedSchedules;
    }

//    public Boolean getRouteInbound() {
//        return routeInbound;
//    }
//
//    public void setRouteInbound(Boolean routeInbound) {
//        this.routeInbound = routeInbound;
//    }
//
//    public Boolean getRouteOutbound() {
//        return routeOutbound;
//    }
//
//    public void setRouteOutbound(Boolean routeOutbound) {
//        this.routeOutbound = routeOutbound;
//    }
//
//    public Boolean getInboundDir() {
//        return inboundDir;
//    }
//
//    public void setInboundDir(Boolean inboundDir) {
//        this.inboundDir = inboundDir;
//    }
//
//    public Boolean getInboundRoutingRule() {
//        return inboundRoutingRule;
//    }
//
//    public void setInboundRoutingRule(Boolean inboundRoutingRule) {
//        this.inboundRoutingRule = inboundRoutingRule;
//    }
//
//    public String getInboundRequestorDN() {
//        return inboundRequestorDN;
//    }
//
//    public void setInboundRequestorDN(String inboundRequestorDN) {
//        this.inboundRequestorDN = inboundRequestorDN;
//    }
//
//    public String getInboundResponderDN() {
//        return inboundResponderDN;
//    }
//
//    public void setInboundResponderDN(String inboundResponderDN) {
//        this.inboundResponderDN = inboundResponderDN;
//    }
//
//    public String getInboundService() {
//        return inboundService;
//    }
//
//    public void setInboundService(String inboundService) {
//        this.inboundService = inboundService;
//    }
//
//    public String getInboundType() {
//        return inboundType;
//    }
//
//    public void setInboundType(String inboundType) {
//        this.inboundType = inboundType;
//    }
//
//    public String[] getInboundRequestType() {
//        return inboundRequestType;
//    }
//
//    public void setInboundRequestType(String[] inboundRequestType) {
//        this.inboundRequestType = inboundRequestType;
//    }
//
//    public Boolean getNonRepudiation() {
//        return nonRepudiation;
//    }
//
//    public void setNonRepudiation(Boolean nonRepudiation) {
//        this.nonRepudiation = nonRepudiation;
//    }
//
//    public Boolean getPauseInbound() {
//        return pauseInbound;
//    }
//
//    public void setPauseInbound(Boolean pauseInbound) {
//        this.pauseInbound = pauseInbound;
//    }
//
//    public Boolean getPauseOutbound() {
//        return pauseOutbound;
//    }
//
//    public void setPauseOutbound(Boolean pauseOutbound) {
//        this.pauseOutbound = pauseOutbound;
//    }
//
//    public Boolean getDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(Boolean deleted) {
//        this.deleted = deleted;
//    }
//
//    public String getChangeID() {
//        return changeID;
//    }
//
//    public void setChangeID(String changeID) {
//        this.changeID = changeID;
//    }
//
//    public String getChangerComments() {
//        return changerComments;
//    }
//
//    public void setChangerComments(String changerComments) {
//        this.changerComments = changerComments;
//    }
//
//    public Boolean getIrishStep2() {
//        return irishStep2;
//    }
//
//    public void setIrishStep2(Boolean irishStep2) {
//        this.irishStep2 = irishStep2;
//    }

    public String getE2eSigning() {
        return e2eSigning;
    }

    public void setE2eSigning(String e2eSigning) {
        this.e2eSigning = e2eSigning;
    }
}
