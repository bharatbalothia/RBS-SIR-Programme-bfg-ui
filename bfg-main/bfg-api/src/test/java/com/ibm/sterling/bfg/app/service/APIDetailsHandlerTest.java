package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForUpdateChangeControlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class APIDetailsHandlerTest {
    @InjectMocks
    private APIDetailsHandler apiDetailsHandler;

    private String errorMessage;

    @Test
    void extractErrorMessage_ShouldReturnErrorMessageByKey() {
        errorMessage = "{\n" +
                "    \"errorCode\": 404,\n" +
                "    \"errorDescription\": \"Not found\"\n" +
                "}";
        assertEquals("Not found",
                apiDetailsHandler.extractErrorMessage(errorMessage, "errorDescription"));
    }

    @Test
    void extractErrorMessage_ShouldReturnNull() {
        assertNull(apiDetailsHandler.extractErrorMessage(errorMessage, "errorDescription"));
    }

    @Test
    void processErrorMessage_ShouldReturnMap() {
        errorMessage = "{\n" +
                "    \"attribute\": 404,\n" +
                "    \"message\": \"Not found\"\n" +
                "}";
        HttpStatusCodeException e = new HttpStatusCodeException(HttpStatus.NOT_FOUND) {
            @Override
            public String getMessage() {
                return errorMessage;
            }
        };
        Map<String, List<Object>> map = new HashMap<>();
        map.put("404", Collections.singletonList("Not found"));
        assertEquals(map, apiDetailsHandler.processErrorMessage(e));
    }

    @Test
    void processErrorMessage_ShouldReturnNull() {
        HttpStatusCodeException e = new HttpStatusCodeException(HttpStatus.NOT_FOUND) {
            @Override
            public String getMessage() {
                return errorMessage;
            }
        };
        assertNull(apiDetailsHandler.processErrorMessage(e));
    }

    @Test
    void getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("userName", "password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        assertEquals(headers, apiDetailsHandler.getHttpHeaders("userName", "password"));
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