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
