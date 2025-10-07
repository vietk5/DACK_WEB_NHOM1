/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.model;

import com.demo.enums.LoaiThanhVien;
import javax.persistence.*;

@Entity
@Table(name = "khach_hang")
public class KhachHang extends NguoiDung {

    @Enumerated(EnumType.STRING)
    @Column(name = "hang_thanh_vien")
    private LoaiThanhVien hangThanhVien = LoaiThanhVien.BAC;
    @OneToOne(mappedBy = "chuSoHuu", cascade = CascadeType.ALL, orphanRemoval = true)
    private com.demo.model.cart.GioHang gioHang;

    public LoaiThanhVien getHangThanhVien() {
        return hangThanhVien;
    }

    public void setHangThanhVien(LoaiThanhVien v) {
        this.hangThanhVien = v;
    }

    public com.demo.model.cart.GioHang getGioHang() {
        return gioHang;
    }

    public void setGioHang(com.demo.model.cart.GioHang g) {
        this.gioHang = g;
    }
}
