package util;

import dal.PermissionDAO;
import model.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Permission Checker - Kiểm tra quyền của user từ database
 * Hỗ trợ cache để tăng performance
 */
public class PermissionChecker {
    
    // Cache permissions trong memory để tăng performance
    private static final ConcurrentHashMap<String, Set<String>> permissionsCache = new ConcurrentHashMap<>();
    private static long lastCacheUpdate = 0;
    private static final long CACHE_TIMEOUT = 5 * 60 * 1000; // 5 phút
    
    /**
     * Kiểm tra user có permission không
     * Admin luôn có tất cả quyền
     */
    public static boolean hasPermission(User user, String permissionCode) {
        if (user == null || permissionCode == null) {
            return false;
        }
        
        String role = user.getRole();
        
        // Admin có tất cả quyền
        if ("Admin".equals(role)) {
            return true;
        }
        
        // Lấy permissions của role từ cache hoặc database
        Set<String> permissions = getRolePermissions(role);
        return permissions.contains(permissionCode);
    }
    
    /**
     * Kiểm tra user có bất kỳ permission nào trong danh sách
     */
    public static boolean hasAnyPermission(User user, String... permissionCodes) {
        if (user == null || permissionCodes == null || permissionCodes.length == 0) {
            return false;
        }
        
        for (String permissionCode : permissionCodes) {
            if (hasPermission(user, permissionCode)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Kiểm tra user có tất cả permissions trong danh sách
     */
    public static boolean hasAllPermissions(User user, String... permissionCodes) {
        if (user == null || permissionCodes == null || permissionCodes.length == 0) {
            return false;
        }
        
        for (String permissionCode : permissionCodes) {
            if (!hasPermission(user, permissionCode)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Lấy tất cả permissions của user
     */
    public static Set<String> getUserPermissions(User user) {
        if (user == null || user.getRole() == null) {
            return Collections.emptySet();
        }
        
        // Admin có tất cả quyền
        if ("Admin".equals(user.getRole())) {
            return getAllPermissionCodes();
        }
        
        return new HashSet<>(getRolePermissions(user.getRole()));
    }
    
    /**
     * Lấy permissions của role từ cache hoặc database (public để các servlet khác dùng)
     */
    public static Set<String> getRolePermissions(String role) {
        // Check cache expiry
        long now = System.currentTimeMillis();
        if (now - lastCacheUpdate > CACHE_TIMEOUT) {
            clearCache();
        }
        
        // Lấy từ cache hoặc database
        return permissionsCache.computeIfAbsent(role, r -> {
            PermissionDAO dao = new PermissionDAO();
            try {
                return dao.getRolePermissions(r);
            } finally {
                dao.close();
            }
        });
    }
    
    /**
     * Cập nhật permissions cho một role
     */
    public static boolean updateRolePermissions(String role, Set<String> permissions) {
        PermissionDAO dao = new PermissionDAO();
        try {
            boolean success = dao.updateRolePermissions(role, permissions);
            if (success) {
                clearCacheForRole(role);
            }
            return success;
        } finally {
            dao.close();
        }
    }
    
    /**
     * Reload permissions từ database (clear toàn bộ cache)
     */
    public static void reloadPermissionsFromDatabase() {
        clearCache();
    }
    
    /**
     * Lấy tất cả permission codes trong hệ thống
     */
    private static Set<String> getAllPermissionCodes() {
        Set<String> allCodes = new HashSet<>();
        for (model.Permission permission : PermissionConstants.getAllPermissions()) {
            allCodes.add(permission.getCode());
        }
        return allCodes;
    }
    
    /**
     * Clear cache - gọi khi cập nhật permissions
     */
    public static void clearCache() {
        permissionsCache.clear();
        lastCacheUpdate = System.currentTimeMillis();
    }
    
    /**
     * Clear cache cho một role cụ thể
     */
    public static void clearCacheForRole(String role) {
        permissionsCache.remove(role);
    }
    
    /**
     * Lấy tất cả roles trong hệ thống
     */
    public static Set<String> getAllRoles() {
        PermissionDAO dao = new PermissionDAO();
        try {
            return dao.getAllRoles();
        } finally {
            dao.close();
        }
    }
}
