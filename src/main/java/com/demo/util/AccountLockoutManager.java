package com.demo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Account lockout manager to prevent brute force attacks
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A06 - Account lockout policy (5 attempts / 30 minutes)
 * Updated: 2026-04-12 - Added IP-based tracking to prevent lockout attacks
 */
public class AccountLockoutManager {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = TimeUnit.MINUTES.toMillis(30);
    
    // Track by IP address (primary defense against distributed attacks)
    private static final ConcurrentHashMap<String, LoginAttempt> attemptsByIP = new ConcurrentHashMap<>();
    
    // Track by account (secondary defense, but with IP context)
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, LoginAttempt>> attemptsByAccount = new ConcurrentHashMap<>();

    /**
     * Check if account is currently locked from this IP
     * This prevents legitimate users from being locked out by attackers from different IPs
     */
    public static boolean isAccountLocked(String email, String ipAddress) {
        // Check IP-based lockout first (prevents brute force from single IP)
        LoginAttempt ipAttempt = attemptsByIP.get(ipAddress);
        if (ipAttempt != null && isLocked(ipAttempt)) {
            return true;
        }
        
        // Check account-specific lockout from this IP
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = attemptsByAccount.get(email);
        if (accountAttempts != null) {
            LoginAttempt attempt = accountAttempts.get(ipAddress);
            if (attempt != null && isLocked(attempt)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Backward compatibility - check if account is locked (any IP)
     */
    public static boolean isAccountLocked(String email) {
        // Check if account is locked from ANY IP
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = attemptsByAccount.get(email);
        if (accountAttempts != null) {
            for (LoginAttempt attempt : accountAttempts.values()) {
                if (isLocked(attempt)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isLocked(LoginAttempt attempt) {
        if (attempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            // Check if lockout period has expired
            if (System.currentTimeMillis() - attempt.lockoutTime > LOCKOUT_DURATION) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Record a failed login attempt (with IP tracking)
     */
    public static void recordFailedAttempt(String email, String ipAddress) {
        // Track by IP (prevents single IP from attacking multiple accounts)
        LoginAttempt ipAttempt = attemptsByIP.computeIfAbsent(ipAddress, k -> new LoginAttempt());
        ipAttempt.failedAttempts++;
        
        if (ipAttempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            ipAttempt.lockoutTime = System.currentTimeMillis();
            System.out.println("🔒 [SECURITY] IP locked: " + ipAddress + " - Failed attempts: " + ipAttempt.failedAttempts);
        }
        
        // Track by account + IP (prevents attacker from locking legitimate user)
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = 
            attemptsByAccount.computeIfAbsent(email, k -> new ConcurrentHashMap<>());
        
        LoginAttempt attempt = accountAttempts.computeIfAbsent(ipAddress, k -> new LoginAttempt());
        attempt.failedAttempts++;
        
        if (attempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            attempt.lockoutTime = System.currentTimeMillis();
            System.out.println("🔒 [SECURITY] Account locked: " + email + " from IP: " + ipAddress + 
                             " - Failed attempts: " + attempt.failedAttempts);
        } else {
            System.out.println("⚠️ [SECURITY] Failed login attempt " + attempt.failedAttempts + 
                             "/" + MAX_FAILED_ATTEMPTS + " for: " + email + " from IP: " + ipAddress);
        }
    }
    
    /**
     * Backward compatibility
     */
    public static void recordFailedAttempt(String email) {
        recordFailedAttempt(email, "unknown");
    }

    /**
     * Reset failed attempts counter (called on successful login)
     */
    public static void resetAttempts(String email, String ipAddress) {
        // Reset IP attempts
        attemptsByIP.remove(ipAddress);
        
        // Reset account attempts from this IP
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = attemptsByAccount.get(email);
        if (accountAttempts != null) {
            LoginAttempt removed = accountAttempts.remove(ipAddress);
            if (removed != null && removed.failedAttempts > 0) {
                System.out.println("✅ [SECURITY] Reset failed attempts for: " + email + " from IP: " + ipAddress);
            }
            
            // Clean up if no more IPs tracking this account
            if (accountAttempts.isEmpty()) {
                attemptsByAccount.remove(email);
            }
        }
    }
    
    /**
     * Backward compatibility
     */
    public static void resetAttempts(String email) {
        attemptsByAccount.remove(email);
        System.out.println("✅ [SECURITY] Reset all failed attempts for: " + email);
    }

    /**
     * Get remaining lockout time in minutes
     */
    public static long getRemainingLockoutMinutes(String email, String ipAddress) {
        // Check IP lockout first
        LoginAttempt ipAttempt = attemptsByIP.get(ipAddress);
        if (ipAttempt != null && ipAttempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            long remaining = calculateRemainingTime(ipAttempt);
            if (remaining > 0) return remaining;
        }
        
        // Check account lockout from this IP
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = attemptsByAccount.get(email);
        if (accountAttempts != null) {
            LoginAttempt attempt = accountAttempts.get(ipAddress);
            if (attempt != null && attempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
                return calculateRemainingTime(attempt);
            }
        }
        
        return 0;
    }
    
    /**
     * Backward compatibility
     */
    public static long getRemainingLockoutMinutes(String email) {
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = attemptsByAccount.get(email);
        if (accountAttempts != null) {
            for (LoginAttempt attempt : accountAttempts.values()) {
                if (attempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    long remaining = calculateRemainingTime(attempt);
                    if (remaining > 0) return remaining;
                }
            }
        }
        return 0;
    }
    
    private static long calculateRemainingTime(LoginAttempt attempt) {
        long elapsed = System.currentTimeMillis() - attempt.lockoutTime;
        long remaining = LOCKOUT_DURATION - elapsed;
        
        if (remaining <= 0) {
            return 0;
        }
        
        return TimeUnit.MILLISECONDS.toMinutes(remaining) + 1;
    }
    
    /**
     * Admin function: Manually unlock an account from specific IP
     */
    public static void unlockAccount(String email, String ipAddress) {
        ConcurrentHashMap<String, LoginAttempt> accountAttempts = attemptsByAccount.get(email);
        if (accountAttempts != null) {
            accountAttempts.remove(ipAddress);
            System.out.println("🔓 [ADMIN] Manually unlocked: " + email + " from IP: " + ipAddress);
        }
    }
    
    /**
     * Admin function: Manually unlock all attempts for an account
     */
    public static void unlockAccountAll(String email) {
        attemptsByAccount.remove(email);
        System.out.println("🔓 [ADMIN] Manually unlocked all IPs for: " + email);
    }
    
    /**
     * Admin function: Block an IP address permanently (until server restart)
     */
    public static void blockIP(String ipAddress) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.failedAttempts = MAX_FAILED_ATTEMPTS;
        // Set lockout time to very old to mark as permanent block
        attempt.lockoutTime = System.currentTimeMillis() - (LOCKOUT_DURATION * 100);
        attemptsByIP.put(ipAddress, attempt);
        System.out.println("🚫 [ADMIN] Permanently blocked IP: " + ipAddress);
    }
    
    /**
     * Admin function: Unblock an IP address
     */
    public static void unblockIP(String ipAddress) {
        attemptsByIP.remove(ipAddress);
        System.out.println("✅ [ADMIN] Unblocked IP: " + ipAddress);
    }
    
    /**
     * Check if an IP is permanently blocked
     */
    public static boolean isIPBlocked(String ipAddress) {
        LoginAttempt attempt = attemptsByIP.get(ipAddress);
        if (attempt != null && attempt.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            // Check if it's a permanent block (lockout time is very old)
            long elapsed = System.currentTimeMillis() - attempt.lockoutTime;
            if (elapsed > LOCKOUT_DURATION * 100) { // Permanent block marker
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get all blocked IPs
     */
    public static Map<String, LoginAttempt> getBlockedIPs() {
        Map<String, LoginAttempt> blocked = new HashMap<>();
        for (Map.Entry<String, LoginAttempt> entry : attemptsByIP.entrySet()) {
            if (entry.getValue().failedAttempts >= MAX_FAILED_ATTEMPTS) {
                blocked.put(entry.getKey(), entry.getValue());
            }
        }
        return blocked;
    }

    static class LoginAttempt {
        int failedAttempts = 0;
        long lockoutTime = 0;
    }
}
