<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Department Manager Dashboard - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>Dept Manager Dashboard</h4>
            <p>Department Management</p>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/dashboard/dept-manager-dashboard.jsp" class="active">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <li class="menu-section">Team Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-users"></i>
                    <span>My Team</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-user-plus"></i>
                    <span>Team Members</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-bar"></i>
                    <span>Team Performance</span>
                </a>
            </li>

            <li class="menu-section">Approval & Requests</li>
            <li>
                <a href="#">
                    <i class="fas fa-clipboard-check"></i>
                    <span>Approval Queue</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-calendar-times"></i>
                    <span>Leave Requests</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-file-alt"></i>
                    <span>Employee Requests</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-history"></i>
                    <span>Approval History</span>
                </a>
            </li>

            <li class="menu-section">Task Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-tasks"></i>
                    <span>Assign Tasks</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-clipboard-list"></i>
                    <span>Task Board</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-line"></i>
                    <span>Task Statistics</span>
                </a>
            </li>

            <li class="menu-section">Reports & Analytics</li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-pie"></i>
                    <span>Department Reports</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-clock"></i>
                    <span>Attendance Reports</span>
                </a>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div class="top-header">
            <h1>Department Manager Dashboard</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="Dept Manager"/></span>
                <div class="dropdown">
                    <button class="btn dropdown-toggle avatar" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        DM
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                <i class="fas fa-user me-2"></i>Profile
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/auth/change-password.jsp">
                                <i class="fas fa-key me-2"></i>Change Password
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
                <li class="breadcrumb-item active">Department Manager Dashboard</li>
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
                                    <i class="fas fa-user-tie fa-3x text-primary"></i>
                                </div>
                                <div>
                                    <h4 class="mb-1">Welcome back, <c:out value="${sessionScope.user.fullName}" default="Department Manager"/></h4>
                                    <p class="text-muted mb-0">Manage your team and oversee department operations.</p>
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
                        <div class="card-header"><i class="fas fa-users me-2"></i>Team Size</div>
                        <div class="card-body">
                            <h3 class="mb-0">12</h3>
                            <small class="text-muted">Active members</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-clipboard-check me-2"></i>Pending Approvals</div>
                        <div class="card-body">
                            <h3 class="mb-0">7</h3>
                            <small class="text-muted">Awaiting review</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-tasks me-2"></i>Active Tasks</div>
                        <div class="card-body">
                            <h3 class="mb-0">18</h3>
                            <small class="text-muted">In progress</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-calendar-times me-2"></i>Leave Requests</div>
                        <div class="card-body">
                            <h3 class="mb-0">3</h3>
                            <small class="text-muted">This week</small>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Feature Cards -->
            <div class="row g-3">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-clipboard-check me-2"></i>Approval Queue</div>
                        <div class="card-body">
                            <p class="text-muted">Review and approve employee requests and leave applications.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-inbox me-1"></i>Pending Approvals
                                </a>
                                <a href="#" class="btn btn-success">
                                    <i class="fas fa-check me-1"></i>Quick Approve
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-tasks me-2"></i>Task Management</div>
                        <div class="card-body">
                            <p class="text-muted">Assign tasks to team members and track progress.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-plus me-1"></i>Assign Task
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-list me-1"></i>Task Board
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-users me-2"></i>Team Management</div>
                        <div class="card-body">
                            <p class="text-muted">View team members and manage their information.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-users me-1"></i>View Team
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-chart-bar me-1"></i>Performance
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-calendar-times me-2"></i>Leave Management</div>
                        <div class="card-body">
                            <p class="text-muted">Review and approve leave requests from team members.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-calendar-check me-1"></i>Leave Requests
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-calendar-alt me-1"></i>Calendar View
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-chart-line me-2"></i>Task Statistics</div>
                        <div class="card-body">
                            <p class="text-muted">View task completion rates and team productivity metrics.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-chart-bar me-1"></i>Task Reports
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-chart-pie me-1"></i>Analytics
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-clock me-2"></i>Attendance Overview</div>
                        <div class="card-body">
                            <p class="text-muted">Monitor team attendance and working hours.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-primary">
                                    <i class="fas fa-calendar me-1"></i>Attendance Report
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-chart-line me-1"></i>Trends
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
                                        <i class="fas fa-check-circle text-success me-2"></i>
                                        <strong>Approved:</strong> Leave request from Michael Brown
                                        <small class="text-muted d-block">1 hour ago</small>
                                    </div>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-tasks text-primary me-2"></i>
                                        <strong>Task Assigned:</strong> Project Review to Emily Davis
                                        <small class="text-muted d-block">3 hours ago</small>
                                    </div>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-calendar-plus text-warning me-2"></i>
                                        <strong>New Request:</strong> Annual leave from David Wilson
                                        <small class="text-muted d-block">5 hours ago</small>
                                    </div>
                                    <span class="badge badge-pending">Pending</span>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-check-circle text-success me-2"></i>
                                        <strong>Task Completed:</strong> Monthly Report by Lisa Anderson
                                        <small class="text-muted d-block">1 day ago</small>
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
                                        <i class="fas fa-exclamation-triangle text-warning me-2 mt-1"></i>
                                        <div>
                                            <small class="text-muted">Urgent</small>
                                            <p class="mb-1 small">3 leave requests need immediate attention</p>
                                            <small class="text-muted">30 minutes ago</small>
                                        </div>
                                    </div>
                                </div>
                                <div class="list-group-item">
                                    <div class="d-flex align-items-start">
                                        <i class="fas fa-info-circle text-info me-2 mt-1"></i>
                                        <div>
                                            <small class="text-muted">System</small>
                                            <p class="mb-1 small">Weekly team performance report ready</p>
                                            <small class="text-muted">2 hours ago</small>
                                        </div>
                                    </div>
                                </div>
                                <div class="list-group-item">
                                    <div class="d-flex align-items-start">
                                        <i class="fas fa-tasks text-primary me-2 mt-1"></i>
                                        <div>
                                            <small class="text-muted">Task</small>
                                            <p class="mb-1 small">5 tasks overdue - requires attention</p>
                                            <small class="text-muted">4 hours ago</small>
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
                                    <a href="#" class="btn btn-outline-primary w-100">
                                        <i class="fas fa-plus me-2"></i>Assign New Task
                                    </a>
                                </div>
                                <div class="col-md-3">
                                    <a href="#" class="btn btn-outline-success w-100">
                                        <i class="fas fa-check me-2"></i>Approve All Pending
                                    </a>
                                </div>
                                <div class="col-md-3">
                                    <a href="#" class="btn btn-outline-info w-100">
                                        <i class="fas fa-chart-bar me-2"></i>Generate Report
                                    </a>
                                </div>
                                <div class="col-md-3">
                                    <a href="#" class="btn btn-outline-warning w-100">
                                        <i class="fas fa-calendar me-2"></i>View Schedule
                                    </a>
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
