package com.ibm.sterling.bfg.app.model.security;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class LoginRequest implements Login {

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public MultiValueMap<String, String> getLoginMap() {
        return new LinkedMultiValueMap<String, String>() {
            {
                add("userName", login);
                add("password", password);
            }
        };
    }

    @Override
    public String getUrlPostfix() {
        return "";
    }


}
