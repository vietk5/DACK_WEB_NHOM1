package com.demo.controller;

import com.demo.model.KhachHang;
import com.demo.model.cart.GioHang;
import com.demo.model.cart.GioHangItem;
import com.demo.model.session.SessionUser;
import com.demo.persistence.GioHangDAO;
import com.demo.persistence.KhachHangDAO;
import com.demo.util.GoogleOAuthConfig;
import com.demo.util.SecurityLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Google OAuth Callback Handler
 * Author: Vũ Văn Thông
 * Date: 2026-04-12
 */
@WebServlet(name = "GoogleCallbackServlet", urlPatterns = {"/google-callback"})
public class GoogleCallbackServlet extends HttpServlet {

    private final KhachHangDAO khDAO = new KhachHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String code = req.getParameter("code");
        String state = req.getParameter("state");
        String error = req.getParameter("error");
        String clientIP = req.getRemoteAddr();
        
        HttpSession session = req.getSession(false);
        
        // Debug logging
        System.out.println("DEBUG Callback - Received state: " + state);
        System.out.println("DEBUG Callback - Session exists: " + (session != null));
        if (session != null) {
            System.out.println("DEBUG Callback - Session ID: " + session.getId());
            System.out.println("DEBUG Callback - Stored state: " + session.getAttribute("google_oauth_state"));
        }
        
        // Check for errors
        if (error != null) {
            req.setAttribute("error", "Đăng nhập Google thất bại: " + error);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }
        
        // Verify state token (CSRF protection)
        // If session is null, try to get it with create=true as fallback
        if (session == null) {
            session = req.getSession(true);
            System.out.println("DEBUG: Session was null, created new session");
        }
        
        String storedState = (String) session.getAttribute("google_oauth_state");
        
        if (state == null || storedState == null || !state.equals(storedState)) {
            String errorMsg = "Invalid state token. ";
            if (session == null) errorMsg += "Session is null. ";
            if (state == null) errorMsg += "Received state is null. ";
            if (storedState == null) errorMsg += "Stored state is null. ";
            
            System.err.println("CSRF Error: " + errorMsg);
            req.setAttribute("error", errorMsg + "Vui lòng thử lại.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }
        
        try {
            // Exchange code for access token
            String accessToken = exchangeCodeForToken(code);
            
            // Get user info from Google
            Map<String, String> userInfo = getUserInfo(accessToken);
            
            String email = userInfo.get("email");
            String name = userInfo.get("name");
            String googleId = userInfo.get("id");
            
            if (email == null || email.isEmpty()) {
                req.setAttribute("error", "Không thể lấy thông tin email từ Google");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }
            
            // Find or create user
            Optional<KhachHang> kOpt = khDAO.findByEmail(email);
            KhachHang k;
            
            if (kOpt.isPresent()) {
                // Existing user
                k = kOpt.get();
                SecurityLogger.logLoginSuccess(email, clientIP);
            } else {
                // Create new user
                k = new KhachHang();
                k.setEmail(email);
                k.setTen(name);
                // Set a random password (user won't use it, they'll login via Google)
                k.setMatKhau(UUID.randomUUID().toString());
                
                khDAO.save(k);
                SecurityLogger.logRegistration(email, clientIP);
            }
            
            // Create session
            SessionUser su = new SessionUser(k.getId(), name != null ? name : email, k.getEmail(), false);
            session = req.getSession(true);
            session.setAttribute("user", su);
            session.setAttribute("IS_ADMIN", false);
            session.removeAttribute("google_oauth_state");
            
            // Load cart from database
            GioHangDAO gioHangDAO = new GioHangDAO();
            List<GioHangItem> dbCart = gioHangDAO.loadCartAfterLogin(k.getId());
            if (!dbCart.isEmpty()) {
                session.setAttribute("cart", dbCart);
            }
            
            // Redirect to home
            resp.sendRedirect(req.getContextPath() + "/home");
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi khi xử lý đăng nhập Google: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
    
    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String code) throws IOException {
        URL url = new URL(GoogleOAuthConfig.TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        String params = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(GoogleOAuthConfig.CLIENT_ID, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(GoogleOAuthConfig.CLIENT_SECRET, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(GoogleOAuthConfig.REDIRECT_URI, StandardCharsets.UTF_8) +
                "&grant_type=authorization_code";
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to get access token. Response code: " + responseCode);
        }
        
        String response = readResponse(conn.getInputStream());
        
        // Parse JSON response (simple parsing)
        String accessToken = extractJsonValue(response, "access_token");
        if (accessToken == null) {
            throw new IOException("No access token in response");
        }
        
        return accessToken;
    }
    
    /**
     * Get user info from Google
     */
    private Map<String, String> getUserInfo(String accessToken) throws IOException {
        URL url = new URL(GoogleOAuthConfig.USER_INFO_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to get user info. Response code: " + responseCode);
        }
        
        String response = readResponse(conn.getInputStream());
        
        // Parse user info
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", extractJsonValue(response, "id"));
        userInfo.put("email", extractJsonValue(response, "email"));
        userInfo.put("name", extractJsonValue(response, "name"));
        userInfo.put("picture", extractJsonValue(response, "picture"));
        
        return userInfo;
    }
    
    /**
     * Read HTTP response
     */
    private String readResponse(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
    
    /**
     * Simple JSON value extractor
     */
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;
        
        startIndex = json.indexOf(":", startIndex) + 1;
        startIndex = json.indexOf("\"", startIndex) + 1;
        int endIndex = json.indexOf("\"", startIndex);
        
        if (endIndex == -1) return null;
        return json.substring(startIndex, endIndex);
    }
}
