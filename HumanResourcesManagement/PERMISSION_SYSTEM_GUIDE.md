# Hướng Dẫn Sử Dụng Hệ Thống Phân Quyền

## 📋 Tổng Quan

Hệ thống phân quyền động cho phép quản lý quyền truy cập chi tiết cho từng vai trò (role) trong hệ thống HR Management.

## 🚀 Các Tính Năng

### 1. **Permission Management UI**
- Truy cập: `/permission-settings`
- Chỉ Admin có quyền truy cập
- Quản lý permissions cho từng role
- Giao diện trực quan, dễ sử dụng

### 2. **Custom JSP Tag**
Sử dụng trong JSP để ẩn/hiện UI dựa trên quyền:

```jsp
<%@ taglib uri="http://hrm.com/permission" prefix="permission" %>

<permission:check permission="USER_CREATE">
    <button>Tạo User Mới</button>
</permission:check>
```

### 3. **Java Permission Checker**
Kiểm tra quyền trong Servlet/Controller:

```java
import util.PermissionChecker;
import util.PermissionConstants;

// Kiểm tra một quyền
if (PermissionChecker.hasPermission(user, PermissionConstants.USER_CREATE)) {
    // Cho phép tạo user
}

// Kiểm tra nhiều quyền (OR)
if (PermissionChecker.hasAnyPermission(user, 
    PermissionConstants.USER_EDIT, 
    PermissionConstants.USER_DELETE)) {
    // Có ít nhất một quyền
}

// Kiểm tra tất cả quyền (AND)
if (PermissionChecker.hasAllPermissions(user,
    PermissionConstants.CONTRACT_CREATE,
    PermissionConstants.CONTRACT_APPROVE)) {
    // Có tất cả quyền
}
```

## 📝 Danh Sách Permissions

### User Management
- `USER_VIEW` - Xem danh sách người dùng
- `USER_CREATE` - Tạo người dùng mới
- `USER_EDIT` - Chỉnh sửa người dùng
- `USER_DELETE` - Xóa người dùng
- `USER_ACTIVATE` - Kích hoạt/Vô hiệu hóa tài khoản

### Employee Management
- `EMPLOYEE_VIEW` - Xem danh sách nhân viên
- `EMPLOYEE_CREATE` - Thêm nhân viên mới
- `EMPLOYEE_EDIT` - Chỉnh sửa nhân viên
- `EMPLOYEE_DELETE` - Xóa nhân viên

### Department Management
- `DEPT_VIEW` - Xem phòng ban
- `DEPT_CREATE` - Tạo phòng ban
- `DEPT_EDIT` - Chỉnh sửa phòng ban
- `DEPT_DELETE` - Xóa phòng ban

### Contract Management
- `CONTRACT_VIEW` - Xem hợp đồng
- `CONTRACT_CREATE` - Tạo hợp đồng
- `CONTRACT_EDIT` - Chỉnh sửa hợp đồng
- `CONTRACT_DELETE` - Xóa hợp đồng
- `CONTRACT_APPROVE` - Phê duyệt hợp đồng

### Job Posting
- `JOB_VIEW` - Xem tin tuyển dụng
- `JOB_CREATE` - Tạo tin tuyển dụng
- `JOB_EDIT` - Chỉnh sửa tin tuyển dụng
- `JOB_DELETE` - Xóa tin tuyển dụng

### System Settings
- `SYSTEM_CONFIG` - Cấu hình hệ thống
- `ROLE_MANAGE` - Quản lý vai trò
- `PERMISSION_MANAGE` - Quản lý phân quyền

## 🔧 Cách Sử Dụng

### 1. Trong JSP Files

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://hrm.com/permission" prefix="permission" %>

<!-- Ẩn/hiện button dựa trên quyền -->
<permission:check permission="USER_CREATE">
    <a href="/create-user" class="btn btn-primary">
        <i class="fas fa-plus"></i> Tạo User
    </a>
</permission:check>

<!-- Ẩn/hiện menu item -->
<permission:check permission="EMPLOYEE_VIEW">
    <li><a href="/employees/list">Quản Lý Nhân Viên</a></li>
</permission:check>

<!-- Ẩn/hiện action buttons -->
<permission:check permission="USER_DELETE">
    <button class="btn btn-danger" onclick="deleteUser()">Xóa</button>
</permission:check>
```

### 2. Trong Servlet/Controller

```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    User user = (User) request.getSession().getAttribute("user");
    
    // Kiểm tra quyền truy cập
    if (!PermissionChecker.hasPermission(user, PermissionConstants.USER_VIEW)) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
        return;
    }
    
    // Tiếp tục xử lý...
}
```

### 3. Thêm Permission Mới

**Bước 1:** Thêm constant trong `PermissionConstants.java`:
```java
public static final String NEW_PERMISSION = "NEW_PERMISSION";
```

**Bước 2:** Thêm vào danh sách permissions trong `getAllPermissions()`:
```java
permissions.add(new Permission(
    NEW_PERMISSION, 
    "Tên Permission", 
    "Mô tả chi tiết", 
    "Category"
));
```

**Bước 3:** Gán permission cho role trong `PermissionChecker.java`:
```java
Set<String> adminPerms = new HashSet<>(Arrays.asList(
    // ... existing permissions
    PermissionConstants.NEW_PERMISSION
));
```

## 🎯 Ví Dụ Thực Tế

### Ví dụ 1: Bảo vệ trang User List

**list-users.jsp:**
```jsp
<%@ taglib uri="http://hrm.com/permission" prefix="permission" %>

<!-- Chỉ hiển thị button Create nếu có quyền -->
<permission:check permission="USER_CREATE">
    <a href="/create-user" class="btn btn-success">+ Tạo User</a>
</permission:check>

<!-- Chỉ hiển thị button Edit nếu có quyền -->
<permission:check permission="USER_EDIT">
    <button class="btn btn-primary">Edit</button>
</permission:check>

<!-- Chỉ hiển thị button Delete nếu có quyền -->
<permission:check permission="USER_DELETE">
    <button class="btn btn-danger">Delete</button>
</permission:check>
```

**ListUsersServlet.java:**
```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    User user = (User) request.getSession().getAttribute("user");
    
    // Kiểm tra quyền xem danh sách
    if (!PermissionChecker.hasPermission(user, PermissionConstants.USER_VIEW)) {
        response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
        return;
    }
    
    // Load users và hiển thị
    // ...
}
```

### Ví dụ 2: Bảo vệ chức năng Delete

**DeleteUserServlet.java:**
```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    User user = (User) request.getSession().getAttribute("user");
    
    // Kiểm tra quyền xóa
    if (!PermissionChecker.hasPermission(user, PermissionConstants.USER_DELETE)) {
        request.setAttribute("errorMessage", "Bạn không có quyền xóa user");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
    }
    
    // Thực hiện xóa
    int userId = Integer.parseInt(request.getParameter("userId"));
    // ... delete logic
}
```

## 🔐 Phân Quyền Mặc Định

### Admin
- Tất cả permissions (full access)

### HR Manager
- User: VIEW, CREATE, EDIT
- Employee: VIEW, CREATE, EDIT, DELETE
- Department: VIEW
- Contract: VIEW, CREATE, EDIT, APPROVE
- Job: VIEW, CREATE, EDIT, DELETE

### HR
- User: VIEW
- Employee: VIEW, CREATE, EDIT
- Department: VIEW
- Contract: VIEW, CREATE, EDIT
- Job: VIEW, CREATE, EDIT

### Dept Manager
- Employee: VIEW
- Department: VIEW
- Contract: VIEW
- Job: VIEW

### Employee
- Employee: VIEW (chỉ xem)
- Contract: VIEW (chỉ xem)
- Job: VIEW (chỉ xem)

## 📌 Lưu Ý

1. **Bảo mật:** Luôn kiểm tra quyền ở cả frontend (JSP) và backend (Servlet)
2. **Session:** Permissions được load từ role trong session
3. **Cập nhật:** Thay đổi permissions yêu cầu user logout/login lại
4. **Mở rộng:** Dễ dàng thêm permissions mới khi cần

## 🛠️ Troubleshooting

### Tag không hoạt động?
- Kiểm tra đã khai báo taglib chưa: `<%@ taglib uri="http://hrm.com/permission" prefix="permission" %>`
- Kiểm tra file `permission.tld` trong `WEB-INF/`

### Permission không được cập nhật?
- Logout và login lại
- Kiểm tra role của user trong database
- Kiểm tra `PermissionChecker.ROLE_PERMISSIONS`

### Lỗi 403 Forbidden?
- Kiểm tra user có quyền không
- Kiểm tra session còn hợp lệ không
