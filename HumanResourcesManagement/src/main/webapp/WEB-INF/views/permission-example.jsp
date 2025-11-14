<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="permission" uri="http://hrm.com/permission" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Permission System - Example Usage</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
        }
        .example-section {
            margin: 30px 0;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background: #f9f9f9;
        }
        .example-section h2 {
            color: #2196F3;
            margin-top: 0;
        }
        .code-block {
            background: #263238;
            color: #aed581;
            padding: 15px;
            border-radius: 4px;
            overflow-x: auto;
            font-family: 'Courier New', monospace;
            margin: 10px 0;
        }
        .permission-box {
            display: inline-block;
            padding: 8px 12px;
            margin: 5px;
            background: #4CAF50;
            color: white;
            border-radius: 4px;
        }
        .no-permission {
            background: #f44336;
        }
        .action-buttons {
            margin: 15px 0;
        }
        .btn {
            padding: 10px 20px;
            margin: 5px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-primary { background: #2196F3; color: white; }
        .btn-success { background: #4CAF50; color: white; }
        .btn-danger { background: #f44336; color: white; }
        .btn-warning { background: #ff9800; color: white; }
    </style>
</head>
<body>
    <h1>🔐 Permission System - Ví dụ sử dụng</h1>
    
    <div class="example-section">
        <h2>1. Thông tin User hiện tại</h2>
        <p><strong>Username:</strong> ${sessionScope.user.username}</p>
        <p><strong>Role:</strong> ${sessionScope.user.role}</p>
        <p><strong>Email:</strong> ${sessionScope.user.email}</p>
    </div>
    
    <div class="example-section">
        <h2>2. Ví dụ: Ẩn/Hiện Buttons theo Permission</h2>
        
        <div class="code-block">
&lt;permission:check code="EMPLOYEE_CREATE"&gt;
    &lt;button class="btn btn-success"&gt;Thêm nhân viên&lt;/button&gt;
&lt;/permission:check&gt;
        </div>
        
        <div class="action-buttons">
            <permission:check code="EMPLOYEE_VIEW">
                <button class="btn btn-primary">👁️ Xem nhân viên</button>
            </permission:check>
            
            <permission:check code="EMPLOYEE_CREATE">
                <button class="btn btn-success">➕ Thêm nhân viên</button>
            </permission:check>
            
            <permission:check code="EMPLOYEE_EDIT">
                <button class="btn btn-warning">✏️ Sửa nhân viên</button>
            </permission:check>
            
            <permission:check code="EMPLOYEE_DELETE">
                <button class="btn btn-danger">🗑️ Xóa nhân viên</button>
            </permission:check>
        </div>
        
        <p><em>Các button chỉ hiển thị nếu user có quyền tương ứng</em></p>
    </div>
    
    <div class="example-section">
        <h2>3. Ví dụ: Ẩn/Hiện Menu Items</h2>
        
        <div class="code-block">
&lt;permission:check code="CONTRACT_VIEW"&gt;
    &lt;li&gt;&lt;a href="/contracts"&gt;Quản lý hợp đồng&lt;/a&gt;&lt;/li&gt;
&lt;/permission:check&gt;
        </div>
        
        <ul style="list-style: none; padding: 0;">
            <permission:check code="EMPLOYEE_VIEW">
                <li>📋 <a href="/employees">Quản lý nhân viên</a></li>
            </permission:check>
            
            <permission:check code="CONTRACT_VIEW">
                <li>📄 <a href="/contracts">Quản lý hợp đồng</a></li>
            </permission:check>
            
            <permission:check code="ATTENDANCE_VIEW">
                <li>⏰ <a href="/attendance">Chấm công</a></li>
            </permission:check>
            
            <permission:check code="JOB_VIEW">
                <li>💼 <a href="/jobs">Tuyển dụng</a></li>
            </permission:check>
            
            <permission:check code="PERMISSION_MANAGE">
                <li>⚙️ <a href="/permission-settings">Quản lý phân quyền</a></li>
            </permission:check>
        </ul>
    </div>
    
    <div class="example-section">
        <h2>4. Ví dụ: Hiển thị thông báo theo Permission</h2>
        
        <permission:check code="EMPLOYEE_CREATE">
            <div class="permission-box">
                ✓ Bạn có quyền tạo nhân viên mới
            </div>
        </permission:check>
        
        <permission:check code="CONTRACT_APPROVE">
            <div class="permission-box">
                ✓ Bạn có quyền phê duyệt hợp đồng
            </div>
        </permission:check>
        
        <permission:check code="ATTENDANCE_IMPORT">
            <div class="permission-box">
                ✓ Bạn có quyền import dữ liệu chấm công
            </div>
        </permission:check>
        
        <permission:check code="PERMISSION_MANAGE">
            <div class="permission-box">
                ✓ Bạn có quyền quản lý phân quyền hệ thống
            </div>
        </permission:check>
    </div>
    
    <div class="example-section">
        <h2>5. Ví dụ: Conditional Rendering phức tạp</h2>
        
        <div class="code-block">
&lt;permission:check code="EMPLOYEE_VIEW"&gt;
    &lt;table&gt;
        &lt;!-- Hiển thị danh sách nhân viên --&gt;
        &lt;permission:check code="EMPLOYEE_EDIT"&gt;
            &lt;th&gt;Actions&lt;/th&gt;
        &lt;/permission:check&gt;
    &lt;/table&gt;
&lt;/permission:check&gt;
        </div>
        
        <permission:check code="EMPLOYEE_VIEW">
            <table border="1" cellpadding="10" style="width: 100%; border-collapse: collapse;">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên nhân viên</th>
                        <th>Email</th>
                        <permission:check code="EMPLOYEE_EDIT">
                            <th>Actions</th>
                        </permission:check>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>001</td>
                        <td>Nguyễn Văn A</td>
                        <td>nva@example.com</td>
                        <permission:check code="EMPLOYEE_EDIT">
                            <td>
                                <button class="btn btn-warning">Edit</button>
                                <permission:check code="EMPLOYEE_DELETE">
                                    <button class="btn btn-danger">Delete</button>
                                </permission:check>
                            </td>
                        </permission:check>
                    </tr>
                </tbody>
            </table>
        </permission:check>
    </div>
    
    <div class="example-section">
        <h2>6. Ví dụ: Form với Permission Check</h2>
        
        <permission:check code="EMPLOYEE_CREATE">
            <form style="max-width: 500px;">
                <h3>Thêm nhân viên mới</h3>
                <div style="margin: 10px 0;">
                    <label>Họ tên:</label><br>
                    <input type="text" name="fullName" style="width: 100%; padding: 8px;">
                </div>
                <div style="margin: 10px 0;">
                    <label>Email:</label><br>
                    <input type="email" name="email" style="width: 100%; padding: 8px;">
                </div>
                <div style="margin: 10px 0;">
                    <label>Phòng ban:</label><br>
                    <select name="department" style="width: 100%; padding: 8px;">
                        <option>IT</option>
                        <option>HR</option>
                        <option>Sales</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-success">Lưu</button>
            </form>
        </permission:check>
        
        <permission:check code="EMPLOYEE_CREATE">
            <p style="display: none;"></p>
        </permission:check>
        <c:if test="${empty pageScope.hasCreatePermission}">
            <div class="permission-box no-permission">
                ✗ Bạn không có quyền tạo nhân viên mới
            </div>
        </c:if>
    </div>
    
    <div class="example-section">
        <h2>7. Link đến trang quản lý</h2>
        <permission:check code="PERMISSION_MANAGE">
            <a href="${pageContext.request.contextPath}/permission-settings" 
               class="btn btn-primary">
                ⚙️ Đi đến trang Quản lý Phân quyền
            </a>
        </permission:check>
    </div>
    
    <div class="example-section">
        <h2>📚 Tài liệu</h2>
        <p>Xem file <code>PERMISSION_SYSTEM_GUIDE.md</code> để biết thêm chi tiết về:</p>
        <ul>
            <li>Cách sử dụng trong Java Controller</li>
            <li>Danh sách đầy đủ các permissions</li>
            <li>Cách thêm permission mới</li>
            <li>Best practices và troubleshooting</li>
        </ul>
    </div>
</body>
</html>
