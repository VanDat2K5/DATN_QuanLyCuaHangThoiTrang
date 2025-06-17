package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "LoaiSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPham {

    @Id
    @Column(name = "MaLoaiSP", length = 20)
    private String maLoaiSP;

    @Column(name = "LoaiSP", nullable = false, length = 150)
    private String loaiSP;

    @OneToMany(mappedBy = "loaiSP")
    private List<SanPham> dsSanPham;
}
