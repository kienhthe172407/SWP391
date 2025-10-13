<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Management - Danh sách công việc</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f4f8;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(65, 105, 225, 0.15);
            padding: 30px;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 3px solid #4169E1;
        }
        
        h1 {
            color: #4169E1;
            font-size: 28px;
            font-weight: 600;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            font-weight: 500;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #4169E1 0%, #5A7FDB 100%);
            color: white;
            box-shadow: 0 4px 8px rgba(65, 105, 225, 0.3);
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(65, 105, 225, 0.4);
        }
        
        .btn-warning {
            background: #FF9800;
            color: white;
        }
        
        .btn-warning:hover {
            background: #F57C00;
            transform: translateY(-2px);
        }
        
        .btn-danger {
            background: #F44336;
            color: white;
        }
        
        .btn-danger:hover {
            background: #D32F2F;
            transform: translateY(-2px);
        }
        
        .btn-info {
            background: #2196F3;
            color: white;
        }
        
        .btn-info:hover {
            background: #1976D2;
            transform: translateY(-2px);
        }
        
        .btn-success {
            background: #4CAF50;
            color: white;
        }
        
        .btn-success:hover {
            background: #45a049;
            transform: translateY(-2px);
        }
        
        .btn-sm {
            padding: 5px 12px;
            font-size: 12px;
            margin: 0 3px;
        }
        
        .alert {
            padding: 15px 20px;
            margin-bottom: 20px;
            border-radius: 8px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            animation: slideIn 0.3s ease;
        }
        
        @keyframes slideIn {
            from {
                transform: translateY(-20px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }
        
        .alert-success {
            background: #E8F5E9;
            color: #2E7D32;
            border-left: 4px solid #4CAF50;
        }
        
        .alert-error {
            background: #FFEBEE;
            color: #C62828;
            border-left: 4px solid #F44336;
        }
        
        .close-btn {
            cursor: pointer;
            font-size: 20px;
            font-weight: bold;
            opacity: 0.7;
            transition: opacity 0.2s;
        }
        
        .close-btn:hover {
            opacity: 1;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th {
            background: linear-gradient(135deg, #4169E1 0%, #5A7FDB 100%);
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: white;
            border-bottom: 2px solid #4169E1;
        }
        
        td {
            padding: 12px 15px;
            border-bottom: 1px solid #E0E0E0;
        }
        
        tr:hover {
            background: #F8F9FF;
        }
        
        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            display: inline-block;
        }
        
        .status-pending {
            background: #FFF3E0;
            color: #E65100;
        }
        
        .status-in-progress {
            background: #E3F2FD;
            color: #1565C0;
        }
        
        .status-completed {
            background: #E8F5E9;
            color: #2E7D32;
        }
        
        .status-cancelled {
            background: #FFEBEE;
            color: #C62828;
        }
        
        .priority-high {
            color: #F44336;
            font-weight: bold;
        }
        
        .priority-medium {
            color: #FF9800;
            font-weight: bold;
        }
        
        .priority-low {
            color: #4CAF50;
            font-weight: bold;
        }
        
        .actions {
            display: flex;
            gap: 3px;
            flex-wrap: wrap;
        }
        
        .no-tasks {
            text-align: center;
            padding: 50px 20px;
            color: #9E9E9E;
            font-size: 16px;
        }
        
        .status-select {
            padding: 5px 10px;
            border: 1px solid #4169E1;
            border-radius: 4px;
            font-size: 12px;
            color: #4169E1;
            cursor: pointer;
        }
        
        .status-select:focus {
            outline: none;
            border-color: #2F51B5;
        }
        
        .filter-section {
            margin-bottom: 20px;
            display: flex;
            gap: 15px;
            align-items: center;
        }
        
        .filter-section label {
            color: #4169E1;
            font-weight: 500;
        }
    </style>
    <script>
        function closeAlert() {
            const alert = document.querySelector('.alert');
            if (alert) {
                alert.style.display = 'none';
            }
        }
        
        function updateStatus(taskId) {
            const select = document.getElementById('status-' + taskId);
            const status = select.value;
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/task/updateStatus';
            
            const taskInput = document.createElement('input');
            taskInput.type = 'hidden';
            taskInput.name = 'taskId';
            taskInput.value = taskId;
            
            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'status';
            statusInput.value = status;
            
            form.appendChild(taskInput);
            form.appendChild(statusInput);
            document.body.appendChild(form);
            form.submit();
        }
        
        function cancelTask(taskId) {
            if (confirm('Bạn chắc chắn muốn hủy công việc này không?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/task/cancel';
                
                const taskInput = document.createElement('input');
                taskInput.type = 'hidden';
                taskInput.name = 'taskId';
                taskInput.value = taskId;
                
                form.appendChild(taskInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📋 Quản lý Công việc</h1>
            <c:if test="${userRole == 'MANAGER' || userRole == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/task/assign" class="btn btn-primary">
                    + Giao Công việc
                </a>
            </c:if>
        </div>
        
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-${sessionScope.messageType}">
                <span>${sessionScope.message}</span>
                <span class="close-btn" onclick="closeAlert()">&times;</span>
            </div>
            <c:remove var="message" scope="session" />
            <c:remove var="messageType" scope="session" />
        </c:if>
        
        <c:choose>
            <c:when test="${not empty tasks}">
                <table>
                    <thead>
                        <tr>
                            <th>Tên Công việc</th>
                            <th>Nhân viên</th>
                            <th>Ưu tiên</th>
                            <th>Trạng thái</th>
                            <th>Ngày Hạn</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${tasks}" var="task">
                            <tr>
                                <td>
                                    <strong>${task.taskName}</strong>
                                </td>
                                <td>${task.employeeName}</td>
                                <td>
                                    <span class="priority-${task.priority.toLowerCase()}">
                                        ${task.priority}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${task.status == 'PENDING'}">
                                            <span class="status-badge status-pending">Chờ xử lý</span>
                                        </c:when>
                                        <c:when test="${task.status == 'IN_PROGRESS'}">
                                            <span class="status-badge status-in-progress">Đang thực hiện</span>
                                        </c:when>
                                        <c:when test="${task.status == 'COMPLETED'}">
                                            <span class="status-badge status-completed">Hoàn thành</span>
                                        </c:when>
                                        <c:when test="${task.status == 'CANCELLED'}">
                                            <span class="status-badge status-cancelled">Đã hủy</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <fmt:formatDate value="${task.dueDate}" pattern="dd/MM/yyyy HH:mm" />
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/task/view?id=${task.taskId}" 
                                           class="btn btn-info btn-sm">Xem</a>
                                        <c:if test="${userRole == 'MANAGER' || userRole == 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/task/edit?id=${task.taskId}" 
                                               class="btn btn-warning btn-sm">Sửa</a>
                                            <c:if test="${task.status != 'CANCELLED' && task.status != 'COMPLETED'}">
                                                <button class="btn btn-danger btn-sm" 
                                                        onclick="cancelTask(${task.taskId})">Hủy</button>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${userRole == 'EMPLOYEE'}">
                                            <c:if test="${task.status != 'COMPLETED' && task.status != 'CANCELLED'}">
                                                <select id="status-${task.taskId}" class="status-select" 
                                                        onchange="updateStatus(${task.taskId})">
                                                    <option value="PENDING" <c:if test="${task.status == 'PENDING'}">selected</c:if>>Chờ xử lý</option>
                                                    <option value="IN_PROGRESS" <c:if test="${task.status == 'IN_PROGRESS'}">selected</c:if>>Đang thực hiện</option>
                                                    <option value="COMPLETED" <c:if test="${task.status == 'COMPLETED'}">selected</c:if>>Hoàn thành</option>
                                                </select>
                                            </c:if>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="no-tasks">
                    <p>📌 Không có công việc nào</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>