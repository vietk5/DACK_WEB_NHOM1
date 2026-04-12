package com.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security event logger with GDPR-compliant email masking
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A09 - Security logging and monitoring
 */
public class SecurityLogger {
    private static final Logger logger = LoggerFactory.getLogger("com.demo.security");
    
    // Store events for SIEM dashboard
    private static void storeEvent(String eventType, String level, String email, 
                                   String ipAddress, String details) {
        SecurityEventStore.addEvent(eventType, level, maskEmail(email), ipAddress, details);
    }

    public static void logLoginSuccess(String email, String ipAddress) {
        String message = String.format("LOGIN_SUCCESS - Email: %s - IP: %s", maskEmail(email), ipAddress);
        logger.info(message);
        System.out.println("✅ [SECURITY LOG] " + message);
        storeEvent("LOGIN_SUCCESS", "INFO", email, ipAddress, "Successful login");
    }

    public static void logLoginFailure(String email, String ipAddress, String reason) {
        String message = String.format("LOGIN_FAILURE - Email: %s - IP: %s - Reason: %s", 
            maskEmail(email), ipAddress, reason);
        logger.warn(message);
        System.out.println("⚠️ [SECURITY LOG] " + message);
        storeEvent("LOGIN_FAILURE", "WARN", email, ipAddress, reason);
    }

    public static void logAccountLockout(String email, String ipAddress) {
        String message = String.format("ACCOUNT_LOCKOUT - Email: %s - IP: %s", maskEmail(email), ipAddress);
        logger.warn(message);
        System.out.println("🔒 [SECURITY LOG] " + message);
        storeEvent("ACCOUNT_LOCKOUT", "ERROR", email, ipAddress, "Account locked due to multiple failed attempts");
    }

    public static void logRegistration(String email, String ipAddress) {
        logger.info("REGISTRATION - Email: {} - IP: {}", maskEmail(email), ipAddress);
        storeEvent("REGISTRATION", "INFO", email, ipAddress, "New user registration");
    }

    public static void logPasswordChange(String email, String ipAddress) {
        logger.info("PASSWORD_CHANGE - Email: {} - IP: {}", maskEmail(email), ipAddress);
        storeEvent("PASSWORD_CHANGE", "INFO", email, ipAddress, "Password changed");
    }

    public static void logSuspiciousActivity(String activity, String email, String ipAddress) {
        logger.warn("SUSPICIOUS_ACTIVITY - Activity: {} - Email: {} - IP: {}", 
            activity, maskEmail(email), ipAddress);
        storeEvent("SUSPICIOUS_ACTIVITY", "WARN", email, ipAddress, activity);
    }

    public static void logDataAccess(String resource, String email, String ipAddress) {
        logger.info("DATA_ACCESS - Resource: {} - Email: {} - IP: {}", 
            resource, maskEmail(email), ipAddress);
        storeEvent("DATA_ACCESS", "INFO", email, ipAddress, "Accessed: " + resource);
    }

    public static void logRateLimitExceeded(String endpoint, String ipAddress) {
        logger.warn("RATE_LIMIT_EXCEEDED - Endpoint: {} - IP: {}", endpoint, ipAddress);
        storeEvent("RATE_LIMIT_EXCEEDED", "WARN", "N/A", ipAddress, "Endpoint: " + endpoint);
    }

    public static void logCSRFViolation(String endpoint, String ipAddress) {
        logger.warn("CSRF_VIOLATION - Endpoint: {} - IP: {}", endpoint, ipAddress);
        storeEvent("CSRF_VIOLATION", "ERROR", "N/A", ipAddress, "Endpoint: " + endpoint);
    }

    /**
     * Mask email for GDPR compliance
     * Example: john.doe@gmail.com -> jo***@gmail.com
     */
    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 2) {
            return "***@" + parts[1];
        }
        return username.substring(0, 2) + "***@" + parts[1];
    }
}
