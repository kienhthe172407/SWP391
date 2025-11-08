<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<%-- 
    Reusable Header Component
    Parameters:
    - role: User role (Admin, HR Manager, HR, Dept Manager, Employee) - Optional, will auto-detect from session
    - pageTitle: Title to display in header
--%>

<%-- Auto-detect role from session if not provided --%>
<c:set var="userRole" value="${sessionScope.user != null ? sessionScope.user.role : ''}" />
<c:set var="roleDisplayName" value="${sessionScope.user != null ? sessionScope.user.roleDisplayName : ''}" />

<%-- Determine the role to use --%>
<c:choose>
    <c:when test="${param.role != null && param.role != ''}">
        <c:set var="role" value="${param.role}" />
    </c:when>
    <c:when test="${userRole == 'HR_MANAGER' || roleDisplayName == 'HR Manager'}">
        <c:set var="role" value="HR Manager" />
    </c:when>
    <c:when test="${userRole == 'HR' || roleDisplayName == 'HR'}">
        <c:set var="role" value="HR" />
    </c:when>
    <c:when test="${userRole == 'DEPT_MANAGER' || roleDisplayName == 'Dept Manager'}">
        <c:set var="role" value="Dept Manager" />
    </c:when>
    <c:when test="${userRole == 'EMPLOYEE' || roleDisplayName == 'Employee'}">
        <c:set var="role" value="Employee" />
    </c:when>
    <c:when test="${userRole == 'ADMIN' || roleDisplayName == 'Admin'}">
        <c:set var="role" value="Admin" />
    </c:when>
    <c:otherwise>
        <c:set var="role" value="HR Manager" />
    </c:otherwise>
</c:choose>

<c:set var="pageTitle" value="${param.pageTitle != null ? param.pageTitle : 'Dashboard'}" />

<div class="top-header">
    <h1>${pageTitle}</h1>
    <div class="user-info">
        <span>
            <c:out value="${sessionScope.user.roleDisplayName}" default="${role}" />
        </span>
        <div class="dropdown">
            <button class="btn dropdown-toggle avatar" type="button" id="userDropdown"
                data-bs-toggle="dropdown" aria-expanded="false">
                <c:choose>
                    <c:when test="${role == 'Admin'}">ADM</c:when>
                    <c:when test="${role == 'HR Manager'}">HR</c:when>
                    <c:when test="${role == 'HR'}">HR</c:when>
                    <c:when test="${role == 'Dept Manager'}">DM</c:when>
                    <c:when test="${role == 'Employee'}">EMP</c:when>
                    <c:otherwise>USR</c:otherwise>
                </c:choose>
            </button>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                        <i class="fas fa-user me-2"></i>Profile
                    </a>
                </li>
                <c:if test="${role != 'Employee'}">
                    <li>
                        <a class="dropdown-item"
                            href="${pageContext.request.contextPath}/auth/change-password.jsp">
                            <i class="fas fa-key me-2"></i>Change Password
                        </a>
                    </li>
                </c:if>
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

