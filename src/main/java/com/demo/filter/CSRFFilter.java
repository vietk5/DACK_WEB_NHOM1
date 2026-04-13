package com.demo.filter;

import com.demo.util.CSRFUtil;
import com.demo.util.SecurityLogger;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * CSRF Protection Filter
 * Validates CSRF tokens for state-changing operations (POST, PUT, DELETE)
 */
@WebFilter(urlPatterns = {"/*"})
public class CSRFFilter implements Filter {
    
    // Endpoints that require CSRF protection
    private static final List<String> PROTECTED_PATHS = Arrays.asList(
        "/profile",
        "/checkout",
        "/cart"
    );
    
    // Endpoints that should be excluded from CSRF check
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/login",
        "/register",
        "/logout",
        "/google-callback",
        "/forgot-password",
        "/requestPassword",
        "/reset-password"
    );
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String method = httpRequest.getMethod();
        String path = httpRequest.getServletPath();
        
        // Only check POST, PUT, DELETE requests
        if (!method.equalsIgnoreCase("POST") && 
            !method.equalsIgnoreCase("PUT") && 
            !method.equalsIgnoreCase("DELETE")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Skip CSRF check for excluded paths
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if path requires CSRF protection
        if (requiresCSRFProtection(path)) {
            if (!CSRFUtil.validateToken(httpRequest)) {
                String clientIP = getClientIP(httpRequest);
                SecurityLogger.logCSRFViolation(path, clientIP);
                
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.setContentType("text/html; charset=UTF-8");
                httpResponse.getWriter().write(
                    "<html><body>" +
                    "<h3>Lỗi bảo mật</h3>" +
                    "<p>Yêu cầu không hợp lệ. Vui lòng thử lại.</p>" +
                    "<a href='" + httpRequest.getContextPath() + path + "'>Quay lại</a>" +
                    "</body></html>"
                );
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean requiresCSRFProtection(String path) {
        return PROTECTED_PATHS.stream().anyMatch(path::startsWith);
    }
    
    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
    
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
        // No cleanup needed
    }
}
