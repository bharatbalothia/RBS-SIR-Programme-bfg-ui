package com.ibm.sterling.bfg.app.config;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ResponseNoCacheHeaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
