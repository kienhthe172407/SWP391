package controller.auth;

import java.io.IOException;
import java.util.List;

import dal.PermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Permission;
import model.User;
import util.PermissionChecker;
import util.PermissionConstants;

/**
 * Servlet để khởi tạo permissions vào database lần đầu
 * Chỉ Admin mới được chạy
 */
public class InitPermissionsServlet extends HttpServlet {
    
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
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền khởi tạo permissions");
            return;
        }
        
        try {
            PermissionDAO permissionDAO = new PermissionDAO();
            
            // 1. Đồng bộ permissions từ code vào database
            List<Permission> allPermissions = PermissionConstants.getAllPermissions();
            int syncedCount = 0;
            
            for (Permission permission : allPermissions) {
                if (!permissionDAO.permissionExists(permission.getCode())) {
                    permissionDAO.addPermission(permission);
                    syncedCount++;
                }
            }
            
            // 2. Khởi tạo role permissions mặc định nếu chưa có
            String[] roles = {"Admin", "HR Manager", "HR", "Dept Manager", "Employee"};
            int roleCount = 0;
            
            for (String role : roles) {
                List<String> rolePerms = permissionDAO.getPermissionsByRole(role);
                if (rolePerms.isEmpty()) {
                    // Lấy permissions mặc định từ code
                    java.util.Set<String> defaultPerms = getDefaultPermissionsForRole(role);
                    
                    // Xóa permissions cũ (nếu có)
                    permissionDAO.deleteRolePermissions(role);
                    
                    // Thêm permissions mặc định
                    for (String permCode : defaultPerms) {
                        permissionDAO.addRolePermission(role, permCode);
                    }
                    roleCount++;
                }
            }
            
            // Reload cache
            PermissionChecker.reloadPermissionsFromDatabase();
            
            request.getSession().setAttribute("successMessage", 
                String.format("Khởi tạo thành công! Đã đồng bộ %d permissions và khởi tạo %d roles.", 
                    syncedCount, roleCount));
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", 
                "Có lỗi xảy ra khi khởi tạo permissions: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/permission-settings");
    }
    
    /**
     * Lấy permissions mặc định cho role
     */
    private java.util.Set<String> getDefaultPermissionsForRole(String role) {
        java.util.Set<String> perms = new java.util.HashSet<>();
        
        switch (role) {
            case "Admin":
                // Admin có tất cả permissions
                perms.addAll(java.util.Arrays.asList(
                    PermissionConstants.USER_VIEW, PermissionConstants.USER_CREATE, 
                    PermissionConstants.USER_EDIT, PermissionConstants.USER_ACTIVATE,
                    PermissionConstants.EMPLOYEE_VIEW, PermissionConstants.EMPLOYEE_CREATE, 
                    PermissionConstants.EMPLOYEE_EDIT, PermissionConstants.EMPLOYEE_DELETE,
                    PermissionConstants.DEPT_VIEW, PermissionConstants.DEPT_CREATE, 
                    PermissionConstants.DEPT_EDIT, PermissionConstants.DEPT_DELETE,
                    PermissionConstants.CONTRACT_VIEW, PermissionConstants.CONTRACT_CREATE, 
                    PermissionConstants.CONTRACT_EDIT, PermissionConstants.CONTRACT_DELETE, 
                    PermissionConstants.CONTRACT_APPROVE,
                    PermissionConstants.JOB_VIEW, PermissionConstants.JOB_CREATE, 
                    PermissionConstants.JOB_EDIT, PermissionConstants.JOB_DELETE,
                    PermissionConstants.SYSTEM_CONFIG, PermissionConstants.ROLE_MANAGE, 
                    PermissionConstants.PERMISSION_MANAGE
                ));
                break;
                
            case "HR Manager":
                perms.addAll(java.util.Arrays.asList(
                    PermissionConstants.USER_VIEW, PermissionConstants.USER_CREATE, PermissionConstants.USER_EDIT,
                    PermissionConstants.EMPLOYEE_VIEW, PermissionConstants.EMPLOYEE_CREATE, 
                    PermissionConstants.EMPLOYEE_EDIT, PermissionConstants.EMPLOYEE_DELETE,
                    PermissionConstants.DEPT_VIEW,
                    PermissionConstants.CONTRACT_VIEW, PermissionConstants.CONTRACT_CREATE, 
                    PermissionConstants.CONTRACT_EDIT, PermissionConstants.CONTRACT_APPROVE,
                    PermissionConstants.JOB_VIEW, PermissionConstants.JOB_CREATE, 
                    PermissionConstants.JOB_EDIT, PermissionConstants.JOB_DELETE
                ));
                break;
                
            case "HR":
                perms.addAll(java.util.Arrays.asList(
                    PermissionConstants.USER_VIEW,
                    PermissionConstants.EMPLOYEE_VIEW, PermissionConstants.EMPLOYEE_CREATE, 
                    PermissionConstants.EMPLOYEE_EDIT,
                    PermissionConstants.DEPT_VIEW,
                    PermissionConstants.CONTRACT_VIEW, PermissionConstants.CONTRACT_CREATE, 
                    PermissionConstants.CONTRACT_EDIT,
                    PermissionConstants.JOB_VIEW, PermissionConstants.JOB_CREATE, 
                    PermissionConstants.JOB_EDIT
                ));
                break;
                
            case "Dept Manager":
                perms.addAll(java.util.Arrays.asList(
                    PermissionConstants.EMPLOYEE_VIEW,
                    PermissionConstants.DEPT_VIEW,
                    PermissionConstants.CONTRACT_VIEW,
                    PermissionConstants.JOB_VIEW
                ));
                break;
                
            case "Employee":
                perms.addAll(java.util.Arrays.asList(
                    PermissionConstants.EMPLOYEE_VIEW,
                    PermissionConstants.CONTRACT_VIEW,
                    PermissionConstants.JOB_VIEW
                ));
                break;
        }
        
        return perms;
    }
}
