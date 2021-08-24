package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForUpdateChangeControlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class APIDetailsHandlerTest {
    @InjectMocks
    private APIDetailsHandler apiDetailsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void extractErrorMessage() {
    }

    @Test
    void processErrorMessage() {
    }

    @Test
    void getHttpHeaders() {
        
    }

    @Test
    void checkPermissionForUpdateChangeControl_ShouldThrowException() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName())
                .thenReturn("TESTUSER");
        assertThrows(InvalidUserForUpdateChangeControlException.class,
                () -> apiDetailsHandler.checkPermissionForUpdateChangeControl("user"));
    }
}