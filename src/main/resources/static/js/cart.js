// Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyAiQr7IZK9i8x5V0BglQIRLfr0G1ByMkrU",
    authDomain: "datn-d3877.firebaseapp.com",
    databaseURL: "https://datn-d3877-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "datn-d3877",
    storageBucket: "datn-d3877.firebasestorage.app",
    messagingSenderId: "416913725261",
    appId: "1:416913725261:web:b9890b8eabbfeb215c8904",
    measurementId: "G-J0RLZQBK1V"
};
firebase.initializeApp(firebaseConfig);
const db = firebase.database();

// --- CARTBOX ---
function addCart() {
    const maKH = document.getElementById('cartMaKH')?.value;
    const tenSP = document.getElementById('carttenSP')?.textContent.trim();
    const hinhAnh = document.getElementById('cartanhChinh')?.src;
    const mau = document.querySelector('input[name="maMau"]:checked')?.value || '';
    const size = document.querySelector('input[name="maSize"]:checked')?.value || '';
    const soLuong = parseInt(document.getElementById('cartSoLuong')?.value);
    const giaText = document.getElementById('cartGia')?.textContent.replace(/[^\d]/g, '');
    const gia = parseFloat(giaText);
    const maSP = document.querySelector('input[name="maSP"]')?.value || '';

    // 👉 Nếu chưa đăng nhập
    if (!maKH) {
        Swal.fire({
            icon: 'warning',
            title: 'Bạn cần đăng nhập để thêm vào giỏ hàng!',
            showConfirmButton: false,
            timer: 2500
        });

        setTimeout(() => {
            window.location.href = '/login'; // 👉 Đổi URL nếu cần
        }, 2500);

        return;
    }
    if (!tenSP || !hinhAnh || !mau || !size || !soLuong || !gia) {
        const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
              toast.onmouseenter = Swal.stopTimer;
              toast.onmouseleave = Swal.resumeTimer;
            }
          });
          Toast.fire({
            icon: "error",
            title: "Vui lòng chọn đầy đủ thông tin sản phẩm!"
          });
        return;
    }

    const item = { tenSP, hinhAnh, mau, size, SoLuong: soLuong, Gia: gia, maSP };
    const cartRef = db.ref('Cart/' + maKH + '/items');

    cartRef.once('value').then(snapshot => {
        let exists = false;
        let existingKey = null;
        snapshot.forEach(child => {
            const val = child.val();
            if (val.tenSP === tenSP && val.mau === mau && val.size === size) {
                exists = true;
                existingKey = child.key;
            }
        });

        if (exists && existingKey) {
            const existingItemRef = cartRef.child(existingKey);
            existingItemRef.child('SoLuong').once('value').then(qSnap => {
                const oldQty = qSnap.val() || 0;
                existingItemRef.update({ SoLuong: oldQty + soLuong }).then(() => {
                    const Toast = Swal.mixin({
                        toast: true,
                        position: "top-end",
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                          toast.onmouseenter = Swal.stopTimer;
                          toast.onmouseleave = Swal.resumeTimer;
                        }
                      });
                      Toast.fire({
                        icon: "success",
                        title: "Cập nhật số lượng sản phẩm thành công!"
                      });
                    resetCartForm();
                    loadCarts(maKH);
                    updateCartItemCount(maKH);
                });
            });
        } else {
            cartRef.push(item).then(() => {
                const Toast = Swal.mixin({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                      toast.onmouseenter = Swal.stopTimer;
                      toast.onmouseleave = Swal.resumeTimer;
                    }
                  });
                  Toast.fire({
                    icon: "success",
                    title: "Thêm vào giỏ hàng thành công!"
                  });
                resetCartForm();
                loadCarts(maKH);
                updateCartItemCount(maKH);
            });
        }
    }).catch(error => {
        console.error("Lỗi thêm giỏ hàng:", error);
        const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
              toast.onmouseenter = Swal.stopTimer;
              toast.onmouseleave = Swal.resumeTimer;
            }
          });
          Toast.fire({ icon: "error", title: "Thêm vào giỏ hàng thất bại!" });
    });
}

function resetCartForm() {
    document.getElementById('cartSoLuong').value = '1';
    document.querySelectorAll('input[name="maMau"]').forEach(el => el.checked = false);
    document.querySelectorAll('input[name="maSize"]').forEach(el => el.checked = false);
}

function loadCarts(maKH) {
    const cartBody = document.getElementById("cartBody");
    cartBody.innerHTML = '';
    let total = 0;

    const cartRef = db.ref('Cart/' + maKH + '/items');
    cartRef.once('value', snapshot => {
        if (!snapshot.exists()) {
            cartBody.innerHTML = `<tr><td colspan="7" class="text-center text-muted py-4"><i class="bi bi-inbox fs-1 d-block mb-2"></i>Không có sản phẩm nào trong giỏ hàng</td></tr>`;
            document.getElementById("totalPrice").textContent = "0 đ";
            return;
        }

        snapshot.forEach(child => {
            const item = child.val();
            const itemId = child.key;
            const lineTotal = item.Gia * item.SoLuong;
            total += lineTotal;

            const row = document.createElement('tr');
            row.className = 'fade-in';
            row.innerHTML = `
                <td><input type="checkbox" class="cartCheckbox" data-item-id="${itemId}" onchange="calculateSelectedTotal('${maKH}')"></td>
                <td><img src="${item.hinhAnh}" alt=""></td>
                <td>${item.maSP ? `<a href="/shop/${item.maSP}" class="text-decoration-none text-dark fw-bold">${item.tenSP}</a>` : item.tenSP}</td>
                <td>${item.mau}</td>
                <td>${item.size}</td>
                <td class="qty-controls text-center">
                    <div class="input-group justify-content-center" style="max-width: 120px; margin: auto;">
                        <button class="btn btn-dark btn-sm" onclick="changeQuantity('${maKH}', '${itemId}', -1)">–</button>
                        <input type="number" id="qty_${itemId}" class="form-control text-center no-spinner" value="${item.SoLuong}" min="1" style="width: 50px;" onchange="updateQuantity('${maKH}', '${itemId}', this.value)">
                        <button class="btn btn-dark btn-sm" onclick="changeQuantity('${maKH}', '${itemId}', 1)">+</button>
                    </div>
                </td>
                <td>${(lineTotal).toLocaleString()} đ</td>`;
            cartBody.appendChild(row);
        });

        document.getElementById("totalPrice").textContent = total.toLocaleString() + " đ";
    });
}

function calculateSelectedTotal(maKH) {
    const checkboxes = document.querySelectorAll(".cartCheckbox:checked");
    let total = 0;

    const cartRef = db.ref('Cart/' + maKH + '/items');
    cartRef.once('value', snapshot => {
        snapshot.forEach(child => {
            const item = child.val();
            const itemId = child.key;
            checkboxes.forEach(checkbox => {
                if (checkbox.getAttribute("data-item-id") === itemId) {
                    total += item.SoLuong * item.Gia;
                }
            });
        });

        document.getElementById("totalPrice").textContent = total.toLocaleString() + " đ";
    });
}

function changeQuantity(maKH, itemId, delta) {
    const qtyInput = document.getElementById(`qty_${itemId}`);
    let currentQty = parseInt(qtyInput.value);
    let newQty = currentQty + delta;
    if (newQty < 1 || isNaN(newQty)) return;

    const itemRef = db.ref('Cart/' + maKH + '/items/' + itemId);
    itemRef.child('SoLuong').set(newQty).then(() => {
        loadCarts(maKH);
        updateCartItemCount(maKH);
    });
}

function updateQuantity(maKH, itemId, value) {
    const newQty = parseInt(value);
    if (isNaN(newQty) || newQty < 1) return;
    const itemRef = db.ref('Cart/' + maKH + '/items/' + itemId);
    itemRef.child('SoLuong').set(newQty).then(() => {
        loadCarts(maKH);
        updateCartItemCount(maKH);
    });
}

function deleteAllItems() {
    const maKH = document.getElementById("cartMaKH")?.value;

    Swal.fire({
        title: 'Bạn có chắc chắn?',
        text: 'Toàn bộ sản phẩm trong giỏ hàng sẽ bị xóa!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xác nhận!',
        cancelButtonText: 'Huỷ'
    }).then((result) => {
        if (result.isConfirmed) {
            db.ref('Cart/' + maKH + '/items').remove().then(() => {
                loadCarts(maKH);
                updateCartItemCount(maKH);
                Swal.fire({
                    icon: 'success',
                    title: 'Đã xóa toàn bộ!',
                    text: 'Giỏ hàng hiện đang trống.',
                    timer: 2000,
                    showConfirmButton: false
                });
            });
        }
    });
}


function deleteSelectedItems() {
    const maKH = document.getElementById("cartMaKH")?.value;
    const checkboxes = document.querySelectorAll(".cartCheckbox:checked");

    if (checkboxes.length === 0) {
        Swal.fire("Vui lòng chọn sản phẩm để xóa!");
        return;
    }

    //  Loại bỏ confirm() — dùng Swal thay thế
    Swal.fire({
        title: 'Bạn có chắc chắn?',
        text: 'Các sản phẩm đã chọn sẽ bị xóa khỏi giỏ hàng!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xác nhận!',
        cancelButtonText: 'Huỷ'
    }).then((result) => {
        if (result.isConfirmed) {
            const cartRef = db.ref('Cart/' + maKH + '/items');
            checkboxes.forEach(checkbox => {
                const itemId = checkbox.getAttribute("data-item-id");
                cartRef.child(itemId).remove();
            });
            setTimeout(() => loadCarts(maKH), 100);

            Swal.fire({
                icon: 'success',
                title: 'Đã xóa!',
                text: 'Sản phẩm đã được xóa khỏi giỏ hàng.',
                timer: 2000,
                showConfirmButton: false
            });
        }
    });
}


function updateCartItemCount(maKH) {
    const cartCountSpan = document.getElementById("cart-count-badge");
    if (!cartCountSpan) return;
    const cartRef = db.ref('Cart/' + maKH + '/items');
    cartRef.on('value', (snapshot) => {
        let totalCount = 0;
        snapshot.forEach((child) => {
            const item = child.val();
            totalCount += item.SoLuong || 0;
        });
        if (totalCount > 0) {
            cartCountSpan.textContent = totalCount;
            cartCountSpan.style.display = "inline-block";
        } else {
            cartCountSpan.textContent = '';
            cartCountSpan.style.display = "none";
        }
    });
}

function ThanhToan() {
    const maKH = document.getElementById("cartMaKH").value;
    const checkboxes = document.querySelectorAll(".cartCheckbox:checked");

    if (checkboxes.length === 0) {
        Swal.fire("Vui lòng chọn sản phẩm để thanh toán!");
        return;
    }

    // Lưu danh sách id sản phẩm đã chọn vào localStorage
    const selectedIds = Array.from(checkboxes).map(cb => cb.getAttribute("data-item-id"));
    localStorage.setItem('selectedCartItems', JSON.stringify(selectedIds));

    // Chuyển hướng sang trang xác nhận đơn hàng
    window.location.href = "/order/checkout";
}

window.onload = function () {
    const maKH = document.getElementById('cartMaKH')?.value;
    if (maKH) {
        loadCarts(maKH);
        updateCartItemCount(maKH);
    }
};
