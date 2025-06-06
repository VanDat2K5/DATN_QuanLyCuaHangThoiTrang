package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "KhachHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {

    @Id
    @Column(name = "MaKH", length = 20)
    private String maKH;

    @Column(name = "TenKH", nullable = false)
    private String tenKH;

    @Column(name = "Email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "SoDT", nullable = false, length = 10)
    private String soDT;

    @OneToOne(mappedBy = "khachHang")
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "khachHang")
    private List<HoaDon> hoaDon;

    @OneToMany(mappedBy = "khachHang")
    private List<DiaChiNhanHang> diaChiNhanHang;
}
