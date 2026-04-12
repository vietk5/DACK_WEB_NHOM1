package com.demo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Google OAuth 2.0 Configuration
 * Author: Vũ Văn Thông
 * Date: 2026-04-12
 * 
 * Setup Instructions:
 * 1. Go to https://console.cloud.google.com/
 * 2. Create a new project or select existing
 * 3. Enable Google+ API
 * 4. Create OAuth 2.0 credentials (Web application)
 * 5. Add authorized redirect URI: http://localhost:8080/DACK_WEB_NHOM1/google-callback
 * 6. Copy google-oauth.properties.template to google-oauth.properties
 * 7. Fill in your Client ID and Client Secret in google-oauth.properties
 */
public class GoogleOAuthConfig {
    
    private static final Properties properties = new Properties();
    
    static {
        try {
            // Try to load from file system first (for development)
            try (InputStream input = new FileInputStream("google-oauth.properties")) {
                properties.load(input);
            } catch (IOException e) {
                // If not found, try classpath (for production)
                try (InputStream input = GoogleOAuthConfig.class.getClassLoader()
                        .getResourceAsStream("google-oauth.properties")) {
                    if (input != null) {
                        properties.load(input);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load google-oauth.properties. Google OAuth will not work.");
            e.printStackTrace();
        }
    }
    
    // Load from properties file
    public static final String CLIENT_ID = properties.getProperty("google.client.id", "");
    public static final String CLIENT_SECRET = properties.getProperty("google.client.secret", "");
    public static final String REDIRECT_URI = properties.getProperty("google.redirect.uri", "http://localhost:8080/google-callback");
    
    // Google OAuth endpoints
    public static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    // Scopes
    public static final String SCOPE = "openid email profile";
    
    /**
     * Check if Google OAuth is configured
     */
    public static boolean isConfigured() {
        return CLIENT_ID != null && !CLIENT_ID.isEmpty() 
            && CLIENT_SECRET != null && !CLIENT_SECRET.isEmpty()
            && !CLIENT_ID.startsWith("YOUR_");
    }
}
