# 🔐 Setup Chức năng Forgot Password

## ✅ Đã tạo

### Backend:
- ✅ `EmailService.java` - Gửi email qua Gmail SMTP
- ✅ `PasswordGenerator.java` - Tạo mật khẩu ngẫu nhiên 12 ký tự
- ✅ `ForgotPasswordServlet.java` - Xử lý logic

### Frontend:
- ✅ `forgot-password.jsp` - Form nhập email với messages
- ✅ Link trong `login.jsp`

### Config:
- ✅ Servlet mapping trong `web.xml`
- ✅ Jakarta Mail dependency trong `pom.xml`

## 🔧 CẤU HÌNH BẮT BUỘC

### Bước 1: Tạo Gmail App Password

1. Truy cập: https://myaccount.google.com/security
2. Bật "2-Step Verification"
3. Vào "App passwords"
4. Chọn "Mail" → Generate
5. Copy mật khẩu 16 ký tự

### Bước 2: Cập nhật EmailService.java

Mở file: `src/main/java/service/EmailService.java`

Tìm và thay đổi 2 dòng này:

```java
private static final String SENDER_EMAIL = "your-email@gmail.com"; // ← Thay email của bạn
private static final String SENDER_PASSWORD = "your-app-password"; // ← Dán App Password
```

**Ví dụ:**
```java
private static final String SENDER_EMAIL = "hrm.system@gmail.com";
private static final String SENDER_PASSWORD = "abcd efgh ijkl mnop";
```

### Bước 3: Build và Deploy

```bash
# Clean và build project
mvn clean install

# Hoặc trong IDE: Clean and Build
```

### Bước 4: Restart Server

Restart Tomcat server để load servlet mới.

## 🧪 TEST

### Test 1: Truy cập trang

```
http://localhost:9999/HumanResourcesManagement/auth/forgot-password
```

Hoặc click "Quên mật khẩu?" trong login page.

### Test 2: Submit form

1. Nhập email của user có trong database
2. Click "Gửi mật khẩu mới"
3. Kiểm tra email (có thể trong spam)
4. Đăng nhập bằng mật khẩu mới

## 📊 Flow hoạt động

```
User nhập email
    ↓
Kiểm tra email tồn tại trong DB
    ↓
Kiểm tra trạng thái Active
    ↓
Tạo mật khẩu mới (12 ký tự)
    ↓
Hash bằng BCrypt
    ↓
Cập nhật database
    ↓
Gửi email HTML đẹp
    ↓
Hiển thị success message
```

## ⚠️ Lưu ý

### Email không gửi được?

**Kiểm tra:**
1. App Password đúng chưa? (16 ký tự, không có khoảng trắng)
2. Đã bật 2-Step Verification chưa?
3. Firewall có chặn port 587 không?
4. Xem logs trong console

**Giải pháp:**
- Thử tạo lại App Password mới
- Kiểm tra internet connection
- Test với email khác

### Email không nhận được?

1. Kiểm tra thư mục spam
2. Đợi 1-2 phút
3. Xem logs: "Email sent successfully" hoặc lỗi

### Lỗi compile?

```bash
# Nếu thiếu Jakarta Mail
mvn clean install -U
```

## 🎯 Tính năng

- ✅ Tạo mật khẩu ngẫu nhiên 12 ký tự an toàn
- ✅ Hash bằng BCrypt trước khi lưu
- ✅ Gửi email HTML đẹp mắt
- ✅ Kiểm tra email tồn tại
- ✅ Kiểm tra trạng thái tài khoản
- ✅ Error handling đầy đủ
- ✅ Success/Error messages
- ✅ UI responsive với Bootstrap 5

## 📝 Checklist

- [ ] Đã tạo Gmail App Password
- [ ] Đã cập nhật SENDER_EMAIL trong EmailService.java
- [ ] Đã cập nhật SENDER_PASSWORD trong EmailService.java
- [ ] Đã build project
- [ ] Đã restart server
- [ ] Đã test với email thật
- [ ] Email đã nhận được
- [ ] Đăng nhập thành công bằng mật khẩu mới

## 🚀 Sẵn sàng!

Sau khi cấu hình email, chức năng sẽ hoạt động ngay!

---

**Lưu ý:** Đây là phiên bản đơn giản. Nếu cần thêm tính năng:
- Rate limiting (giới hạn số lần request)
- OTP thay vì gửi password trực tiếp
- Email template phức tạp hơn
- Logging chi tiết hơn

Hãy cho tôi biết!
