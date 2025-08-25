package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String customerChat(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-chat";
    }

    @GetMapping("/admin/chat")
    public String adminChat() {
        return "admin/chat-management";
    }

}
