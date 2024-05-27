package DAL;

import hibernate.entity.thanhvien;
import hibernate.entity.thietbi;
import hibernate.entity.thongtinsd;
import hibernate.utils.HibernateUtil;
import javassist.convert.TransformBefore;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThanhVienDAL {
    SessionFactory factory;

    public ThanhVienDAL(){
        factory = HibernateUtil.getSessionFactory();
    }

    public thanhvien GetById(int maTV) {
        Session session = factory.openSession();
        thanhvien obj = null;
        try {
            String hql = "FROM thanhvien WHERE MaTV = :maTV";
            Query<thanhvien> query = session.createQuery(hql, thanhvien.class);
            query.setParameter("maTV", maTV);
            obj = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return obj;
    }

    public int CreateThanhVien(thanhvien tv) {
        Session session = factory.openSession();
        int result = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(tv);
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

    public int UpdateThanhVien(thanhvien tv) {
        Session session = factory.openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            session.update(tv);
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

    public int DeleteThanhVien(thanhvien tv) {
        Session session = factory.openSession();
        Transaction tx = null;
        int result = 0;
        try {
            tx = session.beginTransaction();
            session.delete(tv);
            tx.commit();
            result = 1;
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

    public List<thanhvien> GetThanhVienToDelete(String maTV) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<thanhvien> list = null;
        try {
            tx = session.beginTransaction();
            list = session.createQuery("FROM thanhvien WHERE SUBSTRING(MaTV,3,2)= :maTV")
                    .setParameter("maTV",maTV)
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

    public String GetHoTenById(int thanhVien) {
        Session session = factory.openSession();
        String hoTen = null;
        try {
            String hql = "SELECT HoTen FROM thanhvien WHERE maTV = :maTV";
            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("maTV", thanhVien);
            hoTen = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return hoTen;
    }
    public List<String> GetAllThanhVien() {
        Session session = factory.openSession();
        List<String> formattedThanhVienList = new ArrayList<>(); // Use ArrayList for efficiency
        try {
            String hql = "SELECT tv.MaTV, tv.HoTen FROM thanhvien tv";
            Query<Object[]> query = session.createQuery(hql, Object[].class); // Use Object[] for multiple columns
            List<Object[]> resultList = query.getResultList();

            for (Object[] result : resultList) {
                Integer maTV = (Integer) result[0];  // Cast MaTV to Integer (assuming it's integer type)
                String hoTen = (String) result[1];    // Cast HoTen to String
                String formattedString = maTV + "_" + hoTen;
                formattedThanhVienList.add(formattedString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();  // Consider more specific exception handling
        } finally {
            session.close();
        }
        return formattedThanhVienList;
    }
    public List<thanhvien> ReadThanhVien(){
        Session session = factory.openSession();
        Transaction tx = null;
        List<thanhvien> list = null;
        try {
            tx = session.beginTransaction();
            list = session.createQuery("FROM thanhvien ", thanhvien.class).list();
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
    public long isMaTVExists(int maTV) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("SELECT COUNT(*) FROM thanhvien WHERE MaTV = :maTV");
            query.setParameter("maTV", maTV);

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
                    thanhvien obj = new thanhvien();
                    obj.setMaTV((int) row.getCell(0).getNumericCellValue());
                    obj.setHoTen(row.getCell(1).getStringCellValue());
                    obj.setKhoa(row.getCell(2).getStringCellValue());
                    obj.setNganh(row.getCell(3).getStringCellValue());
                    obj.setSDT((int)row.getCell(4).getNumericCellValue());
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
    public long Countvipham(int matv) {
        Session session = factory.openSession();
        Transaction tx = null;
        long dem = 0;
        try {
            String hql = "SELECT COUNT(*) FROM xuly WHERE TrangThaiXL = 1 AND (SoTien IS NULL OR HinhThucXL='Khóa thẻ 1 tháng và bồi thường') AND maTV = :maTV";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("maTV", matv);
            dem = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return dem;
    }
    public List<String> GetTenNganh(String tenKhoa) {
        Session session = factory.openSession();
        List<String> tenNganh = new ArrayList<>();
        try {
            String hql;
            if (tenKhoa.equals("")) {
                hql = "SELECT DISTINCT Nganh FROM thanhvien";
            } else {
                hql = "SELECT DISTINCT Nganh FROM thanhvien WHERE Khoa = :khoa";
            }
            Query<String> query = session.createQuery(hql, String.class);
            if (!tenKhoa.equals("")) {
                query.setParameter("khoa", tenKhoa);
            }
            tenNganh = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return tenNganh;
    }

    public List<String> GetTenKhoa(){
        Session session= factory.openSession();
        List<String> tenKhoa = new ArrayList<>();
        try {
            String hql = "SELECT DISTINCT Khoa FROM thanhvien";
            Query<String> query = session.createQuery(hql, String.class); // Specify the return type as String.class
            tenKhoa = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return tenKhoa;
    }

    public List<thanhvien> Search(String txt) {
        Session session = factory.openSession();
        List<thanhvien> result = null;
        try {
            String hql;
            Integer maThanhVien = null;
            try {
                maThanhVien = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
            }

            if (maThanhVien != null) {
                hql = "FROM thanhvien WHERE MaTV = :maThanhVien";
            } else {
                hql = "FROM thanhvien WHERE HoTen LIKE CONCAT('%', :txt, '%')";
            }

            Query<thanhvien> query = session.createQuery(hql, thanhvien.class);

            if (maThanhVien != null) {
                query.setParameter("maThanhVien", maThanhVien);
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

    public List<thongtinsd>SearchTK(String khoa, String nganh, Date ngayXem) {
        Session session = factory.openSession();
        List<thongtinsd> TKTV = new ArrayList<>();
        try {
            String hql = "FROM thongtinsd tt JOIN thanhvien tv ON tt.MaTV=tv.MaTV WHERE tt.MaTV IS NOT NULL";
            if (!khoa.equals("")) {
                hql += " AND tv.Khoa = :KHOA";
            }
            if (!nganh.equals("")) {
                hql += " AND tv.Nganh = :NGANH";
            }
            if (ngayXem != null) {
                // Chuyển đổi ngày sang chuỗi có định dạng ngày tháng năm để so sánh
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String ngayXemStr = sdf.format(ngayXem);
                // Sử dụng CAST trong HQL để chỉ lấy ngày, tháng, năm của TGVao
                hql += " AND CAST(tt.TGVao AS date) = :TGV";
            }
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            if (!khoa.equals("")) {
                query.setParameter("KHOA", khoa);
            }
            if (!nganh.equals("")) {
                query.setParameter("NGANH", nganh);
            }
            if (ngayXem != null) {
                // Chuyển đổi ngàyXem sang kiểu java.sql.Date
                java.sql.Date ngayXemSql = new java.sql.Date(ngayXem.getTime());
                query.setParameter("TGV", ngayXemSql);
            }
            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                thongtinsd tt = (thongtinsd) result[0];
                // Nếu bạn cần dữ liệu từ 'thanhvien', bạn có thể truy cập từ result[1]
                TKTV.add(tt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return TKTV;
    }






}

