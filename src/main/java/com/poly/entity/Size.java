package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Size")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Size {

    @Id
    @Column(name = "MaSize", length = 20)
    private String maSize;

    @Column(name = "TenSize", nullable = false)
    private String tenSize;
}
