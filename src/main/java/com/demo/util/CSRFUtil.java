package com.demo.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF Token Utility
 * Generates and validates CSRF tokens for form submissions
 */
public class CSRFUtil {
    
    private static final String CSRF_TOKEN_ATTR = "CSRF_TOKEN";
    private static final int TOKEN_LENGTH = 32;
    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Generate a new CSRF token and store it in session
     */
    public static String generateToken(HttpSession session) {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        session.setAttribute(CSRF_TOKEN_ATTR, token);
        return token;
    }
    
    /**
     * Get existing CSRF token from session, or generate new one if not exists
     */
    public static String getToken(HttpSession session) {
        String token = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        if (token == null) {
            token = generateToken(session);
        }
        return token;
    }
    
    /**
     * Validate CSRF token from request against session token
     */
    public static boolean validateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        String requestToken = request.getParameter("csrfToken");
        
        if (sessionToken == null || requestToken == null) {
            return false;
        }
        
        return sessionToken.equals(requestToken);
    }
    
    /**
     * Remove CSRF token from session (useful after successful form submission)
     */
    public static void removeToken(HttpSession session) {
        if (session != null) {
            session.removeAttribute(CSRF_TOKEN_ATTR);
        }
    }
}
