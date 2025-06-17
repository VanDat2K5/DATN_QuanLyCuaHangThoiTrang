package com.poly.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class Security {

    // Kiểm tra xem người dùng đã đăng nhập chưa
    public static boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    // Kiểm tra vai trò của người dùng
    public static String getUserRole(HttpSession session) {
        if (session.getAttribute("userRole") != null) {
            return (String) session.getAttribute("userRole");
        }
        return null;
    }

    // Kiểm tra quyền truy cập cho khách hàng
    public static boolean hasCustomerAccess(HttpSession session) {
        String role = getUserRole(session);
        return "CUSTOMER".equals(role);
    }

    // Kiểm tra quyền truy cập cho nhân viên
    public static boolean hasStaffAccess(HttpSession session) {
        String role = getUserRole(session);
        return "STAFF".equals(role) || "ADMIN".equals(role);
    }

    // Kiểm tra quyền truy cập cho admin
    public static boolean hasAdminAccess(HttpSession session) {
        String role = getUserRole(session);
        return "ADMIN".equals(role);
    }

    // Xử lý chuyển hướng dựa trên vai trò
    public static void handleAccessControl(HttpServletRequest request, HttpServletResponse response,
            String requiredRole) throws IOException {
        HttpSession session = request.getSession();

        if (!isAuthenticated(session)) {
            // Nếu chưa đăng nhập, chuyển về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // String userRole = getUserRole(session);

        switch (requiredRole) {
            case "CUSTOMER":
                if (!hasCustomerAccess(session)) {
                    response.sendRedirect(request.getContextPath() + "/access-denied");
                }
                break;
            case "STAFF":
                if (!hasStaffAccess(session)) {
                    response.sendRedirect(request.getContextPath() + "/access-denied");
                }
                break;
            case "ADMIN":
                if (!hasAdminAccess(session)) {
                    response.sendRedirect(request.getContextPath() + "/access-denied");
                }
                break;
        }
    }
}
