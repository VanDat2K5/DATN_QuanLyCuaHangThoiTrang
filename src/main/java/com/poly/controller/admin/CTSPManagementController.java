package com.poly.controller.admin;
import com.poly.entity.ChiTietSanPham;
import com.poly.entity.Mau;
import com.poly.entity.Size;
import com.poly.entity.SanPham;
import com.poly.service.ChiTietSanPhamService;
import com.poly.service.MauService;
import com.poly.service.SizeService;
import com.poly.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/chitiet")
public class CTSPManagementController {
    @Autowired private ChiTietSanPhamService chiTietService;
    @Autowired private SanPhamService sanPhamService;
    @Autowired private MauService mauService;
    @Autowired private SizeService sizeService;

    // Hiển thị list
    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", chiTietService.findAll());
        return "admin/chitiet/list";
    }

    // Form thêm mới
    // trong CTSPManagementController
    @GetMapping("/add")
    public String addForm(@RequestParam(value="productId", required=false) String productId,
                          Model model) {
        ChiTietSanPham ct = new ChiTietSanPham();
        if (productId != null) {
            sanPhamService.findById(productId)
                    .ifPresent(ct::setSanPham);
        }
        model.addAttribute("chiTiet", ct);
        model.addAttribute("maus", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/chitiet/form";
    }




    // Form sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        ChiTietSanPham ct = chiTietService.findById(id).orElse(new ChiTietSanPham());
        model.addAttribute("chiTiet", ct);
        model.addAttribute("sanphams", sanPhamService.findAll());
        model.addAttribute("maus", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/chitiet/form";
    }

    // Lưu (thêm / sửa)
    @PostMapping("/save")
    public String save(@ModelAttribute ChiTietSanPham chiTiet) {
        chiTietService.save(chiTiet);
        return "redirect:/admin/products";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        chiTietService.deleteById(id);
        return "redirect:/admin/chitiet";
    }
}

