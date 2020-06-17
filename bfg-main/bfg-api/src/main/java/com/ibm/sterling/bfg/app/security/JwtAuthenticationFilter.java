package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.model.security.JwtAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.AUTHENTICATION_HEADER_NAME;
import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX_TOKEN;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private static String extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHENTICATION_HEADER_NAME))
                .filter(head -> head.length() > HEADER_PREFIX_TOKEN.length())
                .map(head -> head.substring(HEADER_PREFIX_TOKEN.length()))
                .orElseThrow(() -> new BadCredentialsException("Token corrupted"));
    }

}
