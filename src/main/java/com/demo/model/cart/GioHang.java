/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.model.cart;

import com.demo.model.KhachHang;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "gio_hang")
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang chuSoHuu;
    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GioHangItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KhachHang getChuSoHuu() {
        return chuSoHuu;
    }

    public void setChuSoHuu(KhachHang v) {
        this.chuSoHuu = v;
    }

    public List<GioHangItem> getItems() {
        return items;
    }

    public void setItems(List<GioHangItem> v) {
        this.items = v;
    }
}
