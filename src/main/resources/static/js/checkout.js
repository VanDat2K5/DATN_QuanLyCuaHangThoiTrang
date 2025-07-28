let currentOrderId = null;
let checkPaymentInterval = null;

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

// Initialize Firebase
if (typeof firebase !== 'undefined') {
    firebase.initializeApp(firebaseConfig);
    const db = firebase.database();

    // Lấy maKH từ input hidden
    const maKH = document.getElementById('checkoutMaKH')?.value;

    // Hàm lấy sản phẩm đã chọn từ localStorage
    function getSelectedCartItems() {
        const selectedIds = JSON.parse(localStorage.getItem('selectedCartItems') || '[]');
        return new Promise((resolve, reject) => {
            if (!maKH || selectedIds.length === 0) {
                resolve([]);
                return;
            }
            db.ref('Cart/' + maKH + '/items').once('value', snapshot => {
                const items = [];
                snapshot.forEach(child => {
                    if (selectedIds.includes(child.key)) {
                        items.push({ id: child.key, ...child.val() });
                    }
                });
                resolve(items);
            });
        });
    }

    // Xử lý submit form xác nhận đơn hàng
    const checkoutForm = document.getElementById('checkoutForm');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            // Hiển thị loading
            const btn = document.getElementById('confirmBtn');
            const btnText = btn.querySelector('.btn-text');
            const loading = btn.querySelector('.loading');
            btnText.style.display = 'none';
            loading.style.display = 'inline-block';
            btn.disabled = true;
            
            // Lấy địa chỉ từ radio
            const selectedAddressRadio = document.querySelector('input[name="selectedAddress"]:checked');
            const selectedAddressId = selectedAddressRadio ? selectedAddressRadio.value : null;
            let addressData = {};
            
            if (selectedAddressId === 'new') {
                addressData = {
                    tenNguoiNhan: checkoutForm.tenNguoiNhan.value,
                    soDTNhanHang: checkoutForm.soDTNhanHang.value,
                    dcNhanHang: checkoutForm.dcNhanHang.value
                };
            } else if (selectedAddressId && selectedAddressId !== 'new') {
                addressData = { id: selectedAddressId };
            } else {
                alert('Vui lòng chọn địa chỉ giao hàng!');
                btnText.style.display = 'inline-block';
                loading.style.display = 'none';
                btn.disabled = false;
                return;
            }
            
            // Lấy phương thức thanh toán
            const paymentMethod = document.getElementById('paymentMethod').value;
            
            // Validation
            if (!paymentMethod) {
                alert('Vui lòng chọn phương thức thanh toán!');
                btnText.style.display = 'inline-block';
                loading.style.display = 'none';
                btn.disabled = false;
                return;
            }
            
            // Lấy sản phẩm đã chọn
            const cartItems = await getSelectedCartItems();
            if (cartItems.length === 0) {
                alert('Không có sản phẩm nào được chọn!');
                btnText.style.display = 'inline-block';
                loading.style.display = 'none';
                btn.disabled = false;
                return;
            }
            
            // Log dữ liệu gửi đi để debug
            const requestData = {
                address: addressData,
                paymentMethod,
                cartItems
            };
            
            // Gửi AJAX về backend
            fetch('/order/confirm', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            })
            .then(res => res.json())
            .then(async data => {
                if (data.success) {
                    // Xóa sản phẩm đã đặt khỏi Firebase
                    for (const item of cartItems) {
                        await db.ref('Cart/' + maKH + '/items/' + item.id).remove();
                    }
                    // Xóa localStorage
                    localStorage.removeItem('selectedCartItems');
                    
                    // Xử lý theo phương thức thanh toán
                    if (paymentMethod === 'Bank') {
                        // Hiển thị QR code
                        showQRCode(data.qrCode, data.totalAmount, data.paymentContent);
                        currentOrderId = data.orderId;
                    } else {
                        // Chuyển hướng sang trang thành công cho COD
                        window.location.href = '/order/success/' + data.orderId;
                    }
                } else {
                    alert('Có lỗi xảy ra: ' + (data.error || 'Không xác định'));
                }
            })
            .catch(err => {
                alert('Lỗi gửi đơn hàng: ' + err);
            })
            .finally(() => {
                // Ẩn loading
                btnText.style.display = 'inline-block';
                loading.style.display = 'none';
                btn.disabled = false;
            });
        });
    }

    function showQRCode(qrCodeData, amount, content) {
        // Sử dụng hàm generateQRCode từ qrcode.js
        if (typeof generateQRCode === 'function') {
            generateQRCode(amount, content);
        } else {
            // Fallback: hiển thị QR code trực tiếp
            document.getElementById('qrcode').innerHTML = 
                `<img src="${qrCodeData}" alt="QR Code" class="img-fluid" style="max-width: 300px;">`;
        }
        
        document.getElementById('modalAmount').textContent = formatCurrency(amount);
        document.getElementById('modalContent').textContent = content;
        
        const modal = new bootstrap.Modal(document.getElementById('qrModal'));
        modal.show();
        
        // Bắt đầu kiểm tra thanh toán
        startPaymentCheck();
    }

    function startPaymentCheck() {
        checkPaymentInterval = setInterval(() => {
            checkPaymentStatus();
        }, 30000);
    }

    function checkPaymentStatus() {
        if (!currentOrderId) return;

        fetch(`/order/check-payment/${currentOrderId}`)
        .then(response => response.json())
        .then(data => {
            if (data.isPaid) {
                clearInterval(checkPaymentInterval);
                Swal.fire({
                    title: "Thanh toán thành công!",
                    icon: "success",
                    draggable: true,
                  }).then(() => {
                    window.location.href = `/order/success/${currentOrderId}`;
                  });
            }
        })
        .catch(error => {
            console.error('Error checking payment:', error);
        });
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    // Event listeners
    document.addEventListener('DOMContentLoaded', function() {
        // Kiểm tra thanh toán button
        const checkPaymentBtn = document.getElementById('checkPaymentBtn');
        if (checkPaymentBtn) {
            checkPaymentBtn.addEventListener('click', function() {
                if (currentOrderId) {
                    checkPaymentStatus();
                }
            });
        }

        // Dừng kiểm tra khi đóng modal
        const qrModal = document.getElementById('qrModal');
        if (qrModal) {
            qrModal.addEventListener('hidden.bs.modal', function() {
                if (checkPaymentInterval) {
                    clearInterval(checkPaymentInterval);
                }
            });
        }

        // Render thông tin sản phẩm và tổng tiền
        renderOrderProducts();
        
        // Xử lý địa chỉ
        setupAddressHandling();
    });

    // Render thông tin sản phẩm và tổng tiền
    function renderOrderProducts() {
        const selectedIds = JSON.parse(localStorage.getItem('selectedCartItems') || '[]');
        if (!maKH || selectedIds.length === 0) return;
        
        db.ref('Cart/' + maKH + '/items').once('value', snapshot => {
            let items = [];
            let total = 0;
            let html = '';
            snapshot.forEach(child => {
                if (selectedIds.includes(child.key)) {
                    const item = child.val();
                    items.push(item.tenSP + ' (' + item.mau + ', ' + item.size + ', SL: ' + item.SoLuong + ')');
                    total += item.Gia * item.SoLuong;
                    html += `<div class='order-product-row'>
                        <img class='order-product-img' src='${item.hinhAnh}' alt=''>
                        <div class='order-product-info'>
                            <b>${item.tenSP}</b> (${item.mau}, ${item.size})<br>SL: ${item.SoLuong}
                        </div>
                    </div>`;
                }
            });
            
            const container = document.getElementById('orderProductsContainer');
            const totalText = document.getElementById('orderTotalText');
            const totalText2 = document.getElementById('orderTotalText2');
            
            if (container) container.innerHTML = html;
            if (totalText) totalText.textContent = total.toLocaleString() + ' đ';
            if (totalText2) totalText2.textContent = total.toLocaleString() + ' đ';
        });
    }

    // Xử lý địa chỉ
    function setupAddressHandling() {
        const addressRadioGroup = document.getElementById('addressRadioGroup');
        if (addressRadioGroup) {
            addressRadioGroup.addEventListener('change', function(e) {
                updateAddressCardStyle();
                fillAddressInputsByCheckedRadio();
            });
            
            // Đảm bảo radio đầu tiên luôn được chọn và điền input khi vào trang
            fillAddressInputsByCheckedRadio();
            updateAddressCardStyle();
        }
    }

    function updateAddressCardStyle() {
        document.querySelectorAll('.address-radio-card').forEach(card => card.classList.remove('selected'));
        let checked = document.querySelector('.address-radio-card input[type="radio"]:checked');
        if (checked) checked.closest('.address-radio-card').classList.add('selected');
    }

    function fillAddressInputsByCheckedRadio() {
        let checkedRadio = document.querySelector('#addressRadioGroup input[type="radio"]:checked');
        if (checkedRadio) {
            if (checkedRadio.value === 'new') {
                document.getElementById('newAddressForm').style.display = 'block';
                document.getElementById('fullName').value = '';
                document.getElementById('phoneNumber').value = '';
                document.getElementById('shippingAddress').value = '';
                document.getElementById('fullName').readOnly = false;
                document.getElementById('phoneNumber').readOnly = false;
                document.getElementById('shippingAddress').readOnly = false;
                if(document.querySelector('textarea[name="dcNhanHang"]')) {
                    document.querySelector('textarea[name="dcNhanHang"]').value = '';
                }
            } else {
                document.getElementById('newAddressForm').style.display = 'none';
                var label = document.querySelector('label[for="' + checkedRadio.id + '"]').textContent;
                var parts = label.split(' - ');
                if (parts.length === 3) {
                    document.getElementById('fullName').value = parts[0];
                    document.getElementById('phoneNumber').value = parts[1];
                    document.getElementById('shippingAddress').value = parts[2];
                }
                document.getElementById('fullName').readOnly = true;
                document.getElementById('phoneNumber').readOnly = true;
                document.getElementById('shippingAddress').readOnly = true;
            }
        }
    }
} 