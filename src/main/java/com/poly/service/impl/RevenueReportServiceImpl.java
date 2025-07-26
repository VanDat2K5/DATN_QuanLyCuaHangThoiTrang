package com.poly.service.impl;

import com.poly.dto.CostByMonthDTO;
import com.poly.dto.RevenueReportDTO;
import com.poly.entity.ChiTietHoaDon;
import com.poly.entity.ChiTietPhieuNhap;
import com.poly.entity.HoaDon;
import com.poly.entity.PhieuNhap;
import com.poly.service.ChiTietHoaDonService;
import com.poly.service.ChiTietPhieuNhapService;
import com.poly.service.HoaDonService;
import com.poly.service.PhieuNhapService;
import com.poly.service.RevenueReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueReportServiceImpl implements RevenueReportService {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PhieuNhapService phieuNhapService;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    @Autowired
    private ChiTietPhieuNhapService chiTietPhieuNhapService;

    @Override
    public RevenueReportDTO getTotalRevenueSummary(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        int totalOrders = 0;
        int totalProductsSold = 0;
        int totalProductsImported = 0;

        // Lấy tất cả hóa đơn trong khoảng thời gian
        List<HoaDon> hoaDons = hoaDonService.findByNgayLapBetween(startDate, endDate);

        // Tính doanh thu từ hóa đơn đã thanh toán
        for (HoaDon hoaDon : hoaDons) {
            if ("DaThanhToan".equals(hoaDon.getTrangThai()) || "DaNhanHang".equals(hoaDon.getTrangThai())) {
                totalRevenue = totalRevenue.add(hoaDon.getTongTien() != null ? hoaDon.getTongTien() : BigDecimal.ZERO);
                totalOrders++;

                // Tính số sản phẩm bán ra
                List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonService.findByHoaDon_MaHD(hoaDon.getMaHD());
                for (ChiTietHoaDon chiTiet : chiTietHoaDons) {
                    totalProductsSold += chiTiet.getSoLuongXuat();
                }
            }
        }

        // Lấy tất cả phiếu nhập trong khoảng thời gian
        List<PhieuNhap> phieuNhaps = phieuNhapService.findByNgayNhapBetween(startDate, endDate);

        // Tính chi phí từ phiếu nhập
        for (PhieuNhap phieuNhap : phieuNhaps) {
            List<ChiTietPhieuNhap> chiTietPhieuNhaps = chiTietPhieuNhapService
                    .findByPhieuNhap_MaPN(phieuNhap.getMaPN());
            for (ChiTietPhieuNhap chiTiet : chiTietPhieuNhaps) {
                BigDecimal cost = chiTiet.getGiaNhap().multiply(BigDecimal.valueOf(chiTiet.getSoLuongNhap()));
                totalCost = totalCost.add(cost);
                totalProductsImported += chiTiet.getSoLuongNhap();
            }
        }

        BigDecimal totalProfit = totalRevenue.subtract(totalCost);
        List<CostByMonthDTO> costByMonth = getCostByMonth(startDate, endDate);

        return new RevenueReportDTO(startDate, totalRevenue, totalCost, totalProfit,
                totalOrders, totalProductsSold, totalProductsImported, costByMonth);
    }

    @Override
    public List<CostByMonthDTO> getCostByMonth(LocalDate startDate, LocalDate endDate) {
        List<CostByMonthDTO> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        LocalDate current = startDate.withDayOfMonth(1);
        LocalDate end = endDate.withDayOfMonth(1);
        while (!current.isAfter(end)) {
            LocalDate monthStart = current.withDayOfMonth(1);
            LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());
            java.math.BigDecimal totalCost = java.math.BigDecimal.ZERO;
            java.math.BigDecimal totalRevenue = java.math.BigDecimal.ZERO;
            // Tính chi phí nhập
            java.util.List<PhieuNhap> phieuNhaps = phieuNhapService.findByNgayNhapBetween(monthStart, monthEnd);
            for (PhieuNhap phieuNhap : phieuNhaps) {
                List<ChiTietPhieuNhap> chiTietPhieuNhaps = chiTietPhieuNhapService
                        .findByPhieuNhap_MaPN(phieuNhap.getMaPN());
                for (ChiTietPhieuNhap chiTiet : chiTietPhieuNhaps) {
                    java.math.BigDecimal cost = chiTiet.getGiaNhap()
                            .multiply(java.math.BigDecimal.valueOf(chiTiet.getSoLuongNhap()));
                    totalCost = totalCost.add(cost);
                }
            }
            // Tính tổng doanh thu
            List<HoaDon> hoaDons = hoaDonService.findByNgayLapBetween(monthStart, monthEnd);
            for (HoaDon hoaDon : hoaDons) {
                if ("DaThanhToan".equals(hoaDon.getTrangThai()) || "DaNhanHang".equals(hoaDon.getTrangThai())) {
                    totalRevenue = totalRevenue
                            .add(hoaDon.getTongTien() != null ? hoaDon.getTongTien() : java.math.BigDecimal.ZERO);
                }
            }
            System.out.println(
                    "Month: " + current.format(formatter) + ", TotalCost: " + totalCost + ", Revenue: " + totalRevenue);
            result.add(new CostByMonthDTO(current.format(formatter), totalCost, totalRevenue));
            current = current.plusMonths(1);
        }
        return result;
    }
}