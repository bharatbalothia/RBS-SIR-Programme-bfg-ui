package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.model.security.UserCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenValidator {

    @Autowired
    private JwtSettings settings;

    public UserCredentials parseToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(settings.getTokenSigningKey())
                .parseClaimsJws(token).getBody();
        if (isTokenExpired(body))
            throw new BadCredentialsException("JWT token is expired");
        List<HashMap<String, String>> groups = body.get("groups", List.class);
        List<GrantedAuthority> groupList = groups.stream()
                .flatMap(groupMap -> groupMap.values().stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UserCredentials(body.getSubject(), token, groupList);
    }

    private boolean isTokenExpired(Claims body) {
        return body.getExpiration().before(new Date());
    }

}
