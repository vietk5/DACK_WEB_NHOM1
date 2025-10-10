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

    // --- MẬT KHẨU: dùng cùng tên cột như Admin/DAO ---
    @Column(name = "mat_khau_hash", length = 255)
    private String matKhauHash;

    /* ================== getter/setter ================== */

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

    public String getMatKhauHash() {
        return matKhauHash;
    }

    public void setMatKhauHash(String matKhauHash) {
        this.matKhauHash = matKhauHash;
    }

    /* ====== Các method “tương thích” với code cũ ======
       LoginServlet/DAO đôi khi gọi getMatKhau() hoặc getHoTen()
       => trả về dữ liệu đúng thay vì ném UnsupportedOperationException. */

    @Transient
    public String getMatKhau() {
        // DEV: trả plaintext đang lưu trong matKhauHash
        // PROD: nên đổi thành compare bằng BCrypt.checkpw(...)
        return this.matKhauHash;
    }

    public void setMatKhau(String rawPassword) {
        // DEV: lưu thẳng
        this.matKhauHash = rawPassword;
    }

    public String getHoTen() {
        // NguoiDung đã có field tên (ví dụ: ten); ánh xạ lại cho code cũ
        return this.getTen();
    }
}
