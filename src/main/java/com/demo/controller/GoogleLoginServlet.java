package com.demo.controller;

import com.demo.util.GoogleOAuthConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Google OAuth Login Initiator
 * Author: Vũ Văn Thông
 * Date: 2026-04-12
 */
@WebServlet(name = "GoogleLoginServlet", urlPatterns = {"/google-login"})
public class GoogleLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Check if Google OAuth is configured
        if (!GoogleOAuthConfig.isConfigured()) {
            req.setAttribute("error", "Google OAuth chưa được cấu hình. Vui lòng liên hệ admin.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }
        
        // Generate state token for CSRF protection
        String state = UUID.randomUUID().toString();
        HttpSession session = req.getSession(true);
        session.setAttribute("google_oauth_state", state);
        
        // Set session cookie properties for HTTPS
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setPath(req.getContextPath());
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(req.isSecure()); // true for HTTPS
        sessionCookie.setMaxAge(600); // 10 minutes
        resp.addCookie(sessionCookie);
        
        System.out.println("DEBUG: Generated state token: " + state);
        System.out.println("DEBUG: Session ID: " + session.getId());
        
        // Build Google OAuth URL
        String googleAuthUrl = GoogleOAuthConfig.AUTH_URL +
                "?client_id=" + URLEncoder.encode(GoogleOAuthConfig.CLIENT_ID, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(GoogleOAuthConfig.REDIRECT_URI, StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&scope=" + URLEncoder.encode(GoogleOAuthConfig.SCOPE, StandardCharsets.UTF_8) +
                "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) +
                "&access_type=online" +
                "&prompt=select_account";
        
        // Redirect to Google
        resp.sendRedirect(googleAuthUrl);
    }
}
