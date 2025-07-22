package com.poly.controller.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.entity.ChiTietPhieuNhap;
import com.poly.entity.Mau;
import com.poly.entity.NhanVien;
import com.poly.entity.PhieuNhap;
import com.poly.entity.SanPham;
import com.poly.entity.Size;

import com.poly.service.ChiTietSanPhamService;
import com.poly.service.ChiTietPhieuNhapService;
import com.poly.service.MauService;
import com.poly.service.PhieuNhapService;
import com.poly.service.SizeService;
import com.poly.service.SanPhamService;

import com.poly.util.CodeGenerator;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/management/input")
public class InputManagementController {

    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final MauService mauService;
    private final SizeService sizeService;
    private final PhieuNhapService phieuNhapService;
    private final ChiTietPhieuNhapService chiTietPhieuNhapService;
    private final CodeGenerator codeGenerator;

    @Autowired
    public InputManagementController(SanPhamService sanPhamService, ChiTietSanPhamService chiTietSanPhamService,
            MauService mauService, SizeService sizeService, PhieuNhapService phieuNhapService,
            ChiTietPhieuNhapService chiTietPhieuNhapService, CodeGenerator codeGenerator) {
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
        this.mauService = mauService;
        this.sizeService = sizeService;
        this.phieuNhapService = phieuNhapService;
        this.chiTietPhieuNhapService = chiTietPhieuNhapService;
        this.codeGenerator = codeGenerator;
    }

    @GetMapping
    public String inputManagement(Model model) {
        List<PhieuNhap> phieuNhaps = phieuNhapService.findAll();
        System.out.println(phieuNhaps.size());
        model.addAttribute("phieuNhaps", phieuNhaps);
        return "admin/input-management.html";
    }

    @GetMapping("/view/{maPN}")
    public String viewInput(@PathVariable("maPN") String maPN, Model model) {
        Optional<PhieuNhap> phieuNhapOpt = phieuNhapService.findById(maPN);
        if (phieuNhapOpt.isPresent()) {
            PhieuNhap phieuNhap = phieuNhapOpt.get();
            List<ChiTietPhieuNhap> chiTietPhieuNhap = chiTietPhieuNhapService
                    .findByPhieuNhap_MaPN(phieuNhap.getMaPN());
            System.out.println(chiTietPhieuNhap.size());
            model.addAttribute("phieuNhap", phieuNhap);
            model.addAttribute("chiTietPhieuNhaps", chiTietPhieuNhap);
        } else {
            return "redirect:/admin/management/input";
        }
        return "admin/input-detail.html";
    }

    @GetMapping("/edit/{maPN}")
    public String editInput(@PathVariable("maPN") String maPN, Model model) {
        Optional<PhieuNhap> phieuNhapOpt = phieuNhapService.findById(maPN);
        if (phieuNhapOpt.isPresent()) {
            PhieuNhap phieuNhap = phieuNhapOpt.get();
            model.addAttribute("phieuNhap", phieuNhap);
        } else {
            return "redirect:/admin/management/input";
        }
        return "admin/input-edit.html";
    }

    @GetMapping("/create")
    public String createInput(Model model, HttpSession session) {
        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setMaPN(codeGenerator.generateImportCode());
        phieuNhap.setTongTien(BigDecimal.ZERO);
        phieuNhap.setNgayNhap(LocalDate.now());
        phieuNhap.setNhanVien((NhanVien) session.getAttribute("user"));
        /*------------------------------------------------------------------------------------------------*/
        List<SanPham> sanPhams = sanPhamService.findAll();
        model.addAttribute("sanPhams", sanPhams);
        List<Mau> mauSacs = mauService.findAll();
        model.addAttribute("mauSacs", mauSacs);
        List<Size> sizes = sizeService.findAll();
        model.addAttribute("sizes", sizes);
        /*------------------------------------------------------------------------------------------------*/
        model.addAttribute("phieuNhap", phieuNhap);
        return "admin/input-create.html";
    }

    @PostMapping("/create")
    public String createInput(@ModelAttribute("phieuNhap") PhieuNhap phieuNhap,
            Model model, HttpSession session) {
        phieuNhap.setNhanVien((NhanVien) session.getAttribute("user"));
        phieuNhap.setNgayNhap(LocalDate.now());
        phieuNhap.setTenNhaCungCap(phieuNhap.getTenNhaCungCap());

        List<ChiTietPhieuNhap> chiTietPhieuNhapList = new ArrayList<>();
        for (ChiTietPhieuNhap chiTietPhieuNhap : chiTietPhieuNhapList) {
            chiTietPhieuNhap.setPhieuNhap(phieuNhap);
            chiTietPhieuNhap.setChiTietSanPham(
                    chiTietSanPhamService.findById(chiTietPhieuNhap.getChiTietSanPham().getId()).get());
            chiTietPhieuNhap.setGiaNhap(chiTietPhieuNhap.getChiTietSanPham().getGiaNhap());
            chiTietPhieuNhap.setSoLuongNhap(chiTietPhieuNhap.getSoLuongNhap());
            chiTietPhieuNhap.setLoHang(chiTietPhieuNhap.getLoHang());
            phieuNhap.setTongTien(phieuNhap.getTongTien().add(
                    chiTietPhieuNhap.getGiaNhap().multiply(BigDecimal.valueOf(chiTietPhieuNhap.getSoLuongNhap()))));
        }
        System.out.println(
                "Ma PN: " + phieuNhap.getMaPN() + " " + "Nhan Vien: " + phieuNhap.getNhanVien().getTenNV() + " "
                        + "Ngay Nhap: " + phieuNhap.getNgayNhap() + " " + "Ten Nha Cung Cap: "
                        + phieuNhap.getTenNhaCungCap() + " " + "Tong Tien: " + phieuNhap.getTongTien());
        // phieuNhapService.save(phieuNhap);
        // chiTietPhieuNhapService.saveAll(chiTietPhieuNhapList);

        return "redirect:/admin/management/input";
    }

    @GetMapping("/create/add-product")
    public String addProduct(Model model) {

        return "admin/input-create.html";
    }
}
