package com.demo.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password hashing and verification utility using BCrypt
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A07 - BCrypt password hashing (12 rounds)
 */
public class PasswordUtil {
    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Hash a plaintext password using BCrypt
     * @param plainPassword The plaintext password
     * @return BCrypt hashed password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify a plaintext password against a BCrypt hash
     * @param plainPassword The plaintext password
     * @param hashedPassword The BCrypt hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
