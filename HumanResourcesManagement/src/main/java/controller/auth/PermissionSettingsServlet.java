package controller.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Permission;
import model.User;
import util.PermissionChecker;

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
        
        // Lấy permissions từ database
        dal.PermissionDAO permissionDAO = new dal.PermissionDAO();
        Map<String, List<Permission>> permissionsByCategory = permissionDAO.getPermissionsByCategory();
        
        // Lấy tất cả roles
        List<String> allRoles = new ArrayList<>(PermissionChecker.getAllRoles());
        Collections.sort(allRoles);
        
        // Tạo map permissions cho từng role
        Map<String, Set<String>> rolePermissionsMap = new LinkedHashMap<>();
        for (String role : allRoles) {
            rolePermissionsMap.put(role, PermissionChecker.getRolePermissions(role));
        }
        
        permissionDAO.close();
        
        // Set attributes
        request.setAttribute("permissionsByCategory", permissionsByCategory);
        request.setAttribute("allRoles", allRoles);
        request.setAttribute("rolePermissionsMap", rolePermissionsMap);
        
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
        
        try {
            // Lấy tất cả roles
            Set<String> allRoles = PermissionChecker.getAllRoles();
            int updatedCount = 0;
            StringBuilder logMessage = new StringBuilder("=== PERMISSION UPDATE LOG ===\n");
            
            // Duyệt qua từng role và cập nhật permissions
            for (String role : allRoles) {
                // Thay khoảng trắng thành underscore để khớp với tên parameter trong form
                String paramName = "permissions_" + role.replace(" ", "_");
                String[] rolePermissions = request.getParameterValues(paramName);
                
                Set<String> newPermissions = new HashSet<>();
                if (rolePermissions != null) {
                    newPermissions.addAll(Arrays.asList(rolePermissions));
                }
                
                // Log thông tin trước khi cập nhật
                Set<String> oldPermissions = PermissionChecker.getRolePermissions(role);
                logMessage.append(String.format("Role: %s\n", role));
                logMessage.append(String.format("  Old permissions count: %d\n", oldPermissions.size()));
                logMessage.append(String.format("  New permissions count: %d\n", newPermissions.size()));
                logMessage.append(String.format("  New permissions: %s\n", newPermissions));
                
                // Cập nhật permissions cho role
                PermissionChecker.updateRolePermissions(role, newPermissions);
                
                // Xác nhận cập nhật thành công
                Set<String> verifyPermissions = PermissionChecker.getRolePermissions(role);
                logMessage.append(String.format("  Verified permissions count: %d\n", verifyPermissions.size()));
                
                updatedCount++;
            }
            
            // In log ra console
            System.out.println(logMessage.toString());
            
            request.getSession().setAttribute("successMessage", 
                "Đã cập nhật phân quyền thành công cho " + updatedCount + " vai trò!");
            
        } catch (Exception e) {
            // Log lỗi để debug
            System.err.println("Error updating permissions: " + e.getMessage());
            request.getSession().setAttribute("errorMessage", 
                "Có lỗi xảy ra khi cập nhật phân quyền: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/permission-settings");
    }
}
