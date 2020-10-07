package com.ibm.sterling.bfg.app.model.security;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class LoginSsoRequest implements Login{

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
    public MultiValueMap<String, String> getLoginMap() {
        return new LinkedMultiValueMap<String, String>() {
            {
                add("userName", login);
                add("nodeName", nodeName);
                add("dlssoToken", dlssoToken);
            }
        };
    }

    @Override
    public String getUrlPostfix() {
        return "/sso";
    }
}
