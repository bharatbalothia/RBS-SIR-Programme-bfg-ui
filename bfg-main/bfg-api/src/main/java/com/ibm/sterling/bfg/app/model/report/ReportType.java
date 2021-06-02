package com.ibm.sterling.bfg.app.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;

public enum ReportType {
    @JsonProperty("pdf")
    PDF(".pdf", MediaType.APPLICATION_PDF),
    @JsonProperty("excel")
    EXCEL(".xlsx", MediaType.parseMediaType("application/vnd.ms-excel"));

    private String extension;
    private MediaType mediaType;

    ReportType(String extension, MediaType mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    public String extension() {
        return extension;
    }

    public MediaType mediaType() {
        return mediaType;
    }
}
