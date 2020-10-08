package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sterling.bfg.app.utils.Decoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class LoginSsoRequest implements Login {
    @JsonAlias("userName")
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
                add("dlssoToken", Decoder.decodeValue(dlssoToken) );
            }
        };
    }

    @Override
    @JsonIgnore
    public String urlPostfix() {
        return "/sso";
    }
}
