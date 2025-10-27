-- ==============================================================================
-- ROLE & PERMISSIONS MANAGEMENT SYSTEM
-- ==============================================================================

USE hr_management_system;

-- Bảng permissions (các quyền trong hệ thống)
CREATE TABLE IF NOT EXISTS permissions (
    permission_id INT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) UNIQUE NOT NULL,
    permission_code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    module VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Bảng roles (các vai trò)
CREATE TABLE IF NOT EXISTS roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    role_code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    is_system_role BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Bảng role_permissions (quyền của từng role)
CREATE TABLE IF NOT EXISTS role_permissions (
    role_permission_id INT PRIMARY KEY AUTO_INCREMENT,
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    granted_by INT,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE,
    FOREIGN KEY (granted_by) REFERENCES users(user_id) ON DELETE SET NULL,
    UNIQUE KEY unique_role_permission (role_id, permission_id)
) ENGINE=InnoDB;

-- Insert default permissions
INSERT INTO permissions (permission_name, permission_code, description, module) VALUES
('View Users', 'USER_VIEW', 'Xem danh sách người dùng', 'User Management'),
('Create User', 'USER_CREATE', 'Tạo người dùng mới', 'User Management'),
('Edit User', 'USER_EDIT', 'Chỉnh sửa thông tin người dùng', 'User Management'),
('Delete User', 'USER_DELETE', 'Xóa người dùng', 'User Management'),
('View Attendance', 'ATTENDANCE_VIEW', 'Xem chấm công', 'Attendance'),
('Edit Attendance', 'ATTENDANCE_EDIT', 'Chỉnh sửa chấm công', 'Attendance'),
('Approve Attendance', 'ATTENDANCE_APPROVE', 'Duyệt yêu cầu chấm công', 'Attendance'),
('View Payroll', 'PAYROLL_VIEW', 'Xem bảng lương', 'Payroll'),
('Edit Payroll', 'PAYROLL_EDIT', 'Chỉnh sửa bảng lương', 'Payroll'),
('Approve Payroll', 'PAYROLL_APPROVE', 'Duyệt bảng lương', 'Payroll'),
('Manage Roles', 'ROLE_MANAGE', 'Quản lý vai trò và quyền', 'System'),
('View Reports', 'REPORT_VIEW', 'Xem báo cáo', 'Reports');

-- Insert default roles
INSERT INTO roles (role_name, role_code, description, is_system_role) VALUES
('Administrator', 'ADMIN', 'Quản trị viên hệ thống - có tất cả quyền', TRUE),
('HR Manager', 'HR_MANAGER', 'Quản lý nhân sự', TRUE),
('HR Staff', 'HR', 'Nhân viên phòng nhân sự', TRUE),
('Department Manager', 'DEPT_MANAGER', 'Trưởng phòng ban', TRUE),
('Employee', 'EMPLOYEE', 'Nhân viên', TRUE);

-- Gán quyền cho Admin (tất cả quyền)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r, permissions p
WHERE r.role_code = 'ADMIN';

-- Gán quyền cho HR Manager
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r, permissions p
WHERE r.role_code = 'HR_MANAGER'
AND p.permission_code IN ('USER_VIEW', 'USER_CREATE', 'USER_EDIT', 'ATTENDANCE_VIEW', 'ATTENDANCE_EDIT', 'ATTENDANCE_APPROVE', 'PAYROLL_VIEW', 'PAYROLL_EDIT', 'PAYROLL_APPROVE', 'REPORT_VIEW');

-- Gán quyền cho HR Staff
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r, permissions p
WHERE r.role_code = 'HR'
AND p.permission_code IN ('USER_VIEW', 'ATTENDANCE_VIEW', 'ATTENDANCE_EDIT', 'PAYROLL_VIEW');

-- Gán quyền cho Department Manager
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r, permissions p
WHERE r.role_code = 'DEPT_MANAGER'
AND p.permission_code IN ('USER_VIEW', 'ATTENDANCE_VIEW', 'ATTENDANCE_APPROVE', 'REPORT_VIEW');

-- Gán quyền cho Employee
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r, permissions p
WHERE r.role_code = 'EMPLOYEE'
AND p.permission_code IN ('ATTENDANCE_VIEW');
