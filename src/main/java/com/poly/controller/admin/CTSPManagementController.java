package com.poly.controller.admin;

import com.poly.entity.ChiTietSanPham;
import com.poly.service.ChiTietSanPhamService;
import com.poly.service.MauService;
import com.poly.service.SanPhamService;
import com.poly.service.SizeService;
import com.poly.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/chitiet")
public class CTSPManagementController {

    @Autowired
    private ChiTietSanPhamService chiTietService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private MauService mauService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private CodeGenerator codeGenerator;

    // Hiển thị form + list trong trang detail-management
    @GetMapping("/detail-management")
    public String detailManagement(@RequestParam("productId") String productId, Model model) {
        ChiTietSanPham ct = new ChiTietSanPham();
        sanPhamService.findById(productId).ifPresent(ct::setSanPham);

        loadFormData(model, ct, productId, null);
        return "admin/product-detail";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("chiTiet") ChiTietSanPham chiTiet,
                       @RequestParam("productId") String productId,
                       Model model) {

        // Đảm bảo chiTiet có SanPham nếu chưa được bind từ form
        if (chiTiet.getSanPham() == null || chiTiet.getSanPham().getMaSP() == null) {
            sanPhamService.findById(productId).ifPresent(chiTiet::setSanPham);
        }

        // Nếu thêm mới thì sinh ID bằng CodeGenerator
        if (chiTiet.getId() == null || chiTiet.getId().isEmpty()) {
            String generatedId = codeGenerator.generateProductDetailCode(
                    chiTiet.getSanPham().getMaSP(),
                    chiTiet.getMau().getMaMau(),
                    chiTiet.getSize().getMaSize()
            );

            // Kiểm tra trùng ID
            if (chiTietService.existsById(generatedId)) {
                loadFormData(model, chiTiet, productId, "Chi tiết sản phẩm với ID này đã tồn tại!");
                return "admin/product-detail";
            }

            chiTiet.setId(generatedId);

            // Nếu entity có trường loHang thì set luôn (tự động, không nhập từ form)
            String loHang = generatedId.substring(generatedId.indexOf("LH"));
            chiTiet.setLoHang(loHang);
        }

        // Lưu chi tiết sản phẩm
        chiTietService.save(chiTiet);
        return "redirect:/admin/chitiet/detail-management?productId=" + productId;
    }

    // Chỉnh sửa từ trang detail-management
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        ChiTietSanPham ct = chiTietService.findById(id).orElse(null);
        if (ct == null)
            return "redirect:/admin/chitiet";

        loadFormData(model, ct, ct.getSanPham().getMaSP(), null);
        return "admin/product-detail";
    }

    // Xóa từ trang detail-management
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        ChiTietSanPham ct = chiTietService.findById(id).orElse(null);
        String productId = (ct != null) ? ct.getSanPham().getMaSP() : "";
        chiTietService.deleteById(id);
        return "redirect:/admin/chitiet/detail-management?productId=" + productId;
    }

    // Tìm kiếm chi tiết sản phẩm theo mã sản phẩm có phân trang
    @GetMapping("/search")
    public String searchChiTietSanPhamByMaSP(
            @RequestParam(name = "maSP", required = false) String maSP,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChiTietSanPham> ctspPage = chiTietService.findBySanPham_MaSPContainingIgnoreCase(maSP == null ? "" : maSP,
                pageable);
        model.addAttribute("list", ctspPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ctspPage.getTotalPages());
        model.addAttribute("totalItems", ctspPage.getTotalElements());
        model.addAttribute("searchMaSP", maSP);
        return "admin/product-detail";
    }

    // Hàm load dữ liệu form để tránh lặp code
    private void loadFormData(Model model, ChiTietSanPham chiTiet, String productId, String errorMessage) {
        model.addAttribute("chiTiet", chiTiet);
        model.addAttribute("list", chiTietService.findBySanPham_MaSP(productId));
        model.addAttribute("maus", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("productId", productId);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }
}
