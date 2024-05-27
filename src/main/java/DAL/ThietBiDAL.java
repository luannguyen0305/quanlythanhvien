/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

/**
 *
 * @author ankho
 */
import hibernate.entity.thietbi;
import hibernate.utils.HibernateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;

public class ThietBiDAL {

    static SessionFactory factory;

    // Constructor
    public ThietBiDAL() {
        factory = HibernateUtil.getSessionFactory();
    }

    public List<thietbi> ReadThietBi() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<thietbi> list = null;
        try {
            tx = session.beginTransaction();
            list = session.createQuery("FROM thietbi ", thietbi.class).list();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    public int CreateThietBi(thietbi tb) {
        Session session = factory.openSession();
        int result = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(tb);
            tx.commit();
            result = 1;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    public int UpdateThietBi(thietbi tb) {
        Session session = factory.openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            session.update(tb);
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

    public int DeleteThietBi(thietbi tb) {
        Session session = factory.openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            session.delete(tb);
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

    public List<thietbi> GetThietBiToDelete(int maTb) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<thietbi> list = null;
        try {
            tx = session.beginTransaction();
            // Lấy danh sách các thiết bị có số đầu bằng maTb
            list = session.createCriteria(thietbi.class)
                    .add(Restrictions.sqlRestriction("SUBSTRING(MaTB, 1, 1) = ?", maTb, IntegerType.INSTANCE))
                    .list();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    public thietbi GetThietBiById(int maTb) {
        Session session = factory.openSession();
        thietbi obj = null;
        try {
            String hql = "FROM thietbi WHERE MaTb = :maTb";
            Query<thietbi> query = session.createQuery(hql, thietbi.class);
            query.setParameter("maTb", maTb);
            obj = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return obj;
    }

    public List<thietbi> Search(String txt) {
        Session session = factory.openSession();
        List<thietbi> result = null;
        try {
            String hql;
            Integer maThietBi = null;
            try {
                maThietBi = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                // Do nothing, txt không thể chuyển đổi thành Integer, vẫn giữ giá trị null cho maThietBi
            }

            if (maThietBi != null) {
                hql = "FROM thietbi WHERE MaTB = :maThietBi";
            } else {
                hql = "FROM thietbi WHERE TenTB LIKE CONCAT('%', :txt, '%')";
            }

            Query<thietbi> query = session.createQuery(hql, thietbi.class);

            if (maThietBi != null) {
                query.setParameter("maThietBi", maThietBi);
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

    public long isMaTBExists(int maTB) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Tạo truy vấn để kiểm tra xem mã có tồn tại trong cơ sở dữ liệu hay không
            Query query = session.createQuery("SELECT COUNT(*) FROM thietbi WHERE MaTB = :maTB");
            query.setParameter("maTB", maTB);

            // Lấy kết quả của truy vấn
            long count = (long) query.uniqueResult();

            // Trả về số lượng bản ghi được tìm thấy
            return count;
        } finally {
            session.close();
        }
    }

    public int ImporExcel(File file) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        int result = 0;
        try {
            try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0); // assuming first sheet is the one to import

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue; // Skip empty rows
                    }
                    thietbi obj = new thietbi();
                    obj.setMaTB((int) row.getCell(0).getNumericCellValue());
                    obj.setTenTB(row.getCell(1).getStringCellValue());
                    obj.setMoTaTB(row.getCell(2).getStringCellValue());
                    session.save(obj);
                    result++;
                }

                tx.commit();
            } catch (IOException e) {
                e.printStackTrace();
                tx.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
    public String GetTenThietBiById(int matb){
        Session session=factory.openSession();
        String tentb=null;
        try{
            String hql="SELECT TenTB FROM thietbi WHERE MaTB =:MaTB";
            Query<String>query= session.createQuery(hql,String.class);
            query.setParameter("MaTB",matb);
            tentb=query.uniqueResult();
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return tentb;
    }
    public List<String>GetThietBiMuon(){
        Session session=factory.openSession();
        List<String> tenTBList=new ArrayList<>();
        try{
            String hql="SELECT tb.MaTB,tb.TenTB FROM thietbi tb WHERE NOT EXISTS (SELECT tt.MaTB FROM thongtinsd tt WHERE tb.MaTB= tt.MaTB AND TGTra IS NULL)";

            Query<Object[]>query = session.createQuery(hql,Object[].class);
            List<Object[]>resultList=query.getResultList();
            for(Object[] result: resultList){
                Integer maTB=(Integer)result[0];
                String tenTB=(String)result[1];
                String ma_ten=maTB+"_"+tenTB;
                tenTBList.add(ma_ten);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return tenTBList;
    }
    public List<String>GetThietBiTra(int matv){
        Session session=factory.openSession();
        List<String> tenTBList=new ArrayList<>();
        try{
            String hql="SELECT tb.MaTB, tb.TenTB FROM thietbi tb WHERE EXISTS ( SELECT tt.MaTB FROM thongtinsd tt WHERE tb.MaTB=tt.MaTB AND tt.TGTra IS NULL AND tt.TGMuon IS NOT NULL AND tt.MaTV = :maTV)" ;

            Query<Object[]>query = session.createQuery(hql,Object[].class);
            query.setParameter("maTV", matv);
            List<Object[]>resultList=query.getResultList();
            for(Object[] result: resultList){
                Integer maTB=(Integer)result[0];
                String tenTB=(String)result[1];
                String ma_ten=maTB+"_"+tenTB;
                tenTBList.add(ma_ten);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return tenTBList;
    }

}
