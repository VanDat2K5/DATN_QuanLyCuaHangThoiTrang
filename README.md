# Hướng dẫn cấu hình Firebase

## Cấu hình Firebase cho dự án

1. Tạo file `.env` trong thư mục gốc của dự án với nội dung sau:

```env
FIREBASE_PROJECT_ID=your-project-id
FIREBASE_PRIVATE_KEY_ID=your-private-key-id
FIREBASE_PRIVATE_KEY=your-private-key
FIREBASE_CLIENT_EMAIL=your-client-email
FIREBASE_CLIENT_ID=your-client-id
FIREBASE_CLIENT_X509_CERT_URL=your-client-x509-cert-url
```

2. Thay thế các giá trị trong file `.env` bằng thông tin từ Firebase Console:
   - Đăng nhập vào [Firebase Console](https://console.firebase.google.com)
   - Chọn project của bạn
   - Vào Project Settings > Service accounts
   - Click "Generate new private key"
   - Sử dụng thông tin từ file JSON được tải về để điền vào file `.env`

3. Đảm bảo file `.env` đã được thêm vào `.gitignore` để không bị đẩy lên git

## Lưu ý bảo mật
- Không bao giờ commit file chứa thông tin xác thực Firebase lên git
- Chia sẻ thông tin xác thực an toàn với các thành viên khác trong team
- Sử dụng biến môi trường để lưu trữ thông tin nhạy cảm 