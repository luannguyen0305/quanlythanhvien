package hibernate.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="thongtinsd")
@Data
public class thongtinsd {
    @Id
    private int MaTT;

    @Column
    private int MaTV;

    @Column
    private Integer MaTB;

    @Column
    private Date TGVao;

    @Column
    private Date TGMuon;

    @Column
    private Date TGTra;

}
