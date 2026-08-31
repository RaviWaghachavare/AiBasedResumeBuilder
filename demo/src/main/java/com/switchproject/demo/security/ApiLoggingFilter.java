package com.switchproject.demo.security;

import com.switchproject.demo.model.ApiLog;
import com.switchproject.demo.repository.ApiLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private final ApiLogRepository apiLogRepository;

    public ApiLoggingFilter(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);

        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        try {

            filterChain.doFilter(wrappedRequest, wrappedResponse);

        } finally {

            long endTime = System.currentTimeMillis();

            ApiLog apiLog = new ApiLog();

            // Full URL
            apiLog.setEndpoint(
                    request.getRequestURL().toString()
            );

            // HTTP method
            apiLog.setHttpMethod(
                    request.getMethod()
            );

            // Status code
            apiLog.setStatusCode(
                    wrappedResponse.getStatus()
            );

            // Timestamp
            apiLog.setTimestamp(
                    LocalDateTime.now()
            );

            // Response time
            apiLog.setResponseTimeMs(
                    endTime - startTime
            );

            // Request body
            String requestBody = new String(
                    wrappedRequest.getContentAsByteArray(),
                    StandardCharsets.UTF_8
            );

            // Response body
            String responseBody = new String(
                    wrappedResponse.getContentAsByteArray(),
                    StandardCharsets.UTF_8
            );

            apiLog.setRequestBody(
                    maskPassword(requestBody)
            );

            apiLog.setResponseBody(
                    maskToken(responseBody)
            );

            // Logged-in user
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication != null &&
                    authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getName())) {

                apiLog.setUserEmail(
                        authentication.getName()
                );

            } else {

                apiLog.setUserEmail("ANONYMOUS");
            }

            apiLogRepository.save(apiLog);

            // Very important
            wrappedResponse.copyBodyToResponse();

            System.out.println(
                    "API: " + request.getRequestURL()
                            + " | Method: " + request.getMethod()
                            + " | Status: " + wrappedResponse.getStatus()
                            + " | Time: " + (endTime - startTime) + " ms"
            );
        }
    }
    private String maskPassword(String body) {

        if (body == null || body.isEmpty()) {
            return body;
        }

        return body.replaceAll(
                "(\"password\"\\s*:\\s*\")[^\"]*(\")",
                "$1********$2"
        );
    }

    private String maskToken(String body) {

        if (body == null || body.isEmpty()) {
            return body;
        }

        return body.replaceAll(
                "(\"token\"\\s*:\\s*\")[^\"]*(\")",
                "$1[HIDDEN]$2"
        );
    }
}