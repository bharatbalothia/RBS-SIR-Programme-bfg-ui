package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class IntegratedCertNameAndDate {
    private String certName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy hh.mm a")
    private LocalDateTime creationOrUpdateTime;

    public IntegratedCertNameAndDate() {
    }

    public IntegratedCertNameAndDate(String certName, LocalDateTime creationOrUpdateTime) {
        this.certName = certName;
        this.creationOrUpdateTime = creationOrUpdateTime;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public LocalDateTime getCreationOrUpdateTime() {
        return creationOrUpdateTime;
    }

    public void setCreationOrUpdateTime(LocalDateTime creationOrUpdateTime) {
        this.creationOrUpdateTime = creationOrUpdateTime;
    }

    @Override
    public String toString() {
        return "IntegratedCertNameAndDate{" +
                "certName='" + certName + '\'' +
                ", creationOrUpdateTime=" + creationOrUpdateTime +
                '}';
    }
}
