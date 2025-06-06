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
@Table(name = "ChiTietHoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_SPCT", nullable = false)
    private ChiTietSanPham chiTietSanPham;

    @ManyToOne
    @JoinColumn(name = "MaHD", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "GiaXuat", nullable = false)
    private BigDecimal giaXuat;

    @Column(name = "SoLuongXuat", nullable = false)
    private Integer soLuongXuat;

    @Column(name = "LoHang", nullable = false)
    private String loHang;

    @Column(name = "ThanhTien", nullable = false)
    private BigDecimal thanhTien;
}