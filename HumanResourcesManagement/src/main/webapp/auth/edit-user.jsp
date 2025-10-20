<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa thông tin người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <style>
        .container { max-width: 900px; margin: 20px auto; background:#fff; padding: 24px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,.06); }
        .row { display:flex; gap:16px; }
        .col { flex:1; }
        label { font-weight:600; margin-bottom:6px; display:block; }
        input, select { width:100%; padding:10px 12px; border:2px solid #e1e8ed; border-radius:8px; font-size:14px; }
        .actions { display:flex; gap:10px; margin-top: 16px; }
        .btn { padding:10px 16px; border:none; border-radius:8px; cursor:pointer; font-weight:600; }
        .btn-primary { background:#2d89ef; color:#fff; }
        .btn-secondary { background:#95a5a6; color:#fff; }
        .alert { padding:12px 14px; border-radius:8px; margin-bottom:12px; }
        .alert-error { background:#fdf2f2; border:1px solid #fecaca; color:#b91c1c; }
    </style>
    <script>
        function validateForm() {
            const email = document.getElementById('email').value;
            if (!email || email.indexOf('@') === -1) {
                alert('Email không hợp lệ');
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/list-users" style="text-decoration:none; color:#2d89ef">← Quay lại danh sách</a>
    <h2 style="margin:10px 0 16px">Sửa thông tin người dùng</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <c:if test="${empty editUser}">
        <div class="alert alert-error">Không có dữ liệu người dùng.</div>
    </c:if>

    <c:if test="${not empty editUser}">
        <form method="post" action="${pageContext.request.contextPath}/edit-user" onsubmit="return validateForm()">
            <input type="hidden" name="id" value="${editUser.userId}" />

            <div class="row">
                <div class="col">
                    <label>Username</label>
                    <input type="text" value="${editUser.username}" disabled />
                </div>
                <div class="col">
                    <label>Email</label>
                    <input id="email" name="email" type="email" value="${editUser.email}" required />
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Họ</label>
                    <input name="firstName" type="text" value="${editUser.firstName}" />
                </div>
                <div class="col">
                    <label>Tên</label>
                    <input name="lastName" type="text" value="${editUser.lastName}" />
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Số điện thoại</label>
                    <input name="phone" type="text" value="${editUser.phone}" />
                </div>
                <div class="col">
                    <label>Ngày sinh</label>
                    <input name="dateOfBirth" type="date" value="${editUser.dateOfBirth}" />
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Giới tính</label>
                    <select name="gender">
                        <option value="" ${empty editUser.gender ? 'selected' : ''}>-- Chọn --</option>
                        <option value="Nam" ${editUser.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                        <option value="Nữ" ${editUser.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                        <option value="Khác" ${editUser.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
                <div class="col">
                    <label>Vai trò</label>
                    <select name="role" required>
                        <option value="Admin" ${editUser.role == 'Admin' ? 'selected' : ''}>Admin</option>
                        <option value="HR Manager" ${editUser.role == 'HR Manager' ? 'selected' : ''}>HR Manager</option>
                        <option value="HR" ${editUser.role == 'HR' ? 'selected' : ''}>HR</option>
                        <option value="Dept Manager" ${editUser.role == 'Dept Manager' ? 'selected' : ''}>Dept Manager</option>
                        <option value="Employee" ${editUser.role == 'Employee' ? 'selected' : ''}>Employee</option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Trạng thái</label>
                    <select name="status" required>
                        <option value="Active" ${editUser.status == 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${editUser.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/list-users">Hủy</a>
            </div>
        </form>
    </c:if>
</div>
</body>
</html>


