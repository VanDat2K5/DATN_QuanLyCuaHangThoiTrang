package com.poly.controller.customer;

import com.poly.entity.ChiTietSanPham;
import com.poly.entity.KhachHang;
import com.poly.entity.SanPham;
import com.poly.service.ChiTietSanPhamService;
import com.poly.service.SanPhamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop") // Ghép chung với danh sách
public class ProductDetailController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @GetMapping("/{maSP}")
    public String productDetail(@PathVariable("maSP") String maSP, Model model, HttpSession session) {
        // Tìm sản phẩm theo mã
        SanPham sanPham = sanPhamService.findById(maSP).orElse(null);
        if (sanPham == null) {
            return "redirect:/shop"; // Nếu không có sản phẩm thì quay về shop
        }

        // Lấy chi tiết sản phẩm
        List<ChiTietSanPham> listCT = chiTietSanPhamService.findBySanPham_MaSP(maSP);

        // Danh sách màu duy nhất
        var dsMau = listCT.stream()
                .map(ChiTietSanPham::getMau)
                .distinct()
                .collect(Collectors.toList());

        // Danh sách size duy nhất
        var dsSize = listCT.stream()
                .map(ChiTietSanPham::getSize)
                .distinct()
                .collect(Collectors.toList());

        KhachHang user = (KhachHang) session.getAttribute("user");
        if (user != null) {
            String maKH = user.getMaKH();
            model.addAttribute("maKH", maKH); // Gửi qua view nếu cần
        }
        model.addAttribute("sanPham", sanPham);
        model.addAttribute("dsMau", dsMau);
        model.addAttribute("dsSize", dsSize);
        model.addAttribute("dsChiTiet", listCT);

        return "Client/demo-fashion-store-single-product"; // View chi tiết sản phẩm
    }
}
