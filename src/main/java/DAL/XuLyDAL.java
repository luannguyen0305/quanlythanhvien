package DAL;
import hibernate.entity.thietbi;
import hibernate.entity.xuly;
import hibernate.utils.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class XuLyDAL {
    static SessionFactory factory;

    public XuLyDAL(){ factory = HibernateUtil.getSessionFactory();}

    public  List<xuly> ReadXuLy() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<xuly> list = null;
        try {
            tx = session.beginTransaction();
            list = session.createQuery("FROM xuly WHERE TrangThaiXL = 1 ", xuly.class).list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }
    public int CreateXuLy(xuly xl){
        Session session = factory.openSession();
        int result =0;
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(xl);
            tx.commit();
            result =1;
        }catch (HibernateException e){
            if (tx !=null){
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;

    }
    public int UpdateXuLy(xuly xl) {
        Session session = factory.openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            session.update(xl);
            tx.commit();
            result = 1; // Nếu không có ngoại lệ, cập nhật thành công
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
    public int DeleteXuLy(xuly xl) {
        Session session = factory.openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            session.delete(xl);
            tx.commit();
            result = 1; // Nếu không có ngoại lệ, xóa thành công
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
    public xuly GetXuLyById(int maXl) {
        Session session = factory.openSession();
        xuly obj = null;
        try {
            String hql = "FROM xuly WHERE TrangThaiXL = 1 AND MaXL = :maXl";
            Query<xuly> query = session.createQuery(hql, xuly.class);
            query.setParameter("maXl", maXl);
            obj = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return obj;
    }
    public List<xuly> Search(String txt) {
        Session session = factory.openSession();
        List<xuly> result = null;
        try {
            String hql;
            Integer maXuLy = null;
            try {
                maXuLy = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                // Do nothing, txt không thể chuyển đổi thành Integer, vẫn giữ giá trị null cho maThietBi
            }

            if (maXuLy != null) {
                hql = "FROM xuly WHERE TrangThaiXL =1 AND MaXL = :maXuLy";
            } else {
                hql = "SELECT xl FROM xuly xl JOIN thanhvien tv ON tv.MaTV = xl.MaTV WHERE xl.TrangThaiXL = 1 AND tv.HoTen LIKE CONCAT('%', :txt, '%')";
            }

            Query<xuly> query = session.createQuery(hql, xuly.class);

            if (maXuLy != null) {
                query.setParameter("maXuLy", maXuLy);
            } else {
                query.setParameter("txt", txt);
            }

            result = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
    public int AutoCreate(){
        Session session=factory.openSession();Integer matt=null;
        try{
            String hql="SELECT MaXL FROM xuly ORDER BY MaXL DESC";
            Query<Integer>query=session.createQuery(hql,Integer.class);
            query.setMaxResults(1);
            matt=query.uniqueResult();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return matt;
    }
    public int TTBT(int matv) {
        Session session = factory.openSession();
        int tongSoTien = 0;
        try {
            String hql = "SELECT SUM(xl.SoTien) FROM xuly xl WHERE xl.MaTV = :matv AND xl.TrangThaiXL = 0";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("matv", matv);
            Long tongSoTienLong = query.uniqueResult();
            if (tongSoTienLong != null) {
                tongSoTien = tongSoTienLong.intValue();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return tongSoTien;
    }
    public long DemVP(int matv) {
        Session session = factory.openSession();
        Transaction tx=null;
        long soDong = 0;
        try {
            String hql = "SELECT COUNT(*) FROM xuly WHERE MaTV = :matv AND TrangThaiXL = 0";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("matv", matv);
            soDong = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return soDong;
    }
    public  List<xuly> TKXuLy() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<xuly> list = null;
        try {
            tx = session.beginTransaction();
            list = session.createQuery("FROM xuly WHERE TrangThaiXL = 0 ", xuly.class).list();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }
}
