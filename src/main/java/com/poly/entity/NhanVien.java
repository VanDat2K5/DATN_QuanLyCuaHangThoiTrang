package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "NhanVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {

    @Id
    @Column(name = "MaNV", length = 20)
    private String maNV;

    @Column(name = "TenNV", nullable = false)
    private String tenNV;

    @Column(name = "Email", nullable = false, length = 50)
    private String email;

    @Column(name = "SoDT", nullable = false, length = 10)
    private String soDT;
} 
