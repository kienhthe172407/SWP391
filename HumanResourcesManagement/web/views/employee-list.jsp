<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Nhân viên</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            padding: 20px;
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        h1 {
            color: #333;
            margin-bottom: 30px;
            text-align: center;
        }
        
        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            transition: all 0.3s;
        }
        
        .btn-primary {
            background: #007bff;
            color: white;
        }
        
        .btn-primary:hover {
            background: #0056b3;
        }
        
        .btn-edit {
            background: #28a745;
            color: white;
            padding: 5px 15px;
            font-size: 13px;
        }
        
        .btn-edit:hover {
            background: #218838;
        }
        
        .btn-delete {
            background: #dc3545;
            color: white;
            padding: 5px 15px;
            font-size: 13px;
        }
        
        .btn-delete:hover {
            background: #c82333;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background: #007bff;
            color: white;
            font-weight: 600;
        }
        
        tr:hover {
            background: #f8f9fa;
        }
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
        }
        
        .status-active {
            color: #28a745;
            font-weight: 600;
        }
        
        .status-inactive {
            color: #dc3545;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Quản lý Nhân sự</h1>
        
        <!-- Thông báo thành công/lỗi -->
        <c:if test="${param.success == 'create'}">
            <div class="alert alert-success">Thêm nhân viên thành công!</div>
        </c:if>
        <c:if test="${param.success == 'update'}">
            <div class="alert alert-success">Cập nhật nhân viên thành công!</div>
        </c:if>
        <c:if test="${param.success == 'delete'}">
            <div class="alert alert-success">Xóa nhân viên thành công!</div>
        </c:if>
        <c:if test="${param.error == 'delete'}">
            <div class="alert alert-error">Xóa nhân viên thất bại!</div>
        </c:if>
        
        <div class="toolbar">
            <h2>Danh sách Nhân viên</h2>
            <a href="${pageContext.request.contextPath}/employee?action=create" class="btn btn-primary">
                + Thêm Nhân viên
            </a>
        </div>
        
        <c:choose>
            <c:when test="${empty employees}">
                <div class="no-data">Chưa có nhân viên nào trong hệ thống</div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Mã NV</th>
                            <th>Họ và Tên</th>
                            <th>Ngày sinh</th>
                            <th>Giới tính</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Chức vụ</th>
                            <th>Phòng ban</th>
                            <th>Lương</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="emp" items="${employees}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td>${emp.employeeCode}</td>
                                <td>${emp.fullName}</td>
                                <td><fmt:formatDate value="${emp.dateOfBirth}" pattern="dd/MM/yyyy"/></td>
                                <td>${emp.gender}</td>
                                <td>${emp.email}</td>
                                <td>${emp.phone}</td>
                                <td>${emp.position}</td>
                                <td>${emp.department}</td>
                                <td><fmt:formatNumber value="${emp.salary}" type="number" groupingUsed="true"/> VNĐ</td>
                                <td>
                                    <span class="${emp.status == 'Đang làm việc' ? 'status-active' : 'status-inactive'}">
                                        ${emp.status}
                                    </span>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/employee?action=edit&id=${emp.id}" 
                                           class="btn btn-edit">Sửa</a>
                                        <a href="javascript:void(0);" 
                                           onclick="confirmDelete(${emp.id}, '${emp.fullName}')" 
                                           class="btn btn-delete">Xóa</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script>
        function confirmDelete(id, name) {
            if (confirm('Bạn có chắc chắn muốn xóa nhân viên "' + name + '" không?')) {
                window.location.href = '${pageContext.request.contextPath}/employee?action=delete&id=' + id;
            }
        }
    </script>
</body>
</html>