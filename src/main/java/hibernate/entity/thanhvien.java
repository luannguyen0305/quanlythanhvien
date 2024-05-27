package hibernate.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "thanhvien")
@Data
public class thanhvien {
    @Id
    private int MaTV;
    @Column
    private String HoTen;
    @Column
    private String Khoa;
    @Column
    private String Nganh;
    @Column
    private int SDT;


}
