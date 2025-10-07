/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.model;

import com.demo.enums.LoaiGiamGia;
import javax.persistence.*; import java.math.*;

import java.time.*;
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ma", unique = true)
    private String ma;
    @Column(name = "mo_ta")
    private String moTa;
    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;
    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;
    @Column(name = "trang_thai")
    private boolean trangThai = true;
    @Column(name = "so_luong_su_dung_toi_da")
    private Integer soLuongSuDungToiDa;
    @Enumerated(EnumType.STRING)
    @Column(name = "loai")
    private LoaiGiamGia loai;
    @Column(name = "ap_dung_giam_gia", precision = 15, scale = 2)
    private BigDecimal apDungGiamGia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String v) {
        this.ma = v;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String v) {
        this.moTa = v;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate v) {
        this.ngayBatDau = v;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate v) {
        this.ngayKetThuc = v;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean v) {
        this.trangThai = v;
    }

    public Integer getSoLuongSuDungToiDa() {
        return soLuongSuDungToiDa;
    }

    public void setSoLuongSuDungToiDa(Integer v) {
        this.soLuongSuDungToiDa = v;
    }

    public LoaiGiamGia getLoai() {
        return loai;
    }

    public void setLoai(LoaiGiamGia v) {
        this.loai = v;
    }

    public BigDecimal getApDungGiamGia() {
        return apDungGiamGia;
    }

    public void setApDungGiamGia(BigDecimal v) {
        this.apDungGiamGia = v;
    }
}
