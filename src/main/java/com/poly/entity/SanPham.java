package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {
    @Id
    @Column(name = "MaSP", length = 20)
    private String maSP;

    @ManyToOne
    @JoinColumn(name = "MaLoaiSP", nullable = false)
    private LoaiSanPham loaiSP;

    @Column(name = "TenSP", nullable = false, length = 255)
    private String tenSP;

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietSanPham> chiTietSanPham;

    @OneToMany(mappedBy = "sanPham")
    private List<HinhAnh> hinhAnh;
}
