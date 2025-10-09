<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    // Mock data for dashboard - no database needed
    int totalEmployees = 25;
    int totalDepartments = 5;
    int totalJobPostings = 8;
    int pendingContracts = 3;
    
    request.setAttribute("totalEmployees", totalEmployees);
    request.setAttribute("totalDepartments", totalDepartments);
    request.setAttribute("totalJobPostings", totalJobPostings);
    request.setAttribute("pendingContracts", pendingContracts);
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>HR Manager Dashboard</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
    
    <style>
        /* Enhanced Dashboard Styles */
        .dashboard-container {
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            min-height: 100vh;
        }
        
        .sidebar {
            background: linear-gradient(180deg, #1e40af 0%, #1e3a8a 100%);
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
        }
        
        .brand-block {
            background: rgba(0, 0, 0, 0.2);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .brand-block h5 {
            color: #fff !important;
            font-weight: 700;
        }
        
        .brand-block small {
            color: rgba(255, 255, 255, 0.7) !important;
        }
        
        .sidebar .nav-section {
            color: rgba(255, 255, 255, 0.5) !important;
            font-size: 0.7rem;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8) !important;
            border-left: 3px solid transparent;
            transition: all 0.2s;
            padding: 12px 16px;
        }
        
        .sidebar .nav-link:hover {
            background: rgba(255, 255, 255, 0.1) !important;
            color: #fff !important;
        }
        
        .sidebar .nav-link.active {
            background: rgba(255, 255, 255, 0.15) !important;
            border-left-color: #60a5fa !important;
            color: #fff !important;
            font-weight: 500;
        }
        
        .sidebar .nav-link i {
            width: 20px;
            text-align: center;
        }
        
        /* KPI Cards */
        .kpi-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            transition: transform 0.2s, box-shadow 0.2s;
            border: none;
        }
        
        .kpi-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
        }
        
        .kpi-icon {
            width: 50px;
            height: 50px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
        }
        
        /* Quick Actions */
        .quick-action {
            background: white;
            border-radius: 12px;
            padding: 20px;
            text-decoration: none;
            color: inherit;
            transition: all 0.2s;
            border: 1px solid #e2e8f0;
        }
        
        .quick-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            color: inherit;
        }
        
        .quick-action-icon {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.1rem;
        }
        
        /* Activity Cards */
        .activity-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            border: none;
        }
        
        .activity-item {
            padding: 12px 0;
            border-bottom: 1px solid #f1f5f9;
        }
        
        .activity-item:last-child {
            border-bottom: none;
        }
        
        .activity-avatar {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: #e2e8f0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.9rem;
            font-weight: 600;
        }
        
        
        /* Status badges */
        .status-badge {
            font-size: 0.75rem;
            padding: 4px 8px;
            border-radius: 6px;
        }
        
        .status-pending {
            background: #fef3c7;
            color: #92400e;
        }
        
        .status-approved {
            background: #d1fae5;
            color: #065f46;
        }
        
        .status-active {
            background: #dbeafe;
            color: #1e40af;
        }
        
        /* Responsive */
        @media (max-width: 768px) {
            .sidebar {
                display: none;
            }
        }
    </style>
  </head>
  <body>
    <div class="dashboard-container">
    <div class="container-fluid">
      <div class="row">
        <!-- Sidebar -->
                <nav class="col-md-2 d-none d-md-block sidebar">
          <div class="brand-block text-white p-3">
            <h5 class="mb-0">HR Manager<br/>Management</h5>
            <small class="text-white-50">HR Manager Portal</small>
          </div>
          <div class="sidebar-sticky pt-3">
            <ul class="nav flex-column mt-2">
              <li class="nav-section px-3 text-secondary small">DASHBOARD</li>
              <li class="nav-item">
                <a class="nav-link active d-flex align-items-center" href="#">
                  <i class="fas fa-home me-2"></i>
                  <span>Dashboard</span>
                </a>
              </li>

              <li class="nav-section px-3 text-secondary small mt-3">HR MANAGER MANAGEMENT</li>
              <li class="nav-item">
                <a class="nav-link d-flex align-items-center" href="#">
                  <i class="fas fa-list me-2"></i>
                  <span>Task Assignment</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link d-flex align-items-center justify-content-between" href="${pageContext.request.contextPath}/manager/approvals">
                  <span><i class="fas fa-check-square me-2"></i> Approval Requests</span>
                                    <span class="badge rounded-pill bg-danger">${pendingContracts}</span>
                </a>
              </li>

              <li class="nav-section px-3 text-secondary small mt-3">EMPLOYEE MANAGEMENT</li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="${pageContext.request.contextPath}/employees"><i class="fas fa-user-tie me-2"></i> Employees</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="${pageContext.request.contextPath}/job-postings/list"><i class="fas fa-briefcase me-2"></i> Recruitment</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-building me-2"></i> Departments</a></li>

              <li class="nav-section px-3 text-secondary small mt-3">CONTRACT & ATTENDANCE</li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-file-contract me-2"></i> Contracts</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-clock me-2"></i> Attendance</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-calendar-days me-2"></i> Leave Requests</a></li>

              <li class="nav-section px-3 text-secondary small mt-3">PAYROLL & BENEFITS</li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-dollar-sign me-2"></i> Payroll</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-gift me-2"></i> Benefits</a></li>

              <li class="nav-section px-3 text-secondary small mt-3">REPORTS & ANALYTICS</li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-file-lines me-2"></i> HR Manager Reports</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-chart-line me-2"></i> HR Manager Analytics</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-chart-pie me-2"></i> Statistics</a></li>

              <li class="nav-section px-3 text-secondary small mt-3">SYSTEM</li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="#"><i class="fas fa-cog me-2"></i> Settings</a></li>
              <li class="nav-item"><a class="nav-link d-flex align-items-center" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-2"></i> Logout</a></li>
            </ul>
          </div>
        </nav>

                <!-- Main content -->
                <main class="col-md-10 ms-sm-auto px-4 py-4">
                    <!-- Page header -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h2 class="fw-bold mb-1">Welcome back, HR Manager</h2>
                            <p class="text-muted mb-0">Here's what's happening in your workspace today.</p>
                        </div>
                        <div class="text-end d-none d-md-block">
                            <div class="text-muted small">Today</div>
                            <div class="fw-semibold"><%= java.time.LocalDate.now() %></div>
                        </div>
                    </div>

                    <!-- KPI Cards -->
                    <div class="row g-4 mb-4">
                        <div class="col-sm-6 col-lg-3">
                            <div class="card kpi-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="kpi-icon bg-primary-subtle text-primary me-3">
                                        <i class="fas fa-user-tie"></i>
                                    </div>
                                    <div>
                                        <div class="text-muted small">Total Employees</div>
                                        <div class="fs-4 fw-bold">${totalEmployees}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-3">
                            <div class="card kpi-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="kpi-icon bg-success-subtle text-success me-3">
                                        <i class="fas fa-building"></i>
                                    </div>
                                    <div>
                                        <div class="text-muted small">Departments</div>
                                        <div class="fs-4 fw-bold">${totalDepartments}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-3">
                            <div class="card kpi-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="kpi-icon bg-warning-subtle text-warning me-3">
                                        <i class="fas fa-briefcase"></i>
                                    </div>
                                    <div>
                                        <div class="text-muted small">Active Jobs</div>
                                        <div class="fs-4 fw-bold">${totalJobPostings}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-3">
                            <div class="card kpi-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="kpi-icon bg-danger-subtle text-danger me-3">
                                        <i class="fas fa-clock"></i>
                                    </div>
                                    <div>
                                        <div class="text-muted small">Pending Approvals</div>
                                        <div class="fs-4 fw-bold">${pendingContracts}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Quick Actions -->
                    <div class="row g-3 mb-4">
                        <div class="col-md-3 col-sm-6">
                            <a class="quick-action d-flex align-items-center" href="${pageContext.request.contextPath}/job-postings/create">
                                <div class="quick-action-icon bg-primary-subtle text-primary me-3">
                                    <i class="fas fa-plus"></i>
                                </div>
                                <div>
                                    <div class="fw-semibold">New Job Posting</div>
                                    <div class="small text-muted">Create opening</div>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <a class="quick-action d-flex align-items-center" href="${pageContext.request.contextPath}/contract-mgt/create-contract.jsp">
                                <div class="quick-action-icon bg-secondary-subtle text-secondary me-3">
                                    <i class="fas fa-file-signature"></i>
                                </div>
                                <div>
                                    <div class="fw-semibold">Create Contract</div>
                                    <div class="small text-muted">Start contract</div>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <a class="quick-action d-flex align-items-center" href="${pageContext.request.contextPath}/employees">
                                <div class="quick-action-icon bg-success-subtle text-success me-3">
                                    <i class="fas fa-user-plus"></i>
                                </div>
                                <div>
                                    <div class="fw-semibold">Add Employee</div>
                                    <div class="small text-muted">Onboard hire</div>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <a class="quick-action d-flex align-items-center" href="#">
                                <div class="quick-action-icon bg-info-subtle text-info me-3">
                                    <i class="fas fa-chart-line"></i>
                                </div>
                                <div>
                                    <div class="fw-semibold">View Reports</div>
                                    <div class="small text-muted">Analytics</div>
                                </div>
                            </a>
                        </div>
                    </div>

                    <!-- Recent Activity -->
                    <div class="row g-4">
                        <div class="col-lg-12">
                            <div class="card activity-card">
                                <div class="card-header bg-white border-0">
                                    <h6 class="fw-semibold mb-0">Recent Activity</h6>
                                </div>
                                <div class="card-body p-0">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="activity-item px-3">
                                                <div class="d-flex align-items-center">
                                                    <div class="activity-avatar me-3">JD</div>
                                                    <div class="flex-grow-1">
                                                        <div class="fw-medium">John Doe</div>
                                                        <div class="small text-muted">New employee added</div>
                                                    </div>
                                                    <div class="small text-muted">Today</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="activity-item px-3">
                                                <div class="d-flex align-items-center">
                                                    <div class="activity-avatar me-3">JS</div>
                                                    <div class="flex-grow-1">
                                                        <div class="fw-medium">Jane Smith</div>
                                                        <div class="small text-muted">Contract approved</div>
                                                    </div>
                                                    <div class="small text-muted">Yesterday</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="activity-item px-3">
                                                <div class="d-flex align-items-center">
                                                    <div class="activity-avatar me-3">MJ</div>
                                                    <div class="flex-grow-1">
                                                        <div class="fw-medium">Mike Johnson</div>
                                                        <div class="small text-muted">Job posting created</div>
                                                    </div>
                                                    <div class="small text-muted">2 days ago</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Recent Data Tables -->
                    <div class="row g-4 mt-2">
                        <!-- Recent Contracts -->
                        <div class="col-lg-6">
                            <div class="card activity-card">
                                <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                                    <h6 class="fw-semibold mb-0">Recent Contracts</h6>
                                    <a href="${pageContext.request.contextPath}/contract-mgt/list-contracts.jsp" class="btn btn-sm btn-outline-primary">View All</a>
                                </div>
                                <div class="card-body p-0">
                                    <div class="activity-item px-3">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div>
                                                <div class="fw-medium">Contract #CT001</div>
                                                <div class="small text-muted">John Doe</div>
                                            </div>
                                            <span class="status-badge status-pending">Pending</span>
                                        </div>
                                    </div>
                                    <div class="activity-item px-3">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div>
                                                <div class="fw-medium">Contract #CT002</div>
                                                <div class="small text-muted">Jane Smith</div>
                                            </div>
                                            <span class="status-badge status-approved">Approved</span>
                                        </div>
                                    </div>
                                    <div class="activity-item px-3">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div>
                                                <div class="fw-medium">Contract #CT003</div>
                                                <div class="small text-muted">Mike Johnson</div>
                                            </div>
                                            <span class="status-badge status-pending">Pending</span>
                                        </div>
                                    </div>
                                </div>
            </div>
          </div>

                        <!-- Recent Job Postings -->
                        <div class="col-lg-6">
                            <div class="card activity-card">
                                <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                                    <h6 class="fw-semibold mb-0">Recent Job Postings</h6>
                                    <a href="${pageContext.request.contextPath}/job-postings/list" class="btn btn-sm btn-outline-primary">View All</a>
                                </div>
                                <div class="card-body p-0">
                                    <div class="activity-item px-3">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div>
                                                <div class="fw-medium">Senior Developer</div>
                                                <div class="small text-muted">IT Department</div>
                                            </div>
                                            <span class="status-badge status-active">Active</span>
                                        </div>
                                    </div>
                                    <div class="activity-item px-3">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div>
                                                <div class="fw-medium">HR Manager</div>
                                                <div class="small text-muted">HR Department</div>
                                            </div>
                                            <span class="status-badge status-active">Active</span>
                                        </div>
                                    </div>
                                    <div class="activity-item px-3">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div>
                                                <div class="fw-medium">Marketing Specialist</div>
                                                <div class="small text-muted">Marketing</div>
                                            </div>
                                            <span class="status-badge status-pending">Pending</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
        </main>
            </div>
      </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
