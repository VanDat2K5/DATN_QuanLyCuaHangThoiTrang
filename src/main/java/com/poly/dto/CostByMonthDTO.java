package com.poly.dto;

import java.math.BigDecimal;

public class CostByMonthDTO {
    private String month; // ví dụ: "07/2025"
    private BigDecimal totalCost;
    private BigDecimal totalRevenue; // Tổng tiền bán ra

    public CostByMonthDTO(String month, BigDecimal totalCost, BigDecimal totalRevenue) {
        this.month = month;
        this.totalCost = totalCost;
        this.totalRevenue = totalRevenue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}