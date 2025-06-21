
document.getElementById('qrForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const tien = document.getElementById('tien').value;
    const noidung = document.getElementById('noidung').value;
    const response = await fetch('/test-qr', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tien, noidung })
    });
    const data = await response.json();
    if (data && data.qrBase64) {
        Swal.fire({
            title: 'QR Code Thanh Toán',
            imageUrl: data.qrBase64,
            imageWidth: 300,
            imageAlt: 'QR Code',
            html: `
                <div style="margin-top: 10px; font-size: 18px; text-align: justify; margin-left: 100px;">
                    <table style="width: 80%;">
                        <tr>
                            <td style="color:black; font-weight: bold;">Ngân hàng:</td>
                            <td style="color:red; font-weight: bold;">BIDV</td>
                        </tr>
                        <tr>
                            <td style="color:black; font-weight: bold;">Tên TK:</td>
                            <td style="color:blue; font-weight: bold;">Nguyen Van Dat</td>
                        </tr>
                        <tr>
                            <td style="color:black; font-weight: bold;">Số TK:</td>
                            <td style="color:green; font-weight: bold;">6910915396</td>
                        </tr>
                        <tr>
                            <td style="color:black; font-weight: bold;">Số tiền:</td>
                            <td style="color:red; font-weight: bold;">${tien} VND</td>
                        </tr>
                        <tr>
                            <td style="color:black; font-weight: bold;">Nội dung:</td>
                            <td style="color:blue; font-weight: bold;">${noidung}</td>
                        </tr>
                    </table>
                </div>
            `
        });
    } else {
        Swal.fire('Lỗi', 'Không tạo được QR code!', 'error');
    }
});