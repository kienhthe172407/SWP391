<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Forgot Password - HR Management System</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                padding: 20px;
            }

            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            }

            .btn-primary {
                background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
                border: none;
                padding: 12px;
                font-weight: 600;
                transition: all 0.3s;
            }

            .btn-primary:hover {
                background: linear-gradient(135deg, #1e40af 0%, #2563eb 100%);
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
            }

            .form-control:focus {
                border-color: #2563eb;
                box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.25);
            }
        </style>
    </head>

    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5">
                    <div class="card">
                        <div class="card-body p-5">
                            <!-- Header -->
                            <div class="text-center mb-4">
                                <div class="mb-3">
                                    <i class="fas fa-key fa-3x text-primary"></i>
                                </div>
                                <h2 class="fw-bold mb-2">Forgot Password?</h2>
                                <p class="text-muted">Enter your email to receive password reset instructions</p>
                            </div>

                            <!-- Success Message -->
                            <% if(request.getAttribute("successMessage") !=null) { %>
                                <div class="alert alert-success" role="alert">
                                    <i class="fas fa-check-circle me-2"></i>
                                    <%= request.getAttribute("successMessage") %>
                                </div>
                                <div class="text-center">
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary w-100">
                                        <i class="fas fa-sign-in-alt me-2"></i>Login Now
                                    </a>
                                </div>
                                <% } else { %>

                                    <!-- Error Message -->
                                    <% if(request.getAttribute("errorMessage") !=null) { %>
                                        <div class="alert alert-danger" role="alert">
                                            <i class="fas fa-exclamation-circle me-2"></i>
                                            <%= request.getAttribute("errorMessage") %>
                                        </div>
                                        <% } %>

                                            <!-- Form -->
                                            <form action="${pageContext.request.contextPath}/auth/forgot-password"
                                                method="post">
                                                <div class="mb-4">
                                                    <label for="email" class="form-label fw-semibold">
                                                        <i class="fas fa-envelope me-2"></i>Email Address
                                                    </label>
                                                    <input type="email" class="form-control form-control-lg" id="email"
                                                        name="email" placeholder="example@email.com" required>
                                                    <div class="form-text">
                                                        <i class="fas fa-info-circle me-1"></i>
                                                        We will send a new password to this email
                                                    </div>
                                                </div>

                                                <button type="submit" class="btn btn-primary w-100 btn-lg mb-3">
                                                    <i class="fas fa-paper-plane me-2"></i>Send New Password
                                                </button>
                                            </form>

                                            <% } %>

                                                <!-- Divider -->
                                                <hr class="my-4">

                                                <!-- Back to login -->
                                                <div class="text-center">
                                                    <a href="${pageContext.request.contextPath}/login"
                                                        class="text-decoration-none">
                                                        <i class="fas fa-arrow-left me-2"></i>Back to Login
                                                    </a>
                                                </div>

                                                <!-- Info box -->
                                                <div class="mt-4 p-3 bg-light rounded">
                                                    <h6 class="mb-2 fw-semibold">
                                                        <i class="fas fa-lightbulb me-2 text-warning"></i>Note:
                                                    </h6>
                                                    <ul class="small mb-0 ps-3">
                                                        <li>Email will be sent within a few minutes</li>
                                                        <li>Check your spam/junk folder</li>
                                                        <li>Password reset link is valid for 24 hours</li>
                                                    </ul>
                                                </div>
                        </div>
                    </div>

                    <!-- Additional help -->
                    <div class="text-center mt-3">
                        <p class="text-white small">
                            <i class="fas fa-question-circle me-1"></i>
                            Need help? Contact
                            <a href="mailto:support@hrm.com" class="text-white fw-bold">support@hrm.com</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap 5 JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>