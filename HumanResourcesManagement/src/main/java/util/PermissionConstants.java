package util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Permission;

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
    
    // Attendance Management Permissions
    public static final String ATTENDANCE_VIEW = "ATTENDANCE_VIEW";
    public static final String ATTENDANCE_IMPORT = "ATTENDANCE_IMPORT";
    public static final String ATTENDANCE_ADJUST = "ATTENDANCE_ADJUST";
    public static final String ATTENDANCE_EXCEPTION_SUBMIT = "ATTENDANCE_EXCEPTION_SUBMIT";
    public static final String ATTENDANCE_EXCEPTION_APPROVE = "ATTENDANCE_EXCEPTION_APPROVE";
    public static final String ATTENDANCE_REPORT = "ATTENDANCE_REPORT";
    
    // Task Management Permissions
    public static final String TASK_VIEW = "TASK_VIEW";
    public static final String TASK_VIEW_LIST = "TASK_VIEW_LIST";
    public static final String TASK_VIEW_DETAIL = "TASK_VIEW_DETAIL";
    public static final String TASK_CREATE = "TASK_CREATE";
    public static final String TASK_EDIT = "TASK_EDIT";
    public static final String TASK_ASSIGN = "TASK_ASSIGN";
    public static final String TASK_UPDATE_STATUS = "TASK_UPDATE_STATUS";
    public static final String TASK_CANCEL = "TASK_CANCEL";
    
    // Salary Management Permissions
    public static final String SALARY_VIEW = "SALARY_VIEW";
    public static final String SALARY_VIEW_SUMMARY = "SALARY_VIEW_SUMMARY";
    public static final String SALARY_VIEW_COMPONENTS = "SALARY_VIEW_COMPONENTS";
    public static final String SALARY_IMPORT = "SALARY_IMPORT";
    public static final String SALARY_CALCULATE = "SALARY_CALCULATE";
    public static final String SALARY_ADJUST_BONUS = "SALARY_ADJUST_BONUS";
    public static final String SALARY_EXPORT_PAYSLIP = "SALARY_EXPORT_PAYSLIP";
    public static final String SALARY_EXPORT_REPORT = "SALARY_EXPORT_REPORT";
    
    // Request Management Permissions
    public static final String REQUEST_VIEW = "REQUEST_VIEW";
    public static final String REQUEST_VIEW_DETAIL = "REQUEST_VIEW_DETAIL";
    public static final String REQUEST_VIEW_DEPT_LIST = "REQUEST_VIEW_DEPT_LIST";
    public static final String REQUEST_APPROVE_REJECT = "REQUEST_APPROVE_REJECT";
    
    // Reporting & Analytics Permissions
    public static final String REPORT_VIEW_STATISTICS = "REPORT_VIEW_STATISTICS";
    
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
        
        // Attendance Management
        permissions.add(new Permission(ATTENDANCE_VIEW, "Xem chấm công", "Cho phép xem dữ liệu chấm công", "Attendance Management"));
        permissions.add(new Permission(ATTENDANCE_IMPORT, "Import chấm công", "Cho phép import dữ liệu chấm công", "Attendance Management"));
        permissions.add(new Permission(ATTENDANCE_ADJUST, "Điều chỉnh chấm công", "Cho phép điều chỉnh dữ liệu chấm công", "Attendance Management"));
        permissions.add(new Permission(ATTENDANCE_EXCEPTION_SUBMIT, "Gửi đơn giải trình", "Cho phép gửi đơn giải trình chấm công", "Attendance Management"));
        permissions.add(new Permission(ATTENDANCE_EXCEPTION_APPROVE, "Duyệt đơn giải trình", "Cho phép phê duyệt đơn giải trình chấm công", "Attendance Management"));
        permissions.add(new Permission(ATTENDANCE_REPORT, "Báo cáo chấm công", "Cho phép xem và xuất báo cáo chấm công", "Attendance Management"));
        
        // Task Management
        permissions.add(new Permission(TASK_VIEW, "Xem công việc", "Cho phép xem danh sách công việc", "Task Management"));
        permissions.add(new Permission(TASK_VIEW_LIST, "Xem danh sách công việc", "Cho phép xem danh sách công việc", "Task Management"));
        permissions.add(new Permission(TASK_VIEW_DETAIL, "Xem chi tiết công việc", "Cho phép xem chi tiết công việc", "Task Management"));
        permissions.add(new Permission(TASK_CREATE, "Tạo công việc", "Cho phép tạo công việc mới", "Task Management"));
        permissions.add(new Permission(TASK_EDIT, "Chỉnh sửa công việc", "Cho phép chỉnh sửa thông tin công việc", "Task Management"));
        permissions.add(new Permission(TASK_ASSIGN, "Phân công việc", "Cho phép phân công công việc cho nhân viên", "Task Management"));
        permissions.add(new Permission(TASK_UPDATE_STATUS, "Cập nhật trạng thái", "Cho phép cập nhật trạng thái công việc", "Task Management"));
        permissions.add(new Permission(TASK_CANCEL, "Hủy công việc", "Cho phép hủy công việc", "Task Management"));
        
        // Salary Management
        permissions.add(new Permission(SALARY_VIEW, "Xem lương", "Cho phép xem thông tin lương", "Salary Management"));
        permissions.add(new Permission(SALARY_VIEW_SUMMARY, "Xem tổng hợp lương", "Cho phép xem tổng hợp lương", "Salary Management"));
        permissions.add(new Permission(SALARY_VIEW_COMPONENTS, "Xem thành phần lương", "Cho phép xem thành phần lương", "Salary Management"));
        permissions.add(new Permission(SALARY_IMPORT, "Import dữ liệu lương", "Cho phép import dữ liệu lương", "Salary Management"));
        permissions.add(new Permission(SALARY_CALCULATE, "Tính lương", "Cho phép tính lương cho nhân viên", "Salary Management"));
        permissions.add(new Permission(SALARY_ADJUST_BONUS, "Điều chỉnh thưởng", "Cho phép điều chỉnh thưởng", "Salary Management"));
        permissions.add(new Permission(SALARY_EXPORT_PAYSLIP, "Xuất phiếu lương", "Cho phép xuất phiếu lương", "Salary Management"));
        permissions.add(new Permission(SALARY_EXPORT_REPORT, "Xuất báo cáo lương", "Cho phép xuất báo cáo lương", "Salary Management"));
        
        // Request Management
        permissions.add(new Permission(REQUEST_VIEW, "Xem yêu cầu", "Cho phép xem yêu cầu", "Request Management"));
        permissions.add(new Permission(REQUEST_VIEW_DETAIL, "Xem chi tiết yêu cầu", "Cho phép xem chi tiết yêu cầu", "Request Management"));
        permissions.add(new Permission(REQUEST_VIEW_DEPT_LIST, "Xem yêu cầu phòng ban", "Cho phép xem danh sách yêu cầu của phòng ban", "Request Management"));
        permissions.add(new Permission(REQUEST_APPROVE_REJECT, "Phê duyệt/Từ chối yêu cầu", "Cho phép phê duyệt hoặc từ chối yêu cầu", "Request Management"));
        
        // Reporting & Analytics
        permissions.add(new Permission(REPORT_VIEW_STATISTICS, "Xem thống kê HR", "Cho phép xem thống kê và báo cáo HR", "Reporting & Analytics"));
        
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
