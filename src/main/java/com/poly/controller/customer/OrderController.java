package com.poly.controller.customer;

import java.time.LocalDate;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.entity.HoaDon;
import com.poly.entity.NhanVien;

import jakarta.servlet.http.HttpSession;

public class OrderController {

	@GetMapping("/create")
	public String showCreateOrder(Model model, HttpSession session) {
		NhanVien nhanVien = (NhanVien) session.getAttribute("user");
		HoaDon hd = new HoaDon();
		hd.setMaHD(codeGenerator.generateOrderCode());
		hd.setNhanVien(nhanVien);
		hd.setNgayLap(LocalDate.now());
		model.addAttribute("orders", hd);
		return "admin/edit-order";
	}


}
