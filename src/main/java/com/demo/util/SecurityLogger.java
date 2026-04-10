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

    public static void logLoginSuccess(String email, String ipAddress) {
        logger.info("LOGIN_SUCCESS - Email: {} - IP: {}", maskEmail(email), ipAddress);
    }

    public static void logLoginFailure(String email, String ipAddress, String reason) {
        logger.warn("LOGIN_FAILURE - Email: {} - IP: {} - Reason: {}", 
            maskEmail(email), ipAddress, reason);
    }

    public static void logAccountLockout(String email, String ipAddress) {
        logger.warn("ACCOUNT_LOCKOUT - Email: {} - IP: {}", maskEmail(email), ipAddress);
    }

    public static void logRegistration(String email, String ipAddress) {
        logger.info("REGISTRATION - Email: {} - IP: {}", maskEmail(email), ipAddress);
    }

    public static void logPasswordChange(String email, String ipAddress) {
        logger.info("PASSWORD_CHANGE - Email: {} - IP: {}", maskEmail(email), ipAddress);
    }

    public static void logSuspiciousActivity(String activity, String email, String ipAddress) {
        logger.warn("SUSPICIOUS_ACTIVITY - Activity: {} - Email: {} - IP: {}", 
            activity, maskEmail(email), ipAddress);
    }

    public static void logDataAccess(String resource, String email, String ipAddress) {
        logger.info("DATA_ACCESS - Resource: {} - Email: {} - IP: {}", 
            resource, maskEmail(email), ipAddress);
    }

    public static void logRateLimitExceeded(String endpoint, String ipAddress) {
        logger.warn("RATE_LIMIT_EXCEEDED - Endpoint: {} - IP: {}", endpoint, ipAddress);
    }

    public static void logCSRFViolation(String endpoint, String ipAddress) {
        logger.warn("CSRF_VIOLATION - Endpoint: {} - IP: {}", endpoint, ipAddress);
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
