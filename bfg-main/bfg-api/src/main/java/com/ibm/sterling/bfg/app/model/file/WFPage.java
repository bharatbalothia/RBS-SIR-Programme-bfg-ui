package com.ibm.sterling.bfg.app.model.file;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class WFPage<T> extends PageImpl<T> {
    private String status;
    private String state;
    private Boolean isFullTracking = true;

    public WFPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public WFPage(List<T> content) {
        super(content);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getFullTracking() {
        return isFullTracking;
    }

    public void setFullTracking(Boolean fullTracking) {
        isFullTracking = fullTracking;
    }
}
