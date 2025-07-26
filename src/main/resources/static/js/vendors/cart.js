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

    if (!tenSP || !hinhAnh || !mau || !size || !soLuong || !gia || !maKH) {
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

    const item = { tenSP, hinhAnh, mau, size, SoLuong: soLuong, Gia: gia };
    const cartRef = db.ref('Cart/' + maKH + '/items');

    // Kiểm tra nếu sản phẩm đã có
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
            // Cập nhật số lượng
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
                });
            });
        } else {
            // Thêm sản phẩm mới
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
            });
        }
    }).catch(error => {
        console.error("Lỗi thêm giỏ hàng:", error);
        Swal.fire({
            title: "Thêm vào giỏ hàng thất bại!",
            icon: "error",
            draggable: true
          });
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

    const cartRef = firebase.database().ref('Cart/' + maKH + '/items');
    cartRef.once('value', snapshot => {
        if (!snapshot.exists()) {
            cartBody.innerHTML = `<tr>
                                    <td colspan="7" class="text-center text-muted py-4">
                                        <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                        Không có sản phẩm nào trong giỏ hàng
                                    </td>
                                </tr>`;
            document.getElementById("totalPrice").textContent = " 0 đ";
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
                <td>${item.tenSP}</td>
                <td>${item.mau}</td>
                <td>${item.size}</td>
                <td class="qty-controls">
                    <button onclick="changeQuantity('${maKH}', '${itemId}', -1)">–</button>
                    <span id="qty_${itemId}" class="mx-2">${item.SoLuong}</span>
                    <button onclick="changeQuantity('${maKH}', '${itemId}', 1)">+</button>
                </td>
                <td>${(lineTotal).toLocaleString()} đ</td>
            `;
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
    const qtySpan = document.getElementById(`qty_${itemId}`);
    let currentQty = parseInt(qtySpan.textContent);

    let newQty = currentQty + delta;
    if (newQty < 1) return;

    const itemRef = db.ref('Cart/' + maKH + '/items/' + itemId);
    itemRef.child('SoLuong').set(newQty).then(() => {
        loadCarts(maKH);
    });
}



function deleteAllItems() {
    const maKH = document.getElementById("cartMaKH").value;
    if (!confirm("Bạn có chắc muốn xóa toàn bộ giỏ hàng?")) return;

    db.ref('Cart/' + maKH + '/items').remove().then(() => {
        loadCarts(maKH);
    });
}


function deleteSelectedItems() {
    const maKH = document.getElementById("cartMaKH").value;
    const checkboxes = document.querySelectorAll(".cartCheckbox:checked");

    if (checkboxes.length === 0) {
        Swal.fire("Vui lòng chọn sản phẩm để xóa!");
        return;
    }

    if (!confirm("Bạn có chắc chắn muốn xóa các sản phẩm đã chọn?")) return;

    const cartRef = db.ref('Cart/' + maKH + '/items');

    checkboxes.forEach(checkbox => {
        const itemId = checkbox.getAttribute("data-item-id");
        cartRef.child(itemId).remove();
    });

    setTimeout(() => loadCarts(maKH), 100);
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






window.onload = function() {
    loadCarts(document.getElementById('cartMaKH').value);
}