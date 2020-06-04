package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.model.security.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    private static final String HEADER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(extractToken(request)));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private static String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHENTICATION_HEADER_NAME);
        if (!StringUtils.isEmpty(header) && header.length() > HEADER_PREFIX.length()) {
            return header.substring(HEADER_PREFIX.length());
        }
        return null;
    }

}
