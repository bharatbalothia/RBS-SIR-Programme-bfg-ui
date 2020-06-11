package com.ibm.sterling.bfg.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "SCT_SCHEDULE")
public class Schedule {

    @Id
    @Column(name = "SCHEDULE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_SCHEDULE_IDSEQ")
    @SequenceGenerator(sequenceName = "SCT_SCHEDULE_IDSEQ", name = "SCT_SCHEDULE_IDSEQ", allocationSize = 1)
    private Long scheduleId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID")
    @JsonBackReference
    private Entity entity;
    @Column(name = "ISWINDOW")
    @NotNull(message = "ISWINDOW has to be present")
    private Boolean isWindow = Boolean.TRUE;
    @Column(name = "TIMESTART")
    private Integer timeStart = 0;
    @Column(name = "WINDOWEND")
    private Integer windowEnd;
    @Column(name = "WINDOWINTERVAL")
    private Integer windowInterval;
    @Column(name = "TRANSTHRESHOLD")
    private Integer transThreshold;
    @NotNull(message = "ACTIVE has to be present")
    private Boolean active = Boolean.TRUE;
    @Column(name = "NEXTRUN")
    private Date nextRun;
    @Column(name = "LASTRUN")
    private Date lastRun;
    @Column(name = "FILETYPE")
    private String fileType;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Boolean getWindow() {
        return isWindow;
    }

    public void setWindow(Boolean window) {
        isWindow = window;
    }

    public Integer getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Integer timeStart) {
        this.timeStart = timeStart;
    }

    public Integer getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Integer windowEnd) {
        this.windowEnd = windowEnd;
    }

    public Integer getWindowInterval() {
        return windowInterval;
    }

    public void setWindowInterval(Integer windowInterval) {
        this.windowInterval = windowInterval;
    }

    public Integer getTransThreshold() {
        return transThreshold;
    }

    public void setTransThreshold(Integer transThreshold) {
        this.transThreshold = transThreshold;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getNextRun() {
        return nextRun;
    }

    public void setNextRun(Date nextRun) {
        this.nextRun = nextRun;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", entity=" + entity +
                ", isWindow=" + isWindow +
                ", timeStart=" + timeStart +
                ", windowEnd=" + windowEnd +
                ", windowInterval=" + windowInterval +
                ", transThreshold=" + transThreshold +
                ", active=" + active +
                ", nextRun=" + nextRun +
                ", lastRun=" + lastRun +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
