package com.poly.controller.admin;

import com.poly.entity.LoaiSanPham;
import com.poly.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
public class LoaiSPManagementController {
    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    @GetMapping("/list")
    public String listLoaiSP(Model model) {
        List<LoaiSanPham> loaiSanPham = loaiSanPhamService.findAll();
        model.addAttribute("loaiSanPham", loaiSanPham);
        return "category/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("loaiSanPham", new LoaiSanPham());
        return "category/create";
    }

    @PostMapping("/save")
    public String saveLoaiSP(@ModelAttribute LoaiSanPham loaiSanPham, RedirectAttributes redirectAttributes) {
        loaiSanPhamService.save(loaiSanPham);
        redirectAttributes.addFlashAttribute("message", "Them loai san pham thanh cong!");
        return "redirect:/category/list";
    }

    @GetMapping("/edit{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        LoaiSanPham loaiSanPham = loaiSanPhamService.findById(id).orElse(null);
        if (loaiSanPham != null) {
            model.addAttribute("loaiSanPham", loaiSanPham);
            return "category/edit";
        } else {
            return "redirect:/category/list";
        }
    }

    @PostMapping("/update")
    public String updateLoaiSP(@ModelAttribute LoaiSanPham loaiSanPham, RedirectAttributes redirectAttributes) {
        loaiSanPhamService.save(loaiSanPham);
        redirectAttributes.addFlashAttribute("message", "Cap nhat loai sp thanh cong!");
        return "redirect:/category/list";
    }

    @GetMapping("/delete{id}")
    public String deleteLoaiSP(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        LoaiSanPham loaiSanPham = loaiSanPhamService.findById(id).orElse(null);
        if (loaiSanPham != null) {
            if (loaiSanPham.getSanPham() != null && !loaiSanPham.getSanPham().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Danh muc nay co san pham, ko the xoa!");
            } else {
                loaiSanPhamService.deleteById(id);
                redirectAttributes.addFlashAttribute("message", "Xoa danh muc thanh cong!");
            }
        }
        return "redirect:/category/list";
    }
}
