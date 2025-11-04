# 📧 Cấu hình Email - Đơn giản!

## ✅ Bạn có thể dùng BẤT KỲ email Gmail nào!

Không cần email đặc biệt, chỉ cần:
- ✅ Email Gmail của bạn (bất kỳ)
- ✅ App Password (16 ký tự)

---

## 🚀 Cấu hình trong 3 bước (5 phút)

### Bước 1: Tạo App Password

**1.1. Mở trình duyệt:**
```
https://myaccount.google.com/security
```

**1.2. Bật "2-Step Verification"**
- Tìm "2-Step Verification"
- Click "Get Started"
- Làm theo hướng dẫn (nhận mã qua SMS)

**1.3. Tạo App Password**
- Quay lại trang Security
- Tìm "App passwords"
- Chọn "Mail" → Generate
- **Copy 16 ký tự** (ví dụ: `abcd efgh ijkl mnop`)

### Bước 2: Mở file email.properties

**Trong NetBeans:**
```
Source Packages → <default package> → email.properties
```

Hoặc đường dẫn:
```
src/main/resources/email.properties
```

### Bước 3: Sửa 2 dòng

**Tìm:**
```properties
mail.sender.email=your-email@gmail.com
mail.sender.password=your-app-password-here
```

**Thay thành (ví dụ):**
```properties
mail.sender.email=myemail@gmail.com
mail.sender.password=abcd efgh ijkl mnop
```

**Lưu ý:**
- Dòng 1: Email Gmail CỦA BẠN
- Dòng 2: App Password 16 ký tự vừa copy

### Bước 4: Save, Build, Restart

1. **Ctrl + S** - Save file
2. **Clean and Build** project
3. **Restart server**
4. **Test!**

---

## 🎯 Ví dụ cụ thể

**Giả sử email của bạn là:** `john.doe@gmail.com`

**Sau khi tạo App Password:** `xyzw abcd efgh ijkl`

**File email.properties sẽ là:**
```properties
mail.sender.email=john.doe@gmail.com
mail.sender.password=xyzw abcd efgh ijkl
mail.sender.name=HR Management System
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
```

**Chỉ cần sửa 2 dòng đầu!**

---

## ❓ FAQ

### Q: Tôi có thể dùng email nào?
**A:** Bất kỳ email Gmail nào của bạn! Không cần email đặc biệt.

### Q: App Password là gì?
**A:** Là mật khẩu 16 ký tự do Google tạo cho ứng dụng. KHÔNG phải password Gmail thường.

### Q: Tôi không có 2-Step Verification?
**A:** Phải bật nó trước. Vào Security → 2-Step Verification → Get Started.

### Q: Tôi quên App Password?
**A:** Không sao! Tạo lại App Password mới là được.

### Q: Email vẫn không gửi được?
**A:** Kiểm tra:
- App Password đúng chưa? (16 ký tự)
- Đã save file chưa?
- Đã rebuild chưa?
- Đã restart server chưa?

---

## 🎥 Video hướng dẫn

Tìm trên YouTube:
- "How to create Gmail App Password"
- "Cách tạo mật khẩu ứng dụng Gmail"

---

## ✅ Checklist

- [ ] Đã bật 2-Step Verification
- [ ] Đã tạo App Password
- [ ] Đã copy 16 ký tự
- [ ] Đã mở file email.properties
- [ ] Đã sửa mail.sender.email
- [ ] Đã sửa mail.sender.password
- [ ] Đã save file (Ctrl + S)
- [ ] Đã Clean and Build
- [ ] Đã Restart server
- [ ] Đã test!

---

**Xong! Giờ test thử nhé!** 🚀
