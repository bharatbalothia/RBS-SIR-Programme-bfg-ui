package com.ibm.sterling.bfg.app.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtSettings {

    private Integer accessTokenExpirationTime;
    private String tokenSigningKey;
    private Integer timePicker;

    public Integer getAccessTokenExpirationTime() {
        return accessTokenExpirationTime;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public Integer getTimePicker() {
        return timePicker;
    }

    public void setAccessTokenExpirationTime(Integer accessTokenExpirationTime) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public void setTimePicker(Integer timePicker) {
        this.timePicker = timePicker;
    }
}
