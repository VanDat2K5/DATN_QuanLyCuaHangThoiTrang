package com.poly.controller.admin;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.entity.HoaDon;
import com.poly.entity.NhanVien;
import com.poly.service.ChiTietHoaDonService;
import com.poly.service.HoaDonService;
import com.poly.util.CodeGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/management/orders")
public class OrderManagementController {

	private final HoaDonService hoaDonService;
	private final ChiTietHoaDonService chitietHoaDonService;

	@Autowired
	private CodeGenerator codeGenerator;

	@Autowired
	public OrderManagementController(HoaDonService hoaDonService, ChiTietHoaDonService chitietHoaDonService) {
		this.hoaDonService = hoaDonService;
		this.chitietHoaDonService = chitietHoaDonService;
	}

	@PostMapping("/create")
	public String showCreateOrder(Model model, HttpSession session) {
		NhanVien nhanVien = (NhanVien) session.getAttribute("user");
		HoaDon hd = new HoaDon();
		hd.setMaHD(codeGenerator.generateOrderCode());
		hd.setNhanVien(nhanVien);
		hd.setNgayLap(LocalDate.now());
		model.addAttribute("orders", hd);
		return "admin/edit-order";
	}

	@GetMapping
	public String listOrders(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) String keyword) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "NgayLap"));
		Page<HoaDon> orders;

		if (keyword != null && !keyword.trim().isEmpty()) {
			orders = hoaDonService.findByMaHDContainingIgnoreCase(keyword, pageable);
			model.addAttribute("keyword", keyword);
		} else {
			orders = hoaDonService.findAll(pageable);
		}

		model.addAttribute("orders", orders.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orders.getTotalPages());
		model.addAttribute("totalItems", orders.getTotalElements());

		return "admin/order-management";
	}

	@GetMapping("/view/{id}")
	public String viewOrder(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
		Optional<HoaDon> optionalOrder = hoaDonService.findById(id);
		if (optionalOrder.isPresent()) {
			model.addAttribute("order", optionalOrder.get());
			model.addAttribute("orderDetails", chitietHoaDonService.findByHoaDon_MaHD(id));
			return "admin/order-detail";
		} else {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn với ID: " + id);
			return "redirect:/admin/management/orders";
		}
	}

	@PostMapping("/save")
	public String saveOrder(@ModelAttribute("orders") HoaDon hoaDon, RedirectAttributes redirectAttributes) {
		try {
			hoaDonService.save(hoaDon);
			return "redirect:/";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo hóa đơn.");
			return "admin/order-management";
		}
	}

	@GetMapping("/edit/{id}")
	public String editOrder(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
		Optional<HoaDon> optionalOrder = hoaDonService.findById(id);
		if (optionalOrder.isPresent()) {
			model.addAttribute("order", optionalOrder.get());
			model.addAttribute("orderDetails", chitietHoaDonService.findByHoaDon_MaHD(id));
			return "admin/edit-order";
		} else {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn để chỉnh sửa.");
			return "admin/order-management";
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteOrder(@PathVariable String id, RedirectAttributes redirectAttributes) {
		try {
			hoaDonService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa hóa đơn thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Không thể xóa hóa đơn.");
		}
		return "admin/show-order";
	}

	@PostMapping("/update")
	public String updateOrderStatus(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();

		for (String key : params.keySet()) {
			if (key.startsWith("save_")) {
				String maHD = key.substring(5);
				String trangThai = request.getParameter("trangThai_" + maHD);

				// Gọi service cập nhật trạng thái
				hoaDonService.updateTrangThaiByMaHD(maHD, trangThai);
				break;
			}
		}

		return "redirect:/admin/management/orders";
	}

}
