# Use Case Development Guide - Hệ Thống Quản Lý Nhân Sự

## Tổng Quan Hệ Thống

Hệ thống quản lý nhân sự được thiết kế để phục vụ các vai trò khác nhau trong tổ chức, từ nhân viên thông thường đến quản trị viên hệ thống.

## Các Actor (Người Dùng)

### 1. **Admin (Quản trị viên hệ thống)**
- Quản lý toàn bộ hệ thống
- Tạo và quản lý tài khoản người dùng
- Cấu hình hệ thống và bảo mật

### 2. **HR Manager (Quản lý nhân sự)**
- Quản lý nhân viên và hợp đồng
- Phê duyệt các quyết định nhân sự
- Báo cáo và thống kê

### 3. **HR Staff (Nhân viên nhân sự)**
- Thực hiện các tác vụ nhân sự hàng ngày
- Quản lý thông tin nhân viên
- Xử lý đơn từ và yêu cầu

### 4. **Dept Manager (Quản lý phòng ban)**
- Quản lý nhân viên trong phòng ban
- Phê duyệt nghỉ phép và chấm công
- Đánh giá hiệu suất nhân viên

### 5. **Employee (Nhân viên)**
- Xem thông tin cá nhân
- Đăng ký nghỉ phép
- Chấm công và xem lương

## Use Cases Chi Tiết

---

## 1. QUẢN LÝ NGƯỜI DÙNG (User Management)

### UC-001: Tạo Tài Khoản Người Dùng
**Actor:** Admin  
**Mô tả:** Admin tạo tài khoản mới cho người dùng trong hệ thống

**Luồng chính:**
1. Admin đăng nhập vào hệ thống
2. Admin truy cập trang quản lý người dùng
3. Admin click "Tạo tài khoản mới"
4. Hệ thống hiển thị form tạo tài khoản
5. Admin điền thông tin:
   - Tên đăng nhập (bắt buộc, duy nhất)
   - Email (bắt buộc, đúng định dạng)
   - Mật khẩu (bắt buộc, tối thiểu 6 ký tự)
   - Xác nhận mật khẩu
   - Vai trò (Admin, HR Manager, HR, Dept Manager, Employee)
   - Thông tin cá nhân (họ tên, số điện thoại, ngày sinh, giới tính)
6. Admin click "Tạo tài khoản"
7. Hệ thống kiểm tra dữ liệu đầu vào
8. Hệ thống kiểm tra username trùng lặp
9. Hệ thống mã hóa mật khẩu bằng BCrypt
10. Hệ thống lưu thông tin vào database
11. Hệ thống hiển thị thông báo thành công

**Luồng thay thế:**
- 7a. Dữ liệu không hợp lệ: Hiển thị lỗi và yêu cầu sửa
- 8a. Username đã tồn tại: Hiển thị lỗi và yêu cầu chọn tên khác
- 10a. Lỗi database: Hiển thị lỗi hệ thống

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Tài khoản mới được tạo thành công

---

### UC-002: Xem Danh Sách Người Dùng
**Actor:** Admin  
**Mô tả:** Admin xem danh sách tất cả người dùng trong hệ thống

**Luồng chính:**
1. Admin đăng nhập vào hệ thống
2. Admin truy cập trang quản lý người dùng
3. Hệ thống hiển thị danh sách người dùng với thông tin:
   - Avatar và tên đầy đủ
   - Username và email
   - Vai trò
   - Trạng thái (Active/Inactive)
   - Ngày tạo
   - Các nút thao tác

**Luồng thay thế:**
- 3a. Không có người dùng nào: Hiển thị thông báo "Không có dữ liệu"

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Danh sách người dùng được hiển thị

---

### UC-003: Tìm Kiếm Người Dùng
**Actor:** Admin  
**Mô tả:** Admin tìm kiếm người dùng theo từ khóa

**Luồng chính:**
1. Admin ở trang danh sách người dùng
2. Admin nhập từ khóa vào ô tìm kiếm
3. Admin click "Tìm kiếm"
4. Hệ thống tìm kiếm trong:
   - Username
   - Email
   - Họ tên
5. Hệ thống hiển thị kết quả tìm kiếm

**Luồng thay thế:**
- 5a. Không tìm thấy: Hiển thị "Không có kết quả phù hợp"

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Kết quả tìm kiếm được hiển thị

---

### UC-004: Lọc Người Dùng Theo Vai Trò
**Actor:** Admin  
**Mô tả:** Admin lọc danh sách người dùng theo vai trò

**Luồng chính:**
1. Admin ở trang danh sách người dùng
2. Admin chọn vai trò từ dropdown
3. Admin click "Lọc"
4. Hệ thống hiển thị chỉ những người dùng có vai trò được chọn

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Danh sách được lọc theo vai trò

---

### UC-005: Thay Đổi Trạng Thái Người Dùng
**Actor:** Admin  
**Mô tả:** Admin kích hoạt hoặc vô hiệu hóa tài khoản người dùng

**Luồng chính:**
1. Admin ở trang danh sách người dùng
2. Admin click nút "Kích hoạt" hoặc "Vô hiệu hóa"
3. Hệ thống hiển thị dialog xác nhận
4. Admin xác nhận hành động
5. Hệ thống cập nhật trạng thái trong database
6. Hệ thống hiển thị thông báo thành công

**Luồng thay thế:**
- 4a. Admin hủy: Không thực hiện thay đổi
- 5a. Lỗi database: Hiển thị lỗi

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Trạng thái người dùng được cập nhật

---

### UC-006: Thay Đổi Vai Trò Người Dùng
**Actor:** Admin  
**Mô tả:** Admin thay đổi vai trò của người dùng

**Luồng chính:**
1. Admin ở trang danh sách người dùng
2. Admin chọn vai trò mới từ dropdown
3. Hệ thống hiển thị dialog xác nhận
4. Admin xác nhận thay đổi
5. Hệ thống cập nhật vai trò trong database
6. Hệ thống hiển thị thông báo thành công

**Luồng thay thế:**
- 4a. Admin hủy: Không thực hiện thay đổi
- 5a. Lỗi database: Hiển thị lỗi

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Vai trò người dùng được cập nhật

---

### UC-007: Xóa Tài Khoản Người Dùng
**Actor:** Admin  
**Mô tả:** Admin xóa tài khoản người dùng (soft delete)

**Luồng chính:**
1. Admin ở trang danh sách người dùng
2. Admin click nút "Xóa"
3. Hệ thống hiển thị dialog xác nhận với cảnh báo
4. Admin xác nhận xóa
5. Hệ thống đổi trạng thái thành "Inactive" (soft delete)
6. Hệ thống hiển thị thông báo thành công

**Luồng thay thế:**
- 4a. Admin hủy: Không thực hiện xóa

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Tài khoản được đánh dấu là Inactive

---

## 2. QUẢN LÝ NHÂN VIÊN (Employee Management)

### UC-008: Thêm Thông Tin Nhân Viên
**Actor:** HR Staff, HR Manager  
**Mô tả:** Thêm thông tin chi tiết cho nhân viên

**Luồng chính:**
1. HR Staff đăng nhập vào hệ thống
2. HR Staff truy cập trang quản lý nhân viên
3. HR Staff click "Thêm nhân viên"
4. Hệ thống hiển thị form thông tin nhân viên
5. HR Staff điền thông tin:
   - Thông tin cá nhân (họ tên, ngày sinh, giới tính, địa chỉ)
   - Thông tin liên hệ (email, số điện thoại, địa chỉ khẩn cấp)
   - Thông tin công việc (phòng ban, chức vụ, ngày bắt đầu)
   - Thông tin lương (mức lương cơ bản, phụ cấp)
   - Thông tin tài khoản ngân hàng
6. HR Staff click "Lưu"
7. Hệ thống kiểm tra dữ liệu đầu vào
8. Hệ thống lưu thông tin vào database
9. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** HR Staff đã đăng nhập  
**Kết quả:** Thông tin nhân viên được thêm vào hệ thống

---

### UC-009: Xem Chi Tiết Nhân Viên
**Actor:** HR Staff, HR Manager, Dept Manager  
**Mô tả:** Xem thông tin chi tiết của nhân viên

**Luồng chính:**
1. User đăng nhập vào hệ thống
2. User truy cập danh sách nhân viên
3. User click vào tên nhân viên
4. Hệ thống hiển thị trang chi tiết với các tab:
   - Thông tin cá nhân
   - Thông tin công việc
   - Lịch sử hợp đồng
   - Lịch sử chấm công
   - Lịch sử lương

**Điều kiện tiên quyết:** User đã đăng nhập và có quyền xem  
**Kết quả:** Thông tin chi tiết nhân viên được hiển thị

---

### UC-010: Cập Nhật Thông Tin Nhân Viên
**Actor:** HR Staff, HR Manager  
**Mô tả:** Cập nhật thông tin nhân viên

**Luồng chính:**
1. HR Staff xem chi tiết nhân viên
2. HR Staff click "Chỉnh sửa"
3. Hệ thống hiển thị form chỉnh sửa
4. HR Staff cập nhật thông tin cần thiết
5. HR Staff click "Lưu thay đổi"
6. Hệ thống kiểm tra dữ liệu
7. Hệ thống cập nhật database
8. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** HR Staff đã đăng nhập  
**Kết quả:** Thông tin nhân viên được cập nhật

---

## 3. QUẢN LÝ HỢP ĐỒNG (Contract Management)

### UC-011: Tạo Hợp Đồng Mới
**Actor:** HR Staff, HR Manager  
**Mô tả:** Tạo hợp đồng lao động cho nhân viên

**Luồng chính:**
1. HR Staff đăng nhập vào hệ thống
2. HR Staff truy cập trang quản lý hợp đồng
3. HR Staff click "Tạo hợp đồng mới"
4. Hệ thống hiển thị form tạo hợp đồng
5. HR Staff điền thông tin:
   - Thông tin nhân viên (chọn từ danh sách)
   - Loại hợp đồng (thử việc, chính thức, lao động)
   - Thời hạn hợp đồng
   - Mức lương và phụ cấp
   - Điều khoản đặc biệt
6. HR Staff click "Tạo hợp đồng"
7. Hệ thống tạo mã hợp đồng tự động
8. Hệ thống lưu vào database
9. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** HR Staff đã đăng nhập  
**Kết quả:** Hợp đồng mới được tạo

---

### UC-012: Phê Duyệt Hợp Đồng
**Actor:** HR Manager  
**Mô tả:** Phê duyệt hợp đồng lao động

**Luồng chính:**
1. HR Manager đăng nhập vào hệ thống
2. HR Manager truy cập danh sách hợp đồng chờ phê duyệt
3. HR Manager click vào hợp đồng cần phê duyệt
4. HR Manager xem chi tiết hợp đồng
5. HR Manager click "Phê duyệt" hoặc "Từ chối"
6. Nếu phê duyệt:
   - Hệ thống cập nhật trạng thái thành "Approved"
   - Hệ thống gửi email thông báo cho nhân viên
7. Nếu từ chối:
   - Hệ thống yêu cầu nhập lý do từ chối
   - Hệ thống cập nhật trạng thái thành "Rejected"
8. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** HR Manager đã đăng nhập  
**Kết quả:** Hợp đồng được phê duyệt hoặc từ chối

---

## 4. QUẢN LÝ CHẤM CÔNG (Attendance Management)

### UC-013: Chấm Công Hàng Ngày
**Actor:** Employee  
**Mô tả:** Nhân viên chấm công vào và ra

**Luồng chính:**
1. Employee đăng nhập vào hệ thống
2. Employee truy cập trang chấm công
3. Employee click "Check In" khi đến công ty
4. Hệ thống ghi nhận thời gian check-in
5. Employee click "Check Out" khi rời công ty
6. Hệ thống ghi nhận thời gian check-out
7. Hệ thống tính toán số giờ làm việc
8. Hệ thống hiển thị thông báo thành công

**Luồng thay thế:**
- 3a. Đã check-in: Hiển thị thông báo "Bạn đã check-in hôm nay"
- 5a. Chưa check-in: Hiển thị thông báo "Vui lòng check-in trước"

**Điều kiện tiên quyết:** Employee đã đăng nhập  
**Kết quả:** Thời gian làm việc được ghi nhận

---

### UC-014: Xem Lịch Sử Chấm Công
**Actor:** Employee, Dept Manager, HR Staff  
**Mô tả:** Xem lịch sử chấm công

**Luồng chính:**
1. User đăng nhập vào hệ thống
2. User truy cập trang lịch sử chấm công
3. User chọn tháng/năm cần xem
4. Hệ thống hiển thị lịch với:
   - Ngày làm việc (màu xanh)
   - Ngày nghỉ (màu đỏ)
   - Giờ vào/ra
   - Tổng giờ làm việc
5. User có thể click vào ngày để xem chi tiết

**Điều kiện tiên quyết:** User đã đăng nhập  
**Kết quả:** Lịch sử chấm công được hiển thị

---

### UC-015: Đăng Ký Nghỉ Phép
**Actor:** Employee  
**Mô tả:** Nhân viên đăng ký nghỉ phép

**Luồng chính:**
1. Employee đăng nhập vào hệ thống
2. Employee truy cập trang đăng ký nghỉ phép
3. Employee click "Đăng ký nghỉ phép"
4. Hệ thống hiển thị form đăng ký
5. Employee điền thông tin:
   - Loại nghỉ phép (nghỉ phép năm, nghỉ ốm, nghỉ việc riêng)
   - Ngày bắt đầu và kết thúc
   - Lý do nghỉ phép
   - Số ngày nghỉ
6. Employee click "Gửi đơn"
7. Hệ thống kiểm tra số ngày phép còn lại
8. Hệ thống gửi đơn cho quản lý phòng ban
9. Hệ thống hiển thị thông báo thành công

**Luồng thay thế:**
- 7a. Không đủ ngày phép: Hiển thị cảnh báo
- 8a. Lỗi gửi đơn: Hiển thị lỗi

**Điều kiện tiên quyết:** Employee đã đăng nhập  
**Kết quả:** Đơn nghỉ phép được gửi

---

### UC-016: Phê Duyệt Nghỉ Phép
**Actor:** Dept Manager  
**Mô tả:** Quản lý phòng ban phê duyệt đơn nghỉ phép

**Luồng chính:**
1. Dept Manager đăng nhập vào hệ thống
2. Dept Manager truy cập trang đơn nghỉ phép chờ phê duyệt
3. Dept Manager click vào đơn cần phê duyệt
4. Dept Manager xem chi tiết đơn nghỉ phép
5. Dept Manager click "Phê duyệt" hoặc "Từ chối"
6. Nếu phê duyệt:
   - Hệ thống cập nhật trạng thái thành "Approved"
   - Hệ thống trừ số ngày phép của nhân viên
   - Hệ thống gửi email thông báo cho nhân viên
7. Nếu từ chối:
   - Hệ thống yêu cầu nhập lý do từ chối
   - Hệ thống cập nhật trạng thái thành "Rejected"
8. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** Dept Manager đã đăng nhập  
**Kết quả:** Đơn nghỉ phép được phê duyệt hoặc từ chối

---

## 5. QUẢN LÝ TUYỂN DỤNG (Recruitment Management)

### UC-017: Tạo Tin Tuyển Dụng
**Actor:** HR Staff, HR Manager  
**Mô tả:** Tạo tin tuyển dụng cho vị trí mới

**Luồng chính:**
1. HR Staff đăng nhập vào hệ thống
2. HR Staff truy cập trang quản lý tuyển dụng
3. HR Staff click "Tạo tin tuyển dụng"
4. Hệ thống hiển thị form tạo tin tuyển dụng
5. HR Staff điền thông tin:
   - Tiêu đề tin tuyển dụng
   - Mô tả công việc
   - Yêu cầu ứng viên
   - Quyền lợi
   - Mức lương
   - Địa điểm làm việc
   - Hạn nộp hồ sơ
6. HR Staff click "Đăng tin"
7. Hệ thống kiểm tra dữ liệu
8. Hệ thống lưu tin tuyển dụng
9. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** HR Staff đã đăng nhập  
**Kết quả:** Tin tuyển dụng được tạo

---

### UC-018: Quản Lý Ứng Viên
**Actor:** HR Staff, HR Manager  
**Mô tả:** Quản lý hồ sơ ứng viên

**Luồng chính:**
1. HR Staff đăng nhập vào hệ thống
2. HR Staff truy cập trang quản lý ứng viên
3. HR Staff xem danh sách ứng viên theo tin tuyển dụng
4. HR Staff click vào ứng viên để xem chi tiết hồ sơ
5. HR Staff có thể:
   - Xem CV và thông tin ứng viên
   - Đánh giá ứng viên
   - Lên lịch phỏng vấn
   - Cập nhật trạng thái (Đang xem xét, Đã phỏng vấn, Trúng tuyển, Không trúng tuyển)

**Điều kiện tiên quyết:** HR Staff đã đăng nhập  
**Kết quả:** Thông tin ứng viên được quản lý

---

## 6. QUẢN LÝ LƯƠNG (Payroll Management)

### UC-019: Tính Lương Tháng
**Actor:** HR Staff, HR Manager  
**Mô tả:** Tính lương cho nhân viên theo tháng

**Luồng chính:**
1. HR Staff đăng nhập vào hệ thống
2. HR Staff truy cập trang quản lý lương
3. HR Staff chọn tháng cần tính lương
4. HR Staff click "Tính lương tháng"
5. Hệ thống tự động tính toán:
   - Lương cơ bản
   - Phụ cấp
   - Thưởng
   - Khấu trừ (bảo hiểm, thuế)
   - Số giờ làm việc thực tế
6. Hệ thống hiển thị bảng lương chi tiết
7. HR Staff kiểm tra và xác nhận
8. HR Staff click "Xác nhận tính lương"
9. Hệ thống lưu bảng lương
10. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** HR Staff đã đăng nhập  
**Kết quả:** Bảng lương tháng được tính toán

---

### UC-020: Xem Bảng Lương
**Actor:** Employee  
**Mô tả:** Nhân viên xem bảng lương của mình

**Luồng chính:**
1. Employee đăng nhập vào hệ thống
2. Employee truy cập trang bảng lương
3. Employee chọn tháng cần xem
4. Hệ thống hiển thị bảng lương chi tiết:
   - Lương cơ bản
   - Phụ cấp
   - Thưởng
   - Khấu trừ
   - Lương thực lĩnh
5. Employee có thể tải xuống file PDF

**Điều kiện tiên quyết:** Employee đã đăng nhập  
**Kết quả:** Bảng lương được hiển thị

---

## 7. BÁO CÁO VÀ THỐNG KÊ (Reports & Analytics)

### UC-021: Báo Cáo Nhân Sự
**Actor:** HR Manager, Admin  
**Mô tả:** Tạo báo cáo tổng hợp về nhân sự

**Luồng chính:**
1. HR Manager đăng nhập vào hệ thống
2. HR Manager truy cập trang báo cáo
3. HR Manager chọn loại báo cáo:
   - Báo cáo nhân sự theo phòng ban
   - Báo cáo chấm công
   - Báo cáo lương
   - Báo cáo nghỉ phép
4. HR Manager chọn khoảng thời gian
5. HR Manager click "Tạo báo cáo"
6. Hệ thống tạo báo cáo với biểu đồ và thống kê
7. HR Manager có thể tải xuống file Excel/PDF

**Điều kiện tiên quyết:** HR Manager đã đăng nhập  
**Kết quả:** Báo cáo được tạo

---

### UC-022: Dashboard Thống Kê
**Actor:** HR Manager, Admin  
**Mô tả:** Xem dashboard với các thống kê tổng quan

**Luồng chính:**
1. HR Manager đăng nhập vào hệ thống
2. Hệ thống hiển thị dashboard với:
   - Tổng số nhân viên
   - Số nhân viên mới trong tháng
   - Tỷ lệ nghỉ phép
   - Biểu đồ xu hướng nhân sự
   - Danh sách nhân viên sắp hết hạn hợp đồng
3. HR Manager có thể click vào các widget để xem chi tiết

**Điều kiện tiên quyết:** HR Manager đã đăng nhập  
**Kết quả:** Dashboard được hiển thị

---

## 8. QUẢN LÝ HỆ THỐNG (System Management)

### UC-023: Cấu Hình Hệ Thống
**Actor:** Admin  
**Mô tả:** Cấu hình các thông số hệ thống

**Luồng chính:**
1. Admin đăng nhập vào hệ thống
2. Admin truy cập trang cấu hình hệ thống
3. Admin có thể cấu hình:
   - Thông tin công ty
   - Cài đặt email
   - Cài đặt bảo mật
   - Cài đặt backup
   - Cài đặt thông báo
4. Admin click "Lưu cấu hình"
5. Hệ thống lưu cấu hình
6. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Cấu hình hệ thống được lưu

---

### UC-024: Quản Lý Backup
**Actor:** Admin  
**Mô tả:** Tạo và quản lý backup dữ liệu

**Luồng chính:**
1. Admin đăng nhập vào hệ thống
2. Admin truy cập trang quản lý backup
3. Admin click "Tạo backup"
4. Hệ thống tạo backup toàn bộ database
5. Hệ thống lưu file backup
6. Hệ thống hiển thị thông báo thành công
7. Admin có thể tải xuống file backup

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Backup được tạo thành công

---

## 9. THÔNG BÁO VÀ EMAIL (Notifications & Email)

### UC-025: Gửi Thông Báo Nội Bộ
**Actor:** HR Staff, HR Manager, Dept Manager  
**Mô tả:** Gửi thông báo cho nhân viên

**Luồng chính:**
1. User đăng nhập vào hệ thống
2. User truy cập trang gửi thông báo
3. User click "Gửi thông báo mới"
4. Hệ thống hiển thị form gửi thông báo
5. User điền thông tin:
   - Tiêu đề thông báo
   - Nội dung thông báo
   - Người nhận (tất cả, theo phòng ban, cá nhân)
   - Mức độ ưu tiên
6. User click "Gửi"
7. Hệ thống gửi thông báo đến người nhận
8. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** User đã đăng nhập  
**Kết quả:** Thông báo được gửi

---

### UC-026: Gửi Email Tự Động
**Actor:** Hệ thống  
**Mô tả:** Hệ thống tự động gửi email thông báo

**Luồng chính:**
1. Hệ thống phát hiện sự kiện cần thông báo:
   - Nhân viên mới được tạo
   - Hợp đồng được phê duyệt
   - Đơn nghỉ phép được phê duyệt
   - Nhắc nhở hết hạn hợp đồng
2. Hệ thống tạo nội dung email
3. Hệ thống gửi email đến người nhận
4. Hệ thống ghi log email đã gửi

**Điều kiện tiên quyết:** Hệ thống đang hoạt động  
**Kết quả:** Email được gửi tự động

---

## 10. BẢO MẬT VÀ PHÂN QUYỀN (Security & Permissions)

### UC-027: Quản Lý Phân Quyền
**Actor:** Admin  
**Mô tả:** Quản lý quyền truy cập của người dùng

**Luồng chính:**
1. Admin đăng nhập vào hệ thống
2. Admin truy cập trang quản lý phân quyền
3. Admin chọn vai trò cần cấu hình
4. Admin cấu hình quyền:
   - Xem thông tin nhân viên
   - Chỉnh sửa thông tin nhân viên
   - Quản lý hợp đồng
   - Quản lý chấm công
   - Xem báo cáo
5. Admin click "Lưu quyền"
6. Hệ thống cập nhật quyền
7. Hệ thống hiển thị thông báo thành công

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Quyền truy cập được cập nhật

---

### UC-028: Audit Log
**Actor:** Admin  
**Mô tả:** Xem log hoạt động của hệ thống

**Luồng chính:**
1. Admin đăng nhập vào hệ thống
2. Admin truy cập trang audit log
3. Admin có thể xem:
   - Log đăng nhập/đăng xuất
   - Log thay đổi dữ liệu
   - Log lỗi hệ thống
4. Admin có thể lọc theo:
   - Người dùng
   - Thời gian
   - Loại hoạt động
5. Admin có thể tải xuống log

**Điều kiện tiên quyết:** Admin đã đăng nhập  
**Kết quả:** Log được hiển thị

---

## Kế Hoạch Phát Triển

### Phase 1: Core Features (Đã hoàn thành)
- ✅ Quản lý người dùng cơ bản
- ✅ Đăng nhập/đăng xuất
- ✅ Dashboard cơ bản

### Phase 2: Employee Management
- 🔄 Quản lý thông tin nhân viên
- 🔄 Quản lý hợp đồng
- 🔄 Quản lý chấm công

### Phase 3: Advanced Features
- ⏳ Quản lý tuyển dụng
- ⏳ Quản lý lương
- ⏳ Báo cáo và thống kê

### Phase 4: System Enhancement
- ⏳ Thông báo và email
- ⏳ Bảo mật nâng cao
- ⏳ Mobile app

## Công Nghệ Sử Dụng

### Backend
- **Java Servlet/JSP** - Web framework
- **MySQL** - Database
- **BCrypt** - Password hashing
- **Apache POI** - Excel processing
- **iText** - PDF generation

### Frontend
- **HTML5/CSS3** - UI/UX
- **JavaScript** - Client-side logic
- **Bootstrap** - Responsive design
- **JSTL** - Server-side templating

### Tools
- **Maven** - Build tool
- **Git** - Version control
- **Tomcat** - Application server

## Kết Luận

Tài liệu Use Case này cung cấp roadmap chi tiết để phát triển hệ thống quản lý nhân sự hoàn chỉnh. Mỗi use case đều có mô tả rõ ràng về luồng hoạt động, điều kiện tiên quyết và kết quả mong đợi, giúp developer dễ dàng implement và test.

Bạn có thể bắt đầu từ Phase 2 để phát triển các tính năng quản lý nhân viên, sau đó tiếp tục với các phase tiếp theo tùy theo nhu cầu của dự án.
