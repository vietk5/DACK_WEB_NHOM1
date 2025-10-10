package com.demo.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Admin kế thừa NguoiDung.
 * - Lưu username (taiKhoan) và mật khẩu (matKhauHash).
 * - Cung cấp các phương thức tương thích với code cũ:
 *   getMatKhau(), setMatKhau() và getIdAdmin().
 */
@Entity
@Table(name = "admin")
public class Admin extends NguoiDung implements Serializable {

    @Column(name = "tai_khoan", unique = true, length = 100)
    private String taiKhoan;

    @Column(name = "mat_khau_hash", length = 255)
    private String matKhauHash;

    public String getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(String v) { this.taiKhoan = v; }

    public String getMatKhauHash() { return matKhauHash; }
    public void setMatKhauHash(String v) { this.matKhauHash = v; }

    /* ----------------- Tương thích LoginServlet ----------------- */

    /** Dùng trong LoginServlet: hiện tại trả về chuỗi mật khẩu (dev: plaintext).
     *  Sau này bạn có thể đổi thành trả về hash và so sánh bằng BCrypt. */
    @Transient
    public String getMatKhau() {
        return this.matKhauHash;
    }

    /** Dùng trong LoginServlet: đặt mật khẩu (lưu vào trường matKhauHash). */
    public void setMatKhau(String matKhau) {
        this.matKhauHash = matKhau;  // dev: lưu thẳng; production nên băm
    }

    /** Trả về id admin để tương thích với code cũ.
     *  NguoiDung nên có getId(). Nếu base class là getIdNguoiDung() thì đổi lại. */
    @Transient
    public Long getIdAdmin() {
        return super.getId();   // <--- nếu NguoiDung dùng tên khác, đổi thành super.getIdNguoiDung()
    }
}
