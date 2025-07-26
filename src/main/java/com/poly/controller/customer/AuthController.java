package com.poly.controller.customer;

import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.entity.TaiKhoan;
import com.poly.service.HoaDonService;
import com.poly.service.KhachHangService;
import com.poly.service.NhanVienService;
import com.poly.service.TaiKhoanService;
import com.poly.service.ChiTietHoaDonService;
import com.poly.util.Security;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.poly.service.DiaChiNhanHangService;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import com.poly.form.AddressForm;
import com.poly.entity.DiaChiNhanHang;
import java.util.function.Consumer;

@Controller
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private DiaChiNhanHangService diaChiNhanHangService;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        String userRole = Security.getUserRole(session);
        Object user = session.getAttribute("user");

        // Debug logging
        System.out.println("=== Profile Debug ===");
        System.out.println("user: " + (user != null ? user.getClass().getSimpleName() : "null"));
        System.out.println("userRole: " + userRole);
        System.out.println("username: " + session.getAttribute("username"));

        // Add common user data
        model.addAttribute("user", user);
        model.addAttribute("userRole", userRole);
        model.addAttribute("username", session.getAttribute("username"));

        // Add OAuth specific attributes
        Boolean oauthUser = (Boolean) session.getAttribute("oauthUser");
        Boolean needsAccount = (Boolean) session.getAttribute("needsAccount");

        // Set default values if null
        model.addAttribute("oauthUser", oauthUser != null ? oauthUser : false);
        model.addAttribute("needsAccount", needsAccount != null ? needsAccount : false);

        // Get order statistics based on user type
        List<com.poly.entity.HoaDon> allUserOrders = new ArrayList<>();
        if (user != null && "CUSTOMER".equals(userRole)) {
            KhachHang khachHang = (KhachHang) user;
            System.out.println("[DEBUG] MaKH: " + khachHang.getMaKH());
            allUserOrders = hoaDonService.findByKhachHang_MaKH(khachHang.getMaKH());
            // Lấy danh sách địa chỉ nhận hàng
            List<com.poly.entity.DiaChiNhanHang> diaChiList = diaChiNhanHangService
                    .findByKhachHang_MaKH(khachHang.getMaKH());
            System.out.println("[DEBUG] So dia chi nhan hang: " + diaChiList.size());
            for (com.poly.entity.DiaChiNhanHang dc : diaChiList) {
                System.out.println("[DEBUG] Dia chi: " + dc.getId() + " - " + dc.getTenNguoiNhan() + " - "
                        + dc.getSoDTNhanHang() + " - " + dc.getDcNhanHang());
            }
            model.addAttribute("diaChiList", diaChiList);
        } else if (user != null && !"CUSTOMER".equals(userRole)) {
            NhanVien nhanVien = (NhanVien) user;
            allUserOrders = hoaDonService.findByNhanVien_MaNV(nhanVien.getMaNV());
        }

        // Common statistics calculation
        model.addAttribute("totalOrders", allUserOrders.size());

        BigDecimal totalAmount = allUserOrders.stream()
                .map(com.poly.entity.HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalSpent", totalAmount);

        // Count orders by status
        Map<String, Long> ordersByStatus = allUserOrders.stream()
                .collect(Collectors.groupingBy(com.poly.entity.HoaDon::getTrangThai, Collectors.counting()));
        model.addAttribute("ordersByStatus", ordersByStatus);

        // Pagination for orders
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayLap"));
        Page<com.poly.entity.HoaDon> ordersPage;

        if (user != null && "CUSTOMER".equals(userRole)) {
            KhachHang khachHang = (KhachHang) user;
            // For customers, we need to get all orders and then paginate manually
            // since we don't have a direct repository method for pagination by customer
            List<com.poly.entity.HoaDon> customerOrders = hoaDonService.findByKhachHang_MaKH(khachHang.getMaKH());
            int start = page * size;
            int end = Math.min(start + size, customerOrders.size());

            List<com.poly.entity.HoaDon> pageContent = customerOrders.subList(start, end);

            // Create a custom page
            ordersPage = new Page<com.poly.entity.HoaDon>() {
                @Override
                public int getTotalPages() {
                    return (int) Math.ceil((double) customerOrders.size() / size);
                }

                @Override
                public long getTotalElements() {
                    return customerOrders.size();
                }

                @Override
                public int getNumber() {
                    return page;
                }

                @Override
                public int getSize() {
                    return size;
                }

                @Override
                public int getNumberOfElements() {
                    return pageContent.size();
                }

                @Override
                public List<com.poly.entity.HoaDon> getContent() {
                    return pageContent;
                }

                @Override
                public boolean hasContent() {
                    return !pageContent.isEmpty();
                }

                @Override
                public Sort getSort() {
                    return Sort.by(Sort.Direction.DESC, "ngayLap");
                }

                @Override
                public boolean isFirst() {
                    return page == 0;
                }

                @Override
                public boolean isLast() {
                    return page >= getTotalPages() - 1;
                }

                @Override
                public boolean hasNext() {
                    return page < getTotalPages() - 1;
                }

                @Override
                public boolean hasPrevious() {
                    return page > 0;
                }

                @Override
                public Pageable nextPageable() {
                    return hasNext() ? PageRequest.of(page + 1, size, getSort()) : null;
                }

                @Override
                public Pageable previousPageable() {
                    return hasPrevious() ? PageRequest.of(page - 1, size, getSort()) : null;
                }

                @Override
                public Pageable getPageable() {
                    return pageable;
                }

                @Override
                public Iterator<com.poly.entity.HoaDon> iterator() {
                    return pageContent.iterator();
                }

                @Override
                public <U> Page<U> map(Function<? super com.poly.entity.HoaDon, ? extends U> converter) {
                    List<U> convertedContent = pageContent.stream().map(converter).collect(Collectors.toList());
                    return new Page<U>() {
                        @Override
                        public int getTotalPages() {
                            return (int) Math.ceil((double) customerOrders.size() / size);
                        }

                        @Override
                        public long getTotalElements() {
                            return customerOrders.size();
                        }

                        @Override
                        public int getNumber() {
                            return page;
                        }

                        @Override
                        public int getSize() {
                            return size;
                        }

                        @Override
                        public int getNumberOfElements() {
                            return convertedContent.size();
                        }

                        @Override
                        public List<U> getContent() {
                            return convertedContent;
                        }

                        @Override
                        public boolean hasContent() {
                            return !convertedContent.isEmpty();
                        }

                        @Override
                        public Sort getSort() {
                            return Sort.by(Sort.Direction.DESC, "ngayLap");
                        }

                        @Override
                        public boolean isFirst() {
                            return page == 0;
                        }

                        @Override
                        public boolean isLast() {
                            return page >= getTotalPages() - 1;
                        }

                        @Override
                        public boolean hasNext() {
                            return page < getTotalPages() - 1;
                        }

                        @Override
                        public boolean hasPrevious() {
                            return page > 0;
                        }

                        @Override
                        public Pageable nextPageable() {
                            return hasNext() ? PageRequest.of(page + 1, size, getSort()) : null;
                        }

                        @Override
                        public Pageable previousPageable() {
                            return hasPrevious() ? PageRequest.of(page - 1, size, getSort()) : null;
                        }

                        @Override
                        public Pageable getPageable() {
                            return pageable;
                        }

                        @Override
                        public Iterator<U> iterator() {
                            return convertedContent.iterator();
                        }

                        @Override
                        public <V> Page<V> map(Function<? super U, ? extends V> converter) {
                            List<V> convertedContent2 = convertedContent.stream().map(converter)
                                    .collect(Collectors.toList());
                            return new Page<V>() {
                                @Override
                                public int getTotalPages() {
                                    return (int) Math.ceil((double) customerOrders.size() / size);
                                }

                                @Override
                                public long getTotalElements() {
                                    return customerOrders.size();
                                }

                                @Override
                                public int getNumber() {
                                    return page;
                                }

                                @Override
                                public int getSize() {
                                    return size;
                                }

                                @Override
                                public int getNumberOfElements() {
                                    return convertedContent2.size();
                                }

                                @Override
                                public List<V> getContent() {
                                    return convertedContent2;
                                }

                                @Override
                                public boolean hasContent() {
                                    return !convertedContent2.isEmpty();
                                }

                                @Override
                                public Sort getSort() {
                                    return Sort.by(Sort.Direction.DESC, "ngayLap");
                                }

                                @Override
                                public boolean isFirst() {
                                    return page == 0;
                                }

                                @Override
                                public boolean isLast() {
                                    return page >= getTotalPages() - 1;
                                }

                                @Override
                                public boolean hasNext() {
                                    return page < getTotalPages() - 1;
                                }

                                @Override
                                public boolean hasPrevious() {
                                    return page > 0;
                                }

                                @Override
                                public Pageable nextPageable() {
                                    return hasNext() ? PageRequest.of(page + 1, size, getSort()) : null;
                                }

                                @Override
                                public Pageable previousPageable() {
                                    return hasPrevious() ? PageRequest.of(page - 1, size, getSort()) : null;
                                }

                                @Override
                                public Pageable getPageable() {
                                    return pageable;
                                }

                                @Override
                                public Iterator<V> iterator() {
                                    return convertedContent2.iterator();
                                }

                                @Override
                                public <W> Page<W> map(Function<? super V, ? extends W> converter) {
                                    throw new UnsupportedOperationException("Nested map not supported");
                                }
                            };
                        }
                    };
                }
            };
        } else if (user != null && !"CUSTOMER".equals(userRole)) {
            NhanVien nhanVien = (NhanVien) user;
            // For employees, we need to get all orders and then paginate manually
            List<com.poly.entity.HoaDon> employeeOrders = hoaDonService.findByNhanVien_MaNV(nhanVien.getMaNV());
            int start = page * size;
            int end = Math.min(start + size, employeeOrders.size());

            List<com.poly.entity.HoaDon> pageContent = employeeOrders.subList(start, end);

            // Create a custom page similar to above
            ordersPage = new Page<com.poly.entity.HoaDon>() {
                @Override
                public int getTotalPages() {
                    return (int) Math.ceil((double) employeeOrders.size() / size);
                }

                @Override
                public long getTotalElements() {
                    return employeeOrders.size();
                }

                @Override
                public int getNumber() {
                    return page;
                }

                @Override
                public int getSize() {
                    return size;
                }

                @Override
                public int getNumberOfElements() {
                    return pageContent.size();
                }

                @Override
                public List<com.poly.entity.HoaDon> getContent() {
                    return pageContent;
                }

                @Override
                public boolean hasContent() {
                    return !pageContent.isEmpty();
                }

                @Override
                public Sort getSort() {
                    return Sort.by(Sort.Direction.DESC, "ngayLap");
                }

                @Override
                public boolean isFirst() {
                    return page == 0;
                }

                @Override
                public boolean isLast() {
                    return page >= getTotalPages() - 1;
                }

                @Override
                public boolean hasNext() {
                    return page < getTotalPages() - 1;
                }

                @Override
                public boolean hasPrevious() {
                    return page > 0;
                }

                @Override
                public Pageable nextPageable() {
                    return hasNext() ? PageRequest.of(page + 1, size, getSort()) : null;
                }

                @Override
                public Pageable previousPageable() {
                    return hasPrevious() ? PageRequest.of(page - 1, size, getSort()) : null;
                }

                @Override
                public Pageable getPageable() {
                    return pageable;
                }

                @Override
                public Iterator<com.poly.entity.HoaDon> iterator() {
                    return pageContent.iterator();
                }

                @Override
                public <U> Page<U> map(Function<? super com.poly.entity.HoaDon, ? extends U> converter) {
                    List<U> convertedContent = pageContent.stream().map(converter).collect(Collectors.toList());
                    return new Page<U>() {
                        @Override
                        public int getTotalPages() {
                            return (int) Math.ceil((double) employeeOrders.size() / size);
                        }

                        @Override
                        public long getTotalElements() {
                            return employeeOrders.size();
                        }

                        @Override
                        public int getNumber() {
                            return page;
                        }

                        @Override
                        public int getSize() {
                            return size;
                        }

                        @Override
                        public int getNumberOfElements() {
                            return convertedContent.size();
                        }

                        @Override
                        public List<U> getContent() {
                            return convertedContent;
                        }

                        @Override
                        public boolean hasContent() {
                            return !convertedContent.isEmpty();
                        }

                        @Override
                        public Sort getSort() {
                            return Sort.by(Sort.Direction.DESC, "ngayLap");
                        }

                        @Override
                        public boolean isFirst() {
                            return page == 0;
                        }

                        @Override
                        public boolean isLast() {
                            return page >= getTotalPages() - 1;
                        }

                        @Override
                        public boolean hasNext() {
                            return page < getTotalPages() - 1;
                        }

                        @Override
                        public boolean hasPrevious() {
                            return page > 0;
                        }

                        @Override
                        public Pageable nextPageable() {
                            return hasNext() ? PageRequest.of(page + 1, size, getSort()) : null;
                        }

                        @Override
                        public Pageable previousPageable() {
                            return hasPrevious() ? PageRequest.of(page - 1, size, getSort()) : null;
                        }

                        @Override
                        public Pageable getPageable() {
                            return pageable;
                        }

                        @Override
                        public Iterator<U> iterator() {
                            return convertedContent.iterator();
                        }

                        @Override
                        public <V> Page<V> map(Function<? super U, ? extends V> converter) {
                            List<V> convertedContent2 = convertedContent.stream().map(converter)
                                    .collect(Collectors.toList());
                            return new Page<V>() {
                                @Override
                                public int getTotalPages() {
                                    return (int) Math.ceil((double) employeeOrders.size() / size);
                                }

                                @Override
                                public long getTotalElements() {
                                    return employeeOrders.size();
                                }

                                @Override
                                public int getNumber() {
                                    return page;
                                }

                                @Override
                                public int getSize() {
                                    return size;
                                }

                                @Override
                                public int getNumberOfElements() {
                                    return convertedContent2.size();
                                }

                                @Override
                                public List<V> getContent() {
                                    return convertedContent2;
                                }

                                @Override
                                public boolean hasContent() {
                                    return !convertedContent2.isEmpty();
                                }

                                @Override
                                public Sort getSort() {
                                    return Sort.by(Sort.Direction.DESC, "ngayLap");
                                }

                                @Override
                                public boolean isFirst() {
                                    return page == 0;
                                }

                                @Override
                                public boolean isLast() {
                                    return page >= getTotalPages() - 1;
                                }

                                @Override
                                public boolean hasNext() {
                                    return page < getTotalPages() - 1;
                                }

                                @Override
                                public boolean hasPrevious() {
                                    return page > 0;
                                }

                                @Override
                                public Pageable nextPageable() {
                                    return hasNext() ? PageRequest.of(page + 1, size, getSort()) : null;
                                }

                                @Override
                                public Pageable previousPageable() {
                                    return hasPrevious() ? PageRequest.of(page - 1, size, getSort()) : null;
                                }

                                @Override
                                public Pageable getPageable() {
                                    return pageable;
                                }

                                @Override
                                public Iterator<V> iterator() {
                                    return convertedContent2.iterator();
                                }

                                @Override
                                public <W> Page<W> map(Function<? super V, ? extends W> converter) {
                                    throw new UnsupportedOperationException("Nested map not supported");
                                }
                            };
                        }
                    };
                }
            };
        } else {
            ordersPage = Page.empty(pageable);
        }

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "Client/demo-fashion-store-profile";
    }

    @GetMapping("/order/{orderId}")
    public String viewOrderDetails(@PathVariable String orderId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        String userRole = Security.getUserRole(session);
        Object user = session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem chi tiết đơn hàng");
            return "redirect:/user/profile";
        }

        // Find order by ID
        Optional<com.poly.entity.HoaDon> orderOpt = hoaDonService.findById(orderId);
        if (!orderOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/user/profile";
        }

        com.poly.entity.HoaDon order = orderOpt.get();

        // Check if user owns this order (for customers) or has permission (for
        // employees)
        if ("CUSTOMER".equals(userRole)) {
            KhachHang customer = (KhachHang) user;
            if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xem đơn hàng này");
                return "redirect:/user/profile";
            }
        }

        // Get order details
        List<com.poly.entity.ChiTietHoaDon> orderDetails = chiTietHoaDonService.findByHoaDon_MaHD(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("userRole", userRole);
        model.addAttribute("user", user);

        return "Client/order-details";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            Object user = session.getAttribute("user");

            if ("CUSTOMER".equals(userRole)) {
                KhachHang khachHang = (KhachHang) user;
                khachHang.setTenKH(fullName);
                khachHang.setEmail(email);
                khachHang.setSoDT(phone);
                khachHangService.save(khachHang);
                session.setAttribute("user", khachHang);
            } else {
                NhanVien nhanVien = (NhanVien) user;
                nhanVien.setTenNV(fullName);
                nhanVien.setEmail(email);
                nhanVien.setSoDT(phone);
                nhanVienService.save(nhanVien);
                session.setAttribute("user", nhanVien);
            }

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp!");
            return "redirect:/user/profile";
        }

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/create-account")
    public String createAccount(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Validation
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/user/profile";
        }

        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/user/profile";
        }

        // Check if username already exists
        if (taiKhoanService.findByTenTK(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/user/profile";
        }

        try {
            String userRole = Security.getUserRole(session);
            if (!"CUSTOMER".equals(userRole)) {
                redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể tạo tài khoản!");
                return "redirect:/user/profile";
            }

            KhachHang khachHang = (KhachHang) session.getAttribute("user");

            // Check if customer already has an account
            Optional<TaiKhoan> existingAccount = taiKhoanService.findByKhachHang(khachHang);
            if (existingAccount.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Tài khoản đã tồn tại!");
                return "redirect:/user/profile";
            }

            // Create new account
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenTK(username);
            taiKhoan.setMatKhau(password);
            taiKhoan.setKhachHang(khachHang);

            taiKhoanService.save(taiKhoan);

            // Update session - remove OAuth flags
            session.setAttribute("username", username);
            session.removeAttribute("needsAccount");
            session.removeAttribute("oauthUser");

            redirectAttributes.addFlashAttribute("success", "Tạo tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo tài khoản: " + e.getMessage());
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            Object user = session.getAttribute("user");

            // Find order by ID
            com.poly.entity.HoaDon order = hoaDonService.findById(orderId).orElse(null);
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
                return "redirect:/user/profile";
            }

            // Check if user owns this order (for customers) or has permission (for employees)
            if ("CUSTOMER".equals(userRole)) {
                KhachHang customer = (KhachHang) user;
                if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
                    redirectAttributes.addFlashAttribute("error", "Bạn không có quyền hủy đơn hàng này");
                    return "redirect:/user/profile";
                }
            }

            // Check if order can be cancelled
            if (!order.getTrangThai().equals("ChoXacNhan") && !order.getTrangThai().equals("DaThanhToan")) {
                redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng ở trạng thái này");
                return "redirect:/user/profile";
            }

            // Update order status to cancelled
            order.setTrangThai("DaHuy");
            hoaDonService.save(order);

            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi hủy đơn hàng");
        }

        return "redirect:/user/profile";
    }

    // ================== ADD ==================
    @PostMapping("/add")
    public String add(@Valid AddressForm form,
                      HttpSession session,
                      RedirectAttributes ra) {
        KhachHang kh = requireLogin(session, ra);
        if (kh == null) return redirectProfile();

        DiaChiNhanHang dc = form.toEntity(kh);
        diaChiNhanHangService.save(dc);
        ra.addFlashAttribute("success", "Đã thêm địa chỉ!");
        return redirectProfile();
    }

    // ================== DELETE ==================
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id,
                         HttpSession session,
                         RedirectAttributes ra) {
        return withOwnedAddress(id, session, ra, dc -> {
            diaChiNhanHangService.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa địa chỉ!");
        });
    }

    // ================== EDIT ==================
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Integer id,
                       @Valid AddressForm form,
                       HttpSession session,
                       RedirectAttributes ra) {
        return withOwnedAddress(id, session, ra, dc -> {
            dc.setTenNguoiNhan(form.getTenNguoiNhan());
            dc.setSoDTNhanHang(form.getSoDTNhanHang());
            dc.setDcNhanHang(form.getDcNhanHang());
            diaChiNhanHangService.save(dc);
            ra.addFlashAttribute("success", "Đã cập nhật địa chỉ!");
        });
    }

    // Helper: kiểm tra đăng nhập và quyền
    private KhachHang requireLogin(HttpSession session, RedirectAttributes ra) {
        String userRole = Security.getUserRole(session);
        Object user = session.getAttribute("user");
        if (!"CUSTOMER".equals(userRole)) {
            ra.addFlashAttribute("error", "Bạn không có quyền thực hiện hành động này.");
            return null;
        }
        return (KhachHang) user;
    }
    private String redirectProfile() {
        return "redirect:/user/profile";
    }
    private String withOwnedAddress(Integer id, HttpSession session, RedirectAttributes ra, Consumer<DiaChiNhanHang> action) {
        KhachHang kh = requireLogin(session, ra);
        if (kh == null) return redirectProfile();
        DiaChiNhanHang dc = diaChiNhanHangService.findById(id).orElse(null);
        if (dc == null || !dc.getKhachHang().getMaKH().equals(kh.getMaKH())) {
            ra.addFlashAttribute("error", "Bạn không có quyền sửa địa chỉ này.");
            return redirectProfile();
        }
        action.accept(dc);
        return redirectProfile();
    }
}
