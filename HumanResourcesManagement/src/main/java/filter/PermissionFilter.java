package filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.PermissionChecker;

/**
 * Filter để check permissions trước khi truy cập các trang
 * Cấu hình trong web.xml hoặc bằng @WebFilter annotation
 */
public class PermissionFilter implements Filter {
    
    // Map URL patterns với permissions cần thiết
    private static final Map<String, String> URL_PERMISSION_MAP = new HashMap<>();
    
    static {
        // User Management (auth)
        URL_PERMISSION_MAP.put("/auth/list-users", "USER_VIEW");
        URL_PERMISSION_MAP.put("/auth/create-user", "USER_CREATE");
        URL_PERMISSION_MAP.put("/auth/edit-user", "USER_EDIT");
        
        // Employee Management
        URL_PERMISSION_MAP.put("/employees/list", "EMPLOYEE_VIEW");
        URL_PERMISSION_MAP.put("/employees/addInformation", "EMPLOYEE_CREATE");
        URL_PERMISSION_MAP.put("/employees/edit", "EMPLOYEE_EDIT");
        URL_PERMISSION_MAP.put("/employees/delete", "EMPLOYEE_DELETE");
        URL_PERMISSION_MAP.put("/employees/detail", "EMPLOYEE_VIEW");
        
        // Department Management
        URL_PERMISSION_MAP.put("/departments/list", "DEPT_VIEW");
        URL_PERMISSION_MAP.put("/departments/create", "DEPT_CREATE");
        URL_PERMISSION_MAP.put("/departments/edit", "DEPT_EDIT");
        URL_PERMISSION_MAP.put("/departments/delete", "DEPT_DELETE");
        
        // Contract Management
        URL_PERMISSION_MAP.put("/contracts/list", "CONTRACT_VIEW");
        URL_PERMISSION_MAP.put("/contracts/create", "CONTRACT_CREATE");
        URL_PERMISSION_MAP.put("/contracts/edit", "CONTRACT_EDIT");
        URL_PERMISSION_MAP.put("/contracts/delete", "CONTRACT_DELETE");
        URL_PERMISSION_MAP.put("/contracts/approve", "CONTRACT_APPROVE");
        URL_PERMISSION_MAP.put("/contracts/detail", "CONTRACT_VIEW");
        
        // Job Posting Management
        URL_PERMISSION_MAP.put("/job-postings/list", "JOB_VIEW");
        URL_PERMISSION_MAP.put("/job-postings/create", "JOB_CREATE");
        URL_PERMISSION_MAP.put("/job-postings/edit", "JOB_EDIT");
        URL_PERMISSION_MAP.put("/job-postings/delete", "JOB_DELETE");
        URL_PERMISSION_MAP.put("/job-postings/detail", "JOB_VIEW");
        
        // Attendance Management
        URL_PERMISSION_MAP.put("/attendance-mgt/view-attendance-summary", "ATTENDANCE_VIEW");
        URL_PERMISSION_MAP.put("/attendance-mgt/import-attendance", "ATTENDANCE_IMPORT");
        URL_PERMISSION_MAP.put("/attendance-mgt/adjust-attendance", "ATTENDANCE_ADJUST");
        URL_PERMISSION_MAP.put("/attendance-mgt/submit-attendance-exception", "ATTENDANCE_EXCEPTION_SUBMIT");
        URL_PERMISSION_MAP.put("/attendance-mgt/list-attendance-exceptions", "ATTENDANCE_VIEW");
        URL_PERMISSION_MAP.put("/attendance-mgt/monthly-attendance-report", "ATTENDANCE_REPORT");
        URL_PERMISSION_MAP.put("/attendance/approve-exception", "ATTENDANCE_EXCEPTION_APPROVE");
        URL_PERMISSION_MAP.put("/attendance/export", "ATTENDANCE_REPORT");
        
        // Task Management
        URL_PERMISSION_MAP.put("/task-mgt/list-tasks", "TASK_VIEW");
        URL_PERMISSION_MAP.put("/task-mgt/task-detail", "TASK_VIEW");
        URL_PERMISSION_MAP.put("/task-mgt/create-task", "TASK_CREATE");
        URL_PERMISSION_MAP.put("/task-mgt/edit-task", "TASK_EDIT");
        URL_PERMISSION_MAP.put("/task-mgt/assign-task", "TASK_ASSIGN");
        URL_PERMISSION_MAP.put("/task/update-status", "TASK_UPDATE_STATUS");
        URL_PERMISSION_MAP.put("/task/cancel", "TASK_CANCEL");
        
        // System Settings
        URL_PERMISSION_MAP.put("/permission-settings", "PERMISSION_MANAGE");
        URL_PERMISSION_MAP.put("/role-management", "ROLE_MANAGE");
        URL_PERMISSION_MAP.put("/system-config", "SYSTEM_CONFIG");
        URL_PERMISSION_MAP.put("/init-permissions", "PERMISSION_MANAGE");
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("PermissionFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Lấy URI và remove context path
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Cho phép các URL public không cần authentication
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Lấy user từ session
        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        
        // Nếu chưa login, redirect to login
        if (currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        // Check permission cho URL này
        String requiredPermission = getRequiredPermission(path);
        
        if (requiredPermission != null) {
            // Cần check permission
            if (!PermissionChecker.hasPermission(currentUser, requiredPermission)) {
                // Không có quyền
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpRequest.setAttribute("errorMessage", 
                    "Bạn không có quyền truy cập trang này. Quyền cần thiết: " + requiredPermission);
                httpRequest.getRequestDispatcher("/error/403.jsp").forward(httpRequest, httpResponse);
                return;
            }
        }
        
        // Có quyền hoặc không cần check permission, tiếp tục request
        chain.doFilter(request, response);
    }
    
    /**
     * Kiểm tra URL có phải là public không (không cần authentication)
     */
    private boolean isPublicUrl(String path) {
        // Trang chủ và index
        if (path.equals("") || path.equals("/") || path.equals("/index.html") || path.equals("/home")) {
            return true;
        }
        
        // Authentication pages
        if (path.startsWith("/login") || path.startsWith("/logout") || 
            path.startsWith("/auth/login") || path.startsWith("/auth/register") ||
            path.startsWith("/auth/forgot-password")) {
            return true;
        }
        
        // Public resources
        if (path.startsWith("/css/") || path.startsWith("/js/") || 
            path.startsWith("/images/") || path.startsWith("/fonts/") ||
            path.startsWith("/assets/") || path.startsWith("/static/") ||
            path.startsWith("/components/")) {
            return true;
        }
        
        // Error pages
        if (path.startsWith("/error/")) {
            return true;
        }
        
        // Public job listings for guests
        if (path.startsWith("/jobs/") && !path.contains("/manage")) {
            return true;
        }
        
        // Dashboard pages - không check permission ở filter, để các trang tự check
        if (path.startsWith("/dashboard/")) {
            return true;
        }
        
        // Test và debug pages
        if (path.startsWith("/auth/test-permissions")) {
            return true;
        }
        
        // Profile và change password - user đã login có thể truy cập
        if (path.equals("/change-password") || path.equals("/profile") || 
            path.startsWith("/auth/profile") || path.startsWith("/auth/change-password")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Lấy permission cần thiết cho một URL
     */
    private String getRequiredPermission(String path) {
        // Check exact match
        if (URL_PERMISSION_MAP.containsKey(path)) {
            return URL_PERMISSION_MAP.get(path);
        }
        
        // Check pattern match (contains)
        for (Map.Entry<String, String> entry : URL_PERMISSION_MAP.entrySet()) {
            if (path.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return null; // Không cần check permission
    }
    
    @Override
    public void destroy() {
        System.out.println("PermissionFilter destroyed");
    }
}
