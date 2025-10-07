/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "admin")
public class Admin extends NguoiDung {

    @Column(name = "tai_khoan", unique = true)
    private String taiKhoan;
    @Column(name = "mat_khau_hash")
    private String matKhauHash;

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String v) {
        this.taiKhoan = v;
    }

    public String getMatKhauHash() {
        return matKhauHash;
    }

    public void setMatKhauHash(String v) {
        this.matKhauHash = v;
    }
}
