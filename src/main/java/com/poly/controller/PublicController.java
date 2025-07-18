package com.poly.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/")
    public String publicHome(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store";
    }

    @GetMapping("/about")
    public String publicAbout(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-about";
    }

    @GetMapping("/contact")
    public String publicContact(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-contact";
    }

    @GetMapping("/faq")
    public String publicFaq(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-faq";
    }

    // Trang tạp chí
    @GetMapping("/magazine")
    public String publicMagazine(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-magazine";
    }

    // // Trang shop với lọc theo giới tính
    // @GetMapping("/shop")
    // public String publicShop(HttpSession session, Model model) {
    // if (session.getAttribute("user") != null) {
    // model.addAttribute("user", session.getAttribute("user"));
    // model.addAttribute("username", session.getAttribute("username"));
    // model.addAttribute("userRole", session.getAttribute("userRole"));
    // }
    // return "Client/demo-fashion-store-shop";
    // }

    // Trang shop với lọc theo giới tính
    // @GetMapping("/shop")
    // public String publicShop(HttpSession session, Model model,
    // @RequestParam(value = "gender", required = false) String gender) {
    // if (session.getAttribute("user") != null) {
    // model.addAttribute("user", session.getAttribute("user"));
    // model.addAttribute("username", session.getAttribute("username"));
    // model.addAttribute("userRole", session.getAttribute("userRole"));
    // }
    //
    // List<SanPham> products;
    // if (gender != null && !gender.isEmpty()) {
    // try {
    // SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
    // products = sanPhamService.findByGioiTinh(genderEnum);
    // model.addAttribute("selectedGender", genderEnum);
    // } catch (IllegalArgumentException e) {
    // products = sanPhamService.findAll();
    // }
    // } else {
    // products = sanPhamService.findAll();
    // }
    //
    // model.addAttribute("products", products);
    // model.addAttribute("genders", SanPham.Gender.values());
    // return "Client/demo-fashion-store-shop";
    // }

    // Trang collection với lọc theo giới tính
    // // Trang collection với lọc theo giới tính
    // // Trang collection với lọc theo giới tính
    // @GetMapping("/collection")
    // public String publicCollection(HttpSession session, Model model,
    // @RequestParam(value = "gender", required = false) String gender) {
    // // Gán thông tin user nếu đã đăng nhập
    // if (session.getAttribute("user") != null) {
    // model.addAttribute("user", session.getAttribute("user"));
    // model.addAttribute("username", session.getAttribute("username"));
    // model.addAttribute("userRole", session.getAttribute("userRole"));
    // }
    //
    // List<SanPham> sanPhams;
    //
    // try {
    // if (gender != null && !gender.isEmpty()) {
    // SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
    // sanPhams = sanPhamService.findByGioiTinh(genderEnum);
    // model.addAttribute("selectedGender", genderEnum);
    // } else {
    // sanPhams = sanPhamService.findAll();
    // }
    // } catch (IllegalArgumentException e) {
    // sanPhams = sanPhamService.findAll();
    // }
    //
    // List<SanPhamViewDTO> products = sanPhams.stream()
    // .map(SanPhamViewDTO::new)
    // .toList();
    //
    // model.addAttribute("products", products);
    // model.addAttribute("genders", SanPham.Gender.values());
    // return "Client/demo-fashion-store-collection";
    // }
    //
    //
    // // API endpoint để lọc sản phẩm theo giới tính (cho AJAX)
    // @GetMapping("/api/products/by-gender")
    // public String getProductsByGender(@RequestParam("gender") String gender,
    // Model model) {
    // List<SanPhamViewDTO> products;
    //
    // try {
    // SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
    // products = sanPhamService.findByGioiTinh(genderEnum)
    // .stream()
    // .map(SanPhamViewDTO::new)
    // .toList();
    // model.addAttribute("selectedGender", genderEnum);
    // } catch (IllegalArgumentException e) {
    // products = sanPhamService.findAll()
    // .stream()
    // .map(SanPhamViewDTO::new)
    // .toList();
    // }
    //
    // model.addAttribute("products", products);
    // model.addAttribute("genders", SanPham.Gender.values());
    // return "Client/fragments/product-list"; // Fragment để render lại danh sách
    // sản phẩm
    // }

}