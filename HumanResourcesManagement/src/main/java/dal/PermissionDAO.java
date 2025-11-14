package dal;

import model.Permission;
import java.sql.*;
import java.util.*;

/**
 * DAO để quản lý permissions trong database
 */
public class PermissionDAO extends DBContext {
    
    /**
     * Lấy tất cả permissions từ database
     */
    public List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT code, name, description, category FROM permissions ORDER BY category, name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Permission permission = new Permission(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("category")
                );
                permissions.add(permission);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all permissions: " + e.getMessage());
        }
        
        return permissions;
    }
    
    /**
     * Lấy permissions theo category
     */
    public Map<String, List<Permission>> getPermissionsByCategory() {
        Map<String, List<Permission>> categoryMap = new LinkedHashMap<>();
        
        for (Permission permission : getAllPermissions()) {
            categoryMap.computeIfAbsent(permission.getCategory(), k -> new ArrayList<>())
                      .add(permission);
        }
        
        return categoryMap;
    }
    
    /**
     * Lấy tất cả permissions của một role
     */
    public Set<String> getRolePermissions(String role) {
        Set<String> permissions = new HashSet<>();
        String sql = "SELECT permission_code FROM role_permissions WHERE role = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permissions.add(rs.getString("permission_code"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting role permissions for " + role + ": " + e.getMessage());
        }
        
        return permissions;
    }
    
    /**
     * Lấy tất cả roles có trong hệ thống
     */
    public Set<String> getAllRoles() {
        // Luôn trả về tất cả roles chuẩn trong hệ thống
        Set<String> roles = new LinkedHashSet<>();
        roles.add("Admin");
        roles.add("HR Manager");
        roles.add("HR");
        roles.add("Dept Manager");
        roles.add("Employee");
        
        // Có thể thêm các role từ database nếu có role tùy chỉnh
        String sql = "SELECT DISTINCT role FROM role_permissions WHERE role NOT IN ('Admin', 'HR Manager', 'HR', 'Dept Manager', 'Employee') ORDER BY role";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting custom roles: " + e.getMessage());
        }
        
        return roles;
    }
    
    /**
     * Cập nhật permissions cho một role (xóa hết và thêm mới)
     */
    public boolean updateRolePermissions(String role, Set<String> permissions) {
        try {
            connection.setAutoCommit(false);
            
            // Xóa tất cả permissions cũ của role
            String deleteSql = "DELETE FROM role_permissions WHERE role = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setString(1, role);
                ps.executeUpdate();
            }
            
            // Thêm permissions mới
            if (permissions != null && !permissions.isEmpty()) {
                String insertSql = "INSERT INTO role_permissions (role, permission_code) VALUES (?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    for (String permissionCode : permissions) {
                        ps.setString(1, role);
                        ps.setString(2, permissionCode);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            
            connection.commit();
            connection.setAutoCommit(true);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error updating role permissions: " + e.getMessage());
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error rolling back: " + ex.getMessage());
            }
            return false;
        }
    }
    
    /**
     * Kiểm tra role có permission không
     */
    public boolean roleHasPermission(String role, String permissionCode) {
        String sql = "SELECT COUNT(*) FROM role_permissions WHERE role = ? AND permission_code = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, permissionCode);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking role permission: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Lấy Map của tất cả roles và permissions của chúng
     */
    public Map<String, Set<String>> getAllRolePermissions() {
        Map<String, Set<String>> rolePermissionsMap = new LinkedHashMap<>();
        String sql = "SELECT role, permission_code FROM role_permissions ORDER BY role";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String role = rs.getString("role");
                String permissionCode = rs.getString("permission_code");
                
                rolePermissionsMap.computeIfAbsent(role, k -> new HashSet<>())
                                 .add(permissionCode);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all role permissions: " + e.getMessage());
        }
        
        return rolePermissionsMap;
    }
    
    /**
     * Kiểm tra permission có tồn tại không
     */
    public boolean permissionExists(String code) {
        String sql = "SELECT COUNT(*) FROM permissions WHERE code = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking permission exists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Thêm permission mới vào database
     */
    public boolean addPermission(Permission permission) {
        String sql = "INSERT INTO permissions (code, name, description, category) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, permission.getCode());
            ps.setString(2, permission.getName());
            ps.setString(3, permission.getDescription());
            ps.setString(4, permission.getCategory());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding permission: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy danh sách permission codes của một role (trả về List thay vì Set)
     */
    public List<String> getPermissionsByRole(String role) {
        List<String> permissions = new ArrayList<>();
        String sql = "SELECT permission_code FROM role_permissions WHERE role = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permissions.add(rs.getString("permission_code"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting permissions by role: " + e.getMessage());
        }
        
        return permissions;
    }
    
    /**
     * Xóa tất cả permissions của một role
     */
    public boolean deleteRolePermissions(String role) {
        String sql = "DELETE FROM role_permissions WHERE role = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting role permissions: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thêm một permission cho role
     */
    public boolean addRolePermission(String role, String permissionCode) {
        String sql = "INSERT INTO role_permissions (role, permission_code) VALUES (?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, permissionCode);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding role permission: " + e.getMessage());
            return false;
        }
    }
}
