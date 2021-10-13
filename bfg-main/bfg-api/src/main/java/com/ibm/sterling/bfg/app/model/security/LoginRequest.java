package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class LoginRequest implements Login {

    private String login;
    private String password;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public MultiValueMap<String, String> retrieveFields() {
        return new LinkedMultiValueMap<String, String>() {
            {
                add("userName", login);
                add("password", password);
            }
        };
    }

    @Override
    @JsonIgnore
    public String urlPostfix() {
        return "";
    }
}
