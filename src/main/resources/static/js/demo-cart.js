// Firebase config
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

let currentMaKH = "";

// Gọi khi người dùng bấm "Tải giỏ hàng"
function loadCart() {
    const maKHInput = document.getElementById("maKH");
    const maKH = maKHInput.value.trim();

    if (!maKH) {
        alert("Thiếu mã khách hàng");
        return;
    }

    currentMaKH = maKH;

    const cartList = document.getElementById("cartList");
    const cartTotal = document.getElementById("cartTotal");

    cartList.innerHTML = "";
    cartTotal.textContent = "Đang tải...";

    let total = 0;

    db.ref("Cart").orderByChild("MaKH").equalTo(maKH).once("value", snapshot => {
        const promises = [];

        snapshot.forEach(child => {
            const cart = child.val();
            const cartKey = child.key;

            if (cart.items) {
                for (const [itemKey, item] of Object.entries(cart.items)) {
                    const p = db.ref("SanPhamChiTiet/" + item.ID_SPCT).once("value").then(spSnap => {
                        const sp = spSnap.val();
                        if (!sp) return;

                        const itemTotal = item.Gia * item.SoLuong;
                        total += itemTotal;

                        const tr = document.createElement("tr");
                        tr.innerHTML = `
                            <td><a href="#" onclick="removeCartItem('${cartKey}', '${itemKey}')">&times;</a></td>
                            <td><img src="${sp.Anh || 'https://via.placeholder.com/60'}" width="60"></td>
                            <td>${sp.TenSP || 'Sản phẩm'}</td>
                            <td>${sp.Size || '-'}</td>
                            <td>${sp.MauSac || '-'}</td>
                            <td>${formatCurrency(item.Gia)}</td>
                            <td>
                                <input type="number" min="1" value="${item.SoLuong}" 
                                    onchange="updateCartQuantity('${cartKey}', '${itemKey}', this.value)">
                            </td>
                            <td>${formatCurrency(itemTotal)}</td>
                        `;
                        cartList.appendChild(tr);
                    });
                    promises.push(p);
                }
            }
        });

        // Sau khi tất cả dữ liệu sản phẩm tải xong, cập nhật tổng tiền
        Promise.all(promises).then(() => {
            cartTotal.textContent = formatCurrency(total);
        });
    });
}

function updateCartQuantity(cartKey, itemKey, newQty) {
    const qty = parseInt(newQty);
    if (qty > 0) {
        db.ref(`Cart/${cartKey}/items/${itemKey}/SoLuong`).set(qty, () => {
            loadCart(); // dùng currentMaKH
        });
    }
}

function removeCartItem(cartKey, itemKey) {
    if (confirm("Bạn có chắc muốn xóa sản phẩm này?")) {
        db.ref(`Cart/${cartKey}/items/${itemKey}`).remove(() => {
            loadCart(); // dùng currentMaKH
        });
    }
}

function formatCurrency(amount) {
    return amount.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
}
