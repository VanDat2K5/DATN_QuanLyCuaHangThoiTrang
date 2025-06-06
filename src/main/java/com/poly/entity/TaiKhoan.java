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
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_MaNV")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ID_MaKH")
    private KhachHang khachHang;

    @Column(name = "TenTK", nullable = false)
    private String tenTK;

    @Column(name = "MatKhau", nullable = false)
    private String matKhau;

    @Column(name = "HinhAnh", length = 50)
    private String hinhAnh;
}
