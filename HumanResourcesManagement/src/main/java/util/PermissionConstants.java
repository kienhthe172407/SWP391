package util;

import model.Permission;
import java.util.*;

/**
 * Permission Constants - Định nghĩa tất cả permissions trong hệ thống
 */
public class PermissionConstants {
    
    // User Management Permissions
    public static final String USER_VIEW = "USER_VIEW";
    public static final String USER_CREATE = "USER_CREATE";
    public static final String USER_EDIT = "USER_EDIT";
    public static final String USER_ACTIVATE = "USER_ACTIVATE";
    
    // Employee Management Permissions
    public static final String EMPLOYEE_VIEW = "EMPLOYEE_VIEW";
    public static final String EMPLOYEE_CREATE = "EMPLOYEE_CREATE";
    public static final String EMPLOYEE_EDIT = "EMPLOYEE_EDIT";
    public static final String EMPLOYEE_DELETE = "EMPLOYEE_DELETE";
    
    // Department Management Permissions
    public static final String DEPT_VIEW = "DEPT_VIEW";
    public static final String DEPT_CREATE = "DEPT_CREATE";
    public static final String DEPT_EDIT = "DEPT_EDIT";
    public static final String DEPT_DELETE = "DEPT_DELETE";
    
    // Contract Management Permissions
    public static final String CONTRACT_VIEW = "CONTRACT_VIEW";
    public static final String CONTRACT_CREATE = "CONTRACT_CREATE";
    public static final String CONTRACT_EDIT = "CONTRACT_EDIT";
    public static final String CONTRACT_DELETE = "CONTRACT_DELETE";
    public static final String CONTRACT_APPROVE = "CONTRACT_APPROVE";
    
    // Job Posting Permissions
    public static final String JOB_VIEW = "JOB_VIEW";
    public static final String JOB_CREATE = "JOB_CREATE";
    public static final String JOB_EDIT = "JOB_EDIT";
    public static final String JOB_DELETE = "JOB_DELETE";
    
    // System Settings Permissions
    public static final String SYSTEM_CONFIG = "SYSTEM_CONFIG";
    public static final String ROLE_MANAGE = "ROLE_MANAGE";
    public static final String PERMISSION_MANAGE = "PERMISSION_MANAGE";
    
    /**
     * Lấy tất cả permissions
     */
    public static List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        
        // User Management
        permissions.add(new Permission(USER_VIEW, "Xem danh sách người dùng", "Cho phép xem danh sách tài khoản người dùng", "User Management"));
        permissions.add(new Permission(USER_CREATE, "Tạo người dùng mới", "Cho phép tạo tài khoản người dùng mới", "User Management"));
        permissions.add(new Permission(USER_EDIT, "Chỉnh sửa người dùng", "Cho phép chỉnh sửa thông tin người dùng", "User Management"));
        permissions.add(new Permission(USER_ACTIVATE, "Kích hoạt/Vô hiệu hóa", "Cho phép kích hoạt hoặc vô hiệu hóa tài khoản", "User Management"));
        
        // Employee Management
        permissions.add(new Permission(EMPLOYEE_VIEW, "Xem danh sách nhân viên", "Cho phép xem thông tin nhân viên", "Employee Management"));
        permissions.add(new Permission(EMPLOYEE_CREATE, "Thêm nhân viên mới", "Cho phép thêm nhân viên mới vào hệ thống", "Employee Management"));
        permissions.add(new Permission(EMPLOYEE_EDIT, "Chỉnh sửa nhân viên", "Cho phép chỉnh sửa thông tin nhân viên", "Employee Management"));
        permissions.add(new Permission(EMPLOYEE_DELETE, "Xóa nhân viên", "Cho phép xóa nhân viên khỏi hệ thống", "Employee Management"));
        
        // Department Management
        permissions.add(new Permission(DEPT_VIEW, "Xem phòng ban", "Cho phép xem danh sách phòng ban", "Department Management"));
        permissions.add(new Permission(DEPT_CREATE, "Tạo phòng ban", "Cho phép tạo phòng ban mới", "Department Management"));
        permissions.add(new Permission(DEPT_EDIT, "Chỉnh sửa phòng ban", "Cho phép chỉnh sửa thông tin phòng ban", "Department Management"));
        permissions.add(new Permission(DEPT_DELETE, "Xóa phòng ban", "Cho phép xóa phòng ban", "Department Management"));
        
        // Contract Management
        permissions.add(new Permission(CONTRACT_VIEW, "Xem hợp đồng", "Cho phép xem danh sách hợp đồng", "Contract Management"));
        permissions.add(new Permission(CONTRACT_CREATE, "Tạo hợp đồng", "Cho phép tạo hợp đồng mới", "Contract Management"));
        permissions.add(new Permission(CONTRACT_EDIT, "Chỉnh sửa hợp đồng", "Cho phép chỉnh sửa hợp đồng", "Contract Management"));
        permissions.add(new Permission(CONTRACT_DELETE, "Xóa hợp đồng", "Cho phép xóa hợp đồng", "Contract Management"));
        permissions.add(new Permission(CONTRACT_APPROVE, "Phê duyệt hợp đồng", "Cho phép phê duyệt hợp đồng", "Contract Management"));
        
        // Job Posting
        permissions.add(new Permission(JOB_VIEW, "Xem tin tuyển dụng", "Cho phép xem danh sách tin tuyển dụng", "Job Posting"));
        permissions.add(new Permission(JOB_CREATE, "Tạo tin tuyển dụng", "Cho phép tạo tin tuyển dụng mới", "Job Posting"));
        permissions.add(new Permission(JOB_EDIT, "Chỉnh sửa tin tuyển dụng", "Cho phép chỉnh sửa tin tuyển dụng", "Job Posting"));
        permissions.add(new Permission(JOB_DELETE, "Xóa tin tuyển dụng", "Cho phép xóa tin tuyển dụng", "Job Posting"));
        
        // System Settings
        permissions.add(new Permission(SYSTEM_CONFIG, "Cấu hình hệ thống", "Cho phép cấu hình các thiết lập hệ thống", "System Settings"));
        permissions.add(new Permission(ROLE_MANAGE, "Quản lý vai trò", "Cho phép quản lý các vai trò người dùng", "System Settings"));
        permissions.add(new Permission(PERMISSION_MANAGE, "Quản lý phân quyền", "Cho phép quản lý phân quyền cho vai trò", "System Settings"));
        
        return permissions;
    }
    
    /**
     * Lấy permissions theo category
     */
    public static Map<String, List<Permission>> getPermissionsByCategory() {
        Map<String, List<Permission>> categoryMap = new LinkedHashMap<>();
        
        for (Permission permission : getAllPermissions()) {
            categoryMap.computeIfAbsent(permission.getCategory(), k -> new ArrayList<>()).add(permission);
        }
        
        return categoryMap;
    }
}
