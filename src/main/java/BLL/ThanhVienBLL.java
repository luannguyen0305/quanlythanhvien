package BLL;

import DAL.ThanhVienDAL;
import hibernate.entity.thanhvien;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ThanhVienBLL {
    private ThanhVienDAL tvDal;
    public List<thanhvien>SearchThanhVien(String txt){
        return tvDal.Search(txt);
    }
    public List TenNganh(String tenKhoa){
        return tvDal.GetTenNganh(tenKhoa);
    }
    public   List TenKhoa(){
        return tvDal.GetTenKhoa();
    }

    public ThanhVienBLL(){
        tvDal = new ThanhVienDAL();
    }
    public String GetHoTenByID(int id) {
        return tvDal.GetHoTenById(id);
    }
    public List GetAllThanhVien(){
        return tvDal.ReadThanhVien();
    }
    public List<thanhvien>GetThanhVienToDelete(String matv){
        return tvDal.GetThanhVienToDelete(matv);
    }
    /*
    public List<thanhvien>GetAllId(){
        return tvDal.GetdsMa();
    }

     */
    public List GetAllThanhVien1(){
        return tvDal.GetAllThanhVien();
    }
    public int AddThanhVien(thanhvien tv){
        return tvDal.CreateThanhVien(tv);
    }
    public int EditThanhVien(thanhvien tv){
        return tvDal.UpdateThanhVien(tv);
    }
    public int RemoveThanhVien(thanhvien tv){
        return tvDal.DeleteThanhVien(tv);
    }
    public int ImportExcel(File file){
        return tvDal.ImporExcel(file);
    }
    public thanhvien getById(int maTV) {return tvDal.GetById(maTV);}

    public long CheckMaTTExists(int maTV){return tvDal.isMaTVExists(maTV);}
    public long checkViPham(int matv){
        return tvDal.Countvipham(matv);
    }
    public List searchTKTV(String a, String b, Date c){
        return tvDal.SearchTK(a,b,c);
    }
}
