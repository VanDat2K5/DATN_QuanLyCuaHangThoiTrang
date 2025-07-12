// Function to generate QR code for profile page
function generateQRCode(tien, noidung) {
    fetch('/test-qr', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tien, noidung })
    })
    .then(response => response.json())
    .then(data => {
        if (data && data.qrBase64) {
            // Display QR code in modal
            const qrcodeElement = document.getElementById('qrcode');
            qrcodeElement.innerHTML = `<img src="${data.qrBase64}" alt="QR Code" style="max-width: 200px; height: auto;">`;
        } else {
            console.error('QR Code generation failed');
            document.getElementById('qrcode').innerHTML = '<p class="text-danger">Không thể tạo mã QR</p>';
        }
    })
    .catch(error => {
        console.error('Error generating QR code:', error);
        document.getElementById('qrcode').innerHTML = '<p class="text-danger">Có lỗi xảy ra khi tạo QR code!</p>';
    });
}

// Original form submit event listener (for backward compatibility)
document.addEventListener('DOMContentLoaded', function() {
    const qrForm = document.getElementById('qrForm');
    if (qrForm) {
        qrForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const tien = document.getElementById('tien').value;
            const noidung = document.getElementById('noidung').value;
            generateQRCode(tien, noidung);
        });
    }
});