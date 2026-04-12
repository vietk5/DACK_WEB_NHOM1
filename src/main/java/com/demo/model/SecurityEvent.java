package com.demo.model;

import java.time.LocalDateTime;

/**
 * Security Event model for SIEM dashboard
 * Author: Vũ Văn Thông
 * Date: 2026-04-12
 */
public class SecurityEvent {
    private LocalDateTime timestamp;
    private String eventType;
    private String level; // INFO, WARN, ERROR
    private String email;
    private String ipAddress;
    private String details;
    private String userAgent;

    public SecurityEvent() {}

    public SecurityEvent(LocalDateTime timestamp, String eventType, String level, 
                        String email, String ipAddress, String details) {
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.level = level;
        this.email = email;
        this.ipAddress = ipAddress;
        this.details = details;
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}
