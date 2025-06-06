package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Mau")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mau {

    @Id
    @Column(name = "MaMau", length = 20)
    private String maMau;

    @Column(name = "TenMau", nullable = false)
    private String tenMau;
}
