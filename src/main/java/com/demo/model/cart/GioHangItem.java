/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.model.cart;
import javax.persistence.*;
import com.demo.model.SanPham;
@Entity
@Table(name = "gio_hang_item")
public class GioHangItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gio_hang_id")
    private GioHang gioHang;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "san_pham_id")
    private SanPham sanPham;
    @Column(name = "so_luong")
    private Integer soLuong = 1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GioHang getGioHang() {
        return gioHang;
    }

    public void setGioHang(GioHang v) {
        this.gioHang = v;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham v) {
        this.sanPham = v;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer v) {
        this.soLuong = v;
    }
}
