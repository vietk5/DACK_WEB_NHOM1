package com.demo.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Account lockout manager to prevent brute force attacks
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A06 - Account lockout policy (5 attempts / 30 minutes)
 */
public class AccountLockoutManager {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = TimeUnit.MINUTES.toMillis(30);
    private static final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    /**
     * Check if account is currently locked
     */
    public static boolean isAccountLocked(String email) {
        LoginAttempt attempt = attempts.get(email);
        if (attempt == null) return false;
        
        if (System.currentTimeMillis() - attempt.lockoutTime > LOCKOUT_DURATION) {
            attempts.remove(email);
            return false;
        }
        
        return attempt.failedAttempts >= MAX_FAILED_ATTEMPTS;
    }

    /**
     * Record a failed login attempt
     */
    public static void recordFailedAttempt(String email) {
        LoginAttempt attempt = attempts.computeIfAbsent(email, k -> new LoginAttempt());
        attempt.failedAttempts++;
        if (attempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            attempt.lockoutTime = System.currentTimeMillis();
        }
    }

    /**
     * Reset failed attempts counter (called on successful login)
     */
    public static void resetAttempts(String email) {
        attempts.remove(email);
    }

    /**
     * Get remaining lockout time in minutes
     */
    public static long getRemainingLockoutMinutes(String email) {
        LoginAttempt attempt = attempts.get(email);
        if (attempt == null || attempt.failedAttempts < MAX_FAILED_ATTEMPTS) {
            return 0;
        }
        
        long elapsed = System.currentTimeMillis() - attempt.lockoutTime;
        long remaining = LOCKOUT_DURATION - elapsed;
        
        if (remaining <= 0) {
            attempts.remove(email);
            return 0;
        }
        
        return TimeUnit.MILLISECONDS.toMinutes(remaining) + 1;
    }

    static class LoginAttempt {
        int failedAttempts = 0;
        long lockoutTime = 0;
    }
}
