package com.poly.controller.admin;

import com.poly.entity.LoaiSanPham;
import com.poly.service.LoaiSanPhamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/management/categories")
@RequiredArgsConstructor
public class LoaiSPManagementController {

    private final LoaiSanPhamService loaiSanPhamService;

    /* ─────────────────────── GET LIST + FORM ─────────────────────── */
    @GetMapping
    public String showCategoryPage(Model model,
            @ModelAttribute("message") String msg,
            @ModelAttribute("error") String err) {

        List<LoaiSanPham> categories = loaiSanPhamService.findAll();
        model.addAttribute("categories", categories);

        if (!model.containsAttribute("categoryForm")) {
            model.addAttribute("categoryForm", new LoaiSanPham());
        }

        return "admin/category-management";
    }

    /* ─────────────────────── THÊM MỚI ─────────────────────── */
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("categoryForm") LoaiSanPham form,
            BindingResult result,
            RedirectAttributes ra) {

        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.categoryForm", result);
            ra.addFlashAttribute("categoryForm", form);
            ra.addFlashAttribute("error", "Vui lòng kiểm tra lại dữ liệu!");
            return "redirect:/admin/management/categories";
        }

        // Nếu không có mã → sinh mã mới
        if (form.getMaLoaiSP() == null || form.getMaLoaiSP().isEmpty()) {
            form.setMaLoaiSP(form.getLoaiSP());
        }

        loaiSanPhamService.save(form);
        ra.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/admin/management/categories";
    }

    /* ─────────────────────── MỞ FORM SỬA ─────────────────────── */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model, RedirectAttributes ra) {
        LoaiSanPham cat = loaiSanPhamService.findById(id).orElse(null);
        if (cat == null) {
            ra.addFlashAttribute("error", "Không tìm thấy danh mục!");
            return "redirect:/admin/management/categories";
        }

        model.addAttribute("categoryForm", cat);
        model.addAttribute("editing", true);
        model.addAttribute("categories", loaiSanPhamService.findAll());
        return "admin/category-management";
    }

    /* ─────────────────────── CẬP NHẬT ─────────────────────── */
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") String id,
            @Valid @ModelAttribute("categoryForm") LoaiSanPham form,
            BindingResult result,
            RedirectAttributes ra) {

        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.categoryForm", result);
            ra.addFlashAttribute("categoryForm", form);
            ra.addFlashAttribute("editing", true);
            ra.addFlashAttribute("error", "Cập nhật thất bại, hãy kiểm tra dữ liệu!");
            return "redirect:/admin/management/categories/edit/" + id;
        }

        loaiSanPhamService.save(form);
        ra.addFlashAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/admin/management/categories";
    }

    /* ─────────────────────── XOÁ ĐƠN LẺ ─────────────────────── */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes ra) {
        loaiSanPhamService.findById(id).ifPresentOrElse(cat -> {
            if (cat.getDsSanPham() != null && !cat.getDsSanPham().isEmpty()) {
                ra.addFlashAttribute("error", "Danh mục đang có sản phẩm. Không thể xoá!");
            } else {
                loaiSanPhamService.deleteById(id);
                ra.addFlashAttribute("message", "Đã xoá thành công!");
            }
        }, () -> ra.addFlashAttribute("error", "Không tìm thấy danh mục!"));

        return "redirect:/admin/management/categories";
    }
}
