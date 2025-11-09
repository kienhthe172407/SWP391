<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<%-- 
    Reusable Sidebar Component
    Parameters:
    - role: User role (Admin, HR Manager, HR, Dept Manager, Employee) - Optional, will auto-detect from session
    - currentPage: Current page identifier for active menu item
--%>

<%-- Auto-detect role from session if not provided --%>
<c:set var="userRole" value="${sessionScope.user != null ? sessionScope.user.role : ''}" />
<c:set var="roleDisplayName" value="${sessionScope.user != null ? sessionScope.user.roleDisplayName : ''}" />

<%-- Determine the role to use --%>
<c:choose>
    <c:when test="${param.role != null && param.role != ''}">
        <c:set var="role" value="${param.role}" />
    </c:when>
    <c:when test="${userRole == 'HR_MANAGER' || userRole == 'HR Manager' || roleDisplayName == 'HR Manager'}">
        <c:set var="role" value="HR Manager" />
    </c:when>
    <c:when test="${userRole == 'HR' || roleDisplayName == 'HR'}">
        <c:set var="role" value="HR" />
    </c:when>
    <c:when test="${userRole == 'DEPT_MANAGER' || userRole == 'Dept Manager' || roleDisplayName == 'Dept Manager' || roleDisplayName == 'Department Manager'}">
        <c:set var="role" value="Dept Manager" />
    </c:when>
    <c:when test="${userRole == 'EMPLOYEE' || userRole == 'Employee' || roleDisplayName == 'Employee'}">
        <c:set var="role" value="Employee" />
    </c:when>
    <c:when test="${userRole == 'ADMIN' || userRole == 'Admin' || roleDisplayName == 'Admin' || roleDisplayName == 'Administrator'}">
        <c:set var="role" value="Admin" />
    </c:when>
    <c:otherwise>
        <c:set var="role" value="HR Manager" />
    </c:otherwise>
</c:choose>
<c:set var="currentPage" value="${param.currentPage}" />

<div class="sidebar">
    <div class="sidebar-header">
        <c:choose>
            <c:when test="${role == 'Admin'}">
                <h4>Admin Dashboard</h4>
                <p>System Administration</p>
            </c:when>
            <c:when test="${role == 'HR Manager'}">
                <h4>HR Manager Dashboard</h4>
                <p>Human Resources</p>
            </c:when>
            <c:when test="${role == 'HR'}">
                <h4>HR Dashboard</h4>
                <p>Human Resources</p>
            </c:when>
            <c:when test="${role == 'Dept Manager'}">
                <h4>Dept Manager Dashboard</h4>
                <p>Department Management</p>
            </c:when>
            <c:when test="${role == 'Employee'}">
                <h4>Employee Dashboard</h4>
                <p>Personal Portal</p>
            </c:when>
            <c:otherwise>
                <h4>Dashboard</h4>
                <p>HR Management System</p>
            </c:otherwise>
        </c:choose>
    </div>

    <ul class="sidebar-menu">
        <!-- Dashboard Section -->
        <li class="menu-section">Dashboard</li>
        <li>
            <c:choose>
                <c:when test="${role == 'Admin'}">
                    <a href="${pageContext.request.contextPath}/dashboard/admin" class="${currentPage == 'dashboard' ? 'active' : ''}">
                        <i class="fas fa-home"></i>
                        <span>Overview</span>
                    </a>
                </c:when>
                <c:when test="${role == 'HR Manager'}">
                    <a href="${pageContext.request.contextPath}/dashboard/hr-manager" class="${currentPage == 'dashboard' ? 'active' : ''}">
                        <i class="fas fa-home"></i>
                        <span>Overview</span>
                    </a>
                </c:when>
                <c:when test="${role == 'HR'}">
                    <a href="${pageContext.request.contextPath}/dashboard/hr" class="${currentPage == 'dashboard' ? 'active' : ''}">
                        <i class="fas fa-home"></i>
                        <span>Overview</span>
                    </a>
                </c:when>
                <c:when test="${role == 'Dept Manager'}">
                    <a href="${pageContext.request.contextPath}/dashboard/dept-manager" class="${currentPage == 'dashboard' ? 'active' : ''}">
                        <i class="fas fa-home"></i>
                        <span>Overview</span>
                    </a>
                </c:when>
                <c:when test="${role == 'Employee'}">
                    <a href="${pageContext.request.contextPath}/dashboard/employee" class="${currentPage == 'dashboard' ? 'active' : ''}">
                        <i class="fas fa-home"></i>
                        <span>Overview</span>
                    </a>
                </c:when>
            </c:choose>
        </li>

        <!-- Admin Menu -->
        <c:if test="${role == 'Admin'}">
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
        </c:if>

        <!-- HR Manager Menu -->
        <c:if test="${role == 'HR Manager'}">
            <li class="menu-section">HR Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-users-cog"></i>
                    <span>HR Staff Management</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/task/list">
                    <i class="fas fa-tasks"></i>
                    <span>Task Management</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/attendance/exception/list">
                    <i class="fas fa-clipboard-check"></i>
                    <span>Approval Queue</span>
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

            <li class="menu-section">Reports & Analytics</li>
            <li>
                <a href="${pageContext.request.contextPath}/hr/statistics">
                    <i class="fas fa-chart-pie"></i>
                    <span>HR Statistics & Reports</span>
                </a>
            </li>
        </c:if>

        <!-- HR Menu -->
        <c:if test="${role == 'HR'}">
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
        </c:if>

        <!-- Department Manager Menu -->
        <c:if test="${role == 'Dept Manager'}">
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

            <li class="menu-section">Attendance & Approval</li>
            <li>
                <a href="${pageContext.request.contextPath}/attendance/summary">
                    <i class="fas fa-clock"></i>
                    <span>Attendance</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/request/manager/list">
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
                <a href="${pageContext.request.contextPath}/task/list">
                    <i class="fas fa-clipboard-list"></i>
                    <span>Task Board</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/task/assign">
                    <i class="fas fa-tasks"></i>
                    <span>Assign Tasks</span>
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
        </c:if>

        <!-- Employee Menu -->
        <c:if test="${role == 'Employee'}">
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
                <a href="${pageContext.request.contextPath}/task/list">
                    <i class="fas fa-tasks"></i>
                    <span>My Tasks</span>
                </a>
            </li>

            <li class="menu-section">Attendance & Payroll</li>
            <li>
                <a href="${pageContext.request.contextPath}/attendance/summary">
                    <i class="fas fa-clock"></i>
                    <span>Attendance Record</span>
                </a>
            </li>
            <li>
                <a href="<%= request.getContextPath() %>/employee/my-payslip">
                    <i class="fas fa-dollar-sign"></i>
                    <span>Payslip</span>
                </a>
            </li>
        </c:if>
    </ul>
</div>

