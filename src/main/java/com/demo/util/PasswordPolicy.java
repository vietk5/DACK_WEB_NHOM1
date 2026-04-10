package com.demo.util;

import java.util.regex.Pattern;

/**
 * Password policy validator
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A07 - Password complexity requirements
 */
public class PasswordPolicy {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

    /**
     * Validate password against policy
     * Requirements:
     * - Min 8 characters
     * - Max 128 characters
     * - At least 1 uppercase letter
     * - At least 1 lowercase letter
     * - At least 1 digit
     * - At least 1 special character
     */
    public static ValidationResult validate(String password) {
        if (password == null || password.isEmpty()) {
            return ValidationResult.error("Mật khẩu không được để trống");
        }
        
        if (password.length() < MIN_LENGTH) {
            return ValidationResult.error("Mật khẩu phải có ít nhất " + MIN_LENGTH + " ký tự");
        }
        
        if (password.length() > MAX_LENGTH) {
            return ValidationResult.error("Mật khẩu không được vượt quá " + MAX_LENGTH + " ký tự");
        }
        
        if (!UPPERCASE.matcher(password).matches()) {
            return ValidationResult.error("Mật khẩu phải có ít nhất 1 chữ hoa");
        }
        
        if (!LOWERCASE.matcher(password).matches()) {
            return ValidationResult.error("Mật khẩu phải có ít nhất 1 chữ thường");
        }
        
        if (!DIGIT.matcher(password).matches()) {
            return ValidationResult.error("Mật khẩu phải có ít nhất 1 chữ số");
        }
        
        if (!SPECIAL.matcher(password).matches()) {
            return ValidationResult.error("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
        }
        
        return ValidationResult.success();
    }
}
