package com.poly.entity;

import java.time.LocalDate;

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
@Table(name = "PhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhap {

    @Id
    @Column(name = "MaPN", length = 20)
    private String maPN;

    @ManyToOne
    @JoinColumn(name = "MaNV", nullable = false)
    private NhanVien nhanVien;

    @Column(name = "TenNhaCungCap", nullable = false)
    private String tenNhaCungCap;

    @Column(name = "NgayNhap", nullable = false)
    private LocalDate ngayNhap;
}
