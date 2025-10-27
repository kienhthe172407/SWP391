<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Account - Human Resources Management System</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts - Nunito -->
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/auth.css" rel="stylesheet">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-xl-10 col-lg-12 col-md-9">
                    <div class="card auth-card">
                        <div class="card-body p-0">
                            <div class="row">
                                <div class="col-lg-6 d-none d-lg-block bg-login-image">
                                    <div class="p-5 h-100 d-flex flex-column justify-content-center" 
                                         style="background: linear-gradient(135deg, #4e73df 0%, #224abe 100%); color: white;">
                                        <h1 class="display-4 mb-4">Join Us</h1>
                                        <p class="lead">Create an account to access all features of the human resources management system</p>
                                        <div class="mt-5">
                                            <div class="d-flex align-items-center mb-3">
                                                <i class="fas fa-check-circle me-3"></i>
                                                <span>Manage personal information</span>
                                            </div>
                                            <div class="d-flex align-items-center mb-3">
                                                <i class="fas fa-check-circle me-3"></i>
                                                <span>Track employment contracts</span>
                                            </div>
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-check-circle me-3"></i>
                                                <span>Access reports and statistics</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="auth-title">Register Account</h1>
                                            <p class="auth-subtitle">Fill in the information to create a new account</p>
                                        </div>
                                        
                                        <!-- Show error message if any -->
                                        <% if(request.getAttribute("errorMessage") != null) { %>
                                            <div class="auth-error">
                                                <i class="fas fa-exclamation-circle me-2"></i>
                                                <%= request.getAttribute("errorMessage") %>
                                            </div>
                                        <% } %>
                                        
                                        <!-- Show success message if any -->
                                        <% if(request.getAttribute("successMessage") != null) { %>
                                            <div class="auth-success">
                                                <i class="fas fa-check-circle me-2"></i>
                                                <%= request.getAttribute("successMessage") %>
                                            </div>
                                        <% } %>
                                        
                                        <form class="auth-form" action="${pageContext.request.contextPath}/register" method="post">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label" for="firstName">First Name</label>
                                                        <div class="input-group">
                                                            <span class="input-group-text bg-transparent border-end-0">
                                                                <i class="fas fa-user"></i>
                                                            </span>
                                                            <input type="text" class="form-control border-start-0" id="firstName" name="firstName" 
                                                                   placeholder="Enter first name" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label" for="lastName">Last Name</label>
                                                        <div class="input-group">
                                                            <span class="input-group-text bg-transparent border-end-0">
                                                                <i class="fas fa-user"></i>
                                                            </span>
                                                            <input type="text" class="form-control border-start-0" id="lastName" name="lastName" 
                                                                   placeholder="Enter last name" required>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label class="form-label" for="email">Email</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-envelope"></i>
                                                    </span>
                                                    <input type="email" class="form-control border-start-0" id="email" name="email" 
                                                           placeholder="Enter email" required>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label" for="dateOfBirth">Date of Birth</label>
                                                        <div class="input-group">
                                                            <span class="input-group-text bg-transparent border-end-0">
                                                                <i class="fas fa-calendar"></i>
                                                            </span>
                                                            <input type="date" class="form-control border-start-0" id="dateOfBirth" name="dateOfBirth" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label" for="gender">Gender</label>
                                                        <div class="input-group">
                                                            <span class="input-group-text bg-transparent border-end-0">
                                                                <i class="fas fa-venus-mars"></i>
                                                            </span>
                                                            <select class="form-select border-start-0" id="gender" name="gender" required>
                                                                <option value="Male">Male</option>
                                                                <option value="Female">Female</option>
                                                                <option value="Other">Other</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label class="form-label" for="phone">Phone Number</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-phone"></i>
                                                    </span>
                                                    <input type="tel" class="form-control border-start-0" id="phone" name="phone" 
                                                           placeholder="Enter phone number" required>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="form-label" for="username">Username</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-at"></i>
                                                    </span>
                                                    <input type="text" class="form-control border-start-0" id="username" name="username" 
                                                           placeholder="Enter username" required>
                                                </div>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label class="form-label" for="password">Password</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-lock"></i>
                                                    </span>
                                                    <input type="password" class="form-control border-start-0" id="password" name="password" 
                                                           placeholder="Enter password" required>
                                                </div>
                                                <div class="password-strength" id="passwordStrength"></div>
                                                <small class="form-text text-muted">Password must be at least 8 characters long, including uppercase, lowercase, numbers and special characters</small>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label class="form-label" for="confirmPassword">Confirm Password</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-check-double"></i>
                                                    </span>
                                                    <input type="password" class="form-control border-start-0" id="confirmPassword" name="confirmPassword" 
                                                           placeholder="Re-enter password" required>
                                                </div>
                                            </div>
                                            
                                            <div class="form-group">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="checkbox" id="agreeTerms" name="agreeTerms" required>
                                                    <label class="form-check-label" for="agreeTerms">
                                                        I agree to the <a href="#" class="text-primary">Terms of Service</a> and <a href="#" class="text-primary">Privacy Policy</a>
                                                    </label>
                                                </div>
                                            </div>
                                            
                                            <button type="submit" class="btn btn-primary btn-block">
                                                Register <i class="fas fa-user-plus ms-2"></i>
                                            </button>
                                        </form>
                                        <hr>
                                        <div class="auth-links">
                                            <a href="${pageContext.request.contextPath}/auth/login.jsp">
                                                <i class="fas fa-arrow-left me-1"></i> Back to login page
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Password Strength Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const passwordInput = document.getElementById('password');
            const strengthIndicator = document.getElementById('passwordStrength');
            
            passwordInput.addEventListener('input', function() {
                const password = passwordInput.value;
                let strength = 0;
                
                // Check password length
                if (password.length >= 8) {
                    strength += 1;
                }
                
                // Check for uppercase letters
                if (/[A-Z]/.test(password)) {
                    strength += 1;
                }
                
                // Check for lowercase letters
                if (/[a-z]/.test(password)) {
                    strength += 1;
                }
                
                // Check for numbers
                if (/[0-9]/.test(password)) {
                    strength += 1;
                }
                
                // Check for special characters
                if (/[^A-Za-z0-9]/.test(password)) {
                    strength += 1;
                }
                
                // Update strength indicator
                strengthIndicator.className = 'password-strength';
                
                if (password.length === 0) {
                    strengthIndicator.style.width = '0';
                } else if (strength <= 2) {
                    strengthIndicator.classList.add('strength-weak');
                } else if (strength <= 4) {
                    strengthIndicator.classList.add('strength-medium');
                } else {
                    strengthIndicator.classList.add('strength-strong');
                }
            });
            
            // Check password match
            const confirmInput = document.getElementById('confirmPassword');
            confirmInput.addEventListener('input', function() {
                if (confirmInput.value === passwordInput.value) {
                    confirmInput.setCustomValidity('');
                } else {
                    confirmInput.setCustomValidity('Passwords do not match');
                }
            });
        });
    </script>
</body>
</html>
