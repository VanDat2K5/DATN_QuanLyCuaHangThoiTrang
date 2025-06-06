package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DiaChiNhanHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaChiNhanHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MaKH", nullable = false)
    private KhachHang khachHang;

    @Column(name = "Ten_NguoiNhan", nullable = false)
    private String tenNguoiNhan;

    @Column(name = "SoDT_NhanHang", nullable = false, length = 10)
    private String soDTNhanHang;

    @Column(name = "DC_NhanHang", nullable = false)
    private String dcNhanHang;
}


