package com.ibm.sterling.bfg.app.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class SWIFTNetRoutingRuleServiceResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SWIFTNetRoutingRuleBfguiRestResponse> swiftNetRoutingRuleBfguiRestResponse;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> swiftNetRoutingRuleErrors;

    public SWIFTNetRoutingRuleServiceResponse(List<SWIFTNetRoutingRuleBfguiRestResponse> swiftNetRoutingRuleBfguiRestResponse,
                                              List<Map<String, String>> swiftNetRoutingRuleErrors) {
        this.swiftNetRoutingRuleBfguiRestResponse = swiftNetRoutingRuleBfguiRestResponse;
        this.swiftNetRoutingRuleErrors = swiftNetRoutingRuleErrors;
    }

    public SWIFTNetRoutingRuleServiceResponse(List<Map<String, String>> swiftNetRoutingRuleErrors) {
        this.swiftNetRoutingRuleErrors = swiftNetRoutingRuleErrors;
    }

    public SWIFTNetRoutingRuleServiceResponse() {
    }

    public List<SWIFTNetRoutingRuleBfguiRestResponse> getSwiftNetRoutingRuleBfguiRestResponse() {
        return swiftNetRoutingRuleBfguiRestResponse;
    }

    public void setSwiftNetRoutingRuleBfguiRestResponse(List<SWIFTNetRoutingRuleBfguiRestResponse> swiftNetRoutingRuleBfguiRestResponse) {
        this.swiftNetRoutingRuleBfguiRestResponse = swiftNetRoutingRuleBfguiRestResponse;
    }

    public List<Map<String, String>> getSwiftNetRoutingRuleErrors() {
        return swiftNetRoutingRuleErrors;
    }

    public void setSwiftNetRoutingRuleErrors(List<Map<String, String>> swiftNetRoutingRuleErrors) {
        this.swiftNetRoutingRuleErrors = swiftNetRoutingRuleErrors;
    }

}
