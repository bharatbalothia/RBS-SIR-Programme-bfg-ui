package com.ibm.sterling.bfg.app.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class SWIFTNetRoutingRuleServiceResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SWIFTNetRoutingRuleBfgUiRestResponse> response;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> errors;

    public SWIFTNetRoutingRuleServiceResponse(List<SWIFTNetRoutingRuleBfgUiRestResponse> response,
                                              List<Map<String, String>> errors) {
        this.response = response;
        this.errors = errors;
    }

    public SWIFTNetRoutingRuleServiceResponse() {
    }

    public List<SWIFTNetRoutingRuleBfgUiRestResponse> getResponse() {
        return response;
    }

    public void setResponse(List<SWIFTNetRoutingRuleBfgUiRestResponse> response) {
        this.response = response;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }

}
