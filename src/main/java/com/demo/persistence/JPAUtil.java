package com.demo.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("shopPU");

    private JPAUtil() { }

    /** Lấy EntityManager mới cho mỗi lần gọi */
    public static EntityManager em() {
        return EMF.createEntityManager();
    }

    /** Nếu nơi khác cần dùng EMF trực tiếp */
    public static EntityManagerFactory getEmFactory() {
        return EMF;
    }

    /** Đóng EMF khi ứng dụng dừng (tùy chọn) */
    public static void close() {
        if (EMF != null && EMF.isOpen()) EMF.close();
    }
}
