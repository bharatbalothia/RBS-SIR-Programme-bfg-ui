package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.security.JwtTokenProvider;
import com.ibm.sterling.bfg.app.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Map<String, String> refreshTokens = new HashMap<>();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/renew")
    public ResponseEntity renew(@RequestBody Map<String, String> userData) {
        String login = userData.get("login");
        String refreshToken = userData.get("refreshToken");
        if (refreshTokens.get(refreshToken) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (refreshTokens.get(refreshToken).equals(login)) {
            List<Object> tokenDataList = jwtTokenProvider.generateAccessToken(
                    userDetailsService
                            .loadUserByUsername(login)
            );
            UUID newRefreshToken = UUID.randomUUID();
            refreshTokens.put(newRefreshToken.toString(), login);
            Map<Object, Object> model = new HashMap<>();
            model.put("accessToken", tokenDataList.get(0));
            model.put("refreshToken", newRefreshToken);
            model.put("expiresIn", tokenDataList.get(1));
            return ok(model);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody Map<String, String> userData) {
        String login = userData.get("login");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, userData.get("password")));
        List<Object> tokenDataList = jwtTokenProvider.generateAccessToken(
                userDetailsService
                        .loadUserByUsername(login)
        );
        UUID refreshToken = UUID.randomUUID();
        refreshTokens.put(refreshToken.toString(), login);
        Map<Object, Object> model = new HashMap<>();
        model.put("accessToken", tokenDataList.get(0));
        model.put("refreshToken", refreshToken);
        model.put("expiresIn", tokenDataList.get(1));
        return ok(model);
    }

}
