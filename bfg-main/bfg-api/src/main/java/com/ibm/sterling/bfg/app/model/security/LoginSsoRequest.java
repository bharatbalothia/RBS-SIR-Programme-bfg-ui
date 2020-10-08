package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.MultiValueMap;

public class LoginSsoRequest extends Login {
    private String nodeName;
    private String dlssoToken;

    public String getNodeName() {
        return nodeName;
    }

    public String getDlssoToken() {
        return dlssoToken;
    }

    @Override
    @JsonIgnore
    public MultiValueMap<String, String> retrieveFields() {
        MultiValueMap<String, String> loginMap = super.retrieveFields();
        loginMap.add("nodeName", nodeName);
        loginMap.add("dlssoToken", dlssoToken);
        return loginMap;
    }

    @Override
    @JsonIgnore
    public String urlPostfix() {
        return "/sso";
    }
}
