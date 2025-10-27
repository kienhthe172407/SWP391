<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create New Account - Human Resources Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <style>
        .create-user-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        .form-header {
            text-align: center;
            margin-bottom: 30px;
            color: #2c3e50;
        }
        
        .form-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
        }
        
        .form-header p {
            margin: 10px 0 0 0;
            color: #7f8c8d;
            font-size: 16px;
        }
        
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            flex: 1;
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2c3e50;
            font-size: 14px;
        }
        
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e8ed;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
            box-sizing: border-box;
        }
        
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
        }
        
        .required {
            color: #e74c3c;
        }
        
        .form-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
        }
        
        .btn-primary:hover {
            background: linear-gradient(135deg, #2980b9, #1f5f8b);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
        }
        
        .btn-secondary {
            background: #95a5a6;
            color: white;
        }
        
        .btn-secondary:hover {
            background: #7f8c8d;
            transform: translateY(-2px);
        }
        
        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
        }
        
        .alert-error {
            background: #fdf2f2;
            border: 1px solid #fecaca;
            color: #dc2626;
        }
        
        .alert-success {
            background: #f0fdf4;
            border: 1px solid #bbf7d0;
            color: #16a34a;
        }
        
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #3498db;
            text-decoration: none;
            font-weight: 500;
        }
        
        .back-link:hover {
            text-decoration: underline;
        }
        
        .help-text {
            font-size: 12px;
            color: #7f8c8d;
            margin-top: 5px;
        }
        
        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
                gap: 0;
            }
            
            .create-user-container {
                margin: 10px;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="create-user-container">
        <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="back-link">
             Back to Dashboard
        </a>
        
        <div class="form-header">
            <h1> Create New Account</h1>
            <p>Create account for new employee in the system</p>
        </div>
        
        <!-- Display error message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ${errorMessage}
            </div>
        </c:if>
        
        <!-- Display success message -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </c:if>
        
        <form method="post" action="${pageContext.request.contextPath}/create-user">
            <!-- Login information -->
            <h3 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #ecf0f1; padding-bottom: 10px;">
                 Login Information
            </h3>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="username">Username <span class="required">*</span></label>
                    <input type="text" id="username" name="username" 
                           value="${param.username}" 
                           placeholder="Enter username" required>
                    <div class="help-text">Username must be unique in the system</div>
                </div>
                
                <div class="form-group">
                    <label for="email">Email <span class="required">*</span></label>
                    <input type="email" id="email" name="email" 
                           value="${param.email}" 
                           placeholder="example@company.com" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="password">Password <span class="required">*</span></label>
                    <input type="password" id="password" name="password" 
                           placeholder="Enter password" required>
                    <div class="help-text">Password must be at least 6 characters long</div>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password <span class="required">*</span></label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           placeholder="Re-enter password" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="role">Role <span class="required">*</span></label>
                    <select id="role" name="role" required>
                        <option value="">-- Select role --</option>
                        <option value="Employee" ${param.role == 'Employee' ? 'selected' : ''}>Employee</option>
                        <option value="HR" ${param.role == 'HR' ? 'selected' : ''}>HR Staff</option>
                        <option value="HR Manager" ${param.role == 'HR Manager' ? 'selected' : ''}>HR Manager</option>
                        <option value="Dept Manager" ${param.role == 'Dept Manager' ? 'selected' : ''}>Department Manager</option>
                        <option value="Admin" ${param.role == 'Admin' ? 'selected' : ''}>Administrator</option>
                    </select>
                </div>
            </div>
            
            <!-- Personal information -->
            <h3 style="color: #2c3e50; margin: 30px 0 20px 0; border-bottom: 2px solid #ecf0f1; padding-bottom: 10px;">
                Personal Information
            </h3>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="firstName">First Name</label>
                    <input type="text" id="firstName" name="firstName" 
                           value="${param.firstName}" 
                           placeholder="Enter first name">
                </div>
                
                <div class="form-group">
                    <label for="lastName">Last Name</label>
                    <input type="text" id="lastName" name="lastName" 
                           value="${param.lastName}" 
                           placeholder="Enter last name">
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone" 
                           value="${param.phone}" 
                           placeholder="0123456789">
                </div>
                
                <div class="form-group">
                    <label for="dateOfBirth">Date of Birth</label>
                    <input type="date" id="dateOfBirth" name="dateOfBirth" 
                           value="${param.dateOfBirth}">
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="gender">Gender</label>
                    <select id="gender" name="gender">
                        <option value="">-- Select gender --</option>
                        <option value="Male" ${param.gender == 'Male' ? 'selected' : ''}>Male</option>
                        <option value="Female" ${param.gender == 'Female' ? 'selected' : ''}>Female</option>
                        <option value="Other" ${param.gender == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                     Create Account
                </button>
                <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="btn btn-secondary">
                     Cancel
                </a>
            </div>
        </form>
    </div>
    
    <script>
        // Check password match
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            
            if (password !== confirmPassword) {
                this.setCustomValidity('Passwords do not match');
            } else {
                this.setCustomValidity('');
            }
        });
        
        // Check password length
        document.getElementById('password').addEventListener('input', function() {
            const password = this.value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password.length < 6) {
                this.setCustomValidity('Password must be at least 6 characters long');
            } else {
                this.setCustomValidity('');
                // Check confirm password again
                if (confirmPassword && password !== confirmPassword) {
                    document.getElementById('confirmPassword').setCustomValidity('Passwords do not match');
                } else {
                    document.getElementById('confirmPassword').setCustomValidity('');
                }
            }
        });
        
        // Auto-generate username from email
        document.getElementById('email').addEventListener('input', function() {
            const email = this.value;
            const usernameField = document.getElementById('username');
            
            if (email && !usernameField.value) {
                const username = email.split('@')[0];
                usernameField.value = username;
            }
        });
    </script>
</body>
</html>
