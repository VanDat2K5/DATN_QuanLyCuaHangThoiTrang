package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LoaiSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPham {

    @Id
    @Column(name = "MaLoaiSP", length = 20)
    private String maLoaiSP;

    @Column(name = "LoaiSP", nullable = false)
    private String loaiSP;
}

