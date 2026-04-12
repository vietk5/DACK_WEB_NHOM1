package com.demo.util;

import com.demo.model.SecurityEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * In-memory store for security events (SIEM)
 * Author: Vũ Văn Thông
 * Date: 2026-04-12
 */
public class SecurityEventStore {
    private static final int MAX_EVENTS = 1000;
    private static final Queue<SecurityEvent> events = new ConcurrentLinkedQueue<>();
    
    /**
     * Add a security event
     */
    public static void addEvent(String eventType, String level, String email, 
                               String ipAddress, String details) {
        SecurityEvent event = new SecurityEvent(
            LocalDateTime.now(),
            eventType,
            level,
            email,
            ipAddress,
            details
        );
        
        events.offer(event);
        
        // Keep only last MAX_EVENTS
        while (events.size() > MAX_EVENTS) {
            events.poll();
        }
    }
    
    /**
     * Get all events
     */
    public static List<SecurityEvent> getAllEvents() {
        return new ArrayList<>(events);
    }
    
    /**
     * Get recent events (last N)
     */
    public static List<SecurityEvent> getRecentEvents(int limit) {
        List<SecurityEvent> allEvents = new ArrayList<>(events);
        Collections.reverse(allEvents);
        return allEvents.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Get events by type
     */
    public static List<SecurityEvent> getEventsByType(String eventType) {
        return events.stream()
                .filter(e -> e.getEventType().equals(eventType))
                .collect(Collectors.toList());
    }
    
    /**
     * Get events by level
     */
    public static List<SecurityEvent> getEventsByLevel(String level) {
        return events.stream()
                .filter(e -> e.getLevel().equals(level))
                .collect(Collectors.toList());
    }
    
    /**
     * Get events by IP address
     */
    public static List<SecurityEvent> getEventsByIP(String ipAddress) {
        return events.stream()
                .filter(e -> e.getIpAddress().equals(ipAddress))
                .collect(Collectors.toList());
    }
    
    /**
     * Get events in time range
     */
    public static List<SecurityEvent> getEventsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return events.stream()
                .filter(e -> e.getTimestamp().isAfter(start) && e.getTimestamp().isBefore(end))
                .collect(Collectors.toList());
    }
    
    /**
     * Get statistics
     */
    public static Map<String, Long> getEventTypeStats() {
        return events.stream()
                .collect(Collectors.groupingBy(
                    SecurityEvent::getEventType,
                    Collectors.counting()
                ));
    }
    
    /**
     * Get top suspicious IPs
     */
    public static Map<String, Long> getTopSuspiciousIPs(int limit) {
        return events.stream()
                .filter(e -> e.getLevel().equals("WARN") || e.getLevel().equals("ERROR"))
                .collect(Collectors.groupingBy(
                    SecurityEvent::getIpAddress,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }
    
    /**
     * Clear all events
     */
    public static void clearEvents() {
        events.clear();
    }
}
