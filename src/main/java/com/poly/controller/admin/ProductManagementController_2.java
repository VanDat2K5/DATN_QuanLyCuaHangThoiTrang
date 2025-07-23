package com.poly.controller.admin;

import com.poly.entity.SanPham;
import com.poly.service.SanPhamService;
import com.poly.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductManagementController_2 {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    // Hiển thị danh sách sản phẩm
    @GetMapping("/product-management")
    public String productManagement(Model model) {
        List<SanPham> products = sanPhamService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        return "admin/product-management";
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/product-management/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new SanPham());
        model.addAttribute("categories", loaiSanPhamService.findAll());
        return "admin/product-form";
    }

    // Thêm sản phẩm mới
    @PostMapping("/product-management/add")
    public String addProduct(@ModelAttribute SanPham product, RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.save(product);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/product-management";
    }

    // Hiển thị form sửa sản phẩm
    @GetMapping("/product-management/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        SanPham product = sanPhamService.findById(id).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/product-management";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        return "admin/product-form";
    }

    // Cập nhật sản phẩm
    @PostMapping("/product-management/edit/{id}")
    public String updateProduct(@PathVariable String id, @ModelAttribute SanPham product,
            RedirectAttributes redirectAttributes) {
        try {
            product.setMaSP(id);
            sanPhamService.save(product);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/product-management";
    }

    // Xóa sản phẩm
    @GetMapping("/product-management/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/product-management";
    }

    // Tìm kiếm sản phẩm
    @GetMapping("/product-management/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<SanPham> products = sanPhamService.findByTenSPContaining(keyword);
        model.addAttribute("products", products);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        model.addAttribute("searchKeyword", keyword);
        return "admin/product-management";
    }
}
