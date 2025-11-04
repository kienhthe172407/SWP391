package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Permission;
import model.User;
import util.PermissionChecker;
import util.PermissionConstants;

import java.io.IOException;
import java.util.*;

/**
 * Servlet quản lý Permission Settings
 */
public class PermissionSettingsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Chỉ Admin mới được truy cập
        if (!"Admin".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền truy cập trang này");
            return;
        }
        
        // Lấy role từ parameter (mặc định là Admin)
        String selectedRole = request.getParameter("role");
        if (selectedRole == null || selectedRole.isEmpty()) {
            selectedRole = "Admin";
        }
        
        // Lấy tất cả permissions theo category
        Map<String, List<Permission>> permissionsByCategory = PermissionConstants.getPermissionsByCategory();
        
        // Lấy permissions hiện tại của role
        Set<String> rolePermissions = PermissionChecker.getRolePermissions(selectedRole);
        
        // Lấy tất cả roles
        Set<String> allRoles = PermissionChecker.getAllRoles();
        
        // Set attributes
        request.setAttribute("permissionsByCategory", permissionsByCategory);
        request.setAttribute("rolePermissions", rolePermissions);
        request.setAttribute("selectedRole", selectedRole);
        request.setAttribute("allRoles", allRoles);
        
        // Forward to JSP
        request.getRequestDispatcher("/auth/permission-settings.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Chỉ Admin mới được truy cập
        if (!"Admin".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền truy cập");
            return;
        }
        
        String role = request.getParameter("role");
        String[] selectedPermissions = request.getParameterValues("permissions");
        
        if (role != null && !role.isEmpty()) {
            // Cập nhật permissions cho role
            Set<String> newPermissions = new HashSet<>();
            if (selectedPermissions != null) {
                newPermissions.addAll(Arrays.asList(selectedPermissions));
            }
            
            PermissionChecker.updateRolePermissions(role, newPermissions);
            
            request.getSession().setAttribute("successMessage", "Cập nhật phân quyền thành công cho role: " + role);
        } else {
            request.getSession().setAttribute("errorMessage", "Vui lòng chọn role");
        }
        
        response.sendRedirect(request.getContextPath() + "/permission-settings?role=" + role);
    }
}
