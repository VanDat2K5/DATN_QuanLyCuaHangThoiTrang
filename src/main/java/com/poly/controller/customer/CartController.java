package com.poly.controller.customer;

import com.poly.entity.KhachHang;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/cart")

    public String cartPage(Model model, HttpSession session) {
        KhachHang user = (KhachHang) session.getAttribute("user");
        if (user != null) {
            String maKH = user.getMaKH();
            model.addAttribute("maKH", maKH); // Gửi qua view nếu cần
        }
        return "Client/demo-fashion-store-cart";
    }
}
