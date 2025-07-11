package com.poly.entity;

import java.math.BigDecimal;
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
@Table(name = "HoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {

    @Id
    @Column(name = "MaHD", length = 20)
    private String maHD;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "MaKM")
    private KhuyenMai khuyenMai;

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @Column(name = "NgayLap", nullable = false)
    private LocalDate ngayLap;

    @Column(name = "TrangThai", nullable = false, columnDefinition = "NVARCHAR(255) CHECK (TrangThai IN ('DaThanhToan', 'DangVanChuyen', 'DaNhanHang', 'DaHuy', 'Doi-Tra_Hang')) DEFAULT 'ChoXacNhan'")
    private String trangThai = "ChoXacNhan";

    @Column(name = "PT_ThanhToan", nullable = false, columnDefinition = "NVARCHAR(255) CHECK (PT_ThanhToan IN ('COD', 'Bank'))")
    private String ptThanhToan;

    @Column(name = "SoDT_NhanHang", nullable = false, length = 10)
    private String soDTNhanHang;

    @Column(name = "DC_NhanHang", nullable = false)
    private String dcNhanHang;

    @Column(name = "Ten_NguoiNhan", nullable = false)
    private String tenNguoiNhan;
}
