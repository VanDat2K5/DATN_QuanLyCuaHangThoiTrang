package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private LoaiSanPham loaiSanPham;

    @Column(name = "TenSP", nullable = false)
    private String tenSP;

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietSanPham> chiTietSanPham;

    @OneToMany(mappedBy = "sanPham")
    private List<HinhAnh> hinhAnh;
}
