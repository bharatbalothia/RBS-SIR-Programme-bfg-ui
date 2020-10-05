package com.ibm.sterling.bfg.app.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class SWIFTNetRoutingRuleServiceResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SWIFTNetRoutingRuleBfguiRestResponse> response;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> errors;

    public SWIFTNetRoutingRuleServiceResponse(List<SWIFTNetRoutingRuleBfguiRestResponse> response,
                                              List<Map<String, String>> errors) {
        this.response = response;
        this.errors = errors;
    }

    public SWIFTNetRoutingRuleServiceResponse(List<Map<String, String>> errors) {
        this.errors = errors;
    }

    public SWIFTNetRoutingRuleServiceResponse() {
    }

    public List<SWIFTNetRoutingRuleBfguiRestResponse> getResponse() {
        return response;
    }

    public void setResponse(List<SWIFTNetRoutingRuleBfguiRestResponse> response) {
        this.response = response;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }

}
