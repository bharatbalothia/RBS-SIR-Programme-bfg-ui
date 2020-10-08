package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Login {
    private String login;

    public Login() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonIgnore
    public MultiValueMap<String, String> retrieveFields() {
        return new LinkedMultiValueMap<String, String>() {
            {
                add("userName", login);
            }
        };
    }

    @JsonIgnore
    public String urlPostfix() {
        return "";
    }
}
