package com.ibm.sterling.bfg.app.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public class UserCredentials implements UserDetails {

    private String userName;
    private String token;
    private List<GrantedAuthority> permissions;

    public UserCredentials(
            String userName,
            String token,
            List<GrantedAuthority> permissions) {
        this.userName = userName;
        this.token = token;
        this.permissions = permissions;
    }

    @JsonIgnore
    public Long getId() {
        return 0L;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return permissions;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPermissions(List<GrantedAuthority> permissions) {
        this.permissions = permissions;
    }

}
