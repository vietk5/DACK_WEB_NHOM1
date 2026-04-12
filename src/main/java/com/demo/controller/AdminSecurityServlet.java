package com.demo.controller;

import com.demo.model.SecurityEvent;
import com.demo.util.SecurityEventStore;
import com.demo.util.AccountLockoutManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * Admin SIEM Dashboard - Security Information and Event Management
 * Author: Vũ Văn Thông
 * Date: 2026-04-12
 */
@WebServlet(name = "AdminSecurityServlet", urlPatterns = {"/admin/security"})
public class AdminSecurityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Check admin authentication
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("IS_ADMIN") == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }
        
        String action = req.getParameter("action");
        
        if ("api".equals(action)) {
            handleApiRequest(req, resp);
            return;
        }
        
        if ("unlock".equals(action)) {
            handleUnlockAccount(req, resp);
            return;
        }
        
        if ("blockip".equals(action)) {
            handleBlockIP(req, resp);
            return;
        }
        
        if ("unblockip".equals(action)) {
            handleUnblockIP(req, resp);
            return;
        }
        
        // Get filter parameters
        String filterType = req.getParameter("type");
        String filterLevel = req.getParameter("level");
        String filterIP = req.getParameter("ip");
        int limit = 50;
        
        try {
            String limitParam = req.getParameter("limit");
            if (limitParam != null) {
                limit = Integer.parseInt(limitParam);
            }
        } catch (NumberFormatException e) {
            limit = 50;
        }
        
        // Get events
        List<SecurityEvent> events;
        
        if (filterType != null && !filterType.isEmpty()) {
            events = SecurityEventStore.getEventsByType(filterType);
        } else if (filterLevel != null && !filterLevel.isEmpty()) {
            events = SecurityEventStore.getEventsByLevel(filterLevel);
        } else if (filterIP != null && !filterIP.isEmpty()) {
            events = SecurityEventStore.getEventsByIP(filterIP);
        } else {
            events = SecurityEventStore.getRecentEvents(limit);
        }
        
        // Get statistics
        Map<String, Long> eventTypeStats = SecurityEventStore.getEventTypeStats();
        Map<String, Long> topSuspiciousIPs = SecurityEventStore.getTopSuspiciousIPs(10);
        Map<String, ?> blockedIPs = AccountLockoutManager.getBlockedIPs();
        
        // Calculate summary stats
        long totalEvents = SecurityEventStore.getAllEvents().size();
        long errorCount = SecurityEventStore.getEventsByLevel("ERROR").size();
        long warnCount = SecurityEventStore.getEventsByLevel("WARN").size();
        long infoCount = SecurityEventStore.getEventsByLevel("INFO").size();
        
        // Set attributes
        req.setAttribute("events", events);
        req.setAttribute("eventTypeStats", eventTypeStats);
        req.setAttribute("topSuspiciousIPs", topSuspiciousIPs);
        req.setAttribute("blockedIPs", blockedIPs);
        req.setAttribute("totalEvents", totalEvents);
        req.setAttribute("errorCount", errorCount);
        req.setAttribute("warnCount", warnCount);
        req.setAttribute("infoCount", infoCount);
        req.setAttribute("pageTitle", "Security Monitoring - SIEM Dashboard");
        
        req.getRequestDispatcher("/WEB-INF/views/admin/security.jsp").forward(req, resp);
    }
    
    /**
     * Handle unlock account request
     */
    private void handleUnlockAccount(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        String email = req.getParameter("email");
        String ip = req.getParameter("ip");
        
        if (email != null && !email.isEmpty()) {
            if (ip != null && !ip.isEmpty()) {
                AccountLockoutManager.unlockAccount(email, ip);
            } else {
                AccountLockoutManager.unlockAccountAll(email);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/security?success=unlocked");
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/security?error=invalid");
        }
    }
    
    /**
     * Handle block IP request
     */
    private void handleBlockIP(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        String ip = req.getParameter("ip");
        
        if (ip != null && !ip.isEmpty()) {
            AccountLockoutManager.blockIP(ip);
            resp.sendRedirect(req.getContextPath() + "/admin/security?success=blocked");
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/security?error=invalid");
        }
    }
    
    /**
     * Handle unblock IP request
     */
    private void handleUnblockIP(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        String ip = req.getParameter("ip");
        
        if (ip != null && !ip.isEmpty()) {
            AccountLockoutManager.unblockIP(ip);
            resp.sendRedirect(req.getContextPath() + "/admin/security?success=unblocked");
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/security?error=invalid");
        }
    }
    
    /**
     * Handle API requests for real-time updates
     */
    private void handleApiRequest(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        String type = req.getParameter("data");
        
        if ("stats".equals(type)) {
            // Return statistics as JSON
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalEvents", SecurityEventStore.getAllEvents().size());
            stats.put("errorCount", SecurityEventStore.getEventsByLevel("ERROR").size());
            stats.put("warnCount", SecurityEventStore.getEventsByLevel("WARN").size());
            stats.put("infoCount", SecurityEventStore.getEventsByLevel("INFO").size());
            stats.put("eventTypeStats", SecurityEventStore.getEventTypeStats());
            stats.put("topIPs", SecurityEventStore.getTopSuspiciousIPs(5));
            
            resp.getWriter().write(convertToJson(stats));
        } else if ("recent".equals(type)) {
            // Return recent events
            List<SecurityEvent> events = SecurityEventStore.getRecentEvents(20);
            resp.getWriter().write(convertEventsToJson(events));
        }
    }
    
    /**
     * Simple JSON converter (for basic data)
     */
    private String convertToJson(Map<String, Object> data) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Map) {
                json.append(convertMapToJson((Map<?, ?>) value));
            } else {
                json.append(value);
            }
            first = false;
        }
        
        json.append("}");
        return json.toString();
    }
    
    private String convertMapToJson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            first = false;
        }
        
        json.append("}");
        return json.toString();
    }
    
    private String convertEventsToJson(List<SecurityEvent> events) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        for (SecurityEvent event : events) {
            if (!first) json.append(",");
            json.append("{")
                .append("\"timestamp\":\"").append(event.getTimestamp()).append("\",")
                .append("\"eventType\":\"").append(event.getEventType()).append("\",")
                .append("\"level\":\"").append(event.getLevel()).append("\",")
                .append("\"email\":\"").append(event.getEmail()).append("\",")
                .append("\"ipAddress\":\"").append(event.getIpAddress()).append("\",")
                .append("\"details\":\"").append(event.getDetails()).append("\"")
                .append("}");
            first = false;
        }
        
        json.append("]");
        return json.toString();
    }
}
