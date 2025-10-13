package com.demo.persistence;

import com.demo.model.SanPham;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanPhamDAO extends GenericDAO<SanPham, Long> {
    public SanPhamDAO() { super(SanPham.class); }

    // Tìm theo từ khóa tên
    public Page<SanPham> search(String keyword, int page, int size) {
        String where = "lower(e.tenSanPham) like :kw";
        Map<String,Object> p = new HashMap<>();
        p.put("kw", "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%");
        return findWhere(where, p, page, size, "id", false);
    }

    // Lấy sản phẩm liên quan theo loại
    public List<SanPham> relatedByLoai(long loaiId, long excludeId, int limit) {
        List<SanPham> list = findWhere("e.loai.id = :lid and e.id <> :ex",
                Map.of("lid", loaiId, "ex", excludeId));
        return list.size() > limit ? list.subList(0, limit) : list;
    }

    // Tìm kiếm nâng cao với nhiều điều kiện
    public Page<SanPham> searchAdvanced(String keyword, String brand, String category, 
                                        BigDecimal minPrice, BigDecimal maxPrice,
                                        int page, int size, String sortBy, boolean asc) {
        StringBuilder where = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        
        // Tìm theo keyword trong tên sản phẩm
        if (keyword != null && !keyword.trim().isEmpty()) {
            where.append(" AND lower(e.tenSanPham) like :kw");
            params.put("kw", "%" + keyword.toLowerCase().trim() + "%");
        }
        
        // Lọc theo thương hiệu
        if (brand != null && !brand.trim().isEmpty()) {
            where.append(" AND lower(e.thuongHieu.tenThuongHieu) = :brand");
            params.put("brand", brand.toLowerCase().trim());
        }
        
        // Lọc theo loại sản phẩm
        if (category != null && !category.trim().isEmpty()) {
            where.append(" AND lower(e.loai.tenLoai) = :cat");
            params.put("cat", category.toLowerCase().trim());
        }
        
        // Lọc theo khoảng giá
        if (minPrice != null) {
            where.append(" AND e.gia >= :minPrice");
            params.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            where.append(" AND e.gia <= :maxPrice");
            params.put("maxPrice", maxPrice);
        }
        
        // Chỉ hiển thị sản phẩm còn hàng
        where.append(" AND e.soLuongTon > 0");
        
        String orderBy = (sortBy == null || sortBy.isEmpty()) ? "id" : sortBy;
        return findWhere(where.toString(), params, page, size, orderBy, asc);
    }
    
    // Lấy danh sách tất cả thương hiệu
    public List<String> getAllBrands() {
        return inTransaction(em -> {
            return em.createQuery(
                "SELECT DISTINCT th.tenThuongHieu FROM ThuongHieu th ORDER BY th.tenThuongHieu",
                String.class
            ).getResultList();
        });
    }
    
    // Lấy danh sách tất cả loại sản phẩm
    public List<String> getAllCategories() {
        return inTransaction(em -> {
            return em.createQuery(
                "SELECT DISTINCT l.tenLoai FROM LoaiSanPham l ORDER BY l.tenLoai",
                String.class
            ).getResultList();
        });
    }
}
