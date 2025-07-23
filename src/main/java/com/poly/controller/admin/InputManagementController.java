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
import org.springframework.web.bind.annotation.RequestParam;

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

    // @GetMapping("/edit/{maPN}")
    // public String editInput(@PathVariable("maPN") String maPN, Model model) {
    // Optional<PhieuNhap> phieuNhapOpt = phieuNhapService.findById(maPN);
    // if (phieuNhapOpt.isPresent()) {
    // PhieuNhap phieuNhap = phieuNhapOpt.get();
    // List<ChiTietPhieuNhap> chiTietPhieuNhap =
    // chiTietPhieuNhapService.findByPhieuNhap_MaPN(phieuNhap.getMaPN());
    // model.addAttribute("chiTietPhieuNhaps", chiTietPhieuNhap);
    // model.addAttribute("phieuNhap", phieuNhap);
    // } else {
    // return "redirect:/admin/management/input";
    // }
    // return "admin/input-edit.html";
    // }

    @GetMapping("/create")
    public String createInput(Model model, HttpSession session) {
        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setMaPN(codeGenerator.generateImportCode());
        phieuNhap.setTongTien(BigDecimal.ZERO);
        phieuNhap.setNgayNhap(LocalDate.now());
        phieuNhap.setNhanVien((NhanVien) session.getAttribute("user"));
        phieuNhap.setTenNhaCungCap("Tên Nhà Cung Cấp");
        phieuNhapService.save(phieuNhap);

        model.addAttribute("phieuNhap", phieuNhap);

        model.addAttribute("sanPhams", sanPhamService.findAll());
        model.addAttribute("mauSacs", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());

        return "admin/input-create.html";
    }

    @PostMapping("/create/add-product")
    public String addProductToList(
            @RequestParam String loaiThem,
            @RequestParam(required = false) String maSP,
            @RequestParam(required = false) String tenSpMoi,
            @RequestParam String mauSac,
            @RequestParam String size,
            @RequestParam int soLuongNhap,
            @RequestParam double giaNhap,
            @RequestParam String loHang,
            HttpSession session,
            Model model) {
        List<ChiTietPhieuNhap> productList = (List<ChiTietPhieuNhap>) session.getAttribute("productList");
        if (productList == null)
            productList = new ArrayList<>();
        ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
        SanPham sp = new SanPham();
        if ("cu".equals(loaiThem)) {
            sp.setMaSP(maSP);
            SanPham spDB = sanPhamService.findById(maSP).orElse(null);
            if (spDB != null)
                sp.setTenSP(spDB.getTenSP());
        } else {
            sp.setMaSP(tenSpMoi != null ? tenSpMoi.replaceAll("\\s+", "_").toUpperCase() : "MOI");
            sp.setTenSP(tenSpMoi);
        }
        Mau m = new Mau();
        m.setMaMau(mauSac);
        Size s = new Size();
        s.setMaSize(size);
        chiTiet.setChiTietSanPham(new com.poly.entity.ChiTietSanPham());
        chiTiet.getChiTietSanPham().setSanPham(sp);
        chiTiet.getChiTietSanPham().setMau(m);
        chiTiet.getChiTietSanPham().setSize(s);
        chiTiet.setSoLuongNhap(soLuongNhap);
        chiTiet.setGiaNhap(BigDecimal.valueOf(giaNhap));
        chiTiet.setLoHang(loHang);
        productList.add(chiTiet);
        session.setAttribute("productList", productList);

        // Render lại view
        model.addAttribute("phieuNhap", new PhieuNhap());
        model.addAttribute("productList", productList);
        model.addAttribute("sanPhams", sanPhamService.findAll());
        model.addAttribute("mauSacs", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/input-create.html";
    }

    @GetMapping("/create/remove-product/{index}")
    public String removeProductFromList(@PathVariable int index, HttpSession session, Model model) {
        List<ChiTietPhieuNhap> productList = (List<ChiTietPhieuNhap>) session.getAttribute("productList");
        if (productList != null && index >= 0 && index < productList.size()) {
            productList.remove(index);
        }
        session.setAttribute("productList", productList);
        model.addAttribute("phieuNhap", new PhieuNhap());
        model.addAttribute("productList", productList);
        model.addAttribute("sanPhams", sanPhamService.findAll());
        model.addAttribute("mauSacs", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/input-create.html";
    }

    @PostMapping("/create")
    public String saveInput(@ModelAttribute PhieuNhap phieuNhap, HttpSession session) {
        List<ChiTietPhieuNhap> productList = (List<ChiTietPhieuNhap>) session.getAttribute("productList");
        if (productList != null && !productList.isEmpty()) {
            // Lưu phiếu nhập trước (nếu chưa có)
            if (phieuNhap.getMaPN() == null || phieuNhap.getMaPN().isEmpty()) {
                phieuNhap.setMaPN(codeGenerator.generateImportCode());
            }
            // Tính tổng tiền
            BigDecimal tongTien = BigDecimal.ZERO;
            for (ChiTietPhieuNhap ct : productList) {
                tongTien = tongTien.add(ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuongNhap())));
            }
            phieuNhap.setTongTien(tongTien);
            phieuNhapService.save(phieuNhap);

            // Lưu chi tiết phiếu nhập
            for (ChiTietPhieuNhap ct : productList) {
                ct.setPhieuNhap(phieuNhap);
                chiTietPhieuNhapService.save(ct);
            }
        }
        session.removeAttribute("productList");
        return "redirect:/admin/management/input";
    }
}
