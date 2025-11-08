<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Dashboard - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>Employee Dashboard</h4>
            <p>Personal Portal</p>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/dashboard/employee" class="active">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <li class="menu-section">Personal Information</li>
            <li>
                <a href="${pageContext.request.contextPath}/profile">
                    <i class="fas fa-user"></i>
                    <span>My Profile</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/employee/my-contract">
                    <i class="fas fa-file-contract"></i>
                    <span>My Contract</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/auth/change-password.jsp">
                    <i class="fas fa-key"></i>
                    <span>Change Password</span>
                </a>
            </li>

            <li class="menu-section">Leave & Requests</li>
            <li>
                <a href="${pageContext.request.contextPath}/request/submit">
                    <i class="fas fa-file-alt"></i>
                    <span>Personal Requests</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/request/list">
                    <i class="fas fa-history"></i>
                    <span>Request History</span>
                </a>
            </li>

            <li class="menu-section">Tasks & Work</li>
            <li>
                <a href="#">
                    <i class="fas fa-tasks"></i>
                    <span>My Tasks</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-clipboard-check"></i>
                    <span>Task Status</span>
                </a>
            </li>

            <li class="menu-section">Attendance & Payroll</li>
            <li>
                <a href="#">
                    <i class="fas fa-clock"></i>
                    <span>Attendance Record</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-dollar-sign"></i>
                    <span>Payslip</span>
                </a>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div class="top-header">
            <h1>Employee Dashboard</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="Employee"/></span>
                <div class="dropdown">
                    <button class="btn dropdown-toggle avatar" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        EMP
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                <i class="fas fa-user me-2"></i>Profile
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
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
                <li class="breadcrumb-item active">Employee Dashboard</li>
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
                                    <i class="fas fa-user-circle fa-3x text-primary"></i>
                                </div>
                                <div>
                                    <h4 class="mb-1">Welcome back, <c:out value="${sessionScope.user.fullName}" default="Employee"/></h4>
                                    <p class="text-muted mb-0">Here's what's happening with your work today.</p>
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
                        <div class="card-header"><i class="fas fa-tasks me-2"></i>Active Tasks</div>
                        <div class="card-body">
                            <h3 class="mb-0">3</h3>
                            <small class="text-muted">In progress</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-calendar-check me-2"></i>Leave Balance</div>
                        <div class="card-body">
                            <h3 class="mb-0">12</h3>
                            <small class="text-muted">Days remaining</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-clock me-2"></i>This Month Hours</div>
                        <div class="card-body">
                            <h3 class="mb-0">168</h3>
                            <small class="text-muted">Working hours</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-file-alt me-2"></i>Pending Requests</div>
                        <div class="card-body">
                            <h3 class="mb-0">1</h3>
                            <small class="text-muted">Awaiting approval</small>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Feature Cards -->
            <div class="row g-3">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-user me-2"></i>Personal Information</div>
                        <div class="card-body">
                            <p class="text-muted">View and update your personal information and profile.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary">
                                    <i class="fas fa-eye me-1"></i>View Profile
                                </a>
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-success">
                                    <i class="fas fa-edit me-1"></i>Edit Profile
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-calendar-times me-2"></i>Leave Management</div>
                        <div class="card-body">
                            <p class="text-muted">Submit leave requests and track your leave balance.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/request/submit" class="btn btn-primary">
                                    <i class="fas fa-plus me-1"></i>New Leave Request
                                </a>
                                <a href="${pageContext.request.contextPath}/request/list" class="btn btn-secondary">
                                    <i class="fas fa-list me-1"></i>Leave History
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-tasks me-2"></i>Task Management</div>
                        <div class="card-body">
                            <p class="text-muted">View assigned tasks and update their status.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-list me-1"></i>My Tasks
                                </a>
                                <a href="#" class="btn btn-success">
                                    <i class="fas fa-check me-1"></i>Update Status
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-file-alt me-2"></i>Personal Requests</div>
                        <div class="card-body">
                            <p class="text-muted">Submit various personal requests and track their status.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/request/submit" class="btn btn-primary">
                                    <i class="fas fa-plus me-1"></i>New Request
                                </a>
                                <a href="${pageContext.request.contextPath}/request/list" class="btn btn-secondary">
                                    <i class="fas fa-history me-1"></i>Request History
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-clock me-2"></i>Attendance Record</div>
                        <div class="card-body">
                            <p class="text-muted">View your attendance records and working hours.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-calendar me-1"></i>View Attendance
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-chart-bar me-1"></i>Monthly Report
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-dollar-sign me-2"></i>Payslip & Benefits</div>
                        <div class="card-body">
                            <p class="text-muted">Access your payslips and benefit information.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-file-invoice me-1"></i>View Payslips
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-gift me-1"></i>Benefits Info
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
                        <div class="card-header"><i class="fas fa-history me-2"></i>Recent Activity</div>
                        <div class="card-body">
                            <div class="list-group list-group-flush">
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-calendar-plus text-primary me-2"></i>
                                        <strong>Recent Request:</strong> Check your latest submissions
                                        <small class="text-muted d-block">View all requests</small>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/request/list" class="btn btn-sm btn-outline-primary">View</a>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-plus-circle text-success me-2"></i>
                                        <strong>Submit Request:</strong> Create new leave or personal request
                                        <small class="text-muted d-block">Quick access</small>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/request/submit" class="btn btn-sm btn-outline-success">Submit</a>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-user-edit text-info me-2"></i>
                                        <strong>Profile Updated:</strong> Contact information
                                        <small class="text-muted d-block">3 days ago</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-bell me-2"></i>Notifications</div>
                        <div class="card-body">
                            <div class="list-group list-group-flush">
                                <div class="list-group-item">
                                    <div class="d-flex align-items-start">
                                        <i class="fas fa-info-circle text-info me-2 mt-1"></i>
                                        <div>
                                            <small class="text-muted">Request</small>
                                            <p class="mb-1 small">Check your request status updates</p>
                                            <small class="text-muted">
                                                <a href="${pageContext.request.contextPath}/request/list" class="text-decoration-none">View requests</a>
                                            </small>
                                        </div>
                                    </div>
                                </div>
                                <div class="list-group-item">
                                    <div class="d-flex align-items-start">
                                        <i class="fas fa-calendar-plus text-primary me-2 mt-1"></i>
                                        <div>
                                            <small class="text-muted">Quick Action</small>
                                            <p class="mb-1 small">Submit a new request</p>
                                            <small class="text-muted">
                                                <a href="${pageContext.request.contextPath}/request/submit" class="text-decoration-none">Create request</a>
                                            </small>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
