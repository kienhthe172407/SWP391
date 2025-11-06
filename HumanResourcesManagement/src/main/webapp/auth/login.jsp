<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="en">
    <!--Bootstrap  -->

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Human Resources Management System</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <!-- Google Fonts - Inter (hỗ trợ tiếng Việt tốt) -->
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
            rel="stylesheet">
        <!-- Custom CSS -->
        <link href="${pageContext.request.contextPath}/css/auth.css" rel="stylesheet">
    </head>

    <body class="auth-page" style="margin: 0; padding: 0; height: 100vh; display: flex; align-items: center; justify-content: center;">
        <div class="auth-container" style="width: 100%; max-width: 1200px; padding: 2rem;">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-xl-10 col-lg-12 col-md-9">
                        <div class="card auth-card">
                            <div class="card-body p-0">
                                <div class="row">
                                    <div class="col-lg-6 d-none d-lg-block bg-login-image">
                                        <div class="p-5 h-100 d-flex flex-column justify-content-center"
                                            style="background: linear-gradient(135deg, #4e73df 0%, #224abe 100%); color: white;">
                                            <h1 class="display-4 mb-4">Human Resources Management System</h1>
                                            <p class="lead">Comprehensive solution for managing your company's human
                                                resources</p>
                                            <div class="mt-5">
                                                <div class="d-flex align-items-center mb-3">
                                                    <i class="fas fa-check-circle me-3"></i>
                                                    <span>Manage employee information</span>
                                                </div>
                                                <div class="d-flex align-items-center mb-3">
                                                    <i class="fas fa-check-circle me-3"></i>
                                                    <span>Track work performance</span>
                                                </div>
                                                <div class="d-flex align-items-center">
                                                    <i class="fas fa-check-circle me-3"></i>
                                                    <span>Manage contracts and compensation</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="p-5">
                                            <div class="text-center">
                                                <h1 class="auth-title">Login</h1><!-- đăng nhập -->
                                                <p class="auth-subtitle">Enter your login information to access the
                                                    system</p>
                                            </div>

                                            <!-- Show error message if any -->
                                            <% 
                                                String errorMessage = (String) request.getAttribute("errorMessage");
                                                if (errorMessage == null) {
                                                    errorMessage = request.getParameter("error");
                                                }
                                                if(errorMessage != null) { 
                                            %>
                                                <div class="auth-error" style="background-color: #fee2e2; border: 1px solid #ef4444; border-left: 4px solid #dc2626; border-radius: 0.375rem; color: #991b1b; font-size: 0.9rem; font-weight: 500; margin-bottom: 1.5rem; padding: 0.875rem 1rem; box-shadow: 0 1px 3px rgba(220, 38, 38, 0.1);">
                                                    <i class="fas fa-exclamation-circle me-2" style="color: #dc2626;"></i>
                                                    <%= errorMessage %>
                                                </div>
                                                <% } %>

                                                    <form class="auth-form"
                                                        action="${pageContext.request.contextPath}/login" method="post">
                                                        <div class="form-group">
                                                            <label class="form-label" for="username">Username hoặc
                                                                Email</label>
                                                            <div class="input-group">
                                                                <span
                                                                    class="input-group-text bg-transparent border-end-0">
                                                                    <i class="fas fa-user"></i>
                                                                </span>
                                                                <input type="text" class="form-control border-start-0"
                                                                    id="username" name="username"
                                                                    placeholder="Enter username or email" required>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="form-label" for="password">Password</label>
                                                            <div class="input-group">
                                                                <span
                                                                    class="input-group-text bg-transparent border-end-0">
                                                                    <i class="fas fa-lock"></i>
                                                                </span>
                                                                <input type="password"
                                                                    class="form-control border-start-0" id="password"
                                                                    name="password" placeholder="Enter password"
                                                                    required>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <div
                                                                class="d-flex justify-content-between align-items-center">
                                                                <div class="form-check">
                                                                    <input class="form-check-input" type="checkbox"
                                                                        id="rememberMe" name="rememberMe">
                                                                    <label class="form-check-label" for="rememberMe">
                                                                        Remember me
                                                                    </label>
                                                                </div>
                                                                <a href="${pageContext.request.contextPath}/auth/forgot-password.jsp"
                                                                    class="text-decoration-none small">
                                                                    <i class="fas fa-key me-1"></i>Quên mật khẩu?
                                                                </a>
                                                            </div>
                                                        </div>
                                                        <button type="submit" class="btn btn-primary btn-block">
                                                            Login <i class="fas fa-sign-in-alt ms-2"></i>
                                                        </button>
                                                    </form>

                                                    <div class="text-center my-3">
                                                        <span class="text-muted">OR</span>
                                                    </div>

                                                    <!-- Google Sign In Button -->
                                                    <a href="${pageContext.request.contextPath}/google-login"
                                                        class="btn btn-outline-danger btn-block d-flex align-items-center justify-content-center">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18"
                                                            viewBox="0 0 48 48" class="me-2">
                                                            <path fill="#FFC107"
                                                                d="M43.611,20.083H42V20H24v8h11.303c-1.649,4.657-6.08,8-11.303,8c-6.627,0-12-5.373-12-12c0-6.627,5.373-12,12-12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C12.955,4,4,12.955,4,24c0,11.045,8.955,20,20,20c11.045,0,20-8.955,20-20C44,22.659,43.862,21.35,43.611,20.083z" />
                                                            <path fill="#FF3D00"
                                                                d="M6.306,14.691l6.571,4.819C14.655,15.108,18.961,12,24,12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C16.318,4,9.656,8.337,6.306,14.691z" />
                                                            <path fill="#4CAF50"
                                                                d="M24,44c5.166,0,9.86-1.977,13.409-5.192l-6.19-5.238C29.211,35.091,26.715,36,24,36c-5.202,0-9.619-3.317-11.283-7.946l-6.522,5.025C9.505,39.556,16.227,44,24,44z" />
                                                            <path fill="#1976D2"
                                                                d="M43.611,20.083H42V20H24v8h11.303c-0.792,2.237-2.231,4.166-4.087,5.571c0.001-0.001,0.002-0.001,0.003-0.002l6.19,5.238C36.971,39.205,44,34,44,24C44,22.659,43.862,21.35,43.611,20.083z" />
                                                        </svg>
                                                        Sign in with Google
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

        <!-- Bootstrap 5 JS Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>