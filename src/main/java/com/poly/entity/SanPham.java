package com.poly.entity;

import jakarta.persistence.*;
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
    private LoaiSanPham loaiSP;

    @Column(name = "TenSP", nullable = false, length = 255)
    private String tenSP;

    @Enumerated(EnumType.STRING)
    @Column(name = "GioiTinh", nullable = false)
    private Gender gioiTinh = Gender.UNISEX; // Mặc định unisex

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietSanPham> chiTietSanPham;

    @OneToMany(mappedBy = "sanPham")
    private List<HinhAnh> hinhAnh;

    // Enum cho giới tính
    public enum Gender {
        NAM("Nam"),
        NU("Nữ"),
        TRE_EM("Trẻ em"),
        UNISEX("Unisex");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
