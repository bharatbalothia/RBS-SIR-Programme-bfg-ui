package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.model.security.UserCredentials;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenValidator {

    @Autowired
    private JwtSettings settings;

    public UserCredentials parseToken(String token) {
        Claims body = parseClaims(token).getBody();
        List<String> permissions = body.get("permissions", List.class);
        List<GrantedAuthority> grantedAuthorityList = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UserCredentials(body.getSubject(), token, grantedAuthorityList);
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new BadCredentialsException("JWT Token expired", expiredEx);
        }
    }

}
