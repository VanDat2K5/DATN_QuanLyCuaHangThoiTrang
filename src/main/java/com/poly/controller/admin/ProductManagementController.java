package com.poly.controller.admin;

import com.poly.entity.HinhAnh;
import com.poly.entity.SanPham;
import com.poly.service.HinhAnhService;
import com.poly.service.LoaiSanPhamService;
import com.poly.service.SanPhamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductManagementController {

    private final SanPhamService sanPhamService;
    private final LoaiSanPhamService loaiSanPhamService;
    private final HinhAnhService hinhAnhService;

    // ✅ Danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        List<SanPham> products = sanPhamService.findAll();
        Map<String, String> productImages = new HashMap<>();

        for (SanPham sp : products) {
            List<HinhAnh> images = hinhAnhService.findBySanPham_MaSP(sp.getMaSP());
            if (!images.isEmpty()) {
                productImages.put(sp.getMaSP(), images.get(0).getHinhAnh());
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("productImages", productImages);
        model.addAttribute("genders", SanPham.Gender.values()); // Thêm enum giới tính
        return "admin/product/list";
    }

    // Tìm kiếm sản phẩm theo mã có phân trang
    @GetMapping("/search")
    public String searchProductsByMaSP(
            @RequestParam(name = "keyword", required = false) String maSP,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> productPage = sanPhamService.findByMaSPContainingIgnoreCase(maSP == null ? "" : maSP, pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("keyword", maSP);
        return "admin/product/list";
    }

    // ✅ Form tạo sản phẩm
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("productForm", new SanPham());
        model.addAttribute("categories", loaiSanPhamService.findAll());
        model.addAttribute("genders", SanPham.Gender.values()); // Thêm enum giới tính
        return "admin/product/create";
    }

    // ✅ Tạo sản phẩm + gán ảnh từ thư mục có sẵn
    @PostMapping("/create")
    public String createProduct(@ModelAttribute("productForm") @Valid SanPham sanPham,
            BindingResult br,
            @RequestParam("images") MultipartFile[] images,
            RedirectAttributes ra,
            Model model) throws IOException {
        if (br.hasErrors()) {
            model.addAttribute("categories", loaiSanPhamService.findAll());
            model.addAttribute("genders", SanPham.Gender.values()); // Thêm enum giới tính
            return "admin/product/create";
        }

        if (sanPham.getMaSP() == null || sanPham.getMaSP().isBlank()) {
            sanPham.setMaSP("SP" + System.currentTimeMillis());
        }

        // Đảm bảo có giá trị giới tính mặc định
        if (sanPham.getGioiTinh() == null) {
            sanPham.setGioiTinh(SanPham.Gender.UNISEX);
        }

        SanPham savedProduct = sanPhamService.save(sanPham);
        hinhAnhService.storeImages(savedProduct, images); // Lưu theo file đã tồn tại

        ra.addFlashAttribute("message", "Tạo sản phẩm thành công và ảnh đã được gán từ thư mục /images!");
        return "redirect:/admin/products";
    }

    // ✅ Mở form sửa sản phẩm
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") String id, Model model, RedirectAttributes ra) {
        SanPham sp = sanPhamService.findById(id).orElse(null);
        if (sp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/products";
        }

        List<HinhAnh> images = hinhAnhService.findBySanPham_MaSP(id);

        model.addAttribute("productForm", sp);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        model.addAttribute("images", images);
        model.addAttribute("genders", SanPham.Gender.values()); // Thêm enum giới tính
        return "admin/product/edit";
    }

    // ✅ Cập nhật sản phẩm (không ảnh)
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable("id") String id,
            @Valid @ModelAttribute("productForm") SanPham sanPham,
            BindingResult result,
            RedirectAttributes ra,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", loaiSanPhamService.findAll());
            model.addAttribute("genders", SanPham.Gender.values()); // Thêm enum giới tính
            return "admin/product/edit";
        }

        // Đảm bảo có giá trị giới tính
        if (sanPham.getGioiTinh() == null) {
            sanPham.setGioiTinh(SanPham.Gender.UNISEX);
        }

        sanPhamService.save(sanPham);
        ra.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/admin/products";
    }

    // ✅ Thêm ảnh từ thư mục /images (không upload)
    @PostMapping("/{id}/edit/images")
    public String uploadMoreImages(@PathVariable("id") String id,
            @RequestParam("images") MultipartFile[] images,
            RedirectAttributes ra) throws IOException {
        SanPham sp = sanPhamService.findById(id).orElse(null);
        if (sp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm để cập nhật ảnh!");
            return "redirect:/admin/products";
        }

        hinhAnhService.storeImages(sp, images); // Dùng service đã sửa
        ra.addFlashAttribute("message", "Gán thêm ảnh từ thư mục /images thành công!");
        return "redirect:/admin/products/" + id + "/edit";
    }

    // ✅ Xoá ảnh theo ID
    @PostMapping("/images/{id}/delete")
    public String deleteImage(@PathVariable("id") Integer imageId,
            @RequestParam("productId") String productId,
            RedirectAttributes ra) {
        hinhAnhService.deleteById(imageId);
        ra.addFlashAttribute("message", "Xoá ảnh thành công!");
        return "redirect:/admin/products/" + productId + "/edit";
    }

    // ✅ Xoá sản phẩm + ảnh liên quan
    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") String id, RedirectAttributes ra) {
        SanPham sp = sanPhamService.findById(id).orElse(null);
        if (sp == null) {
            ra.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/admin/products";
        }

        List<HinhAnh> images = hinhAnhService.findBySanPham_MaSP(id);
        for (HinhAnh img : images) {
            hinhAnhService.deleteById(img.getId());
        }

        sanPhamService.deleteById(id);
        ra.addFlashAttribute("message", "Đã xoá sản phẩm và ảnh liên quan!");
        return "redirect:/admin/products";
    }
}
