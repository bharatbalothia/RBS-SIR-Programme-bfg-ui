package com.ibm.sterling.bfg.app.model.file;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public class WFPage<T> extends PageImpl<T> {
    private String status;
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

    public Boolean getFullTracking() {
        return isFullTracking;
    }

    public void setFullTracking(Boolean fullTracking) {
        isFullTracking = fullTracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WFPage<?> wfPage = (WFPage<?>) o;
        return Objects.equals(status, wfPage.status) &&
                Objects.equals(isFullTracking, wfPage.isFullTracking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, isFullTracking);
    }
}
