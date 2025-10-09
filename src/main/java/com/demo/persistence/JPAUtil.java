/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.persistence;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("shopPU");
    
    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}
