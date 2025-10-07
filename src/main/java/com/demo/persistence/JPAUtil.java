/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.persistence;


import javax.persistence.*;
public final class JPAUtil {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("shopPU");

    private JPAUtil() {
    }

    public static EntityManager em() {
        return EMF.createEntityManager();
    }
}
