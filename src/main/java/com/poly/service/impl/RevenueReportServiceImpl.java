package com.poly.service.impl;

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

        return new RevenueReportDTO(startDate, totalRevenue, totalCost, totalProfit,
                totalOrders, totalProductsSold, totalProductsImported);
    }
}