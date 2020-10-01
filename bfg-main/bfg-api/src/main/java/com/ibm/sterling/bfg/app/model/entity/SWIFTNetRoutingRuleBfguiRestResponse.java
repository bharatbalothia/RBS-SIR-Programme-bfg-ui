package com.ibm.sterling.bfg.app.model.entity;

public class SWIFTNetRoutingRuleBfguiRestResponse {
    private String routingRuleName;
    private String updateAction;
    private String failCause;
    private Integer code;

    public String getRoutingRuleName() {
        return routingRuleName;
    }

    public void setRoutingRuleName(String routingRuleName) {
        this.routingRuleName = routingRuleName;
    }

    public String getUpdateAction() {
        return updateAction;
    }

    public void setUpdateAction(String updateAction) {
        this.updateAction = updateAction;
    }

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
