package com.demo.filter;

import com.demo.util.SecurityLogger;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiting filter to prevent brute force attacks
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A06 - Rate limiting (5 requests / 5 seconds)
 */
@WebFilter(urlPatterns = {"/login", "/register", "/forgot-password", "/requestPassword"})
public class RateLimitFilter implements Filter {
    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW = TimeUnit.SECONDS.toMillis(5);
    private final ConcurrentHashMap<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String clientIP = getClientIP(httpRequest);
        RequestCounter counter = requestCounts.computeIfAbsent(clientIP, k -> new RequestCounter());
        
        if (counter.isLimitExceeded()) {
            SecurityLogger.logRateLimitExceeded(httpRequest.getRequestURI(), clientIP);
            httpResponse.setStatus(429);
            httpResponse.setContentType("text/html; charset=UTF-8");
            httpResponse.getWriter().write("Quá nhiều yêu cầu. Vui lòng thử lại sau 5 giây.");
            return;
        }
        
        counter.increment();
        chain.doFilter(request, response);
    }

    /**
     * Get client IP address, supporting X-Forwarded-For header for proxies
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void destroy() {
        requestCounts.clear();
    }

    static class RequestCounter {
        private int count = 0;
        private long windowStart = System.currentTimeMillis();

        synchronized boolean isLimitExceeded() {
            long now = System.currentTimeMillis();
            if (now - windowStart > TIME_WINDOW) {
                count = 0;
                windowStart = now;
            }
            return count >= MAX_REQUESTS;
        }

        synchronized void increment() {
            count++;
        }
    }
}
