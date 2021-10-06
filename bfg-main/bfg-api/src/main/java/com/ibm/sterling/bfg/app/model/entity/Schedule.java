package com.ibm.sterling.bfg.app.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.ScheduleValid;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.SctValidation;
import com.ibm.sterling.bfg.app.utils.StringTimeToIntegerMinuteConverter;
import com.ibm.sterling.bfg.app.utils.StringToIntegerConverter;
import com.ibm.sterling.bfg.app.utils.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.ibm.sterling.bfg.app.utils.TimeUtil.DEFAULT_DATE_FORMAT;

@javax.persistence.Entity
@Table(name = "SCT_SCHEDULE")
@ScheduleValid(groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})
public class Schedule implements Serializable {
    private static final Logger LOG = LogManager.getLogger(Schedule.class);

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
    private Boolean isWindow = Boolean.TRUE;

    @Column(name = "TIMESTART")
    @Pattern(
            regexp = "^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])",
            message = "Time Start should be in HH:mm format (24HR)",
            groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Convert(converter = StringTimeToIntegerMinuteConverter.class)
    private String timeStart = "00:00";

    @Column(name = "WINDOWEND")
    @Pattern(
            regexp = "^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])",
            message = "Time End should be in HH:mm format (24HR)",
            groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Convert(converter = StringTimeToIntegerMinuteConverter.class)
    private String windowEnd;

    @Column(name = "WINDOWINTERVAL")
    @Pattern(regexp = "^\\d+$",
            message = "WINDOWINTERVAL should be a positive number or 0",
            groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Convert(converter = StringToIntegerConverter.class)
    private String windowInterval;

    @Column(name = "TRANSTHRESHOLD")
    private Integer transThreshold;

    @NotNull(message = "ACTIVE has to be present",
            groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    private Boolean active = Boolean.TRUE;

    @Column(name = "NEXTRUN")
    @JsonFormat(pattern = DEFAULT_DATE_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime nextRun;

    @Column(name = "LASTRUN")
    @JsonFormat(pattern = DEFAULT_DATE_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastRun;

    @Column(name = "FILETYPE")
    @NotNull
    private String fileType;

    @PrePersist
    @PreUpdate
    public void init() {
        nextRun = TimeUtil.convertTimeToLocalDateTime(timeStart);
        LOG.debug("Setting transaction next runtime to : {}", nextRun);
    }

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

    public Boolean getIsWindow() {
        return isWindow;
    }

    public void setIsWindow(Boolean isWindow) {
        this.isWindow = isWindow;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(String windowEnd) {
        this.windowEnd = windowEnd;
    }

    public String getWindowInterval() {
        return windowInterval;
    }

    public void setWindowInterval(String windowInterval) {
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

    public static Logger getLOG() {
        return LOG;
    }

    public LocalDateTime getNextRun() {
        return nextRun;
    }

    public void setNextRun(LocalDateTime nextRun) {
        this.nextRun = nextRun;
    }

    public LocalDateTime getLastRun() {
        return lastRun;
    }

    public void setLastRun(LocalDateTime lastRun) {
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
