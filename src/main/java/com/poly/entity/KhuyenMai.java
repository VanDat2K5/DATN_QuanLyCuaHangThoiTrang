package com.poly.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KhuyenMai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {

    @Id
    @Column(name = "MaKM", length = 20)
    private String maKM;

    @Column(name = "TenKM", nullable = false)
    private String tenKM;

    @Column(name = "Giam", nullable = false)
    private Integer giam;

    @Column(name = "GioiHan", nullable = false)
    private Integer gioiHan;

    @Column(name = "NgayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "NgayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;
}
