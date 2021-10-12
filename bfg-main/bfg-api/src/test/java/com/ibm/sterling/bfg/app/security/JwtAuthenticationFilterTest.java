package com.ibm.sterling.bfg.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.AUTHENTICATION_HEADER_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {

        headers = new HashMap<>();
        headers.put(AUTHENTICATION_HEADER_NAME, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJURVNUVVNFUiIsInBlcm1pc3Npb25zIjpbIkZCX1VJX1RSVVNURURfQ0VSVFMiLCJTRkdfVUlfU0NUX0RFTEVURV9FTlRJVFlfR1BMIiwiU0ZHX1VJX1NDVF9ERUxFVEVfRU5USVRZX1NDVCIsIlNGR19VSV9TQ1RfQ1JFQVRFX0VOVElUWV9TQ1QiLCJBUElVc2VyIiwiU0ZHX1VJX1NDVF9FRElUX0VOVElUWV9TQ1QiLCJTRkdfVUlfU0NUX0VESVRfRU5USVRZX0dQTCIsIkZCX1VJX1RSVVNURURfQ0VSVFNfTkVXIiwiU0ZHX1VJX1NDVF9BUFBST1ZFX0VOVElUWV9HUEwiLCJTRkdfVUlfU0NUX0FQUFJPVkVfRU5USVRZX1NDVCIsIk15QWNjb3VudCIsIkFkbWluIFdlYiBBcHAgUGVybWlzc2lvbiIsIlNGR19VSV9TQ1RfQ1JFQVRFX0VOVElUWV9HUEwiXSwiaWF0IjoxNTk2MDExMTk4LCJleHAiOjE1OTYwOTc1OTh9.tbjhlZhFk1qy3U3kq0NEAROMFJnx3ivLMWTRYF1Cc0ssbFXHYKqMa7FyB5Ns0X5IINDH1JOysmS0oX6Wt5WqxA");
    }

    @Test
    void requiresAuthentication() {
        assertTrue(jwtAuthenticationFilter.requiresAuthentication(request, response));
    }
}