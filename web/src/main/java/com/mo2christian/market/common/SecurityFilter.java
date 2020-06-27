package com.mo2christian.market.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private final String API_KEY_HEADER = "api-key";
    private final String apiKey;

    public SecurityFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String key = req.getHeader(API_KEY_HEADER);
        if (apiKey.equals(key))
            filterChain.doFilter(servletRequest, servletResponse);
        else{
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            response.setStatus(403);
        }
    }
}
