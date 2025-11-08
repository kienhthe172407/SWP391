<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Statistics & Reports - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    
    <style>
        .stats-card {
            border-radius: 0.5rem;
            padding: 1.5rem;
            background: white;
            border: 1px solid #e5e7eb;
            height: 100%;
            transition: all 0.3s;
        }
        .stats-card:hover {
            box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.1);
            transform: translateY(-2px);
        }
        .stats-card .icon {
            width: 60px;
            height: 60px;
            border-radius: 0.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            color: white;
            margin-bottom: 1rem;
        }
        .stats-card .value {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }
        .stats-card .label {
            font-size: 0.875rem;
            color: #6b7280;
            margin: 0;
        }
        .chart-container {
            position: relative;
            height: 300px;
            margin-bottom: 1rem;
        }
        .section-title {
            font-size: 1.25rem;
            font-weight: 600;
            margin-bottom: 1.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid var(--primary-color);
        }
        .print-button {
            position: fixed;
            bottom: 2rem;
            right: 2rem;
            z-index: 1000;
        }
        @media print {
            .sidebar, .top-header, .breadcrumb, .print-button {
                display: none;
            }
            .main-content {
                margin-left: 0;
            }
            .chart-container {
                page-break-inside: avoid;
            }
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="HR Statistics & Reports" />
        </jsp:include>

        <nav aria-label="breadcrumb" class="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item active">Statistics & Reports</li>
            </ol>
        </nav>

        <div class="content-area">
            <!-- Employee Statistics -->
            <h2 class="section-title"><i class="fas fa-users me-2"></i>Employee Statistics</h2>
            <div class="row mb-4">
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="value text-primary">${statistics.totalEmployees}</div>
                        <p class="label">Total Employees</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="fas fa-user-check"></i>
                        </div>
                        <div class="value text-success">${statistics.activeEmployees}</div>
                        <p class="label">Active Employees</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="fas fa-user-plus"></i>
                        </div>
                        <div class="value text-info">${statistics.newHiresThisMonth}</div>
                        <p class="label">New Hires This Month</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
                            <i class="fas fa-calendar-alt"></i>
                        </div>
                        <div class="value text-warning">${statistics.newHiresThisYear}</div>
                        <p class="label">New Hires This Year</p>
                    </div>
                </div>
            </div>

            <!-- Employee Distribution Charts -->
            <div class="row mb-4">
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-pie me-2"></i>Employees by Department
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="employeesByDepartmentChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-bar me-2"></i>Employees by Position (Top 10)
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="employeesByPositionChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Attendance Statistics -->
            <h2 class="section-title"><i class="fas fa-clock me-2"></i>Attendance Statistics</h2>
            <div class="row mb-4">
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <div class="value text-success">${statistics.totalPresentToday}</div>
                        <p class="label">Present Today</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
                            <i class="fas fa-exclamation-circle"></i>
                        </div>
                        <div class="value text-warning">${statistics.totalLateToday}</div>
                        <p class="label">Late Today</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="fas fa-times-circle"></i>
                        </div>
                        <div class="value text-danger">${statistics.totalAbsentToday}</div>
                        <p class="label">Absent Today</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="fas fa-home"></i>
                        </div>
                        <div class="value text-info">${statistics.totalRemoteToday}</div>
                        <p class="label">Remote Today</p>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-line me-2"></i>Attendance Rate Trend (6 Months)
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="attendanceTrendChart"></canvas>
                            </div>
                            <div class="text-center mt-2">
                                <strong>Average Attendance Rate: 
                                    <span class="text-success">
                                        <fmt:formatNumber value="${statistics.averageAttendanceRate}" maxFractionDigits="2"/>%
                                    </span>
                                </strong>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-pie me-2"></i>Attendance by Status (Last 30 Days)
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="attendanceByStatusChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recruitment Statistics -->
            <h2 class="section-title"><i class="fas fa-briefcase me-2"></i>Recruitment Statistics</h2>
            <div class="row mb-4">
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <i class="fas fa-clipboard-list"></i>
                        </div>
                        <div class="value text-primary">${statistics.totalJobPostings}</div>
                        <p class="label">Total Job Postings</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <div class="value text-success">${statistics.activeJobPostings}</div>
                        <p class="label">Active Job Postings</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="fas fa-file-alt"></i>
                        </div>
                        <div class="value text-info">${statistics.totalApplications}</div>
                        <p class="label">Total Applications</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                        <div class="value text-warning">${statistics.applicationsThisMonth}</div>
                        <p class="label">Applications This Month</p>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-line me-2"></i>Application Trend (6 Months)
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="applicationsByMonthChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-pie me-2"></i>Applications by Status
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="applicationsByStatusChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Task Statistics -->
            <h2 class="section-title"><i class="fas fa-tasks me-2"></i>Task Statistics</h2>
            <div class="row mb-4">
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <i class="fas fa-tasks"></i>
                        </div>
                        <div class="value text-primary">${statistics.totalTasks}</div>
                        <p class="label">Total Tasks</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="fas fa-check-double"></i>
                        </div>
                        <div class="value text-success">${statistics.completedTasks}</div>
                        <p class="label">Completed Tasks</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="fas fa-spinner"></i>
                        </div>
                        <div class="value text-info">${statistics.inProgressTasks}</div>
                        <p class="label">In Progress</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="fas fa-exclamation-triangle"></i>
                        </div>
                        <div class="value text-danger">${statistics.overdueTasksCount}</div>
                        <p class="label">Overdue Tasks</p>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-pie me-2"></i>Tasks by Status
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="tasksByStatusChart"></canvas>
                            </div>
                            <div class="text-center mt-2">
                                <strong>Task Completion Rate: 
                                    <span class="text-success">
                                        <fmt:formatNumber value="${statistics.taskCompletionRate}" maxFractionDigits="2"/>%
                                    </span>
                                </strong>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-bar me-2"></i>Tasks by Priority
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="tasksByPriorityChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Request Statistics -->
            <h2 class="section-title"><i class="fas fa-file-alt me-2"></i>Request Statistics</h2>
            <div class="row mb-4">
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <i class="fas fa-file-alt"></i>
                        </div>
                        <div class="value text-primary">${statistics.totalRequests}</div>
                        <p class="label">Total Requests</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
                            <i class="fas fa-clock"></i>
                        </div>
                        <div class="value text-warning">${statistics.pendingRequests}</div>
                        <p class="label">Pending Requests</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <div class="value text-success">${statistics.approvedRequestsThisMonth}</div>
                        <p class="label">Approved This Month</p>
                    </div>
                </div>
                <div class="col-md-3 mb-3">
                    <div class="stats-card">
                        <div class="icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="fas fa-times-circle"></i>
                        </div>
                        <div class="value text-danger">${statistics.rejectedRequestsThisMonth}</div>
                        <p class="label">Rejected This Month</p>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-bar me-2"></i>Requests by Type
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="requestsByTypeChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-pie me-2"></i>Requests by Status
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="requestsByStatusChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Contract & Salary Statistics -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <h2 class="section-title"><i class="fas fa-file-contract me-2"></i>Contract Statistics</h2>
                    <div class="row mb-3">
                        <div class="col-md-6 mb-3">
                            <div class="stats-card">
                                <div class="icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                                    <i class="fas fa-file-contract"></i>
                                </div>
                                <div class="value text-primary">${statistics.totalContracts}</div>
                                <p class="label">Total Contracts</p>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="stats-card">
                                <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                                    <i class="fas fa-check-circle"></i>
                                </div>
                                <div class="value text-success">${statistics.activeContracts}</div>
                                <p class="label">Active Contracts</p>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="stats-card">
                                <div class="icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
                                    <i class="fas fa-exclamation-triangle"></i>
                                </div>
                                <div class="value text-warning">${statistics.expiringContractsThisMonth}</div>
                                <p class="label">Expiring This Month</p>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="stats-card">
                                <div class="icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                                    <i class="fas fa-times-circle"></i>
                                </div>
                                <div class="value text-danger">${statistics.expiredContracts}</div>
                                <p class="label">Expired Contracts</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <h2 class="section-title"><i class="fas fa-dollar-sign me-2"></i>Salary Statistics</h2>
                    <div class="row mb-3">
                        <div class="col-md-12 mb-3">
                            <div class="stats-card">
                                <div class="icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                                    <i class="fas fa-money-bill-wave"></i>
                                </div>
                                <div class="value text-primary">
                                    <fmt:formatNumber value="${statistics.averageSalary}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </div>
                                <p class="label">Average Salary</p>
                            </div>
                        </div>
                        <div class="col-md-12 mb-3">
                            <div class="stats-card">
                                <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                                    <i class="fas fa-wallet"></i>
                                </div>
                                <div class="value text-success">
                                    <fmt:formatNumber value="${statistics.totalPayrollThisMonth}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </div>
                                <p class="label">Total Payroll This Month</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Hiring Trend -->
            <div class="row mb-4">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-chart-line me-2"></i>Hiring Trend (Last 12 Months)
                        </div>
                        <div class="card-body">
                            <div class="chart-container" style="height: 400px;">
                                <canvas id="hiringTrendChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Print Button -->
    <button class="btn btn-primary btn-lg print-button" onclick="window.print()">
        <i class="fas fa-print me-2"></i>Print Report
    </button>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Chart.js Initialization -->
    <script>
        // Parse JSON data from server
        const employeesByDepartment = ${employeesByDepartmentJson};
        const employeesByPosition = ${employeesByPositionJson};
        const attendanceByStatus = ${attendanceByStatusJson};
        const applicationsByStatus = ${applicationsByStatusJson};
        const applicationsByMonth = ${applicationsByMonthJson};
        const tasksByStatus = ${tasksByStatusJson};
        const tasksByPriority = ${tasksByPriorityJson};
        const requestsByType = ${requestsByTypeJson};
        const requestsByStatus = ${requestsByStatusJson};
        const hiringTrendByMonth = ${hiringTrendByMonthJson};
        const attendanceTrendByMonth = ${attendanceTrendByMonthJson};

        // Color schemes
        const colors = {
            primary: ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#43e97b', '#38f9d7'],
            success: ['#10b981', '#059669', '#047857'],
            warning: ['#f59e0b', '#d97706', '#b45309'],
            danger: ['#ef4444', '#dc2626', '#b91c1c'],
            info: ['#3b82f6', '#2563eb', '#1d4ed8']
        };

        // Helper function to create chart
        function createChart(canvasId, type, data, options = {}) {
            const ctx = document.getElementById(canvasId).getContext('2d');
            return new Chart(ctx, {
                type: type,
                data: data,
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    ...options
                }
            });
        }

        // Employees by Department (Pie Chart)
        createChart('employeesByDepartmentChart', 'pie', {
            labels: Object.keys(employeesByDepartment),
            datasets: [{
                data: Object.values(employeesByDepartment),
                backgroundColor: colors.primary
            }]
        }, {
            plugins: {
                legend: {
                    position: 'right'
                }
            }
        });

        // Employees by Position (Bar Chart)
        createChart('employeesByPositionChart', 'bar', {
            labels: Object.keys(employeesByPosition),
            datasets: [{
                label: 'Employees',
                data: Object.values(employeesByPosition),
                backgroundColor: colors.primary[0]
            }]
        }, {
            indexAxis: 'y',
            plugins: {
                legend: {
                    display: false
                }
            }
        });

        // Attendance by Status (Doughnut Chart)
        createChart('attendanceByStatusChart', 'doughnut', {
            labels: Object.keys(attendanceByStatus),
            datasets: [{
                data: Object.values(attendanceByStatus),
                backgroundColor: colors.primary
            }]
        }, {
            plugins: {
                legend: {
                    position: 'right'
                }
            }
        });

        // Attendance Trend (Line Chart)
        createChart('attendanceTrendChart', 'line', {
            labels: Object.keys(attendanceTrendByMonth),
            datasets: [{
                label: 'Attendance Rate (%)',
                data: Object.values(attendanceTrendByMonth),
                borderColor: colors.success[0],
                backgroundColor: colors.success[0] + '20',
                fill: true,
                tension: 0.4
            }]
        }, {
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100
                }
            }
        });

        // Applications by Month (Line Chart)
        createChart('applicationsByMonthChart', 'line', {
            labels: Object.keys(applicationsByMonth),
            datasets: [{
                label: 'Applications',
                data: Object.values(applicationsByMonth),
                borderColor: colors.primary[0],
                backgroundColor: colors.primary[0] + '20',
                fill: true,
                tension: 0.4
            }]
        });

        // Applications by Status (Pie Chart)
        createChart('applicationsByStatusChart', 'pie', {
            labels: Object.keys(applicationsByStatus),
            datasets: [{
                data: Object.values(applicationsByStatus),
                backgroundColor: colors.primary
            }]
        }, {
            plugins: {
                legend: {
                    position: 'right'
                }
            }
        });

        // Tasks by Status (Doughnut Chart)
        createChart('tasksByStatusChart', 'doughnut', {
            labels: Object.keys(tasksByStatus),
            datasets: [{
                data: Object.values(tasksByStatus),
                backgroundColor: colors.primary
            }]
        }, {
            plugins: {
                legend: {
                    position: 'right'
                }
            }
        });

        // Tasks by Priority (Bar Chart)
        createChart('tasksByPriorityChart', 'bar', {
            labels: Object.keys(tasksByPriority),
            datasets: [{
                label: 'Tasks',
                data: Object.values(tasksByPriority),
                backgroundColor: [colors.success[0], colors.warning[0], colors.danger[0], colors.danger[1]]
            }]
        });

        // Requests by Type (Bar Chart)
        createChart('requestsByTypeChart', 'bar', {
            labels: Object.keys(requestsByType),
            datasets: [{
                label: 'Requests',
                data: Object.values(requestsByType),
                backgroundColor: colors.primary[0]
            }]
        }, {
            indexAxis: 'y'
        });

        // Requests by Status (Pie Chart)
        createChart('requestsByStatusChart', 'pie', {
            labels: Object.keys(requestsByStatus),
            datasets: [{
                data: Object.values(requestsByStatus),
                backgroundColor: colors.primary
            }]
        }, {
            plugins: {
                legend: {
                    position: 'right'
                }
            }
        });

        // Hiring Trend (Line Chart)
        createChart('hiringTrendChart', 'line', {
            labels: Object.keys(hiringTrendByMonth),
            datasets: [{
                label: 'New Hires',
                data: Object.values(hiringTrendByMonth),
                borderColor: colors.primary[0],
                backgroundColor: colors.primary[0] + '20',
                fill: true,
                tension: 0.4
            }]
        }, {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        });
    </script>
</body>
</html>

