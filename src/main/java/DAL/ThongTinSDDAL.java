package DAL;
import hibernate.entity.thongtinsd;
import hibernate.utils.HibernateUtil;
import org.apache.xmlbeans.impl.xpath.XQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThongTinSDDAL {
    static SessionFactory factory;
    public ThongTinSDDAL(){
        factory=HibernateUtil.getSessionFactory();
    }

    public int AddTTSD(thongtinsd ttsd){
        Session session=factory.openSession();
        int result=0;
        Transaction tx=null;
        try{
            tx=session.beginTransaction();
            session.save(ttsd);
            tx.commit();
            result=1;
        }catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally
            {
                session.close();
            }
        return result;
    }
    public int UpdateTTSD(thongtinsd ttsd){
        Session session=factory.openSession();
        Transaction tx=null;
        int result=0;
        try{
            tx = session.beginTransaction();
            session.update(ttsd);
            tx.commit();
            result = 1;
        }catch (Exception ex){
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }


    public List<thongtinsd> ReadTTSD(){
        Session session=factory.openSession();
        Transaction tx =null;
        List<thongtinsd>list=null;
        try{
            tx= session.beginTransaction();
            list=session.createQuery("FROM thongtinsd", thongtinsd.class).list();

        }catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return list;
    }
    public List<thongtinsd> readTVById(int matv){
        Session session=factory.openSession();
        List<thongtinsd>result =null;
        try{
            String hql="FROM thongtinsd WHERE MaTV= : maTV";
            Query<thongtinsd>query =session.createQuery(hql, thongtinsd.class);
            query.setParameter("maTV",matv);
            result=query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public int AutoCreate(){
        Session session=factory.openSession();
        Integer matt=null;
        try{
            String hql="SELECT MaTT FROM thongtinsd ORDER BY MaTT DESC";
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
    public thongtinsd UpdateMuon(int matv, int matb) {
        Session session = factory.openSession();
        thongtinsd obj = null;
        try {
            String hql = "FROM thongtinsd tt WHERE tt.MaTV = :maTV AND tt.MaTB = :maTB AND TGTra IS NULL";
            Query<thongtinsd> query = session.createQuery(hql, thongtinsd.class);
            query.setParameter("maTV", matv);
            query.setParameter("maTB", matb);
            obj = query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return obj;
    }
    public List<thongtinsd>GetThongKeTBDa(){
        Session session=factory.openSession();
        List<thongtinsd>list=null;
        try{
            String hql="FROM thongtinsd tt WHERE tt.MaTB IS NOT NULL AND tt.TGTra IS NOT NULL AND tt.TGMuon IS NOT NULL";

            Query<thongtinsd>query=session.createQuery(hql, thongtinsd.class);
            list=query.getResultList();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return list;
    }
    public List<thongtinsd>ThongKeTBDa(String tentb, String tentv, Date ngaymuon){
        Session session=factory.openSession();
        List<thongtinsd>list=new ArrayList<>();
        tentv="";
        tentb="";
        try{
            String hql="FROM thongtinsd tt JOIN thietbi tb ON tt.MaTB=tb.MaTB JOIN thanhvien tv ON tt.MaTV=tv.MaTV WHERE tt.MaTB IS NOT NULL AND tt.TGTra IS NOT NULL AND tt.TGMuon IS NOT NULL";
            if((!tentb.equals("") )){
                hql += " AND tb.TenTB LIKE CONCAT('%', :TTB, '%')";
            }
            if((!tentv.equals(""))){
                hql += " AND tv.HoTen LIKE CONCAT('%', :TTV, '%') ";
            }
            if(ngaymuon !=null){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String ngaymuonStr=sdf.format(ngaymuon);
                hql += " AND CAST(tt.TGMuon AS date) = :TGM";
            }

            Query<Object[]>query=session.createQuery(hql, Object[].class);
            if(!tentb.equals("")){query.setParameter("TTB", tentb);}
            if(!tentv.equals("")){query.setParameter("TTV",tentv);}
            if(ngaymuon !=null){
                java.sql.Date ngayMuonsql=new java.sql.Date(ngaymuon.getTime());
                query.setParameter("TGM",ngayMuonsql);
            }
            List<Object[]>results=query.getResultList();
            for(Object[] result : results){
                thongtinsd tt =(thongtinsd) result[0];
                list.add(tt);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return list;
    }
    public List<thongtinsd>GetThongKeTBDM(){
        Session session=factory.openSession();
        List<thongtinsd>list=null;
        try{
            String hql="FROM thongtinsd tt WHERE tt.MaTB IS NOT NULL AND tt.TGTra IS NULL AND tt.TGMuon IS NOT NULL";
            Query<thongtinsd>query=session.createQuery(hql, thongtinsd.class);
            list=query.getResultList();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return list;
    }

}
