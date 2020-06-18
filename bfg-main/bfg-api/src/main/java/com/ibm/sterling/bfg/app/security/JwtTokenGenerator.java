package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.model.security.UserCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenGenerator {

    @Autowired
    private JwtSettings settings;

    public String createAccessJwtToken(UserCredentials userCredentials) {
        Claims claims = Jwts.claims().setSubject(userCredentials.getUsername());
        claims.put("permissions",
                userCredentials.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        LocalDateTime currentTime = LocalDateTime.now();
        Date refreshExpirationTime = Date.from(currentTime
                .plusMinutes(settings.getAccessTokenExpirationTime())
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(refreshExpirationTime)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
    }

}
