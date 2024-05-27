/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;

import DAL.ThietBiDAL;
import hibernate.entity.thietbi;
import java.io.File;
import java.util.List;

/**
 *
 * @author ankho
 */
public class ThietBiBLL {
    ThietBiDAL tbDal;
    public ThietBiBLL() {
        tbDal = new ThietBiDAL();
    }
    
    public List GetAllThietBi() {
        return tbDal.ReadThietBi();
    }
    
    public int AddThietBi(thietbi tb) {
        return tbDal.CreateThietBi(tb);
    }
    
    public int EditThietBi(thietbi tb) {
        return tbDal.UpdateThietBi(tb);
    }
    
    public int RemoveThietBi(thietbi tb) {
        return tbDal.DeleteThietBi(tb);
    }
    
    public List<thietbi> GetThietBiToDelete(int maTb) {
        return tbDal.GetThietBiToDelete(maTb);
    }
    
    public List<thietbi> SearchThietBi(String txt) {
        return tbDal.Search(txt);
    }
    
    public int ImportExcel(File file) {
        return tbDal.ImporExcel(file);
    }
    
    public thietbi GetThietBiById(int maTb) {
        return tbDal.GetThietBiById(maTb);
    }
    
    public long CheckMaTBExists(int maTb) {
        return tbDal.isMaTBExists(maTb);
    }
    public String GetTenThietBiById(int matb){
        return tbDal.GetTenThietBiById(matb);
    }
    public List GetThietBiMuon(){
        return tbDal.GetThietBiMuon();
    }
    public List GetThietBiTra(int matv){
        return tbDal.GetThietBiTra(matv);
    }
}
