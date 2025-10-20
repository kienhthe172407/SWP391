# Luồng Hoạt Động Chức Năng Quản Lý Người Dùng

## 1. Luồng Tạo Tài Khoản Mới

```
Admin đăng nhập
    ↓
Truy cập Admin Dashboard
    ↓
Click "Tạo Tài Khoản Mới"
    ↓
CreateUserServlet.doGet()
    ↓
Hiển thị form create-user.jsp
    ↓
Admin điền thông tin và submit
    ↓
CreateUserServlet.doPost()
    ↓
Kiểm tra quyền admin
    ↓
Validation dữ liệu đầu vào
    ↓
Kiểm tra username trùng lặp
    ↓
Mã hóa mật khẩu bằng BCrypt
    ↓
UserDAO.createUser()
    ↓
INSERT vào database
    ↓
Hiển thị thông báo thành công/lỗi
```

## 2. Luồng Xem Danh Sách Người Dùng

```
Admin đăng nhập
    ↓
Truy cập Admin Dashboard
    ↓
Click "Quản Lý Người Dùng"
    ↓
ListUsersServlet.doGet()
    ↓
Kiểm tra quyền admin
    ↓
Lấy tham số tìm kiếm/lọc
    ↓
UserDAO.getAllUsers() hoặc searchUsers()
    ↓
SELECT từ database
    ↓
Hiển thị danh sách list-users.jsp
```

## 3. Luồng Quản Lý Người Dùng

```
Admin xem danh sách người dùng
    ↓
Thực hiện hành động (thay đổi trạng thái/vai trò/xóa)
    ↓
ListUsersServlet.doPost()
    ↓
Kiểm tra quyền admin
    ↓
Xác định hành động (action)
    ↓
Thực hiện hành động tương ứng:
    - updateUserStatus() - Thay đổi trạng thái
    - updateUserRole() - Thay đổi vai trò  
    - deleteUser() - Xóa tài khoản
    ↓
UPDATE database
    ↓
Chuyển hướng với thông báo kết quả
```

## 4. Các Phương Thức DAO Chính

### UserDAO.createUser()
```java
1. Mã hóa mật khẩu: BCrypt.hashpw(password, BCrypt.gensalt(10))
2. Chuẩn bị SQL: INSERT INTO users (...)
3. Thực thi PreparedStatement
4. Lấy generated key (user_id)
5. Trả về User object mới
```

### UserDAO.getAllUsers()
```java
1. Chuẩn bị SQL: SELECT * FROM users ORDER BY created_at DESC
2. Thực thi query
3. Map ResultSet thành List<User>
4. Trả về danh sách người dùng
```

### UserDAO.searchUsers()
```java
1. Chuẩn bị SQL: SELECT * FROM users WHERE username LIKE ? OR email LIKE ?...
2. Set parameters với wildcard (%searchTerm%)
3. Thực thi query
4. Map ResultSet thành List<User>
5. Trả về kết quả tìm kiếm
```

### UserDAO.updateUserStatus()
```java
1. Chuẩn bị SQL: UPDATE users SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?
2. Set parameters
3. Thực thi update
4. Trả về boolean (true nếu thành công)
```

## 5. Cấu Trúc Database

### Bảng users
```sql
- user_id (INT, PRIMARY KEY, AUTO_INCREMENT)
- username (VARCHAR, UNIQUE)
- password_hash (VARCHAR) - BCrypt hash
- email (VARCHAR)
- role (VARCHAR) - Admin, HR Manager, HR, Dept Manager, Employee
- status (VARCHAR) - Active, Inactive
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
- first_name (VARCHAR)
- last_name (VARCHAR)
- phone (VARCHAR)
- date_of_birth (DATE)
- gender (VARCHAR)
```

## 6. Bảo Mật

### Kiểm Tra Quyền
```java
// Trong mọi servlet
HttpSession session = request.getSession();
User currentUser = (User) session.getAttribute("user");

if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
```

### Mã Hóa Mật Khẩu
```java
// Sử dụng BCrypt với salt rounds = 10
String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
```

### Validation
```java
// Kiểm tra dữ liệu đầu vào
- Username không được trống và phải duy nhất
- Email phải đúng định dạng
- Mật khẩu tối thiểu 6 ký tự
- Xác nhận mật khẩu phải khớp
```

## 7. Xử Lý Lỗi

### Lỗi Database
```java
try {
    // Database operations
} catch (SQLException ex) {
    System.err.println("Error: " + ex.getMessage());
    ex.printStackTrace();
    return false; // hoặc null
}
```

### Lỗi Validation
```java
if (username == null || username.trim().isEmpty()) {
    errorMessage += "Tên đăng nhập không được để trống.<br>";
}
```

### Thông Báo Người Dùng
```jsp
<c:if test="${not empty errorMessage}">
    <div class="alert alert-error">${errorMessage}</div>
</c:if>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success">${successMessage}</div>
</c:if>
```

## 8. URL Mapping

### Servlet Annotations
```java
@WebServlet(name = "CreateUserServlet", urlPatterns = {"/create-user"})
@WebServlet(name = "ListUsersServlet", urlPatterns = {"/list-users"})
```

### Các URL Chính
- `/create-user` - Tạo tài khoản mới
- `/list-users` - Danh sách người dùng
- `/dashboard/admin-dashboard.jsp` - Admin dashboard

## 9. Responsive Design

### CSS Framework
- Sử dụng CSS tùy chỉnh với responsive design
- Hỗ trợ mobile và desktop
- Bootstrap-like styling

### JavaScript
- Validation form phía client
- Confirmation dialogs
- Auto-hide alerts
- Dynamic form behavior

## 10. Best Practices

### Code Organization
- Tách biệt rõ ràng giữa Controller, DAO, Model
- Sử dụng PreparedStatement để tránh SQL injection
- Exception handling đầy đủ
- Comment code rõ ràng

### User Experience
- Form validation real-time
- Thông báo rõ ràng
- Confirmation cho các hành động quan trọng
- Responsive design

### Security
- Kiểm tra quyền ở mọi endpoint
- Mã hóa mật khẩu
- Validation dữ liệu đầu vào
- Soft delete thay vì hard delete
