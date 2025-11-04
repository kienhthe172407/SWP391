# Hướng Dẫn Xử Lý UTF-8 Encoding

## ✅ Đã Thực Hiện

### 1. **Database Connection (DBContext.java)**
```java
String url = "jdbc:mysql://127.0.0.1:3306/SWP2004?characterEncoding=UTF-8&useUnicode=true";
```
✅ Đã thêm `characterEncoding=UTF-8&useUnicode=true`

### 2. **Global Encoding Filter (EncodingFilter.java)**
```java
@WebFilter(urlPatterns = {"/*"})
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        chain.doFilter(request, response);
    }
}
```
✅ Filter tự động áp dụng cho tất cả requests

### 3. **Servlet Encoding**
Đã thêm encoding vào:
- ✅ CreateUserServlet.java
- ✅ EditUserServlet.java

### 4. **JSP Files**
Tất cả JSP đã có:
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
```

## 🔧 Cách Sửa Dữ Liệu Cũ Bị Lỗi

### SQL Script để sửa dữ liệu bị lỗi encoding:

```sql
USE SWP2004;

-- 1. Xem tất cả user bị lỗi encoding
SELECT user_id, username, first_name, last_name, email
FROM users
WHERE first_name LIKE '%Ã%' 
   OR last_name LIKE '%Ã%'
   OR email LIKE '%Ã%';

-- 2. Sửa từng user cụ thể (thay đổi theo dữ liệu thực tế)
UPDATE users 
SET first_name = 'Tú', 
    last_name = 'Bùi Anh'
WHERE user_id = 1;  -- Thay ID phù hợp

-- 3. Hoặc xóa và tạo lại user
DELETE FROM users WHERE user_id = 1;
-- Sau đó tạo lại qua giao diện web
```

## 📝 Checklist Khi Tạo Servlet Mới

Khi tạo servlet mới xử lý form tiếng Việt, luôn thêm vào đầu `doPost()`:

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    // ✅ BẮT BUỘC: Set encoding UTF-8
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
    
    // Tiếp tục xử lý...
}
```

## 🎯 Kết Quả

Sau khi thực hiện các bước trên:

### ✅ Tài khoản mới
- Tạo user mới với tên tiếng Việt → Hiển thị đúng
- Edit user với tên tiếng Việt → Hiển thị đúng
- Lưu vào database → Encoding đúng

### ⚠️ Tài khoản cũ
- Cần chạy SQL script để sửa dữ liệu bị lỗi
- Hoặc xóa và tạo lại

## 🔍 Cách Kiểm Tra

### 1. Test tạo user mới:
1. Vào `/create-user`
2. Nhập tên: `Nguyễn Văn Á`
3. Submit
4. Kiểm tra trong database và trên web

### 2. Test edit user:
1. Vào `/edit-user?id=X`
2. Sửa tên thành: `Trần Thị Ế`
3. Submit
4. Kiểm tra hiển thị

### 3. Kiểm tra database:
```sql
SELECT first_name, last_name FROM users WHERE user_id = X;
```
Phải thấy: `Nguyễn`, `Văn Á` (không có ký tự lạ)

## 🚨 Lưu Ý

1. **Luôn rebuild project** sau khi thay đổi code
2. **Restart server** để filter có hiệu lực
3. **Clear browser cache** nếu vẫn thấy lỗi
4. **Kiểm tra MySQL charset**: Database phải là `utf8mb4_unicode_ci`

## 📞 Troubleshooting

### Vẫn bị lỗi encoding?

1. **Kiểm tra database charset:**
```sql
SHOW CREATE DATABASE SWP2004;
-- Phải thấy: CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
```

2. **Kiểm tra table charset:**
```sql
SHOW CREATE TABLE users;
-- Phải thấy: CHARSET=utf8mb4
```

3. **Nếu sai, sửa lại:**
```sql
ALTER DATABASE SWP2004 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. **Restart MySQL service**

5. **Rebuild và restart Tomcat**
