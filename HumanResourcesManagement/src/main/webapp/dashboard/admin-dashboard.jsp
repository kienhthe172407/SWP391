<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="jakarta.tags.core" %>
        <%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Admin Dashboard - HR Management System</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
            </head>

            <body>
                <div class="sidebar">
                    <div class="sidebar-header">
                        <h4>Admin Dashboard</h4>
                        <p>System Administration</p>
                    </div>

                    <ul class="sidebar-menu">
                        <li class="menu-section">Dashboard</li>
                        <li>
                            <a href="${pageContext.request.contextPath}/dashboard/admin" class="active">
                                <i class="fas fa-home"></i>
                                <span>Overview</span>
                            </a>
                        </li>

                        <li class="menu-section">User Management</li>
                        <li>
                            <a href="${pageContext.request.contextPath}/list-users">
                                <i class="fas fa-users"></i>
                                <span>All Users</span>
                            </a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/create-user">
                                <i class="fas fa-user-plus"></i>
                                <span>Create User</span>
                            </a>
                        </li>

                        <li class="menu-section">Role & Permissions</li>
                        <li>
                            <a href="${pageContext.request.contextPath}/role-management">
                                <i class="fas fa-user-shield"></i>
                                <span>Role Management</span>
                            </a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/permission-settings">
                                <i class="fas fa-key"></i>
                                <span>Permission Settings</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-users-cog"></i>
                                <span>Assign Roles</span>
                            </a>
                        </li>

                        <li class="menu-section">System Settings</li>
                        <li>
                            <a href="#">
                                <i class="fas fa-cog"></i>
                                <span>System Configuration</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-database"></i>
                                <span>Database Management</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-shield-alt"></i>
                                <span>Security Settings</span>
                            </a>
                        </li>

                        <li class="menu-section">Monitoring & Logs</li>
                        <li>
                            <a href="#">
                                <i class="fas fa-chart-line"></i>
                                <span>System Monitoring</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-file-alt"></i>
                                <span>Audit Logs</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-exclamation-triangle"></i>
                                <span>Error Logs</span>
                            </a>
                        </li>
                    </ul>
                </div>

                <div class="main-content">
                    <div class="top-header">
                        <h1>Admin Dashboard</h1>
                        <div class="user-info">
                            <span>
                                <c:out value="${sessionScope.user.roleDisplayName}" default="Admin" />
                            </span>
                            <div class="dropdown">
                                <button class="btn dropdown-toggle avatar" type="button" id="userDropdown"
                                    data-bs-toggle="dropdown" aria-expanded="false">
                                    ADM
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                            <i class="fas fa-user me-2"></i>Profile
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item"
                                            href="${pageContext.request.contextPath}/auth/change-password.jsp">
                                            <i class="fas fa-key me-2"></i>Change Password
                                        </a>
                                    </li>
                                    <li>
                                        <hr class="dropdown-divider">
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                            <i class="fas fa-sign-out-alt me-2"></i>Logout
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                            <li class="breadcrumb-item active">Admin Dashboard</li>
                        </ol>
                    </nav>

                    <div class="content-area">
                        <!-- Welcome Message -->
                        <div class="row g-3 mb-3">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="d-flex align-items-center">
                                            <div class="me-3">
                                                <i class="fas fa-crown fa-3x text-warning"></i>
                                            </div>
                                            <div>
                                                <h4 class="mb-1">Welcome back,
                                                    <c:out value="${sessionScope.user.fullName}"
                                                        default="System Administrator" />
                                                </h4>
                                                <p class="text-muted mb-0">Manage system users, roles, and
                                                    configurations.</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Statistics Cards -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-users me-2"></i>Total Users</div>
                                    <div class="card-body">
                                        <h3 class="mb-0">45</h3>
                                        <small class="text-muted">System-wide</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-user-check me-2"></i>Active Users</div>
                                    <div class="card-body">
                                        <h3 class="mb-0">42</h3>
                                        <small class="text-muted">Currently online</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-user-shield me-2"></i>User Roles</div>
                                    <div class="card-body">
                                        <h3 class="mb-0">6</h3>
                                        <small class="text-muted">Different roles</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-exclamation-triangle me-2"></i>System
                                        Alerts</div>
                                    <div class="card-body">
                                        <h3 class="mb-0">2</h3>
                                        <small class="text-muted">Require attention</small>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Feature Cards -->
                        <div class="row g-3">
                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-users me-2"></i>User Management</div>
                                    <div class="card-body">
                                        <p class="text-muted">Create, edit, and manage user accounts across the system.
                                        </p>
                                        <div class="d-flex gap-2">
                                            <a href="${pageContext.request.contextPath}/list-users"
                                                class="btn btn-primary">
                                                <i class="fas fa-list me-1"></i>View All Users
                                            </a>
                                            <a href="${pageContext.request.contextPath}/create-user"
                                                class="btn btn-success">
                                                <i class="fas fa-user-plus me-1"></i>Create User
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-user-shield me-2"></i>Role Management
                                    </div>
                                    <div class="card-body">
                                        <p class="text-muted">Manage user roles and permissions throughout the system.
                                        </p>
                                        <div class="d-flex gap-2">
                                            <a href="${pageContext.request.contextPath}/role-management"
                                                class="btn btn-primary">
                                                <i class="fas fa-users-cog me-1"></i>Manage Roles
                                            </a>
                                            <a href="#" class="btn btn-secondary">
                                                <i class="fas fa-key me-1"></i>Permissions
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-cog me-2"></i>System Settings</div>
                                    <div class="card-body">
                                        <p class="text-muted">Configure system-wide settings and parameters.</p>
                                        <div class="d-flex gap-2">
                                            <a href="#" class="btn btn-primary">
                                                <i class="fas fa-sliders-h me-1"></i>Configuration
                                            </a>
                                            <a href="#" class="btn btn-secondary">
                                                <i class="fas fa-shield-alt me-1"></i>Security
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-database me-2"></i>Database Management
                                    </div>
                                    <div class="card-body">
                                        <p class="text-muted">Monitor and manage database operations and backups.</p>
                                        <div class="d-flex gap-2">
                                            <a href="#" class="btn btn-primary">
                                                <i class="fas fa-database me-1"></i>DB Status
                                            </a>
                                            <a href="#" class="btn btn-secondary">
                                                <i class="fas fa-download me-1"></i>Backup
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-chart-line me-2"></i>System Monitoring
                                    </div>
                                    <div class="card-body">
                                        <p class="text-muted">Monitor system performance and user activity.</p>
                                        <div class="d-flex gap-2">
                                            <a href="#" class="btn btn-primary">
                                                <i class="fas fa-chart-bar me-1"></i>Performance
                                            </a>
                                            <a href="#" class="btn btn-secondary">
                                                <i class="fas fa-eye me-1"></i>Activity Log
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-file-alt me-2"></i>Audit & Logs</div>
                                    <div class="card-body">
                                        <p class="text-muted">View system logs and audit trails for security monitoring.
                                        </p>
                                        <div class="d-flex gap-2">
                                            <a href="#" class="btn btn-primary">
                                                <i class="fas fa-file-alt me-1"></i>Audit Logs
                                            </a>
                                            <a href="#" class="btn btn-secondary">
                                                <i class="fas fa-exclamation-triangle me-1"></i>Error Logs
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Recent Activity Section -->
                        <div class="row g-3 mt-3">
                            <div class="col-md-8">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-history me-2"></i>Recent System Activity
                                    </div>
                                    <div class="card-body">
                                        <div class="list-group list-group-flush">
                                            <div
                                                class="list-group-item d-flex justify-content-between align-items-center">
                                                <div>
                                                    <i class="fas fa-user-plus text-success me-2"></i>
                                                    <strong>User Created:</strong> New HR Manager account
                                                    <small class="text-muted d-block">2 hours ago</small>
                                                </div>
                                            </div>
                                            <div
                                                class="list-group-item d-flex justify-content-between align-items-center">
                                                <div>
                                                    <i class="fas fa-user-shield text-primary me-2"></i>
                                                    <strong>Role Updated:</strong> Employee permissions modified
                                                    <small class="text-muted d-block">4 hours ago</small>
                                                </div>
                                            </div>
                                            <div
                                                class="list-group-item d-flex justify-content-between align-items-center">
                                                <div>
                                                    <i class="fas fa-user-times text-warning me-2"></i>
                                                    <strong>User Deactivated:</strong> Inactive account suspended
                                                    <small class="text-muted d-block">1 day ago</small>
                                                </div>
                                            </div>
                                            <div
                                                class="list-group-item d-flex justify-content-between align-items-center">
                                                <div>
                                                    <i class="fas fa-cog text-info me-2"></i>
                                                    <strong>System Updated:</strong> Security settings modified
                                                    <small class="text-muted d-block">2 days ago</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-bell me-2"></i>System Alerts</div>
                                    <div class="card-body">
                                        <div class="list-group list-group-flush">
                                            <div class="list-group-item">
                                                <div class="d-flex align-items-start">
                                                    <i class="fas fa-exclamation-triangle text-warning me-2 mt-1"></i>
                                                    <div>
                                                        <small class="text-muted">Security</small>
                                                        <p class="mb-1 small">3 failed login attempts detected</p>
                                                        <small class="text-muted">30 minutes ago</small>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="list-group-item">
                                                <div class="d-flex align-items-start">
                                                    <i class="fas fa-info-circle text-info me-2 mt-1"></i>
                                                    <div>
                                                        <small class="text-muted">System</small>
                                                        <p class="mb-1 small">Database backup completed successfully</p>
                                                        <small class="text-muted">2 hours ago</small>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="list-group-item">
                                                <div class="d-flex align-items-start">
                                                    <i class="fas fa-check-circle text-success me-2 mt-1"></i>
                                                    <div>
                                                        <small class="text-muted">Update</small>
                                                        <p class="mb-1 small">System security patch applied</p>
                                                        <small class="text-muted">1 day ago</small>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Quick Actions Section -->
                        <div class="row g-3 mt-3">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-bolt me-2"></i>Quick Actions</div>
                                    <div class="card-body">
                                        <div class="row g-2">
                                            <div class="col-md-3">
                                                <a href="${pageContext.request.contextPath}/create-user"
                                                    class="btn btn-outline-primary w-100">
                                                    <i class="fas fa-user-plus me-2"></i>Create New User
                                                </a>
                                            </div>
                                            <div class="col-md-3">
                                                <a href="${pageContext.request.contextPath}/role-management"
                                                    class="btn btn-outline-success w-100">
                                                    <i class="fas fa-users-cog me-2"></i>Manage Roles
                                                </a>
                                            </div>
                                            <div class="col-md-3">
                                                <a href="#" class="btn btn-outline-info w-100">
                                                    <i class="fas fa-download me-2"></i>Backup Database
                                                </a>
                                            </div>
                                            <div class="col-md-3">
                                                <a href="#" class="btn btn-outline-warning w-100">
                                                    <i class="fas fa-shield-alt me-2"></i>Security Check
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- User Role Distribution -->
                        <div class="row g-3 mt-3">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-header"><i class="fas fa-chart-pie me-2"></i>User Role Distribution
                                    </div>
                                    <div class="card-body">
                                        <div class="row g-3">
                                            <div class="col-md-2">
                                                <div class="text-center">
                                                    <div class="bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-2"
                                                        style="width: 60px; height: 60px;">
                                                        <i class="fas fa-crown"></i>
                                                    </div>
                                                    <h5 class="mb-1">1</h5>
                                                    <small class="text-muted">Admin</small>
                                                </div>
                                            </div>
                                            <div class="col-md-2">
                                                <div class="text-center">
                                                    <div class="bg-success text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-2"
                                                        style="width: 60px; height: 60px;">
                                                        <i class="fas fa-user-tie"></i>
                                                    </div>
                                                    <h5 class="mb-1">1</h5>
                                                    <small class="text-muted">HR Manager</small>
                                                </div>
                                            </div>
                                            <div class="col-md-2">
                                                <div class="text-center">
                                                    <div class="bg-info text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-2"
                                                        style="width: 60px; height: 60px;">
                                                        <i class="fas fa-users"></i>
                                                    </div>
                                                    <h5 class="mb-1">3</h5>
                                                    <small class="text-muted">HR Staff</small>
                                                </div>
                                            </div>
                                            <div class="col-md-2">
                                                <div class="text-center">
                                                    <div class="bg-warning text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-2"
                                                        style="width: 60px; height: 60px;">
                                                        <i class="fas fa-user-tie"></i>
                                                    </div>
                                                    <h5 class="mb-1">2</h5>
                                                    <small class="text-muted">Dept Manager</small>
                                                </div>
                                            </div>
                                            <div class="col-md-2">
                                                <div class="text-center">
                                                    <div class="bg-secondary text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-2"
                                                        style="width: 60px; height: 60px;">
                                                        <i class="fas fa-user"></i>
                                                    </div>
                                                    <h5 class="mb-1">38</h5>
                                                    <small class="text-muted">Employee</small>
                                                </div>
                                            </div>
                                            <div class="col-md-2">
                                                <div class="text-center">
                                                    <div class="bg-dark text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-2"
                                                        style="width: 60px; height: 60px;">
                                                        <i class="fas fa-user-friends"></i>
                                                    </div>
                                                    <h5 class="mb-1">0</h5>
                                                    <small class="text-muted">Guest</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>