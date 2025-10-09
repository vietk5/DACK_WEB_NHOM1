/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.model.database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.demo.model.*;
import com.demo.persistence.JPAUtil;
import javax.persistence.Query;

public class SanPhamDB {
    public static List<SanPham> selectAllSanPham() {
        EntityManager em = JPAUtil.getEmFactory().createEntityManager();
        String qString = "SELECT s FROM SanPham s";  // dùng alias và tên field trong entity
        TypedQuery<SanPham> query = em.createQuery(qString, SanPham.class);
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public static void insert(SanPham sanPham) {
        EntityManager em = JPAUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();        
        try {
            em.persist(sanPham);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
    }
    public static void delete(SanPham sanPham) {
        EntityManager em = JPAUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();        
        try {
            em.remove(em.merge(sanPham));
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }       
    }
    public static boolean isProductExistById(Long id) {
        EntityManager em = JPAUtil.getEmFactory().createEntityManager();
        try {
            SanPham sanPham = em.find(SanPham.class, id);
            return sanPham != null; 
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            em.close();
        }
    }
    public static boolean updateSoLuongTonById(Long id) {
        EntityManager em = JPAUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        String qString = "UPDATE SanPham s "
                + "SET s.soLuongTon = s.soLuongTon + 1 "
                + "WHERE s.id = :id";
        
        Query query = em.createQuery(qString); 
        try {
        trans.begin();
        query.setParameter("id", id);
        int updatedCount = query.executeUpdate();
        trans.commit();
        return updatedCount > 0; 
        } catch (Exception e) {
            System.out.println(e);
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
            return false;

        } finally {
            em.close();
        }
    }
}
