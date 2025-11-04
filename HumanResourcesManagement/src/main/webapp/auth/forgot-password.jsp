<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quên mật khẩu - HR Management System</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border: none;
                padding: 12px;
                font-weight: 600;
                transition: all 0.3s;
            }

            .btn-primary:hover {
                background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
            }

            .form-control:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
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
                                <h2 class="fw-bold mb-2">Quên mật khẩu?</h2>
                                <p class="text-muted">Nhập email của bạn để nhận hướng dẫn đặt lại mật khẩu</p>
                            </div>

                            <!-- Success Message -->
                            <% if(request.getAttribute("successMessage") !=null) { %>
                                <div class="alert alert-success" role="alert">
                                    <i class="fas fa-check-circle me-2"></i>
                                    <%= request.getAttribute("successMessage") %>
                                </div>
                                <div class="text-center">
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary w-100">
                                        <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập ngay
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
                                                        <i class="fas fa-envelope me-2"></i>Địa chỉ Email
                                                    </label>
                                                    <input type="email" class="form-control form-control-lg" id="email"
                                                        name="email" placeholder="example@email.com" required>
                                                    <div class="form-text">
                                                        <i class="fas fa-info-circle me-1"></i>
                                                        Chúng tôi sẽ gửi mật khẩu mới đến email này
                                                    </div>
                                                </div>

                                                <button type="submit" class="btn btn-primary w-100 btn-lg mb-3">
                                                    <i class="fas fa-paper-plane me-2"></i>Gửi mật khẩu mới
                                                </button>
                                            </form>

                                            <% } %>

                                                <!-- Divider -->
                                                <hr class="my-4">

                                                <!-- Back to login -->
                                                <div class="text-center">
                                                    <a href="${pageContext.request.contextPath}/login"
                                                        class="text-decoration-none">
                                                        <i class="fas fa-arrow-left me-2"></i>Quay lại đăng nhập
                                                    </a>
                                                </div>

                                                <!-- Info box -->
                                                <div class="mt-4 p-3 bg-light rounded">
                                                    <h6 class="mb-2 fw-semibold">
                                                        <i class="fas fa-lightbulb me-2 text-warning"></i>Lưu ý:
                                                    </h6>
                                                    <ul class="small mb-0 ps-3">
                                                        <li>Email sẽ được gửi trong vòng vài phút</li>
                                                        <li>Kiểm tra cả thư mục spam/junk</li>
                                                        <li>Link đặt lại mật khẩu có hiệu lực trong 24 giờ</li>
                                                    </ul>
                                                </div>
                        </div>
                    </div>

                    <!-- Additional help -->
                    <div class="text-center mt-3">
                        <p class="text-white small">
                            <i class="fas fa-question-circle me-1"></i>
                            Cần hỗ trợ? Liên hệ
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