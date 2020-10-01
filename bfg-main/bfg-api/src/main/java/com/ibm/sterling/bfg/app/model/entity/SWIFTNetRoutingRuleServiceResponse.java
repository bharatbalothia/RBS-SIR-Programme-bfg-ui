package com.ibm.sterling.bfg.app.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class SWIFTNetRoutingRuleServiceResponse {
    private List<SWIFTNetRoutingRuleBfguiRestResponse> swiftNetRoutingRuleBfguiRestResponse;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> swiftNetRoutingRuleErrors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> swiftNetRoutingRuleWarnings;

    public SWIFTNetRoutingRuleServiceResponse(List<SWIFTNetRoutingRuleBfguiRestResponse> swiftNetRoutingRuleBfguiRestResponse,
                                              List<Map<String, String>> swiftNetRoutingRuleErrors,
                                              List<Map<String, String>> swiftNetRoutingRuleWarnings) {
        this.swiftNetRoutingRuleBfguiRestResponse = swiftNetRoutingRuleBfguiRestResponse;
        this.swiftNetRoutingRuleErrors = swiftNetRoutingRuleErrors;
        this.swiftNetRoutingRuleWarnings = swiftNetRoutingRuleWarnings;
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

    public List<Map<String, String>> getSwiftNetRoutingRuleWarnings() {
        return swiftNetRoutingRuleWarnings;
    }

    public void setSwiftNetRoutingRuleWarnings(List<Map<String, String>> swiftNetRoutingRuleWarnings) {
        this.swiftNetRoutingRuleWarnings = swiftNetRoutingRuleWarnings;
    }

}
