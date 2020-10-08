package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.MultiValueMap;

public class LoginRequest extends Login {
    private String password;

    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public MultiValueMap<String, String> retrieveFields() {
        MultiValueMap<String, String> loginMap = super.retrieveFields();
        loginMap.add("password", password);
        return loginMap;
    }
}
