package com.studying.first_spring_app.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        try {
            String requestID = UUID.randomUUID().toString();
            response.setHeader("X-Request-ID", requestID);
            MDC.put("requestId", requestID);
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (log.isDebugEnabled()) {
                log.info("{} {} | Status: {} | Time: {} ms", request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
            }
            MDC.clear();
        }
    }
}
