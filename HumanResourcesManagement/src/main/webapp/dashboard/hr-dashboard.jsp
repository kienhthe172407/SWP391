<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Dashboard - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>HR Dashboard</h4>
            <p>Human Resources</p>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp" class="active">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <li class="menu-section">Employee Management</li>
            <li>
                <a href="${pageContext.request.contextPath}/employees/list">
                    <i class="fas fa-users"></i>
                    <span>All Employees</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/employees/addInformation">
                    <i class="fas fa-user-plus"></i>
                    <span>Add Employee Information</span>
                </a>
            </li>

            <li class="menu-section">Contracts & Attendance</li>
            <li>
                <a href="${pageContext.request.contextPath}/contracts/list">
                    <i class="fas fa-file"></i>
                    <span>Contracts</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/attendance/import">
                    <i class="fas fa-clock"></i>
                    <span>Attendance</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-calendar-check"></i>
                    <span>Leave Requests</span>
                </a>
            </li>

            <li class="menu-section">Recruitment</li>
            <li>
                <a href="${pageContext.request.contextPath}/job-postings/list">
                    <i class="fas fa-briefcase"></i>
                    <span> Job Postings</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/job-postings/create">
                    <i class="fas fa-plus"></i>
                    <span>Create Job Posting</span>
                </a>
            </li>

            <li class="menu-section">Payroll & Benefits</li>
            <li>
                <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions">
                    <i class="fas fa-gift"></i>
                    <span>Benefits & Deductions</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/salary/import">
                    <i class="fas fa-dollar-sign"></i>
                    <span>Payroll</span>
                </a>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div class="top-header">
            <h1>HR Dashboard</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR"/></span>
                <div class="dropdown">
                    <button class="btn dropdown-toggle avatar" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        HR
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
                <li class="breadcrumb-item active">HR Dashboard</li>
            </ol>
        </nav>

        <div class="content-area">
            <div class="row g-3 mb-3">
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-user-friends me-2"></i>Total Employees</div>
                        <div class="card-body">
                            <h3 class="mb-0">128</h3>
                            <small class="text-muted">Company-wide</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-file-signature me-2"></i>Active Contracts</div>
                        <div class="card-body">
                            <h3 class="mb-0">56</h3>
                            <small class="text-muted">Currently in effect</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-briefcase me-2"></i>Open Job Posts</div>
                        <div class="card-body">
                            <h3 class="mb-0">8</h3>
                            <small class="text-muted">Awaiting candidates</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-calendar-check me-2"></i>Pending Leaves</div>
                        <div class="card-body">
                            <h3 class="mb-0">5</h3>
                            <small class="text-muted">Requests to review</small>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row g-3">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-users me-2"></i>Employees</div>
                        <div class="card-body">
                            <p class="text-muted">View, add, and manage employee records.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/employees/list" class="btn btn-primary">
                                    <i class="fas fa-list me-1"></i>List Employees
                                </a>
                                <a href="${pageContext.request.contextPath}/employees/addInformation" class="btn btn-success">
                                    <i class="fas fa-user-plus me-1"></i>Add Employee
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-file-contract me-2"></i>Contracts</div>
                        <div class="card-body">
                            <p class="text-muted">Manage contracts and approvals.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/contract-mgt/list-contracts.jsp" class="btn btn-primary">
                                    <i class="fas fa-file-alt me-1"></i>View Contracts
                                </a>
                                <a href="${pageContext.request.contextPath}/contract-mgt/create-contract.jsp" class="btn btn-success">
                                    <i class="fas fa-plus me-1"></i>New Contract
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-briefcase me-2"></i>Recruitment</div>
                        <div class="card-body">
                            <p class="text-muted">Create and track job postings.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/job-postings/list" class="btn btn-primary">
                                    <i class="fas fa-list me-1"></i>Job Postings
                                </a>
                                <a href="${pageContext.request.contextPath}/job-postings/create" class="btn btn-success">
                                    <i class="fas fa-plus me-1"></i>Create Posting
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-clock me-2"></i>Attendance Management</div>
                        <div class="card-body">
                            <p class="text-muted">Import and manage employee attendance records.</p>
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/attendance/import" class="btn btn-primary">
                                    <i class="fas fa-upload me-1"></i>Attendance
                                </a>
                                <a href="#" class="btn btn-secondary">
                                    <i class="fas fa-list me-1"></i>View Records
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-dollar-sign me-2"></i>Payroll & Benefits</div>
                        <div class="card-body">
                            <p class="text-muted">Payroll processing and benefits management.</p>
                            <div class="d-flex gap-2">
                                <a href="#" class="btn btn-secondary"><i class="fas fa-coins me-1"></i>Payroll</a>
                                <a href="#" class="btn btn-secondary"><i class="fas fa-gift me-1"></i>Benefits</a>
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


