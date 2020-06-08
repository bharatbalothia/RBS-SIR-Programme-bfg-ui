package com.ibm.sterling.bfg.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.model.exception.AuthErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ErrorConfig errorConfig;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        ErrorMessage errorMessage =
                errorConfig.getErrorMessage(AuthErrorCode.BadCredentialsException,
                        Optional.ofNullable(exception.getCause())
                                .map(Throwable::getLocalizedMessage)
                                .orElse(exception.getLocalizedMessage())
                );
        response.setStatus(errorMessage.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), errorMessage);
    }

}
