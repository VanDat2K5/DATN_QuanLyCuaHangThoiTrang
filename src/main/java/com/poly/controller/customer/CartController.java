package com.poly.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String cartPage(@RequestParam(name = "maKH", required = false) String maKH, Model model) {
        model.addAttribute("maKH", maKH); // có thể là null
        return "Client/demo-fashion-store-cart";
    }
}
