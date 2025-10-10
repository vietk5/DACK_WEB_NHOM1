package com.demo.persistence;

import com.demo.model.SanPham;
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
}
