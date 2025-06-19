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
    const chatRef = db.ref('ChatBox').push();
    chatRef.set({
        MaKH: maKH,
        items: [{ NoiDung: noiDung, ThoiGian: Date.now(), VaiTro: vaiTro }]
    }, () => { loadChats(); });
}
function loadChats() {
    db.ref('ChatBox').once('value', snap => {
        const list = document.getElementById('chatList');
        list.innerHTML = '';
        snap.forEach(child => {
            const val = child.val();
            const div = document.createElement('div');
            div.className = 'item';
            div.innerHTML = `<b>MaKH:</b> ${val.MaKH}<br>
                <b>items:</b> ${val.items.map(i => i.NoiDung + ' (' + new Date(i.ThoiGian).toLocaleString() + ', VaiTrò: ' + (i.VaiTro ? 'NV/Admin' : 'KH') + ')').join('<br>')}
                <br><button onclick="deleteChat('${child.key}')">Xóa</button>
                <button onclick="editChatPrompt('${child.key}', '${val.MaKH}')">Sửa</button>`;
            list.appendChild(div);
        });
    });
}
function deleteChat(key) {
    if (confirm('Xóa chatbox này?')) db.ref('ChatBox/' + key).remove(() => loadChats());
}
function editChatPrompt(key, maKH) {
    const noiDung = prompt('Nội dung mới:');
    if (noiDung) {
        db.ref('ChatBox/' + key + '/items/0/NoiDung').set(noiDung, () => loadChats());
    }
}

// --- CARTBOX ---
function addCart() {
    const maKH = document.getElementById('cartMaKH').value;
    const idSPCT = document.getElementById('cartIDSPCT').value;
    const soLuong = parseInt(document.getElementById('cartSoLuong').value);
    const gia = parseInt(document.getElementById('cartGia').value);
    if (!maKH || !idSPCT || !soLuong || !gia) { alert('Nhập đủ thông tin!'); return; }
    const cartRef = db.ref('Cart').push();
    cartRef.set({
        MaKH: maKH,
        items: [{ ID_SPCT: idSPCT, SoLuong: soLuong, Gia: gia }]
    }, () => { loadCarts(); });
}
function loadCarts() {
    db.ref('Cart').once('value', snap => {
        const list = document.getElementById('cartList');
        list.innerHTML = '';
        snap.forEach(child => {
            const val = child.val();
            const div = document.createElement('div');
            div.className = 'item';
            div.innerHTML = `<b>MaKH:</b> ${val.MaKH}<br>
                <b>items:</b> ${val.items.map(i => i.ID_SPCT + ' - SL: ' + i.SoLuong + ', Giá: ' + i.Gia).join('<br>')}
                <br><button onclick="deleteCart('${child.key}')">Xóa</button>
                <button onclick="editCartPrompt('${child.key}', '${val.MaKH}')">Sửa</button>`;
            list.appendChild(div);
        });
    });
}
function deleteCart(key) {
    if (confirm('Xóa giỏ hàng này?')) db.ref('Cart/' + key).remove(() => loadCarts());
}
function editCartPrompt(key, maKH) {
    const soLuong = prompt('Số lượng mới:');
    if (soLuong) {
        db.ref('Cart/' + key + '/items/0/SoLuong').set(parseInt(soLuong), () => loadCarts());
    }
} 