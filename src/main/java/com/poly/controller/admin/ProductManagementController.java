package com.poly.controller.admin;

import com.poly.entity.HinhAnh;
import com.poly.entity.SanPham;
import com.poly.service.HinhAnhService;
import com.poly.service.SanPhamService;
import com.poly.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/management/product")
public class ProductManagementController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    @Autowired
    private HinhAnhService hinhAnhService;

    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String productManagement(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = sanPhamService.findByMaSPContainingIgnoreCase(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            products = sanPhamService.findAll(pageable);
        }
        for (SanPham sp : products.getContent()) {
            List<HinhAnh> images = hinhAnhService.findBySanPham_MaSP(sp.getMaSP());
            if (!images.isEmpty()) {
                sp.setHinhAnh(images);
            }
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        return "admin/product-management";
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("product", new SanPham());
        model.addAttribute("categories", loaiSanPhamService.findAll());
        model.addAttribute("genders", SanPham.Gender.values()); // Thêm enum giới tính
        return "admin/product-form";
    }

    // Thêm sản phẩm mới + upload nhiều ảnh
    @PostMapping("/create")
    public String addProduct(@ModelAttribute @Valid SanPham product,
            BindingResult br,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "returnToInput", required = false) Boolean returnToInput,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (br.hasErrors()) {
            model.addAttribute("categories", loaiSanPhamService.findAll());
            model.addAttribute("genders", SanPham.Gender.values());
            return "admin/product-form";
        }
        try {
            if (product.getMaSP() == null || product.getMaSP().isBlank()) {
                product.setMaSP("SP" + System.currentTimeMillis());
            }
            if (product.getGioiTinh() == null) {
                product.setGioiTinh(SanPham.Gender.UNISEX);
            }
            SanPham savedProduct = sanPhamService.save(product);
            if (images != null && images.length > 0) {
                hinhAnhService.storeImages(savedProduct, images);
            }
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        if (returnToInput != null && returnToInput) {
            return "redirect:/admin/management/input/create";
        }
        return "redirect:/admin/management/product";
    }

    // Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model, RedirectAttributes ra) {
        SanPham sp = sanPhamService.findById(id).orElse(null);
        if (sp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/management/product";
        }
        List<HinhAnh> images = hinhAnhService.findBySanPham_MaSP(id);
        sp.setHinhAnh(images);
        model.addAttribute("product", sp);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        // model.addAttribute("images", images);
        model.addAttribute("genders", SanPham.Gender.values());
        return "admin/product-form";
    }

    // Cập nhật sản phẩm (không ảnh)
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable String id,
            @Valid @ModelAttribute("product") SanPham product,
            BindingResult br,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (br.hasErrors()) {
            model.addAttribute("categories", loaiSanPhamService.findAll());
            model.addAttribute("genders", SanPham.Gender.values());
            return "admin/product-form";
        }
        try {
            product.setMaSP(id);
            if (product.getGioiTinh() == null) {
                product.setGioiTinh(SanPham.Gender.UNISEX);
            }
            sanPhamService.save(product);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/management/product";
    }

    // Thêm ảnh cho sản phẩm (không upload, chỉ gán từ thư mục)
    @PostMapping("/edit/{id}/images")
    public String uploadMoreImages(@PathVariable("id") String id,
            @RequestParam("images") MultipartFile[] images,
            RedirectAttributes ra) {
        SanPham sp = sanPhamService.findById(id).orElse(null);
        if (sp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm để cập nhật ảnh!");
            return "redirect:/admin/management/product";
        }
        try {
            hinhAnhService.storeImages(sp, images);
            ra.addFlashAttribute("success", "Gán thêm ảnh thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi gán ảnh: " + e.getMessage());
        }
        return "redirect:/admin/management/product/edit/" + id;
    }

    // Xoá ảnh theo ID
    @PostMapping("/images/{id}/delete")
    public String deleteImage(@PathVariable("id") Integer imageId,
            @RequestParam("productId") String productId,
            RedirectAttributes ra) {
        try {
            hinhAnhService.deleteById(imageId);
            ra.addFlashAttribute("success", "Xoá ảnh thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi xoá ảnh: " + e.getMessage());
        }
        return "redirect:/admin/management/product/edit/" + productId;
    }

    // Xóa sản phẩm + ảnh liên quan
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            List<HinhAnh> images = hinhAnhService.findBySanPham_MaSP(id);
            for (HinhAnh img : images) {
                hinhAnhService.deleteById(img.getId());
            }
            sanPhamService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Đã xoá sản phẩm và ảnh liên quan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xoá sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/management/product";
    }

    // Tìm kiếm sản phẩm
    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> products = sanPhamService.findByMaSPContainingIgnoreCase(keyword, pageable);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("categories", loaiSanPhamService.findAll());
        model.addAttribute("keyword", keyword);
        return "admin/product-management";
    }
}
