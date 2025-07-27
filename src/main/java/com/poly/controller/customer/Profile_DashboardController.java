package com.poly.controller.customer;

import com.poly.entity.ChiTietHoaDon;
import com.poly.entity.DiaChiNhanHang;
import com.poly.entity.HoaDon;
import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.service.HoaDonService;
import com.poly.service.ChiTietHoaDonService;
import com.poly.service.DiaChiNhanHangService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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

@Controller
@RequestMapping("/user")
public class Profile_DashboardController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    @Autowired
    private DiaChiNhanHangService diaChiNhanHangService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println("[DEBUG] Profile GET called");
        
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
        List<HoaDon> allUserOrders = new ArrayList<>();
        if (user != null && "CUSTOMER".equals(userRole)) {
            KhachHang khachHang = (KhachHang) user;
            System.out.println("[DEBUG] MaKH: " + khachHang.getMaKH());
            allUserOrders = hoaDonService.findByKhachHang_MaKH(khachHang.getMaKH());
            // Lấy danh sách địa chỉ nhận hàng
            List<DiaChiNhanHang> diaChiList = diaChiNhanHangService
                    .findByKhachHang_MaKH(khachHang.getMaKH());
            System.out.println("[DEBUG] So dia chi nhan hang: " + diaChiList.size());
            for (DiaChiNhanHang dc : diaChiList) {
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
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalSpent", totalAmount);

        // Count orders by status
        Map<String, Long> ordersByStatus = allUserOrders.stream()
                .collect(Collectors.groupingBy(HoaDon::getTrangThai, Collectors.counting()));
        model.addAttribute("ordersByStatus", ordersByStatus);

        // Pagination for orders
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayLap"));
        Page<HoaDon> ordersPage;

        if (user != null && "CUSTOMER".equals(userRole)) {
            KhachHang khachHang = (KhachHang) user;
            // For customers, we need to get all orders and then paginate manually
            // since we don't have a direct repository method for pagination by customer
            List<HoaDon> customerOrders = hoaDonService.findByKhachHang_MaKH(khachHang.getMaKH());
            int start = page * size;
            int end = Math.min(start + size, customerOrders.size());

            List<HoaDon> pageContent = customerOrders.subList(start, end);

            // Create a custom page
            ordersPage = new Page<HoaDon>() {
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
                public List<HoaDon> getContent() {
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
                public Iterator<HoaDon> iterator() {
                    return pageContent.iterator();
                }

                @Override
                public <U> Page<U> map(Function<? super HoaDon, ? extends U> converter) {
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
            List<HoaDon> employeeOrders = hoaDonService.findByNhanVien_MaNV(nhanVien.getMaNV());
            int start = page * size;
            int end = Math.min(start + size, employeeOrders.size());

            List<HoaDon> pageContent = employeeOrders.subList(start, end);

            // Create a custom page similar to above
            ordersPage = new Page<HoaDon>() {
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
                public List<HoaDon> getContent() {
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
                public Iterator<HoaDon> iterator() {
                    return pageContent.iterator();
                }

                @Override
                public <U> Page<U> map(Function<? super HoaDon, ? extends U> converter) {
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

        return "Client/demo-fashion-store-profile-new";
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
        Optional<HoaDon> orderOpt = hoaDonService.findById(orderId);
        if (!orderOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/user/profile";
        }

        HoaDon order = orderOpt.get();

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
        List<ChiTietHoaDon> orderDetails = chiTietHoaDonService.findByHoaDon_MaHD(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("userRole", userRole);
        model.addAttribute("user", user);

        return "Client/order-details";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            Object user = session.getAttribute("user");

            // Find order by ID
            HoaDon order = hoaDonService.findById(orderId).orElse(null);
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
    public String add(@ModelAttribute DiaChiNhanHang dc,
                     HttpSession session,
                     RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            if (!"CUSTOMER".equals(userRole)) {
                redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể thêm địa chỉ!");
                return "redirect:/user/profile";
            }

            KhachHang khachHang = (KhachHang) session.getAttribute("user");
            dc.setKhachHang(khachHang);

            diaChiNhanHangService.save(dc);
            redirectAttributes.addFlashAttribute("success", "Thêm địa chỉ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm địa chỉ: " + e.getMessage());
        }
        return "redirect:/user/profile";
    }

    // ================== DELETE ==================
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            if (!"CUSTOMER".equals(userRole)) {
                redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể xóa địa chỉ!");
                return "redirect:/user/profile";
            }
            
            diaChiNhanHangService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa địa chỉ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa địa chỉ: " + e.getMessage());
        }
        return "redirect:/user/profile";
    }

    // ================== EDIT ==================
    @GetMapping("/edit-address/{id}")
    public String editAddress(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("[DEBUG] Edit GET called with id: " + id);
            
            String userRole = Security.getUserRole(session);
            if (!"CUSTOMER".equals(userRole)) {
                redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể sửa địa chỉ!");
                return "redirect:/user/profile";
            }
            
            Object user = session.getAttribute("user");
            
            // Add common user data
            model.addAttribute("user", user);
            model.addAttribute("userRole", userRole);
            model.addAttribute("username", session.getAttribute("username"));
            
            // Add OAuth specific attributes
            Boolean oauthUser = (Boolean) session.getAttribute("oauthUser");
            Boolean needsAccount = (Boolean) session.getAttribute("needsAccount");
            model.addAttribute("oauthUser", oauthUser != null ? oauthUser : false);
            model.addAttribute("needsAccount", needsAccount != null ? needsAccount : false);
            
            // Load address to edit
            DiaChiNhanHang dc = diaChiNhanHangService.findById(id).orElse(null);
            if (dc == null) {
                System.out.println("[DEBUG] Address not found with id: " + id);
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy địa chỉ!");
                return "redirect:/user/profile";
            }
            
            System.out.println("[DEBUG] Found address: " + dc.getTenNguoiNhan() + " - " + dc.getDcNhanHang());
            
            // Load all addresses for the customer
            if (user != null && "CUSTOMER".equals(userRole)) {
                KhachHang khachHang = (KhachHang) user;
                List<DiaChiNhanHang> diaChiList = diaChiNhanHangService
                        .findByKhachHang_MaKH(khachHang.getMaKH());
                model.addAttribute("diaChiList", diaChiList);
                System.out.println("[DEBUG] Loaded " + diaChiList.size() + " addresses for customer");
            }
            
            model.addAttribute("dc", dc);
            System.out.println("[DEBUG] Returning to template with dc: " + (dc != null ? "not null" : "null"));
            return "Client/demo-fashion-store-profile-new";
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception in edit GET: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/user/profile";
        }
    }

        @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute DiaChiNhanHang dc,
                      @PathVariable Integer id,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            if (!"CUSTOMER".equals(userRole)) {
                redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể sửa địa chỉ!");
                return "redirect:/user/profile";
            }

            // Lấy địa chỉ hiện tại để giữ nguyên khách hàng
            DiaChiNhanHang existingDc = diaChiNhanHangService.findById(id).orElse(null);
            if (existingDc == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy địa chỉ!");
                return "redirect:/user/profile";
            }

            // Cập nhật thông tin mới nhưng giữ nguyên khách hàng
            dc.setId(id);
            dc.setKhachHang(existingDc.getKhachHang());

            diaChiNhanHangService.save(dc);
            redirectAttributes.addFlashAttribute("success", "Cập nhật địa chỉ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật địa chỉ: " + e.getMessage());
        }
        return "redirect:/user/profile";
    }
} 