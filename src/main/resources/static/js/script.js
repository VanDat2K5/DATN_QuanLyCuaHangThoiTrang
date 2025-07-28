// JavaScript để chọn/deselect tất cả các checkbox
document.getElementById("select-all").addEventListener("click", function () {
  const checkboxes = document.querySelectorAll(".select-checkbox");
  checkboxes.forEach(checkbox => (checkbox.checked = this.checked));
});

// JavaScript để áp dụng hành động (chỉnh sửa/xóa)
document.querySelector("#apply-action-btn").addEventListener("click", function () {
  const selectedAction = document.querySelector("#action-select").value;
  const selectedProducts = Array.from(document.querySelectorAll(".select-checkbox:checked"));

  if (selectedProducts.length > 0 && selectedAction) {
    if (selectedAction === "delete") {
      alert("Đang xóa các mục đã chọn");
      document.getElementById("select-all").checked = false;
      selectedProducts.forEach(checkbox => (checkbox.checked = false));
    }
  } else {
    alert("Vui lòng chọn hành động và sản phẩm để áp dụng");
  }
});

// ==================== COMMON UTILITY FUNCTIONS ====================
/**
 * Format date từ yyyy-MM-dd sang dd/MM/yyyy
 * @param {string} dateString - Chuỗi ngày tháng
 * @returns {string} - Ngày tháng đã format
 */
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
}

/**
 * Auto-hide alerts sau 5 giây
 */
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert && alert.parentNode) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000);
    });
});

// ==================== VOUCHER MANAGEMENT FUNCTIONS ====================
/**
 * Hiển thị modal chỉnh sửa voucher
 * @param {string} maKM - Mã voucher
 * @param {string} tenKM - Tên voucher
 * @param {number} giam - Phần trăm giảm giá
 * @param {number} gioiHan - Giới hạn sử dụng
 * @param {string} ngayBatDau - Ngày bắt đầu
 * @param {string} ngayKetThuc - Ngày kết thúc
 */
function editVoucher(maKM, tenKM, giam, gioiHan, ngayBatDau, ngayKetThuc) {
    // Điền thông tin vào form chỉnh sửa
    document.getElementById('editMaKM').value = maKM;
    document.getElementById('editTenKM').value = tenKM;
    document.getElementById('editGiam').value = giam;
    document.getElementById('editGioiHan').value = gioiHan;
    document.getElementById('editNgayBatDau').value = ngayBatDau;
    document.getElementById('editNgayKetThuc').value = ngayKetThuc;
    
    // Thiết lập action URL cho form
    document.getElementById('editForm').action = `/admin/management/voucher/edit/${maKM}`;
    
    // Hiển thị modal
    new bootstrap.Modal(document.getElementById('editVoucherModal')).show();
}

/**
 * Hiển thị modal xác nhận xóa voucher
 * @param {string} maKM - Mã voucher cần xóa
 */
function deleteVoucher(maKM) {
    // Tạo instance modal
    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    const form = document.getElementById('deleteForm');
    
    // Thiết lập action URL cho form xóa
    form.action = `/admin/management/voucher/delete/${maKM}`;
    
    // Hiển thị modal
    modal.show();
}

// ==================== CUSTOMER MANAGEMENT FUNCTIONS ====================
/**
 * Hiển thị modal chỉnh sửa khách hàng
 * @param {string} maKH - Mã khách hàng
 * @param {string} tenKH - Tên khách hàng
 * @param {string} email - Email khách hàng
 * @param {string} soDT - Số điện thoại khách hàng
 */
function editCustomer(maKH, tenKH, email, soDT) {
    // Điền thông tin vào form chỉnh sửa
    document.getElementById('editMaKH').value = maKH;
    document.getElementById('editTenKH').value = tenKH;
    document.getElementById('editEmail').value = email;
    document.getElementById('editSoDT').value = soDT;
    
    // Thiết lập action URL cho form
    document.getElementById('editForm').action = `/admin/management/customer/edit/${maKH}`;
    
    // Hiển thị modal
    new bootstrap.Modal(document.getElementById('editCustomerModal')).show();
}

/**
 * Hiển thị modal xác nhận xóa khách hàng
 * @param {string} maKH - Mã khách hàng cần xóa
 */
function deleteCustomer(maKH) {
    // Tạo instance modal
    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    const form = document.getElementById('deleteForm');
    
    // Thiết lập action URL cho form xóa
    form.action = `/admin/management/customer/delete/${maKH}`;
    
    // Hiển thị modal
    modal.show();
}
