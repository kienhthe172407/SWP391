# Hướng Dẫn Sử Dụng Chức Năng Quản Lý Người Dùng

## Tổng Quan
Chức năng quản lý người dùng cho phép admin tạo, xem, và quản lý tài khoản người dùng trong hệ thống.

## Các Tính Năng Chính

### 1. Tạo Tài Khoản Mới
- **URL**: `/create-user`
- **Quyền truy cập**: Chỉ Admin
- **Chức năng**:
  - Tạo tài khoản mới với thông tin đầy đủ
  - Tự động mã hóa mật khẩu bằng BCrypt
  - Kiểm tra username trùng lặp
  - Validation dữ liệu đầu vào

### 2. Xem Danh Sách Người Dùng
- **URL**: `/list-users`
- **Quyền truy cập**: Chỉ Admin
- **Chức năng**:
  - Hiển thị danh sách tất cả người dùng
  - Tìm kiếm theo tên, email, username
  - Lọc theo vai trò và trạng thái
  - Quản lý trạng thái tài khoản (Active/Inactive)
  - Thay đổi vai trò người dùng
  - Xóa tài khoản (soft delete)

## Cách Sử Dụng

### Bước 1: Đăng Nhập với Tài Khoản Admin
1. Truy cập trang đăng nhập
2. Đăng nhập với tài khoản có vai trò "Admin"

### Bước 2: Truy Cập Admin Dashboard
1. Sau khi đăng nhập, bạn sẽ được chuyển đến admin dashboard
2. Tại đây có các link để quản lý người dùng

### Bước 3: Tạo Tài Khoản Mới
1. Click vào "Create User" hoặc "Tạo Tài Khoản Mới"
2. Điền đầy đủ thông tin:
   - **Tên đăng nhập**: Phải là duy nhất
   - **Email**: Địa chỉ email hợp lệ
   - **Mật khẩu**: Tối thiểu 6 ký tự
   - **Xác nhận mật khẩu**: Phải khớp với mật khẩu
   - **Vai trò**: Chọn từ danh sách có sẵn
   - **Thông tin cá nhân**: Họ tên, số điện thoại, ngày sinh, giới tính
3. Click "Tạo Tài Khoản"

### Bước 4: Quản Lý Người Dùng
1. Click vào "All Users" hoặc "Quản Lý Người Dùng"
2. Sử dụng các tính năng:
   - **Tìm kiếm**: Nhập từ khóa vào ô tìm kiếm
   - **Lọc**: Chọn vai trò hoặc trạng thái
   - **Thay đổi trạng thái**: Click nút "Kích hoạt" hoặc "Vô hiệu hóa"
   - **Thay đổi vai trò**: Chọn vai trò mới từ dropdown
   - **Xóa tài khoản**: Click nút "Xóa" (cần xác nhận)

## Các Vai Trò Trong Hệ Thống

1. **Admin**: Quản trị viên hệ thống
2. **HR Manager**: Quản lý nhân sự
3. **HR**: Nhân viên nhân sự
4. **Dept Manager**: Quản lý phòng ban
5. **Employee**: Nhân viên thông thường

## Lưu Ý Quan Trọng

### Bảo Mật
- Mật khẩu được mã hóa bằng BCrypt
- Chỉ admin mới có thể truy cập các chức năng quản lý
- Có kiểm tra quyền truy cập ở mọi servlet

### Validation
- Username phải là duy nhất
- Email phải đúng định dạng
- Mật khẩu tối thiểu 6 ký tự
- Tất cả trường bắt buộc phải được điền

### Xóa Tài Khoản
- Sử dụng soft delete (chỉ đổi status thành Inactive)
- Dữ liệu không bị xóa hoàn toàn khỏi database
- Có thể khôi phục bằng cách kích hoạt lại

## Xử Lý Lỗi

### Lỗi Thường Gặp
1. **Username đã tồn tại**: Chọn tên đăng nhập khác
2. **Mật khẩu không khớp**: Kiểm tra lại mật khẩu xác nhận
3. **Email không đúng định dạng**: Nhập email hợp lệ
4. **Không có quyền truy cập**: Đăng nhập với tài khoản admin

### Thông Báo
- Thông báo thành công: Màu xanh
- Thông báo lỗi: Màu đỏ
- Tất cả thông báo sẽ tự động ẩn sau 5 giây

## Cấu Trúc Code

### Servlet
- `CreateUserServlet`: Xử lý tạo tài khoản
- `ListUsersServlet`: Xử lý danh sách và quản lý người dùng

### DAO
- `UserDAO`: Các phương thức truy cập database
  - `createUser()`: Tạo tài khoản mới
  - `getAllUsers()`: Lấy danh sách tất cả người dùng
  - `getUsersByRole()`: Lấy người dùng theo vai trò
  - `searchUsers()`: Tìm kiếm người dùng
  - `updateUserStatus()`: Cập nhật trạng thái
  - `updateUserRole()`: Cập nhật vai trò
  - `deleteUser()`: Xóa người dùng

### JSP
- `create-user.jsp`: Form tạo tài khoản
- `list-users.jsp`: Danh sách người dùng

## Kết Luận

Chức năng quản lý người dùng được thiết kế đơn giản, dễ sử dụng và bảo mật cao. Code được viết rõ ràng với nhiều comment để người mới bắt đầu có thể hiểu và mở rộng dễ dàng.
