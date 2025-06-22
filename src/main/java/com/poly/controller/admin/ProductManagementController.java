package com.poly.controller.admin;

import com.poly.entity.SanPham;
import com.poly.service.LoaiSanPhamService;
import com.poly.service.SanPhamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductManagementController {

    private final SanPhamService sanPhamService;
    private final LoaiSanPhamService loaiSanPhamService;

    // Trang danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", sanPhamService.findAll());
        return "admin/product/list";
    }

    // Form tạo mới sản phẩm
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("productForm", new SanPham());
        model.addAttribute("categories", loaiSanPhamService.findAll());
        return "admin/product/create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("productForm") SanPham sanPham,
                                BindingResult result,
                                RedirectAttributes ra,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", loaiSanPhamService.findAll());
            return "admin/product/create";
        }

        if (sanPham.getMaSP() == null || sanPham.getMaSP().isEmpty()) {
            sanPham.setMaSP("SP" + System.currentTimeMillis());
        }

        sanPhamService.save(sanPham);
        ra.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        return "redirect:/admin/products";
    }

    // Mở form sửa
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") String id, Model model, RedirectAttributes ra) {
        SanPham sp = sanPhamService.findById(id).orElse(null);
        if (sp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/products";
        }

        model.addAttribute("productForm", sp);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        return "admin/product/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable("id") String id,
                                @Valid @ModelAttribute("productForm") SanPham sanPham,
                                BindingResult result,
                                RedirectAttributes ra,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", loaiSanPhamService.findAll());
            return "admin/product/edit";
        }

        sanPhamService.save(sanPham);
        ra.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/admin/products";
    }

    // Xoá sản phẩm
    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") String id, RedirectAttributes ra) {
        sanPhamService.deleteById(id);
        ra.addFlashAttribute("message", "Đã xoá sản phẩm thành công!");
        return "redirect:/admin/products";
    }
}

