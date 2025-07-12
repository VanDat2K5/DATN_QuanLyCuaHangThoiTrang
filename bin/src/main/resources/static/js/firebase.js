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

// --- CHATBOX ---
function addChat() {
    const maKH = document.getElementById('chatMaKH').value;
    const noiDung = document.getElementById('chatNoiDung').value;
    const vaiTro = document.getElementById('chatVaiTro').value === 'true';
    if (!maKH || !noiDung) { alert('Nhập đủ thông tin!'); return; }
    
    // Kiểm tra xem đã có chat của khách hàng này chưa
    db.ref('ChatBox').orderByChild('MaKH').equalTo(maKH).once('value', snap => {
        if (snap.exists()) {
            // Nếu đã có, thêm item mới vào items
            const chatKey = Object.keys(snap.val())[0];
            const newItem = { NoiDung: noiDung, ThoiGian: Date.now(), VaiTro: vaiTro };
            db.ref('ChatBox/' + chatKey + '/items').push(newItem, () => { 
                loadChats(); 
                document.getElementById('chatNoiDung').value = '';
            });
        } else {
            // Nếu chưa có, tạo mới
            const chatRef = db.ref('ChatBox').push();
            chatRef.set({
                MaKH: maKH,
                items: [{ NoiDung: noiDung, ThoiGian: Date.now(), VaiTro: vaiTro }]
            }, () => { 
                loadChats(); 
                document.getElementById('chatNoiDung').value = '';
            });
        }
    });
}

function loadChats() {
    db.ref('ChatBox').once('value', snap => {
        const list = document.getElementById('chatList');
        list.innerHTML = '';
        snap.forEach(child => {
            const val = child.val();
            const div = document.createElement('div');
            div.className = 'item';
            
            let itemsHtml = '';
            if (val.items) {
                Object.keys(val.items).forEach(itemKey => {
                    const item = val.items[itemKey];
                    itemsHtml += `
                        <div style="margin: 5px 0; padding: 5px; border-left: 3px solid #007bff;">
                            <b>Nội dung:</b> ${item.NoiDung}<br>
                            <b>Thời gian:</b> ${new Date(item.ThoiGian).toLocaleString()}<br>
                            <b>Vai trò:</b> ${item.VaiTro ? 'NV/Admin' : 'KH'}
                        </div>
                    `;
                });
            }
            
            div.innerHTML = `
                <b>MaKH:</b> ${val.MaKH}<br>
                <b>Items:</b><br>${itemsHtml}
                <br><button onclick="deleteChat('${child.key}')" style="background: #dc3545; color: white; border: none; padding: 5px 10px;">Xóa Chat</button>
            `;
            list.appendChild(div);
        });
    });
}

function deleteChat(key) {
    if (confirm('Xóa toàn bộ chatbox này?')) db.ref('ChatBox/' + key).remove(() => loadChats());
}

// --- CARTBOX ---
function addCart() {
    const maKH = document.getElementById('cartMaKH').value;
    const idSPCT = document.getElementById('cartIDSPCT').value;
    const soLuong = parseInt(document.getElementById('cartSoLuong').value);
    const gia = parseInt(document.getElementById('cartGia').value);
    if (!maKH || !idSPCT || !soLuong || !gia) { alert('Nhập đủ thông tin!'); return; }
    
    // Kiểm tra xem đã có cart của khách hàng này chưa
    db.ref('Cart').orderByChild('MaKH').equalTo(maKH).once('value', snap => {
        if (snap.exists()) {
            // Nếu đã có, kiểm tra xem sản phẩm đã tồn tại chưa
            const cartKey = Object.keys(snap.val())[0];
            const cartData = snap.val()[cartKey];
            
            let existingItemKey = null;
            if (cartData.items) {
                existingItemKey = Object.keys(cartData.items).find(key => cartData.items[key].ID_SPCT === idSPCT);
            }

            if (existingItemKey) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng
                const existingItem = cartData.items[existingItemKey];
                const newQuantity = existingItem.SoLuong + soLuong;
                db.ref('Cart/' + cartKey + '/items/' + existingItemKey + '/SoLuong').set(newQuantity, () => {
                    loadCarts();
                    document.getElementById('cartIDSPCT').value = '';
                    document.getElementById('cartSoLuong').value = '';
                    document.getElementById('cartGia').value = '';
                });
            } else {
                // Nếu sản phẩm chưa tồn tại, thêm item mới vào items
                const newItem = { ID_SPCT: idSPCT, SoLuong: soLuong, Gia: gia };
                db.ref('Cart/' + cartKey + '/items').push(newItem, () => { 
                    loadCarts(); 
                    document.getElementById('cartIDSPCT').value = '';
                    document.getElementById('cartSoLuong').value = '';
                    document.getElementById('cartGia').value = '';
                });
            }
        } else {
            // Nếu chưa có, tạo mới
            const cartRef = db.ref('Cart').push();
            cartRef.set({
                MaKH: maKH,
                items: [{ ID_SPCT: idSPCT, SoLuong: soLuong, Gia: gia }]
            }, () => { 
                loadCarts(); 
                document.getElementById('cartIDSPCT').value = '';
                document.getElementById('cartSoLuong').value = '';
                document.getElementById('cartGia').value = '';
            });
        }
    });
}

function loadCarts() {
    db.ref('Cart').once('value', snap => {
        const list = document.getElementById('cartList');
        list.innerHTML = '';
        snap.forEach(child => {
            const val = child.val();
            const div = document.createElement('div');
            div.className = 'item';
            
            let itemsHtml = '';
            if (val.items) {
                Object.keys(val.items).forEach(itemKey => {
                    const item = val.items[itemKey];
                    itemsHtml += `
                        <div style="margin: 5px 0; padding: 5px; border-left: 3px solid #28a745;">
                            <input type="checkbox" id="cart_${child.key}_${itemKey}" style="margin-right: 5px;">
                            <b>ID_SPCT:</b> ${item.ID_SPCT}<br>
                            <b>Số lượng:</b> ${item.SoLuong} 
                            <button onclick="editCartQuantity('${child.key}', '${itemKey}', ${item.SoLuong})" style="background: #ffc107; color: black; border: none; padding: 2px 5px; margin: 2px;">Sửa SL</button><br>
                            <b>Giá:</b> ${item.Gia}
                        </div>
                    `;
                });
            }
            
            div.innerHTML = `
                <b>MaKH:</b> ${val.MaKH}<br>
                <b>Items:</b><br>${itemsHtml}
                <br>
                <button onclick="deleteSelectedCartItems('${child.key}')" style="background: #dc3545; color: white; border: none; padding: 5px 10px;">Xóa Items Đã Chọn</button>
                <button onclick="deleteCart('${child.key}')" style="background: #6c757d; color: white; border: none; padding: 5px 10px;">Xóa Toàn Bộ Cart</button>
            `;
            list.appendChild(div);
        });
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