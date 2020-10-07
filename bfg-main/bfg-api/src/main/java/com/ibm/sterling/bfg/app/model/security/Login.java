package com.ibm.sterling.bfg.app.model.security;

import org.springframework.util.MultiValueMap;

public interface Login {
    String getLogin();

    MultiValueMap<String, String> retrieveFields();

    String urlPostfix();
}
