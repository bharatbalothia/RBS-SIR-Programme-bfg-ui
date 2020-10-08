package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class LoginSsoRequest implements Login {
    private String login;
    private String nodeName;
    private String dlssoToken;

    public String getLogin() {
        return login;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getDlssoToken() {
        return dlssoToken;
    }

    @Override
    @JsonIgnore
    public MultiValueMap<String, String> retrieveFields() {
        return new LinkedMultiValueMap<String, String>() {
            {
                add("userName", login);
                add("nodeName", nodeName);
                add("dlssoToken", dlssoToken);
            }
        };
    }

    @Override
    @JsonIgnore
    public String urlPostfix() {
        return "/sso";
    }
}
