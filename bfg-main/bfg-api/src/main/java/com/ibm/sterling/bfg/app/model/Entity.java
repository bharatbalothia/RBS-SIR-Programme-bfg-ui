package com.ibm.sterling.bfg.app.model;

import org.apache.logging.log4j.*;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

@javax.persistence.Entity
//@Embeddable
@Table(name = "SCT_ENTITY")
public class Entity implements Serializable, ByteEntity {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(Entity.class);

    @Id
    @Column(name = "ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_ENTITY_IDSEQ")
    @SequenceGenerator(sequenceName = "SCT_ENTITY_IDSEQ", name = "SCT_ENTITY_IDSEQ", allocationSize = 1)
    private Integer entityId;

    private String entity;
    private String service;

    @Embedded
    private EntityFields entityFields;

    @Transient
    private List schedules = new ArrayList();
    @Transient
    private List deletedSchedules = new ArrayList();

    @Transient
    private Boolean routeInbound = Boolean.TRUE;
    @Transient
    private Boolean routeOutbound = Boolean.TRUE;
    @Transient
    private Boolean inboundDir = Boolean.FALSE;
    @Transient
    private Boolean inboundRoutingRule = Boolean.FALSE;

    @Column(name = "ROUTE_REQUESTORDN")
    private String inboundRequestorDN = "";
    @Column(name = "ROUTE_RESPONDERDN")
    private String inboundResponderDN = "";
    @Column(name = "ROUTE_SERVICE")
    private String inboundService = "";
    @Column(name = "ROUTE_REQUESTTYPE")
    private String inboundType = "";
    @Transient
    private String[] inboundRequestType = new String[0];
    @Column(name = "NONREPUDIATION")
    private Boolean nonRepudiation = Boolean.FALSE;
    @Column(name = "PAUSE_INBOUND")
    private Boolean pauseInbound = Boolean.FALSE;
    @Column(name = "PAUSE_OUTBOUND")
    private Boolean pauseOutbound = Boolean.FALSE;
    @Column(name = "ISDELETED")
    private Boolean deleted = Boolean.FALSE;
    @Transient
    private String changeID = "";
    @Transient
    private String changerComments = "";
    @Transient
    private Boolean irishStep2 = Boolean.FALSE;

    @Column(name = "E2ESIGNING")
    private String e2eSigning;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
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

    public Boolean getRouteInbound() {
        return routeInbound;
    }

    public void setRouteInbound(Boolean routeInbound) {
        this.routeInbound = routeInbound;
    }

    public Boolean getRouteOutbound() {
        return routeOutbound;
    }

    public void setRouteOutbound(Boolean routeOutbound) {
        this.routeOutbound = routeOutbound;
    }

    public Boolean getInboundDir() {
        return inboundDir;
    }

    public void setInboundDir(Boolean inboundDir) {
        this.inboundDir = inboundDir;
    }

    public Boolean getInboundRoutingRule() {
        return inboundRoutingRule;
    }

    public void setInboundRoutingRule(Boolean inboundRoutingRule) {
        this.inboundRoutingRule = inboundRoutingRule;
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

    public String getInboundService() {
        return inboundService;
    }

    public void setInboundService(String inboundService) {
        this.inboundService = inboundService;
    }

    public String getInboundType() {
        return inboundType;
    }

    public void setInboundType(String inboundType) {
        this.inboundType = inboundType;
    }

    public String[] getInboundRequestType() {
        return inboundRequestType;
    }

    public void setInboundRequestType(String[] inboundRequestType) {
        this.inboundRequestType = inboundRequestType;
    }

    public Boolean getNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(Boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public Boolean getPauseInbound() {
        return pauseInbound;
    }

    public void setPauseInbound(Boolean pauseInbound) {
        this.pauseInbound = pauseInbound;
    }

    public Boolean getPauseOutbound() {
        return pauseOutbound;
    }

    public void setPauseOutbound(Boolean pauseOutbound) {
        this.pauseOutbound = pauseOutbound;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getChangeID() {
        return changeID;
    }

    public void setChangeID(String changeID) {
        this.changeID = changeID;
    }

    public String getChangerComments() {
        return changerComments;
    }

    public void setChangerComments(String changerComments) {
        this.changerComments = changerComments;
    }

    public Boolean getIrishStep2() {
        return irishStep2;
    }

    public void setIrishStep2(Boolean irishStep2) {
        this.irishStep2 = irishStep2;
    }

    public String getE2eSigning() {
        return e2eSigning;
    }

    public void setE2eSigning(String e2eSigning) {
        this.e2eSigning = e2eSigning;
    }

    //    public byte[] getObjectBytes() {
//        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oout = new ObjectOutputStream(baos)) {
//            oout.writeObject(this);
//            return baos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entityId=" + entityId +
                ", entity='" + entity + '\'' +
                ", service='" + service + '\'' +
                ", entityFields=" + entityFields +
                ", schedules=" + schedules +
                ", deletedSchedules=" + deletedSchedules +
                ", routeInbound=" + routeInbound +
                ", routeOutbound=" + routeOutbound +
                ", inboundDir=" + inboundDir +
                ", inboundRoutingRule=" + inboundRoutingRule +
                ", inboundRequestorDN='" + inboundRequestorDN + '\'' +
                ", inboundResponderDN='" + inboundResponderDN + '\'' +
                ", inboundService='" + inboundService + '\'' +
                ", inboundType='" + inboundType + '\'' +
                ", inboundRequestType=" + Arrays.toString(inboundRequestType) +
                ", nonRepudiation=" + nonRepudiation +
                ", pauseInbound=" + pauseInbound +
                ", pauseOutbound=" + pauseOutbound +
                ", deleted=" + deleted +
                ", changeID='" + changeID + '\'' +
                ", changerComments='" + changerComments + '\'' +
                ", irishStep2=" + irishStep2 +
                ", e2eSigning='" + e2eSigning + '\'' +
                '}';
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
