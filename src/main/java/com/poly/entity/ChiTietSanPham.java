package com.poly.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietSanPham {

    @Id
    @Column(name = "ID_SPCT", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "MaSP", nullable = false)
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "MaMau", nullable = false)
    private Mau mau;

    @ManyToOne
    @JoinColumn(name = "MaSize", nullable = false)
    private Size size;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "GiaNhap", nullable = false)
    private BigDecimal giaNhap;

    @Column(name = "GiaXuat", nullable = false)
    private BigDecimal giaXuat;

    @Column(name = "LoHang", nullable = false)
    private String loHang;
}
