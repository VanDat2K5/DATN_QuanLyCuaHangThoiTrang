package com.poly.entity;

import java.math.BigDecimal;

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
@Table(name = "ChiTietPhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_SPCT", nullable = false)
    private ChiTietSanPham chiTietSanPham;

    @ManyToOne
    @JoinColumn(name = "MaPN", nullable = false)
    private PhieuNhap phieuNhap;

    @Column(name = "GiaNhap", nullable = false)
    private BigDecimal giaNhap;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuongNhap;

    @Column(name = "LoHang", nullable = false)
    private String loHang;
}
