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
// function addCart() {
//     const maKH = document.getElementById('cartMaKH').value;
//     const tenSP = document.getElementById('carttenSP').innerText;
//     const hinhAnh = document.getElementById('cartanhChinh').src;
//     const mau = document.querySelector('input[name="maMau"]:checked')?.value;
//     const size = document.querySelector('input[name="maSize"]:checked')?.value;
//     const soLuong = parseInt(document.getElementById('cartSoLuong').value);
//     const gia = parseInt(document.getElementById('cartGia').innerText.replace(/\D/g, ''));
//
//     if (!maKH || !tenSP || !hinhAnh || !mau || !size || !soLuong || !gia) {
//         alert('Vui lòng nhập đầy đủ thông tin sản phẩm!');
//         return;
//     }
//
//     // Kiểm tra xem đã có cart của khách hàng này chưa
//     db.ref('Cart').orderByChild('MaKH').equalTo(maKH).once('value', snap => {
//         if (snap.exists()) {
//             // Nếu đã có, kiểm tra xem sản phẩm đã tồn tại chưa
//             const cartKey = Object.keys(snap.val())[0];
//             const cartData = snap.val()[cartKey];
//
//             let existingItemKey = null;
//             if (cartData.items) {
//                 existingItemKey = Object.keys(cartData.items).find(key => cartData.items[key].tenSP === tenSP);
//             }
//
//             if (existingItemKey) {
//                 // Nếu sản phẩm đã tồn tại, cập nhật số lượng
//                 const existingItem = cartData.items[existingItemKey];
//                 const newQuantity = existingItem.SoLuong + soLuong;
//                 db.ref('Cart/' + cartKey + '/items/' + existingItemKey + '/SoLuong').set(newQuantity, () => {
//                     loadCarts();
//                     document.getElementById('carttenSP').value = '';
//                     document.getElementById('cartanhChinh').value = '';
//                     document.getElementById('cartMau').value = '';
//                     document.getElementById('cartSize').value = '';
//                     document.getElementById('cartSoLuong').value = '';
//                     document.getElementById('cartGia').value = '';
//                 });
//             } else {
//                 // Nếu sản phẩm chưa tồn tại, thêm item mới vào items
//                 const newItem = { tenSP: tenSP, hinhAnh: hinhAnh, mau: mau, size: size, SoLuong: soLuong, Gia: gia };
//                 db.ref('Cart/' + cartKey + '/items').push(newItem, () => {
//                     loadCarts();
//                     document.getElementById('carttenSP').value = '';
//                     document.getElementById('cartanhChinh').value = '';
//                     document.getElementById('cartMau').value = '';
//                     document.getElementById('cartSize').value = '';
//                     document.getElementById('cartSoLuong').value = '';
//                     document.getElementById('cartGia').value = '';
//                 });
//             }
//         } else {
//             // Nếu chưa có, tạo mới
//             const cartRef = db.ref('Cart').push();
//             cartRef.set({
//                 MaKH: maKH,
//                 items: [{ tenSP : tenSP, hinhAnh: hinhAnh, mau: mau, size: size, SoLuong: soLuong, Gia: gia }]
//             }, () => {
//                 loadCarts();
//                 document.getElementById('cartIDSPCT').value = '';
//                 document.getElementById('cartSoLuong').value = '';
//                 document.getElementById('cartGia').value = '';
//             });
//         }
//     });
// }

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
        alert("Vui lòng chọn đầy đủ thông tin sản phẩm!");
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
                    alert('Đã cập nhật số lượng sản phẩm!');
                    resetCartForm();
                    loadCarts(maKH);
                });
            });
        } else {
            // Thêm sản phẩm mới
            cartRef.push(item).then(() => {
                alert('Thêm vào giỏ hàng thành công!');
                resetCartForm();
                loadCarts(maKH);
            });
        }
    }).catch(error => {
        console.error("Lỗi thêm giỏ hàng:", error);
        alert("Thêm vào giỏ hàng thất bại!");
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

    const cartRef = db.ref('Cart/' + maKH + '/items');

    cartRef.once('value', (snapshot) => {
        if (!snapshot.exists()) {
            cartBody.innerHTML = '<tr><td colspan="7" class="text-center">Giỏ hàng trống</td></tr>';
            return;
        }

        snapshot.forEach((childSnapshot) => {
            const itemId = childSnapshot.key;
            const item = childSnapshot.val();

            const row = `
                <tr>
                    <td><input type="checkbox" class="cartCheckbox" data-item-id="${itemId}"></td>
                    <td><img src="${item.hinhAnh}" width="60"></td>
                    <td>${item.tenSP}</td>
                    <td>${item.mau}</td>
                    <td>${item.size}</td>
                    <td>${item.SoLuong}</td>
                    <td>${item.Gia.toLocaleString()} đ</td>
                </tr>
            `;
            cartBody.innerHTML += row;
        });
    }, (error) => {
        console.error("Lỗi tải giỏ hàng:", error);
        cartBody.innerHTML = '<tr><td colspan="7" class="text-center">Lỗi tải giỏ hàng</td></tr>';
    });
}


function deleteCart(key) {
    if (confirm('Xóa toàn bộ giỏ hàng này?')) db.ref('Cart/' + key).remove(() => loadCarts());
}

function editCartQuantity(cartKey, itemKey, currentSoLuong) {
    const soLuong = prompt('Số lượng mới:', currentSoLuong);
    if (soLuong && parseInt(soLuong) !== currentSoLuong && parseInt(soLuong) > 0) {
        db.ref('Cart/' + cartKey + '/items/' + itemKey + '/SoLuong').set(parseInt(soLuong), () => loadCarts());
    }
}

function deleteSelectedCartItems(cartKey) {
    const selectedItems = [];

    // Lấy tất cả checkbox trong cart này
    const checkboxes = document.querySelectorAll(`input[id^="cart_${cartKey}_"]`);
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            const itemKey = checkbox.id.split('_')[2];
            selectedItems.push(itemKey);
        }
    });

    if (selectedItems.length === 0) {
        alert('Vui lòng chọn ít nhất một item để xóa!');
        return;
    }

    if (confirm(`Bạn có chắc muốn xóa ${selectedItems.length} item(s) đã chọn?`)) {
        // Xóa từng item đã chọn
        const promises = selectedItems.map(itemKey => {
            return db.ref('Cart/' + cartKey + '/items/' + itemKey).remove();
        });

        Promise.all(promises).then(() => {
            loadCarts();
        });
    }
} 