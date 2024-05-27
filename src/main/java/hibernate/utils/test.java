package hibernate.utils;

import hibernate.entity.thanhvien;
import org.hibernate.Session;

import java.util.List;

public class test {
    public static void main(String[] args) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            List<thanhvien> thanhvienList = session.createQuery("FROM thanhvien ", thanhvien.class).list();

            thanhvienList.forEach(System.out::println);

            thanhvien tv = new thanhvien();
            tv.setMaTV(1120150185);
            tv.setHoTen("ten test");
            tv.setKhoa("khoa test");
            tv.setNganh("nghanh test");
            tv.setSDT(123456788);
            session.save(tv);
            session.getTransaction().commit();

            thanhvien tv_update = session.get(thanhvien.class, 1120150185);
            tv_update.setHoTen("ten update");
            session.save(tv_update);
            session.getTransaction().commit();

            
            thanhvien tv_delete = session.get(thanhvien.class, 1120150185);
            session.delete(tv_delete);
            session.getTransaction().commit();


        }
    }
}
