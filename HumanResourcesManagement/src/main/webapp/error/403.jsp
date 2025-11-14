<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Access Denied</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .error-container {
            background: white;
            border-radius: 20px;
            padding: 60px;
            box-shadow: 0 10px 50px rgba(0,0,0,0.3);
            text-align: center;
            max-width: 600px;
        }
        .error-icon {
            font-size: 120px;
            color: #dc3545;
            margin-bottom: 30px;
        }
        .error-code {
            font-size: 80px;
            font-weight: 700;
            color: #333;
            margin-bottom: 20px;
        }
        .error-message {
            font-size: 24px;
            color: #666;
            margin-bottom: 30px;
        }
        .error-details {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="fas fa-lock"></i>
        </div>
        <div class="error-code">403</div>
        <div class="error-message">Access Denied</div>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-details">
                <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                <strong>Details:</strong><br>
                ${errorMessage}
            </div>
        </c:if>
        
        <p class="text-muted">
            You don't have permission to access this page. Please contact the administrator if you believe this is an error.
        </p>
        
        <div class="mt-4">
            <a href="javascript:history.back()" class="btn btn-outline-secondary me-2">
                <i class="fas fa-arrow-left me-2"></i>Go Back
            </a>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                <i class="fas fa-home me-2"></i>Home
            </a>
        </div>
    </div>
</body>
</html>
