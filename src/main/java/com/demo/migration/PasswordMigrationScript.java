package com.demo.migration;

import com.demo.model.Admin;
import com.demo.model.KhachHang;
import com.demo.persistence.JPAUtil;
import com.demo.util.PasswordUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Password migration script - Hash all plaintext passwords to BCrypt
 * Author: Vũ Văn Thông
 * Date: 2026-04-10
 * Fix: A07 - Migrate existing passwords to BCrypt
 * 
 * ⚠️ IMPORTANT: BACKUP DATABASE BEFORE RUNNING!
 * 
 * Usage:
 * mvn exec:java -Dexec.mainClass="com.demo.migration.PasswordMigrationScript"
 */
public class PasswordMigrationScript {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     PASSWORD MIGRATION TO BCRYPT - Vũ Văn Thông      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
        
        EntityManager em = null;
        try {
            em = JPAUtil.em();
            
            // Migrate customer passwords
            migrateCustomerPasswords(em);
            
            // Migrate admin passwords
            migrateAdminPasswords(em);
            
            System.out.println();
            System.out.println("🎉 Migration completed successfully!");
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("❌ Migration failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    private static void migrateCustomerPasswords(EntityManager em) {
        System.out.println("📋 Migrating customer passwords...");
        
        try {
            em.getTransaction().begin();
            
            List<KhachHang> customers = em.createQuery(
                "SELECT k FROM KhachHang k", KhachHang.class).getResultList();
            
            int total = customers.size();
            int migrated = 0;
            int skipped = 0;
            
            for (KhachHang kh : customers) {
                String plainPassword = kh.getMatKhau();
                
                // Skip if already hashed (BCrypt hashes start with $2a$)
                if (plainPassword != null && plainPassword.startsWith("$2a$")) {
                    System.out.println("   ⏭️  Already hashed: " + maskEmail(kh.getEmail()));
                    skipped++;
                    continue;
                }
                
                // Hash the password
                String hashedPassword = PasswordUtil.hashPassword(plainPassword);
                kh.setMatKhau(hashedPassword);
                em.merge(kh);
                migrated++;
                
                System.out.println("   ✅ Migrated: " + maskEmail(kh.getEmail()));
            }
            
            em.getTransaction().commit();
            
            System.out.println();
            System.out.println("   📊 Customer Summary:");
            System.out.println("      Total: " + total);
            System.out.println("      Migrated: " + migrated);
            System.out.println("      Skipped: " + skipped);
            System.out.println();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to migrate customer passwords", e);
        }
    }
    
    private static void migrateAdminPasswords(EntityManager em) {
        System.out.println("📋 Migrating admin passwords...");
        
        try {
            em.getTransaction().begin();
            
            List<Admin> admins = em.createQuery(
                "SELECT a FROM Admin a", Admin.class).getResultList();
            
            int total = admins.size();
            int migrated = 0;
            int skipped = 0;
            
            for (Admin admin : admins) {
                String plainPassword = admin.getMatKhau();
                
                // Skip if already hashed
                if (plainPassword != null && plainPassword.startsWith("$2a$")) {
                    System.out.println("   ⏭️  Already hashed: " + admin.getTaiKhoan());
                    skipped++;
                    continue;
                }
                
                // Hash the password
                String hashedPassword = PasswordUtil.hashPassword(plainPassword);
                admin.setMatKhau(hashedPassword);
                em.merge(admin);
                migrated++;
                
                System.out.println("   ✅ Migrated: " + admin.getTaiKhoan());
            }
            
            em.getTransaction().commit();
            
            System.out.println();
            System.out.println("   📊 Admin Summary:");
            System.out.println("      Total: " + total);
            System.out.println("      Migrated: " + migrated);
            System.out.println("      Skipped: " + skipped);
            System.out.println();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to migrate admin passwords", e);
        }
    }
    
    /**
     * Mask email for privacy (GDPR compliance)
     */
    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 2) {
            return "***@" + parts[1];
        }
        return username.substring(0, 2) + "***@" + parts[1];
    }
}
