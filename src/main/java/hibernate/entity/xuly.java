/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hibernate.entity;

import javax.persistence.*;

import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "xuly")
@Data
public class xuly {
    @Id
    private int MaXL;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "MaTV", nullable = false,
//            foreignKey = @ForeignKey(name = "xuly_ibfk_1"))
    @Column
    private int MaTV;

    @Column
    private String HinhThucXL;

    @Column
    private Integer  SoTien;

    @Column
    private Date NgayXL;

    @Column
    private int TrangThaiXL;


}

