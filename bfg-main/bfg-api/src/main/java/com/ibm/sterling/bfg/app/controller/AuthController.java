package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import com.ibm.sterling.bfg.app.model.security.LoginSsoRequest;
import com.ibm.sterling.bfg.app.security.JwtTokenGenerator;
import com.ibm.sterling.bfg.app.service.security.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    public static final String ACCESS_TOKEN = "accessToken";
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private JwtTokenGenerator jwtTokenFactory;

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody LoginRequest credentials) throws JsonProcessingException {
        return ok(Collections.singletonMap(ACCESS_TOKEN,
                jwtTokenFactory.createAccessJwtToken(credentialsService.getSBIAuthResponse(credentials))));
    }

    @PostMapping("/sso")
    public ResponseEntity<Object> signInSso(@RequestBody LoginSsoRequest credentials) throws JsonProcessingException {
        return ok(Collections.singletonMap(ACCESS_TOKEN,
                jwtTokenFactory.createAccessJwtToken(credentialsService.getSBIAuthResponse(credentials))));
    }

    @PostMapping("/reauth")
    public ResponseEntity<Object> continueSession(@RequestBody LoginRequest credentials) throws JsonProcessingException {
        return ok(Collections.singletonMap(ACCESS_TOKEN,
                jwtTokenFactory.createAccessJwtToken(credentialsService.getSBIAuthResponse(credentials))));
    }
}
