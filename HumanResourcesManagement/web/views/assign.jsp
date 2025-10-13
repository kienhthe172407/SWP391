<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Management - Giao Công việc</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #4169E1 0%, #5A7FDB 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .container {
            width: 100%;
            max-width: 600px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 8px 32px rgba(65, 105, 225, 0.3);
            overflow: hidden;
        }
        
        .form-header {
            background: linear-gradient(135deg, #4169E1 0%, #5A7FDB 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .form-header h1 {
            font-size: 24px;
            margin-bottom: 10px;
        }
        
        .form-header p {
            opacity: 0.9;
            font-size: 14px;
        }
        
        .form-body {
            padding: 30px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #4169E1;
            font-weight: 600;
            font-size: 14px;
        }
        
        input[type="text"],
        input[type="datetime-local"],
        textarea,
        select {
            width: 100%;
            padding: 12px;
            border: 2px solid #E0E0E0;
            border-radius: 6px;
            font-size: 14px;
            font-family: inherit;
            transition: border-color 0.3s;
        }
        
        input[type="text"]:focus,
        input[type="datetime-local"]:focus,
        textarea:focus,
        select:focus {
            outline: none;
            border-color: #4169E1;
            box-shadow: 0 0 0 3px rgba(65, 105, 225, 0.1);
        }
        
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        
        .form-row-full {
            grid-column: 1 / -1;
        }
        
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        
        .btn {
            flex: 1;
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #4169E1 0%, #5A7FDB 100%);
            color: white;
            box-shadow: 0 4px 8px rgba(65, 105, 225, 0.3);
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(65, 105, 225, 0.4);
        }
        
        .btn-cancel {
            background: #F5F5F5;
            color: #666;
            border: 2px solid #E0E0E0;
        }
        
        .btn-cancel:hover {
            background: #EEEEEE;
            border-color: #4169E1;
        }
        
        .required {
            color: #F44336;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-header">
            <h1>📝 Giao Công việc Mới</h1>
            <p>Nhập thông tin để giao công việc cho nhân viên</p>
        </div>
        
        <div class="form-body">
            <form method="POST" action="${pageContext.request.contextPath}/task/assign">
                <div class="form-row">
                    <div class="form-group">
                        <label for="taskName">Tên Công việc <span class="required">*</span></label>
                        <input type="text" id="taskName" name="taskName" required placeholder="Nhập tên công việc">
                    </div>
                    
                    <div class="form-group">
                        <label for="assignedTo">Giao cho Nhân viên <span class="required">*</span></label>
                        <select id="assignedTo" name="assignedTo" required>
                            <option value="">-- Chọn nhân viên --</option>
                            <c:forEach items="${employees}" var="emp">
                                <option value="${emp.employeeId}">${emp.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="priority">Độ Ưu tiên <span class="required">*</span></label>
                        <select id="priority" name="priority" required>
                            <option value="">-- Chọn ưu tiên --</option>
                            <option value="LOW">Thấp</option>
                            <option value="MEDIUM">Trung bình</option>
                            <option value="HIGH">Cao</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="dueDate">Ngày Hạn <span class="required">*</span></label>
                        <input type="datetime-local" id="dueDate" name="dueDate" required>
                    </div>
                </div>
                
                <div class="form-group form-row-full">
                    <label for="description">Mô Tả Chi Tiết</label>
                    <textarea id="description" name="description" placeholder="Nhập mô tả chi tiết công việc..."></textarea>
                </div>
                
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/task/" class="btn btn-cancel">Hủy</a>
                    <button type="submit" class="btn btn-submit">Giao Công việc</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>