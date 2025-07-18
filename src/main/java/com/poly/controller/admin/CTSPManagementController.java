package com.poly.controller.admin;

import com.poly.entity.ChiTietSanPham;
import com.poly.service.ChiTietSanPhamService;
import com.poly.service.MauService;
import com.poly.service.SanPhamService;
import com.poly.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //  Hiển thị form + list trong trang detail-management
    @GetMapping("/detail-management")
    public String detailManagement(@RequestParam("productId") String productId, Model model) {
        ChiTietSanPham ct = new ChiTietSanPham();
        sanPhamService.findById(productId).ifPresent(ct::setSanPham);

        model.addAttribute("chiTiet", ct);
        model.addAttribute("list", chiTietService.findBySanPham_MaSP(productId));
        model.addAttribute("maus", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("productId", productId);

        return "admin/detail-management";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("chiTiet") ChiTietSanPham chiTiet,
                       @RequestParam("productId") String productId,
                       Model model) {

        //  Đảm bảo chiTiet có SanPham nếu chưa được bind từ form
        if (chiTiet.getSanPham() == null || chiTiet.getSanPham().getMaSP() == null) {
            sanPhamService.findById(productId).ifPresent(chiTiet::setSanPham);
        }

        // Tạo ID nếu thêm mới
        if (chiTiet.getId() == null || chiTiet.getId().isEmpty()) {
            String generatedId = chiTiet.getSanPham().getMaSP()
                    + "_" + chiTiet.getMau().getMaMau()
                    + "_" + chiTiet.getSize().getMaSize();

            if (chiTietService.existsById(generatedId)) {
                model.addAttribute("chiTiet", chiTiet);
                model.addAttribute("list", chiTietService.findBySanPham_MaSP(productId));
                model.addAttribute("maus", mauService.findAll());
                model.addAttribute("sizes", sizeService.findAll());
                model.addAttribute("productId", productId);
                model.addAttribute("errorMessage", "Chi tiết sản phẩm với ID này đã tồn tại!");
                return "admin/detail-management";
            }

            chiTiet.setId(generatedId);
        }

        chiTietService.save(chiTiet);
        return "redirect:/admin/chitiet/detail-management?productId=" + productId;
    }

    //  Chỉnh sửa từ trang detail-management
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        ChiTietSanPham ct = chiTietService.findById(id).orElse(null);
        if (ct == null) return "redirect:/admin/chitiet";

        model.addAttribute("chiTiet", ct);
        model.addAttribute("list", chiTietService.findBySanPham_MaSP(ct.getSanPham().getMaSP()));
        model.addAttribute("maus", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("productId", ct.getSanPham().getMaSP());

        return "admin/detail-management";
    }

    //  Xóa từ trang detail-management
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        ChiTietSanPham ct = chiTietService.findById(id).orElse(null);
        String productId = (ct != null) ? ct.getSanPham().getMaSP() : "";
        chiTietService.deleteById(id);
        return "redirect:/admin/chitiet/detail-management?productId=" + productId;
    }
}
