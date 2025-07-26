package com.poly.controller.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.entity.ChiTietPhieuNhap;
import com.poly.entity.ChiTietSanPham;
import com.poly.entity.Mau;
import com.poly.entity.NhanVien;
import com.poly.entity.PhieuNhap;
import com.poly.entity.SanPham;
import com.poly.entity.Size;

import com.poly.service.ChiTietSanPhamService;
import com.poly.service.ChiTietPhieuNhapService;
import com.poly.service.LoaiSanPhamService;
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
    private final LoaiSanPhamService loaiSanPhamService;

    @Autowired
    public InputManagementController(SanPhamService sanPhamService, ChiTietSanPhamService chiTietSanPhamService,
            MauService mauService, SizeService sizeService, PhieuNhapService phieuNhapService,
            ChiTietPhieuNhapService chiTietPhieuNhapService, CodeGenerator codeGenerator,
            LoaiSanPhamService loaiSanPhamService) {
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
        this.mauService = mauService;
        this.sizeService = sizeService;
        this.phieuNhapService = phieuNhapService;
        this.chiTietPhieuNhapService = chiTietPhieuNhapService;
        this.codeGenerator = codeGenerator;
        this.loaiSanPhamService = loaiSanPhamService;
    }

    @GetMapping
    public String inputManagement(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PhieuNhap> phieuNhapPage = phieuNhapService.findAll(pageable);
        model.addAttribute("phieuNhaps", phieuNhapPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phieuNhapPage.getTotalPages());
        model.addAttribute("totalItems", phieuNhapPage.getTotalElements());
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
        PhieuNhap phieuNhap = (PhieuNhap) session.getAttribute("phieuNhap");
        if (phieuNhap == null) {
            phieuNhap = new PhieuNhap();
            phieuNhap.setMaPN(codeGenerator.generateImportCode());
            phieuNhap.setTongTien(BigDecimal.ZERO);
            phieuNhap.setNgayNhap(LocalDate.now());
            phieuNhap.setNhanVien((NhanVien) session.getAttribute("user"));
            phieuNhap.setTenNhaCungCap("Ten nha cung cap");
            session.setAttribute("phieuNhap", phieuNhap);
        }
        model.addAttribute("phieuNhap", phieuNhap);
        model.addAttribute("sanPhams", sanPhamService.findAll());
        model.addAttribute("mauSacs", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("loaiSanPhams", loaiSanPhamService.findAll());
        List<ChiTietPhieuNhap> productList = (List<ChiTietPhieuNhap>) session.getAttribute("productList");
        if (productList == null)
            productList = new ArrayList<>();
        model.addAttribute("productList", productList);
        return "admin/input-create.html";
    }

    @PostMapping("/create/add-product")
    public String addProductToList(
            @RequestParam String loaiThem,
            @RequestParam(required = false) String maSP,
            @RequestParam(required = false) String maLoaiSP,
            @RequestParam String mauSac,
            @RequestParam String size,
            @RequestParam int soLuongNhap,
            @RequestParam double giaNhap,
            HttpSession session,
            Model model) {
        List<ChiTietPhieuNhap> productList = (List<ChiTietPhieuNhap>) session.getAttribute("productList");
        if (productList == null)
            productList = new ArrayList<>();
        // Nếu là sản phẩm mới, chuyển hướng sang trang tạo sản phẩm mới
        if ("moi".equals(loaiThem)) {
            return "redirect:/admin/management/product/create?returnToInput=true";
        }
        // Nếu là sản phẩm cũ, thêm vào list tạm
        ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
        SanPham sp = new SanPham();
        sp.setMaSP(maSP);
        SanPham spDB = sanPhamService.findById(maSP).orElse(null);
        if (spDB != null)
            sp.setTenSP(spDB.getTenSP());
        Mau m = new Mau();
        m.setMaMau(mauSac);
        Size s = new Size();
        s.setMaSize(size);

        chiTiet.setSoLuongNhap(soLuongNhap);
        chiTiet.setGiaNhap(BigDecimal.valueOf(giaNhap));
        chiTiet.setLoHang(codeGenerator.generateImportDetailCode(maSP, mauSac, size));

        chiTiet.setChiTietSanPham(new ChiTietSanPham());
        chiTiet.getChiTietSanPham().setId(codeGenerator.generateProductDetailCode(maSP, mauSac, size));
        chiTiet.getChiTietSanPham().setSanPham(sp);
        chiTiet.getChiTietSanPham().setMau(m);
        chiTiet.getChiTietSanPham().setSize(s);
        chiTiet.getChiTietSanPham().setSoLuong(soLuongNhap);
        chiTiet.getChiTietSanPham().setGiaNhap(BigDecimal.valueOf(giaNhap));
        chiTiet.getChiTietSanPham().setGiaXuat(BigDecimal.valueOf(giaNhap * 1.2));
        chiTiet.getChiTietSanPham().setLoHang(codeGenerator.generateImportDetailCode(maSP, mauSac, size));

        productList.add(chiTiet);
        session.setAttribute("productList", productList);
        // Lấy lại phieuNhap từ session để giữ thông tin
        PhieuNhap phieuNhap = (PhieuNhap) session.getAttribute("phieuNhap");
        model.addAttribute("phieuNhap", phieuNhap);
        model.addAttribute("productList", productList);
        model.addAttribute("sanPhams", sanPhamService.findAll());
        model.addAttribute("mauSacs", mauService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("loaiSanPhams", loaiSanPhamService.findAll());
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
        PhieuNhap phieuNhapSession = (PhieuNhap) session.getAttribute("phieuNhap");
        if (phieuNhapSession != null) {
            // Cập nhật lại thông tin phiếu nhập từ form
            phieuNhapSession.setTenNhaCungCap(phieuNhap.getTenNhaCungCap());
            phieuNhapSession.setNgayNhap(phieuNhap.getNgayNhap());
            // Tính tổng tiền
            BigDecimal tongTien = BigDecimal.ZERO;
            if (productList != null) {
                for (ChiTietPhieuNhap ct : productList) {
                    tongTien = tongTien.add(ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuongNhap())));
                }
            }
            phieuNhapSession.setTongTien(tongTien);
            phieuNhapService.save(phieuNhapSession);
            // Lưu chi tiết phiếu nhập
            if (productList != null) {
                for (ChiTietPhieuNhap ct : productList) {
                    ct.setPhieuNhap(phieuNhapSession);
                    chiTietSanPhamService.save(ct.getChiTietSanPham());
                    chiTietPhieuNhapService.save(ct);
                }
            }
        }
        session.removeAttribute("productList");
        session.removeAttribute("phieuNhap");
        return "redirect:/admin/management/input";
    }

    // API tìm kiếm sản phẩm autocomplete
    @GetMapping("/api/search-product")
    @ResponseBody
    public List<SanPham> searchProduct(@RequestParam String query, @RequestParam(required = false) String maLoaiSP) {
        return sanPhamService.findAll().stream()
                .filter(sp -> (maLoaiSP == null || maLoaiSP.isEmpty()
                        || (sp.getLoaiSP() != null && maLoaiSP.equals(sp.getLoaiSP().getMaLoaiSP())))
                        && (sp.getMaSP().toLowerCase().contains(query.toLowerCase())
                                || sp.getTenSP().toLowerCase().contains(query.toLowerCase())))
                .limit(10)
                .collect(Collectors.toList());
    }

    // API lấy chi tiết size/màu theo mã sản phẩm
    @GetMapping("/api/product-detail")
    @ResponseBody
    public java.util.Map<String, Object> getProductDetail(@RequestParam String maSP) {
        List<com.poly.entity.ChiTietSanPham> chiTietList = chiTietSanPhamService.findBySanPham_MaSP(maSP);
        List<Mau> mauSacs = chiTietList.stream().map(ct -> ct.getMau()).distinct().collect(Collectors.toList());
        List<Size> sizes = chiTietList.stream().map(ct -> ct.getSize()).distinct().collect(Collectors.toList());
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("mauSacs", mauSacs);
        map.put("sizes", sizes);
        return map;
    }

    @GetMapping("/api/products-by-type")
    @ResponseBody
    public List<SanPham> getProductsByType(@RequestParam String maLoaiSP) {
        return sanPhamService.findAll().stream()
                .filter(sp -> sp.getLoaiSP() != null && maLoaiSP.equals(sp.getLoaiSP().getMaLoaiSP()))
                .collect(Collectors.toList());
    }
}
