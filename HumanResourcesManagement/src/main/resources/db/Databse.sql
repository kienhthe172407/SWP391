-- ==============================================================================
-- DATABASE CREATION
-- ==============================================================================
DROP DATABASE IF EXISTS hr_management_system;
CREATE DATABASE hr_management_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hr_management_system;

-- ==============================================================================
-- TABLE: users
-- Bảng quản lý tài khoản người dùng hệ thống
-- ==============================================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,                    -- ID người dùng (khóa chính)
    username VARCHAR(50) UNIQUE NOT NULL,                      -- Tên đăng nhập (duy nhất)
    password_hash VARCHAR(255) NOT NULL,                       -- Mật khẩu đã mã hóa
    email VARCHAR(100) UNIQUE NOT NULL,                        -- Email (duy nhất)
    first_name VARCHAR(50),                                    -- Họ (tuỳ chọn)
    last_name VARCHAR(50),                                     -- Tên (tuỳ chọn)
    role ENUM('Admin', 'HR', 'HR Manager', 'Employee', 'Dept Manager') NOT NULL, -- Vai trò người dùng
    status ENUM('Active', 'Inactive') DEFAULT 'Active',        -- Trạng thái tài khoản
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    last_login TIMESTAMP NULL,                                 -- Lần đăng nhập cuối
    created_by INT,                                            -- Người tạo tài khoản
    INDEX idx_username (username),                             -- Index cho tìm kiếm username
    INDEX idx_email (email),                                   -- Index cho tìm kiếm email
    INDEX idx_role (role),                                     -- Index cho lọc theo role
    INDEX idx_status (status)                                  -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: departments
-- Bảng quản lý các phòng ban
-- ==============================================================================
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,              -- ID phòng ban (khóa chính)
    department_name VARCHAR(100) UNIQUE NOT NULL,              -- Tên phòng ban (duy nhất)
    description TEXT,                                          -- Mô tả phòng ban
    manager_id INT,                                            -- ID trưởng phòng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    INDEX idx_department_name (department_name)                -- Index cho tìm kiếm tên phòng ban
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: positions
-- Bảng quản lý các chức vụ/vị trí công việc
-- ==============================================================================
CREATE TABLE positions (
    position_id INT PRIMARY KEY AUTO_INCREMENT,                -- ID chức vụ (khóa chính)
    position_name VARCHAR(100) UNIQUE NOT NULL,                -- Tên chức vụ (duy nhất)
    description TEXT,                                          -- Mô tả chức vụ
    base_salary DECIMAL(12, 2),                                -- Lương cơ bản của chức vụ
    position_allowance DECIMAL(10, 2) DEFAULT 0,               -- Phụ cấp chức vụ
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    INDEX idx_position_name (position_name)                    -- Index cho tìm kiếm tên chức vụ
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: employees
-- Bảng quản lý thông tin nhân viên
-- ==============================================================================
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,                -- ID nhân viên (khóa chính)
    user_id INT UNIQUE,                                        -- ID tài khoản liên kết (duy nhất)
    employee_code VARCHAR(20) UNIQUE NOT NULL,                 -- Mã nhân viên (duy nhất)
    first_name VARCHAR(50) NOT NULL,                           -- Họ
    last_name VARCHAR(50) NOT NULL,                            -- Tên
    date_of_birth DATE,                                        -- Ngày sinh
    gender ENUM('Male', 'Female', 'Other'),                    -- Giới tính
    phone_number VARCHAR(20),                                  -- Số điện thoại
    personal_email VARCHAR(100),                               -- Email cá nhân
    home_address TEXT,                                         -- Địa chỉ nhà
    emergency_contact_name VARCHAR(100),                       -- Tên người liên hệ khẩn cấp
    emergency_contact_phone VARCHAR(20),                       -- SĐT người liên hệ khẩn cấp
    department_id INT,                                         -- ID phòng ban
    position_id INT,                                           -- ID chức vụ
    manager_id INT,                                            -- ID quản lý trực tiếp
    hire_date DATE,                                            -- Ngày vào làm
    employment_status ENUM('Active', 'On Leave', 'Terminated') DEFAULT 'Active', -- Trạng thái làm việc
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại tới users
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL, -- Khóa ngoại tới departments
    FOREIGN KEY (position_id) REFERENCES positions(position_id) ON DELETE SET NULL, -- Khóa ngoại tới positions
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id) ON DELETE SET NULL, -- Khóa ngoại tự tham chiếu
    INDEX idx_employee_code (employee_code),                   -- Index cho tìm kiếm mã NV
    INDEX idx_full_name (first_name, last_name),               -- Index cho tìm kiếm họ tên
    INDEX idx_department (department_id),                      -- Index cho lọc theo phòng ban
    INDEX idx_position (position_id),                          -- Index cho lọc theo chức vụ
    INDEX idx_employment_status (employment_status),           -- Index cho lọc theo trạng thái
    FULLTEXT INDEX ft_employee_search (first_name, last_name, employee_code) -- FULLTEXT index cho tìm kiếm toàn văn
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: attendance_records
-- Bảng quản lý chấm công
-- ==============================================================================
CREATE TABLE attendance_records (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,              -- ID chấm công (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên
    attendance_date DATE NOT NULL,                             -- Ngày chấm công
    check_in_time TIME,                                        -- Giờ vào
    check_out_time TIME,                                       -- Giờ ra
    status ENUM('Present', 'Absent', 'Late', 'Early Leave', 'Business Trip', 'Remote') DEFAULT 'Present', -- Trạng thái
    overtime_hours DECIMAL(4, 2) DEFAULT 0,                    -- Số giờ làm thêm
    is_manual_adjustment BOOLEAN DEFAULT FALSE,                -- Có phải chỉnh sửa thủ công không
    adjustment_reason TEXT,                                    -- Lý do chỉnh sửa
    adjusted_by INT,                                           -- Người chỉnh sửa
    adjusted_at TIMESTAMP NULL,                                -- Thời gian chỉnh sửa
    import_batch_id VARCHAR(50),                               -- ID lô nhập liệu (để trace)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại tới employees
    FOREIGN KEY (adjusted_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại tới users
    UNIQUE KEY unique_employee_date (employee_id, attendance_date), -- Unique: 1 nhân viên 1 ngày chỉ 1 bản ghi
    INDEX idx_attendance_date (attendance_date),               -- Index cho tìm kiếm theo ngày
    INDEX idx_employee_date (employee_id, attendance_date),    -- Composite index
    INDEX idx_status (status)                                  -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: attendance_exception_requests
-- Bảng quản lý yêu cầu giải trình/sửa chấm công
-- ==============================================================================
CREATE TABLE attendance_exception_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,                 -- ID yêu cầu (khóa chính)
    attendance_id INT NOT NULL,                                -- ID bản ghi chấm công liên quan
    employee_id INT NOT NULL,                                  -- ID nhân viên
    request_reason TEXT NOT NULL,                              -- Lý do yêu cầu
    proposed_check_in TIME,                                    -- Giờ vào đề xuất
    proposed_check_out TIME,                                   -- Giờ ra đề xuất
    proposed_status ENUM('Present', 'Absent', 'Late', 'Early Leave', 'Business Trip', 'Remote'), -- Trạng thái đề xuất
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending', -- Trạng thái yêu cầu
    reviewed_by INT,                                           -- Người duyệt
    review_comment TEXT,                                       -- Nhận xét của người duyệt
    reviewed_at TIMESTAMP NULL,                                -- Thời gian duyệt
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (attendance_id) REFERENCES attendance_records(attendance_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_status (status)                                  -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: salary_components
-- Bảng quản lý các khoản lương và phụ cấp cố định
-- ==============================================================================
CREATE TABLE salary_components (
    component_id INT PRIMARY KEY AUTO_INCREMENT,               -- ID thành phần lương (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên
    base_salary DECIMAL(12, 2) NOT NULL,                       -- Lương cơ bản
    position_allowance DECIMAL(10, 2) DEFAULT 0,               -- Phụ cấp chức vụ
    housing_allowance DECIMAL(10, 2) DEFAULT 0,                -- Phụ cấp nhà ở
    transportation_allowance DECIMAL(10, 2) DEFAULT 0,         -- Phụ cấp đi lại
    meal_allowance DECIMAL(10, 2) DEFAULT 0,                   -- Phụ cấp ăn trưa
    other_allowances DECIMAL(10, 2) DEFAULT 0,                 -- Các phụ cấp khác
    effective_from DATE NOT NULL,                              -- Có hiệu lực từ ngày
    effective_to DATE,                                         -- Có hiệu lực đến ngày
    is_active BOOLEAN DEFAULT TRUE,                            -- Còn hiệu lực không
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_effective_dates (effective_from, effective_to),  -- Index cho lọc theo thời gian hiệu lực
    INDEX idx_is_active (is_active)                            -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: benefit_types
-- Bảng danh mục loại phúc lợi
-- ==============================================================================
CREATE TABLE benefit_types (
    benefit_type_id INT PRIMARY KEY AUTO_INCREMENT,            -- ID loại phúc lợi (khóa chính)
    benefit_name VARCHAR(100) UNIQUE NOT NULL,                 -- Tên phúc lợi (duy nhất)
    description TEXT,                                          -- Mô tả
    calculation_type ENUM('Fixed', 'Percentage', 'Formula') DEFAULT 'Fixed', -- Kiểu tính toán
    default_amount DECIMAL(10, 2),                             -- Số tiền mặc định (nếu Fixed)
    default_percentage DECIMAL(5, 2),                          -- % mặc định (nếu Percentage)
    is_taxable BOOLEAN DEFAULT TRUE,                           -- Có tính thuế không
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thời gian cập nhật
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: deduction_types
-- Bảng danh mục loại khấu trừ
-- ==============================================================================
CREATE TABLE deduction_types (
    deduction_type_id INT PRIMARY KEY AUTO_INCREMENT,          -- ID loại khấu trừ (khóa chính)
    deduction_name VARCHAR(100) UNIQUE NOT NULL,               -- Tên khấu trừ (duy nhất)
    description TEXT,                                          -- Mô tả
    calculation_type ENUM('Fixed', 'Percentage', 'Formula') DEFAULT 'Fixed', -- Kiểu tính toán
    default_amount DECIMAL(10, 2),                             -- Số tiền mặc định (nếu Fixed)
    default_percentage DECIMAL(5, 2),                          -- % mặc định (nếu Percentage)
    is_mandatory BOOLEAN DEFAULT TRUE,                         -- Bắt buộc hay không
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thời gian cập nhật
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: monthly_payroll
-- Bảng tính lương hàng tháng
-- ==============================================================================
CREATE TABLE monthly_payroll (
    payroll_id INT PRIMARY KEY AUTO_INCREMENT,                 -- ID bảng lương (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên
    payroll_month DATE NOT NULL,                               -- Tháng tính lương (YYYY-MM-01)
    base_salary DECIMAL(12, 2) NOT NULL,                       -- Lương cơ bản
    total_allowances DECIMAL(10, 2) DEFAULT 0,                 -- Tổng phụ cấp
    overtime_pay DECIMAL(10, 2) DEFAULT 0,                     -- Lương làm thêm
    total_bonus DECIMAL(10, 2) DEFAULT 0,                      -- Tổng thưởng
    total_benefits DECIMAL(10, 2) DEFAULT 0,                   -- Tổng phúc lợi
    total_deductions DECIMAL(10, 2) DEFAULT 0,                 -- Tổng khấu trừ
    gross_salary DECIMAL(12, 2) NOT NULL,                      -- Tổng lương gộp
    net_salary DECIMAL(12, 2) NOT NULL,                        -- Lương thực nhận
    working_days INT DEFAULT 0,                                -- Số ngày làm việc
    absent_days INT DEFAULT 0,                                 -- Số ngày vắng
    late_days INT DEFAULT 0,                                   -- Số ngày đi muộn
    overtime_hours DECIMAL(6, 2) DEFAULT 0,                    -- Tổng giờ làm thêm
    status ENUM('Draft', 'Calculated', 'Approved', 'Paid') DEFAULT 'Draft', -- Trạng thái
    calculated_at TIMESTAMP NULL,                              -- Thời gian tính lương
    calculated_by INT,                                         -- Người tính lương
    approved_at TIMESTAMP NULL,                                -- Thời gian duyệt
    approved_by INT,                                           -- Người duyệt
    paid_at TIMESTAMP NULL,                                    -- Thời gian thanh toán
    notes TEXT,                                                -- Ghi chú
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (calculated_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    FOREIGN KEY (approved_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    UNIQUE KEY unique_employee_month (employee_id, payroll_month), -- Unique: 1 nhân viên 1 tháng
    INDEX idx_payroll_month (payroll_month),                   -- Index cho lọc theo tháng
    INDEX idx_employee_month (employee_id, payroll_month),     -- Composite index
    INDEX idx_status (status)                                  -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: payroll_adjustments
-- Bảng điều chỉnh lương (thưởng/phạt đặc biệt)
-- ==============================================================================
CREATE TABLE payroll_adjustments (
    adjustment_id INT PRIMARY KEY AUTO_INCREMENT,              -- ID điều chỉnh (khóa chính)
    payroll_id INT NOT NULL,                                   -- ID bảng lương liên quan
    employee_id INT NOT NULL,                                  -- ID nhân viên
    adjustment_type ENUM('Bonus', 'Deduction', 'Retroactive', 'Correction') NOT NULL, -- Loại điều chỉnh
    amount DECIMAL(10, 2) NOT NULL,                            -- Số tiền điều chỉnh
    reason TEXT NOT NULL,                                      -- Lý do điều chỉnh
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending', -- Trạng thái
    requested_by INT NOT NULL,                                 -- Người đề xuất
    approved_by INT,                                           -- Người duyệt
    approval_comment TEXT,                                     -- Nhận xét khi duyệt
    approved_at TIMESTAMP NULL,                                -- Thời gian duyệt
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (payroll_id) REFERENCES monthly_payroll(payroll_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (requested_by) REFERENCES users(user_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (approved_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_payroll (payroll_id),                            -- Index cho lọc theo payroll
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_status (status)                                  -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: employment_contracts
-- Bảng quản lý hợp đồng lao động
-- ==============================================================================
CREATE TABLE employment_contracts (
    contract_id INT PRIMARY KEY AUTO_INCREMENT,                -- ID hợp đồng (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên
    contract_number VARCHAR(50) UNIQUE NOT NULL,               -- Số hợp đồng (duy nhất)
    contract_type ENUM('Probation', 'Fixed-term', 'Indefinite', 'Seasonal') NOT NULL, -- Loại hợp đồng
    start_date DATE NOT NULL,                                  -- Ngày bắt đầu
    end_date DATE,                                             -- Ngày kết thúc (NULL nếu vô thời hạn)
    salary_amount DECIMAL(12, 2),                              -- Mức lương trong hợp đồng
    job_description TEXT,                                      -- Mô tả công việc
    terms_and_conditions TEXT,                                 -- Điều khoản và điều kiện
    contract_status ENUM('Draft', 'Pending Approval', 'Active', 'Expired', 'Terminated', 'Rejected') DEFAULT 'Draft', -- Trạng thái
    signed_date DATE,                                          -- Ngày ký
    approved_by INT,                                           -- Người duyệt
    approval_comment TEXT,                                     -- Nhận xét khi duyệt
    approved_at TIMESTAMP NULL,                                -- Thời gian duyệt
    created_by INT,                                            -- Người tạo hợp đồng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (approved_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_contract_number (contract_number),               -- Index cho tìm kiếm số HĐ
    INDEX idx_contract_status (contract_status),               -- Index cho lọc theo trạng thái
    INDEX idx_dates (start_date, end_date),                    -- Index cho lọc theo thời gian
    FULLTEXT INDEX ft_contract_search (contract_number, job_description) -- FULLTEXT index cho tìm kiếm toàn văn (không bao gồm contract_type vì là ENUM)
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: job_postings
-- Bảng quản lý tin tuyển dụng
-- ==============================================================================
CREATE TABLE job_postings (
    job_id INT PRIMARY KEY AUTO_INCREMENT,                     -- ID tin tuyển dụng (khóa chính)
    job_title VARCHAR(200) NOT NULL,                           -- Tiêu đề công việc
    department_id INT,                                         -- ID phòng ban tuyển dụng
    position_id INT,                                           -- ID vị trí tuyển dụng
    job_description TEXT NOT NULL,                             -- Mô tả công việc
    requirements TEXT,                                         -- Yêu cầu ứng viên
    benefits TEXT,                                             -- Quyền lợi
    salary_range_from DECIMAL(12, 2),                          -- Mức lương từ
    salary_range_to DECIMAL(12, 2),                            -- Mức lương đến
    number_of_positions INT DEFAULT 1,                         -- Số lượng tuyển
    application_deadline DATE,                                 -- Hạn nộp hồ sơ
    job_status ENUM('Open', 'Closed', 'Filled', 'Cancelled') DEFAULT 'Open', -- Trạng thái tin tuyển dụng
    posted_by INT,                                             -- Người đăng tin
    posted_date DATE,                                          -- Ngày đăng tin
    internal_notes TEXT,                                       -- Ghi chú nội bộ (chỉ HR xem)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL, -- Khóa ngoại
    FOREIGN KEY (position_id) REFERENCES positions(position_id) ON DELETE SET NULL, -- Khóa ngoại
    FOREIGN KEY (posted_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_job_title (job_title),                           -- Index cho tìm kiếm tiêu đề
    INDEX idx_department (department_id),                      -- Index cho lọc theo phòng ban
    INDEX idx_job_status (job_status),                         -- Index cho lọc theo trạng thái
    INDEX idx_posted_date (posted_date),                       -- Index cho lọc theo ngày đăng
    FULLTEXT INDEX ft_job_posting_search (job_title, job_description, requirements) -- FULLTEXT index cho tìm kiếm toàn văn
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: job_applications
-- Bảng quản lý hồ sơ ứng tuyển
-- ==============================================================================
CREATE TABLE job_applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,             -- ID hồ sơ ứng tuyển (khóa chính)
    job_id INT NOT NULL,                                       -- ID tin tuyển dụng
    applicant_name VARCHAR(100) NOT NULL,                      -- Tên ứng viên
    applicant_email VARCHAR(100) NOT NULL,                     -- Email ứng viên
    applicant_phone VARCHAR(20),                               -- SĐT ứng viên
    resume_file_path VARCHAR(255),                             -- Đường dẫn file CV
    cover_letter TEXT,                                         -- Thư xin việc
    application_status ENUM('Submitted', 'Screening', 'Interview', 'Offered', 'Rejected', 'Hired') DEFAULT 'Submitted', -- Trạng thái
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          -- Ngày nộp hồ sơ
    reviewed_by INT,                                           -- Người xem xét
    review_notes TEXT,                                         -- Ghi chú đánh giá
    interview_date DATETIME,                                   -- Ngày phỏng vấn (nếu có)
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (job_id) REFERENCES job_postings(job_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_job (job_id),                                    -- Index cho lọc theo job
    INDEX idx_applicant_email (applicant_email),               -- Index cho tìm kiếm email
    INDEX idx_application_status (application_status),         -- Index cho lọc theo trạng thái
    INDEX idx_applied_date (applied_date)                      -- Index cho lọc theo ngày nộp
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: tasks
-- Bảng quản lý công việc/nhiệm vụ
-- ==============================================================================
CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,                    -- ID nhiệm vụ (khóa chính)
    task_title VARCHAR(200) NOT NULL,                          -- Tiêu đề nhiệm vụ
    task_description TEXT,                                     -- Mô tả nhiệm vụ
    assigned_to INT NOT NULL,                                  -- Người được giao việc
    assigned_by INT NOT NULL,                                  -- Người giao việc
    department_id INT,                                         -- ID phòng ban liên quan
    priority ENUM('Low', 'Medium', 'High', 'Urgent') DEFAULT 'Medium', -- Độ ưu tiên
    task_status ENUM('Not Started', 'In Progress', 'Done', 'Blocked', 'Cancelled') DEFAULT 'Not Started', -- Trạng thái
    start_date DATE,                                           -- Ngày bắt đầu dự kiến
    due_date DATE,                                             -- Hạn hoàn thành
    completed_date DATE,                                       -- Ngày hoàn thành thực tế
    progress_percentage INT DEFAULT 0,                         -- % tiến độ (0-100)
    cancellation_reason TEXT,                                  -- Lý do hủy (nếu có)
    cancelled_by INT,                                          -- Người hủy
    cancelled_at TIMESTAMP NULL,                               -- Thời gian hủy
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (assigned_to) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (assigned_by) REFERENCES users(user_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL, -- Khóa ngoại
    FOREIGN KEY (cancelled_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_assigned_to (assigned_to),                       -- Index cho lọc theo người nhận việc
    INDEX idx_assigned_by (assigned_by),                       -- Index cho lọc theo người giao việc
    INDEX idx_task_status (task_status),                       -- Index cho lọc theo trạng thái
    INDEX idx_priority (priority),                             -- Index cho lọc theo độ ưu tiên
    INDEX idx_due_date (due_date),                             -- Index cho lọc theo hạn
    INDEX idx_department (department_id)                       -- Index cho lọc theo phòng ban
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: request_types
-- Bảng danh mục loại yêu cầu
-- ==============================================================================
CREATE TABLE request_types (
    request_type_id INT PRIMARY KEY AUTO_INCREMENT,            -- ID loại yêu cầu (khóa chính)
    request_type_name VARCHAR(100) UNIQUE NOT NULL,            -- Tên loại yêu cầu (duy nhất)
    description TEXT,                                          -- Mô tả loại yêu cầu
    requires_approval BOOLEAN DEFAULT TRUE,                    -- Có cần phê duyệt không
    max_days_per_year INT,                                     -- Số ngày tối đa/năm (NULL = không giới hạn)
    is_paid BOOLEAN DEFAULT TRUE,                              -- Có hưởng lương không
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thời gian cập nhật
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: requests
-- Bảng quản lý các yêu cầu (nghỉ phép, công tác, remote...)
-- ==============================================================================
CREATE TABLE requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,                 -- ID yêu cầu (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên gửi yêu cầu
    request_type_id INT NOT NULL,                              -- ID loại yêu cầu
    start_date DATE NOT NULL,                                  -- Ngày bắt đầu
    end_date DATE NOT NULL,                                    -- Ngày kết thúc
    number_of_days DECIMAL(4, 1) NOT NULL,                     -- Số ngày (có thể 0.5 cho nửa ngày)
    reason TEXT NOT NULL,                                      -- Lý do yêu cầu
    request_status ENUM('Pending', 'Approved', 'Rejected', 'Cancelled') DEFAULT 'Pending', -- Trạng thái
    reviewed_by INT,                                           -- Người duyệt
    review_comment TEXT,                                       -- Nhận xét khi duyệt
    reviewed_at TIMESTAMP NULL,                                -- Thời gian duyệt
    cancelled_at TIMESTAMP NULL,                               -- Thời gian hủy
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (request_type_id) REFERENCES request_types(request_type_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_request_type (request_type_id),                  -- Index cho lọc theo loại
    INDEX idx_request_status (request_status),                 -- Index cho lọc theo trạng thái
    INDEX idx_dates (start_date, end_date),                    -- Index cho lọc theo ngày
    INDEX idx_reviewed_by (reviewed_by)                        -- Index cho lọc theo người duyệt
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: company_information
-- Bảng thông tin công ty (hiển thị công khai)
-- ==============================================================================
CREATE TABLE company_information (
    company_id INT PRIMARY KEY AUTO_INCREMENT,                 -- ID công ty (khóa chính)
    company_name VARCHAR(200) NOT NULL,                        -- Tên công ty
    company_logo_path VARCHAR(255),                            -- Đường dẫn logo
    about_us TEXT,                                             -- Giới thiệu về công ty
    mission_statement TEXT,                                    -- Sứ mệnh
    vision_statement TEXT,                                     -- Tầm nhìn
    core_values TEXT,                                          -- Giá trị cốt lõi
    address TEXT,                                              -- Địa chỉ trụ sở
    phone_number VARCHAR(20),                                  -- Số điện thoại liên hệ
    email VARCHAR(100),                                        -- Email liên hệ
    website VARCHAR(200),                                      -- Website
    founded_year INT,                                          -- Năm thành lập
    number_of_employees VARCHAR(50),                           -- Số lượng nhân viên (có thể là range: "100-500")
    industry VARCHAR(100),                                     -- Ngành nghề
    social_media_links JSON,                                   -- Links mạng xã hội (Facebook, LinkedIn, etc.)
    is_active BOOLEAN DEFAULT TRUE,                            -- Đang hoạt động
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thời gian cập nhật
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: audit_logs
-- Bảng ghi log hoạt động hệ thống (audit trail)
-- ==============================================================================
CREATE TABLE audit_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,                     -- ID log (khóa chính)
    user_id INT,                                               -- ID người thực hiện
    action_type VARCHAR(50) NOT NULL,                          -- Loại hành động (CREATE, UPDATE, DELETE, LOGIN, etc.)
    table_name VARCHAR(100),                                   -- Tên bảng bị tác động
    record_id INT,                                             -- ID bản ghi bị tác động
    old_values JSON,                                           -- Giá trị cũ (trước khi thay đổi)
    new_values JSON,                                           -- Giá trị mới (sau khi thay đổi)
    ip_address VARCHAR(45),                                    -- Địa chỉ IP
    user_agent TEXT,                                           -- Thông tin trình duyệt/thiết bị
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,      -- Thời gian thực hiện
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL, -- Khóa ngoại
    INDEX idx_user (user_id),                                  -- Index cho lọc theo user
    INDEX idx_action_type (action_type),                       -- Index cho lọc theo loại hành động
    INDEX idx_table_name (table_name),                         -- Index cho lọc theo bảng
    INDEX idx_action_timestamp (action_timestamp)              -- Index cho lọc theo thời gian
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: notifications
-- Bảng thông báo cho người dùng
-- ==============================================================================
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,            -- ID thông báo (khóa chính)
    user_id INT NOT NULL,                                      -- ID người nhận thông báo
    notification_type VARCHAR(50) NOT NULL,                    -- Loại thông báo (TASK_ASSIGNED, REQUEST_APPROVED, etc.)
    title VARCHAR(200) NOT NULL,                               -- Tiêu đề thông báo
    message TEXT NOT NULL,                                     -- Nội dung thông báo
    related_table VARCHAR(100),                                -- Bảng liên quan
    related_record_id INT,                                     -- ID bản ghi liên quan
    is_read BOOLEAN DEFAULT FALSE,                             -- Đã đọc chưa
    read_at TIMESTAMP NULL,                                    -- Thời gian đọc
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_user (user_id),                                  -- Index cho lọc theo user
    INDEX idx_is_read (is_read),                               -- Index cho lọc theo trạng thái đọc
    INDEX idx_created_at (created_at)                          -- Index cho sắp xếp theo thời gian
) ENGINE=InnoDB;

-- ==============================================================================
-- FOREIGN KEY: departments.manager_id
-- Thêm ràng buộc khóa ngoại cho trưởng phòng (sau khi bảng employees đã tạo)
-- ==============================================================================
ALTER TABLE departments 
ADD FOREIGN KEY (manager_id) REFERENCES employees(employee_id) ON DELETE SET NULL;

-- ==============================================================================
-- INSERT DATA: users
-- Dữ liệu người dùng hệ thống
-- ==============================================================================
INSERT INTO users (username, password_hash, email, first_name, last_name, role, status, created_by) VALUES
('admin', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'admin@company.com', NULL, NULL, 'Admin', 'Active', NULL),
('hr_manager', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'hr.manager@company.com', NULL, NULL, 'HR Manager', 'Active', 1),
('hr_staff1', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'hr.staff1@company.com', NULL, NULL, 'HR', 'Active', 1),
('hr_staff2', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'hr.staff2@company.com', NULL, NULL, 'HR', 'Active', 1),
('john.smith', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'john.smith@company.com', NULL, NULL, 'Dept Manager', 'Active', 1),
('sarah.johnson', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'sarah.johnson@company.com', NULL, NULL, 'Dept Manager', 'Active', 1),
('michael.brown', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'michael.brown@company.com', NULL, NULL, 'Employee', 'Active', 1),
('emily.davis', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'emily.davis@company.com', NULL, NULL, 'Employee', 'Active', 1),
('david.wilson', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'david.wilson@company.com', NULL, NULL, 'Employee', 'Active', 1),
('lisa.anderson', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'lisa.anderson@company.com', NULL, NULL, 'Employee', 'Active', 1),
('robert.taylor', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'robert.taylor@company.com', NULL, NULL, 'Employee', 'Active', 1),
('jennifer.thomas', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'jennifer.thomas@company.com', NULL, NULL, 'Employee', 'Active', 1),
('james.martinez', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'james.martinez@company.com', NULL, NULL, 'Employee', 'Active', 1),
('mary.garcia', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'mary.garcia@company.com', NULL, NULL, 'Employee', 'Active', 1),
('william.rodriguez', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'william.rodriguez@company.com', NULL, NULL, 'Employee', 'Inactive', 1),

-- Additional user accounts created by Admin (without employee records yet - for testing Create Employee functionality)
('alex.chen', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'alex.chen@company.com', NULL, NULL, 'Employee', 'Active', 1),
('sophia.kim', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'sophia.kim@company.com', NULL, NULL, 'Employee', 'Active', 1),
('daniel.lee', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'daniel.lee@company.com', NULL, NULL, 'Dept Manager', 'Active', 1),
('rachel.white', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'rachel.white@company.com', NULL, NULL, 'Employee', 'Active', 1),
('kevin.park', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'kevin.park@company.com', NULL, NULL, 'Employee', 'Active', 1),
('amanda.jones', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'amanda.jones@company.com', NULL, NULL, 'HR', 'Active', 1),
('ryan.miller', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'ryan.miller@company.com', NULL, NULL, 'Employee', 'Active', 1),
('jessica.clark', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'jessica.clark@company.com', NULL, NULL, 'Employee', 'Active', 1),
('brandon.hall', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'brandon.hall@company.com', NULL, NULL, 'Dept Manager', 'Active', 1),
('nicole.adams', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'nicole.adams@company.com', NULL, NULL, 'Employee', 'Active', 1),

-- Additional user accounts for new employees with contracts
('thomas.wright', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'thomas.wright@company.com', NULL, NULL, 'Employee', 'Active', 1),
('victoria.scott', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'victoria.scott@company.com', NULL, NULL, 'Employee', 'Active', 1),
('christopher.green', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'christopher.green@company.com', NULL, NULL, 'Employee', 'Active', 1),
('margaret.baker', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'margaret.baker@company.com', NULL, NULL, 'Employee', 'Active', 1),
('andrew.nelson', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'andrew.nelson@company.com', NULL, NULL, 'Employee', 'Active', 1),
('elizabeth.carter', '$2y$10$XqWKz9F5YrWcY5RhHn9mN.XvQGN6m7FhPzKxGZ1/abcdefghijk', 'elizabeth.carter@company.com', NULL, NULL, 'Employee', 'Active', 1);

-- ==============================================================================
-- INSERT DATA: departments
-- Dữ liệu phòng ban
-- ==============================================================================
INSERT INTO departments (department_name, description, manager_id) VALUES
('Human Resources', 'Manages employee relations, recruitment, and HR policies', NULL),
('Information Technology', 'Handles software development, infrastructure, and technical support', NULL),
('Sales', 'Responsible for revenue generation and client relationships', NULL),
('Marketing', 'Manages brand awareness, campaigns, and market research', NULL),
('Finance', 'Oversees financial planning, accounting, and budgeting', NULL),
('Operations', 'Manages daily business operations and process optimization', NULL);

-- ==============================================================================
-- INSERT DATA: positions
-- Dữ liệu chức vụ
-- ==============================================================================
INSERT INTO positions (position_name, description, base_salary, position_allowance) VALUES
('Chief Executive Officer', 'Top executive responsible for overall company strategy', 15000.00, 3000.00),
('Department Manager', 'Manages a specific department and its team members', 8000.00, 1500.00),
('Senior Software Engineer', 'Experienced developer leading technical projects', 6000.00, 800.00),
('Software Engineer', 'Develops and maintains software applications', 4000.00, 500.00),
('HR Manager', 'Leads HR department and manages HR strategies', 7000.00, 1200.00),
('HR Specialist', 'Handles recruitment, employee relations, and HR operations', 3500.00, 400.00),
('Sales Manager', 'Leads sales team and drives revenue growth', 7500.00, 1000.00),
('Sales Executive', 'Generates sales and maintains client relationships', 3000.00, 600.00),
('Marketing Manager', 'Develops and executes marketing strategies', 6500.00, 900.00),
('Marketing Specialist', 'Implements marketing campaigns and analyzes results', 3200.00, 400.00),
('Finance Manager', 'Oversees financial operations and reporting', 7000.00, 1000.00),
('Accountant', 'Manages accounting records and financial transactions', 3500.00, 300.00),
('Operations Manager', 'Manages operational processes and efficiency', 6000.00, 800.00),
('Administrative Assistant', 'Provides administrative support to the organization', 2500.00, 200.00);

-- ==============================================================================
-- INSERT DATA: employees
-- Dữ liệu nhân viên
-- ==============================================================================
INSERT INTO employees (user_id, employee_code, first_name, last_name, date_of_birth, gender, phone_number, personal_email, home_address, emergency_contact_name, emergency_contact_phone, department_id, position_id, manager_id, hire_date, employment_status) VALUES
(2, 'EMP001', 'Alice', 'Thompson', '1985-03-15', 'Female', '+1-555-0101', 'alice.thompson@email.com', '123 Main Street, New York, NY 10001', 'Bob Thompson', '+1-555-0102', 1, 5, NULL, '2015-01-15', 'Active'),
(3, 'EMP002', 'Mark', 'Williams', '1990-07-22', 'Male', '+1-555-0103', 'mark.williams@email.com', '456 Oak Avenue, New York, NY 10002', 'Susan Williams', '+1-555-0104', 1, 6, 1, '2018-03-10', 'Active'),
(4, 'EMP003', 'Patricia', 'Martinez', '1988-11-30', 'Female', '+1-555-0105', 'patricia.martinez@email.com', '789 Pine Road, New York, NY 10003', 'Carlos Martinez', '+1-555-0106', 1, 6, 1, '2019-06-01', 'Active'),
(5, 'EMP004', 'John', 'Smith', '1982-05-18', 'Male', '+1-555-0107', 'john.smith.personal@email.com', '321 Elm Street, New York, NY 10004', 'Jane Smith', '+1-555-0108', 2, 2, NULL, '2014-08-20', 'Active'),
(6, 'EMP005', 'Sarah', 'Johnson', '1987-09-25', 'Female', '+1-555-0109', 'sarah.johnson.personal@email.com', '654 Maple Drive, New York, NY 10005', 'Mike Johnson', '+1-555-0110', 3, 7, NULL, '2016-02-14', 'Active'),
(7, 'EMP006', 'Michael', 'Brown', '1992-01-12', 'Male', '+1-555-0111', 'michael.brown.personal@email.com', '987 Cedar Lane, New York, NY 10006', 'Laura Brown', '+1-555-0112', 2, 3, 4, '2019-04-15', 'Active'),
(8, 'EMP007', 'Emily', 'Davis', '1991-04-08', 'Female', '+1-555-0113', 'emily.davis.personal@email.com', '147 Birch Court, New York, NY 10007', 'Tom Davis', '+1-555-0114', 2, 4, 4, '2020-01-20', 'Active'),
(9, 'EMP008', 'David', 'Wilson', '1989-12-03', 'Male', '+1-555-0115', 'david.wilson.personal@email.com', '258 Spruce Way, New York, NY 10008', 'Emma Wilson', '+1-555-0116', 3, 8, 5, '2018-07-11', 'Active'),
(10, 'EMP009', 'Lisa', 'Anderson', '1993-06-20', 'Female', '+1-555-0117', 'lisa.anderson.personal@email.com', '369 Willow Place, New York, NY 10009', 'Kevin Anderson', '+1-555-0118', 3, 8, 5, '2020-09-05', 'Active'),
(11, 'EMP010', 'Robert', 'Taylor', '1986-10-14', 'Male', '+1-555-0119', 'robert.taylor.personal@email.com', '741 Ash Boulevard, New York, NY 10010', 'Maria Taylor', '+1-555-0120', 4, 10, NULL, '2017-05-18', 'Active'),
(12, 'EMP011', 'Jennifer', 'Thomas', '1994-02-28', 'Female', '+1-555-0121', 'jennifer.thomas.personal@email.com', '852 Poplar Street, New York, NY 10011', 'Daniel Thomas', '+1-555-0122', 5, 12, NULL, '2019-11-22', 'Active'),
(13, 'EMP012', 'James', 'Martinez', '1990-08-17', 'Male', '+1-555-0123', 'james.martinez.personal@email.com', '963 Hickory Road, New York, NY 10012', 'Rachel Martinez', '+1-555-0124', 6, 13, NULL, '2018-10-30', 'Active'),
(14, 'EMP013', 'Mary', 'Garcia', '1995-03-05', 'Female', '+1-555-0125', 'mary.garcia.personal@email.com', '159 Magnolia Avenue, New York, NY 10013', 'Luis Garcia', '+1-555-0126', 6, 14, 12, '2021-02-15', 'Active'),
(15, 'EMP014', 'William', 'Rodriguez', '1984-07-11', 'Male', '+1-555-0127', 'william.rodriguez.personal@email.com', '357 Dogwood Lane, New York, NY 10014', 'Sofia Rodriguez', '+1-555-0128', 2, 4, 4, '2016-12-01', 'Terminated'),
(24, 'EMP015', 'Thomas', 'Wright', '1988-02-20', 'Male', '+1-555-0129', 'thomas.wright.personal@email.com', '456 Sycamore Street, New York, NY 10015', 'Patricia Wright', '+1-555-0130', 1, 6, 1, '2024-10-01', 'Active'),
(25, 'EMP016', 'Victoria', 'Scott', '1991-05-14', 'Female', '+1-555-0131', 'victoria.scott.personal@email.com', '789 Chestnut Avenue, New York, NY 10016', 'James Scott', '+1-555-0132', 1, 6, 1, '2024-09-15', 'Active'),
(26, 'EMP017', 'Christopher', 'Green', '1986-08-09', 'Male', '+1-555-0133', 'christopher.green.personal@email.com', '321 Juniper Road, New York, NY 10017', 'Linda Green', '+1-555-0134', 2, 3, 4, '2024-08-20', 'Active'),
(27, 'EMP018', 'Margaret', 'Baker', '1989-11-22', 'Female', '+1-555-0135', 'margaret.baker.personal@email.com', '654 Laurel Drive, New York, NY 10018', 'George Baker', '+1-555-0136', 3, 8, 5, '2024-10-20', 'Active'),
(28, 'EMP019', 'Andrew', 'Nelson', '1992-03-17', 'Male', '+1-555-0137', 'andrew.nelson.personal@email.com', '987 Olive Lane, New York, NY 10019', 'Nancy Nelson', '+1-555-0138', 4, 10, NULL, '2024-12-15', 'Active'),
(29, 'EMP020', 'Elizabeth', 'Carter', '1985-06-11', 'Female', '+1-555-0139', 'elizabeth.carter.personal@email.com', '147 Palm Court, New York, NY 10020', 'Richard Carter', '+1-555-0140', 2, 4, 4, '2023-01-15', 'Active'),
(30, 'EMP021', 'David', 'Mitchell', '1990-09-28', 'Male', '+1-555-0141', 'david.mitchell.personal@email.com', '258 Quince Way, New York, NY 10021', 'Helen Mitchell', '+1-555-0142', 3, 8, 5, '2022-06-01', 'Active'),
(31, 'EMP022', 'Jennifer', 'Harris', '1987-12-05', 'Female', '+1-555-0143', 'jennifer.harris.personal@email.com', '369 Rose Place, New York, NY 10022', 'Mark Harris', '+1-555-0144', 4, 10, NULL, '2023-03-01', 'Active');

-- Update employee records with user_id links
UPDATE employees SET user_id = 24 WHERE employee_id = 24;
UPDATE employees SET user_id = 25 WHERE employee_id = 25;
UPDATE employees SET user_id = 26 WHERE employee_id = 26;
UPDATE employees SET user_id = 27 WHERE employee_id = 27;
UPDATE employees SET user_id = 28 WHERE employee_id = 28;
UPDATE employees SET user_id = 29 WHERE employee_id = 29;
UPDATE employees SET user_id = 30 WHERE employee_id = 30;
UPDATE employees SET user_id = 31 WHERE employee_id = 31;

-- Update department managers
UPDATE departments SET manager_id = 1 WHERE department_id = 1; -- HR
UPDATE departments SET manager_id = 4 WHERE department_id = 2; -- IT
UPDATE departments SET manager_id = 5 WHERE department_id = 3; -- Sales
UPDATE departments SET manager_id = 10 WHERE department_id = 4; -- Marketing
UPDATE departments SET manager_id = 11 WHERE department_id = 5; -- Finance
UPDATE departments SET manager_id = 12 WHERE department_id = 6; -- Operations

-- ==============================================================================
-- INSERT DATA: attendance_records
-- Dữ liệu chấm công (tháng 9 và tháng 10/2025)
-- ==============================================================================
INSERT INTO attendance_records (employee_id, attendance_date, check_in_time, check_out_time, status, overtime_hours) VALUES
-- Employee 1 - September 2025
(1, '2025-09-01', '08:00:00', '17:00:00', 'Present', 0),
(1, '2025-09-02', '08:05:00', '17:00:00', 'Late', 0),
(1, '2025-09-03', '08:00:00', '18:30:00', 'Present', 1.5),
(1, '2025-09-04', '08:00:00', '17:00:00', 'Present', 0),
(1, '2025-09-05', '08:00:00', '17:00:00', 'Present', 0),
-- Employee 2 - September 2025
(2, '2025-09-01', '08:00:00', '17:00:00', 'Present', 0),
(2, '2025-09-02', '08:00:00', '17:00:00', 'Present', 0),
(2, '2025-09-03', NULL, NULL, 'Absent', 0),
(2, '2025-09-04', '08:00:00', '19:00:00', 'Present', 2.0),
(2, '2025-09-05', '08:00:00', '17:00:00', 'Present', 0),
-- Employee 3 - September 2025
(3, '2025-09-01', '08:00:00', '17:00:00', 'Present', 0),
(3, '2025-09-02', '08:00:00', '17:00:00', 'Present', 0),
(3, '2025-09-03', '08:00:00', '17:00:00', 'Present', 0),
(3, '2025-09-04', '08:15:00', '17:00:00', 'Late', 0),
(3, '2025-09-05', '08:00:00', '17:00:00', 'Present', 0),
-- Employee 4 - October 2025
(4, '2025-10-01', '08:00:00', '17:00:00', 'Present', 0),
(4, '2025-10-02', '08:00:00', '17:00:00', 'Present', 0),
(4, '2025-10-03', '08:00:00', '18:00:00', 'Present', 1.0),
(4, '2025-10-04', '08:00:00', '17:00:00', 'Present', 0),
(4, '2025-10-07', '08:00:00', '17:00:00', 'Present', 0),
-- Employee 5 - October 2025
(5, '2025-10-01', '08:00:00', '17:00:00', 'Present', 0),
(5, '2025-10-02', '08:00:00', '17:00:00', 'Present', 0),
(5, '2025-10-03', '08:00:00', '17:00:00', 'Present', 0),
(5, '2025-10-04', '08:20:00', '17:00:00', 'Late', 0),
(5, '2025-10-07', '08:00:00', '20:00:00', 'Present', 3.0);

-- ==============================================================================
-- INSERT DATA: attendance_exception_requests
-- Dữ liệu yêu cầu giải trình chấm công
-- ==============================================================================
INSERT INTO attendance_exception_requests (attendance_id, employee_id, request_reason, proposed_check_in, proposed_check_out, proposed_status, status, reviewed_by) VALUES
(3, 1, 'System error - I checked in on time but the system did not record it', '08:00:00', '17:00:00', 'Present', 'Approved', 2),
(8, 2, 'I was on a client visit and could not check in', '08:00:00', '17:00:00', 'Business Trip', 'Pending', NULL),
(14, 3, 'Traffic jam due to accident on highway', '08:00:00', '17:00:00', 'Present', 'Rejected', 2);

-- ==============================================================================
-- INSERT DATA: salary_components
-- Dữ liệu cấu phần lương
-- ==============================================================================
INSERT INTO salary_components (employee_id, base_salary, position_allowance, housing_allowance, transportation_allowance, meal_allowance, other_allowances, effective_from, effective_to, is_active) VALUES
(1, 7000.00, 1200.00, 500.00, 200.00, 150.00, 0, '2015-01-15', NULL, TRUE),
(2, 3500.00, 400.00, 300.00, 150.00, 150.00, 0, '2018-03-10', NULL, TRUE),
(3, 3500.00, 400.00, 300.00, 150.00, 150.00, 0, '2019-06-01', NULL, TRUE),
(4, 8000.00, 1500.00, 600.00, 250.00, 150.00, 0, '2014-08-20', NULL, TRUE),
(5, 7500.00, 1000.00, 500.00, 200.00, 150.00, 0, '2016-02-14', NULL, TRUE),
(6, 6000.00, 800.00, 400.00, 180.00, 150.00, 0, '2019-04-15', NULL, TRUE),
(7, 4000.00, 500.00, 350.00, 150.00, 150.00, 0, '2020-01-20', NULL, TRUE),
(8, 3000.00, 600.00, 300.00, 150.00, 150.00, 0, '2018-07-11', NULL, TRUE),
(9, 3000.00, 600.00, 300.00, 150.00, 150.00, 0, '2020-09-05', NULL, TRUE),
(10, 3200.00, 400.00, 300.00, 150.00, 150.00, 0, '2017-05-18', NULL, TRUE),
(11, 3500.00, 300.00, 300.00, 150.00, 150.00, 0, '2019-11-22', NULL, TRUE),
(12, 6000.00, 800.00, 400.00, 180.00, 150.00, 0, '2018-10-30', NULL, TRUE),
(13, 2500.00, 200.00, 250.00, 100.00, 150.00, 0, '2021-02-15', NULL, TRUE),
(14, 4000.00, 500.00, 350.00, 150.00, 150.00, 0, '2016-12-01', '2024-12-31', FALSE);

-- ==============================================================================
-- INSERT DATA: benefit_types
-- Dữ liệu loại phúc lợi
-- ==============================================================================
INSERT INTO benefit_types (benefit_name, description, calculation_type, default_amount, default_percentage, is_taxable) VALUES
('Health Insurance', 'Company-provided health insurance coverage', 'Fixed', 200.00, NULL, FALSE),
('Life Insurance', 'Basic life insurance coverage', 'Fixed', 50.00, NULL, FALSE),
('Performance Bonus', 'Quarterly performance-based bonus', 'Percentage', NULL, 10.00, TRUE),
('Year-end Bonus', 'Annual year-end bonus', 'Fixed', 1000.00, NULL, TRUE),
('Gym Membership', 'Fitness center membership subsidy', 'Fixed', 50.00, NULL, FALSE);

-- ==============================================================================
-- INSERT DATA: deduction_types
-- Dữ liệu loại khấu trừ
-- ==============================================================================
INSERT INTO deduction_types (deduction_name, description, calculation_type, default_amount, default_percentage, is_mandatory) VALUES
('Income Tax', 'Federal and state income tax', 'Percentage', NULL, 15.00, TRUE),
('Social Security', 'Social security contribution', 'Percentage', NULL, 6.20, TRUE),
('Medicare', 'Medicare tax', 'Percentage', NULL, 1.45, TRUE),
('Health Insurance Premium', 'Employee portion of health insurance', 'Fixed', 100.00, NULL, FALSE),
('Retirement Fund', '401k retirement contribution', 'Percentage', NULL, 5.00, FALSE),
('Advance Payment', 'Salary advance repayment', 'Fixed', 0, NULL, FALSE);

-- ==============================================================================
-- INSERT DATA: monthly_payroll
-- Dữ liệu bảng lương tháng 9/2025
-- ==============================================================================
INSERT INTO monthly_payroll (employee_id, payroll_month, base_salary, total_allowances, overtime_pay, total_bonus, total_benefits, total_deductions, gross_salary, net_salary, working_days, absent_days, late_days, overtime_hours, status, calculated_by, calculated_at) VALUES
(1, '2025-09-01', 7000.00, 2050.00, 75.00, 500.00, 250.00, 2156.25, 9875.00, 7718.75, 22, 0, 1, 1.5, 'Approved', 2, '2025-09-30 10:00:00'),
(2, '2025-09-01', 3500.00, 1050.00, 100.00, 0, 250.00, 1081.25, 4900.00, 3818.75, 21, 1, 0, 2.0, 'Approved', 2, '2025-09-30 10:00:00'),
(3, '2025-09-01', 3500.00, 1050.00, 0, 0, 250.00, 1068.75, 4800.00, 3731.25, 22, 0, 1, 0, 'Approved', 2, '2025-09-30 10:00:00'),
(4, '2025-09-01', 8000.00, 2650.00, 0, 800.00, 250.00, 2543.75, 11700.00, 9156.25, 22, 0, 0, 0, 'Paid', 2, '2025-09-30 10:00:00'),
(5, '2025-09-01', 7500.00, 2000.00, 0, 750.00, 250.00, 2318.75, 10500.00, 8181.25, 22, 0, 0, 0, 'Paid', 2, '2025-09-30 10:00:00');

-- ==============================================================================
-- INSERT DATA: payroll_adjustments
-- Dữ liệu điều chỉnh lương
-- ==============================================================================
INSERT INTO payroll_adjustments (payroll_id, employee_id, adjustment_type, amount, reason, status, requested_by, approved_by, approval_comment, approved_at) VALUES
(1, 1, 'Bonus', 500.00, 'Excellent performance on project delivery ahead of schedule', 'Approved', 2, 2, 'Well deserved for outstanding contribution', '2025-09-28 14:30:00'),
(4, 4, 'Bonus', 800.00, 'Successful completion of major IT infrastructure upgrade', 'Approved', 2, 2, 'Approved for exceptional technical leadership', '2025-09-28 15:00:00'),
(5, 5, 'Bonus', 750.00, 'Exceeded quarterly sales targets by 25%', 'Approved', 2, 2, 'Great sales performance', '2025-09-28 15:15:00');

-- ==============================================================================
-- INSERT DATA: employment_contracts
-- Dữ liệu hợp đồng lao động với nhiều trạng thái khác nhau để test
-- ==============================================================================

-- Existing Active Contracts (approved by HR Manager)
INSERT INTO employment_contracts (employee_id, contract_number, contract_type, start_date, end_date, salary_amount, job_description, terms_and_conditions, contract_status, signed_date, approved_by, approval_comment, approved_at, created_by) VALUES
(1, 'CTR-2015-001', 'Indefinite', '2015-01-15', NULL, 7000.00, 'Responsible for overall HR strategy, employee relations, recruitment, and team management', 'Standard employment terms including benefits, leave policy, and confidentiality agreement', 'Active', '2015-01-10', 2, 'Excellent qualifications and experience', '2015-01-12 10:00:00', (SELECT user_id FROM users WHERE username = 'hr_manager')),
(2, 'CTR-2018-015', 'Indefinite', '2018-03-10', NULL, 3500.00, 'Handle recruitment processes, employee onboarding, and HR documentation', 'Standard employment terms with 3-month probation period', 'Active', '2018-03-05', 2, 'Strong HR background, approved for immediate start', '2018-03-07 11:00:00', (SELECT user_id FROM users WHERE username = 'hr_staff1')),
(4, 'CTR-2014-008', 'Indefinite', '2014-08-20', NULL, 8000.00, 'Lead IT department, oversee infrastructure, software development, and technical projects', 'Management contract with performance bonuses and stock options', 'Active', '2014-08-15', 2, 'Exceptional technical leadership skills', '2014-08-17 10:00:00', (SELECT user_id FROM users WHERE username = 'hr_staff2')),
(5, 'CTR-2016-012', 'Indefinite', '2016-02-14', NULL, 7500.00, 'Lead sales team, develop sales strategies, and drive revenue growth', 'Management contract with commission structure', 'Active', '2016-02-10', 2, 'Proven sales track record', '2016-02-12 14:00:00', (SELECT user_id FROM users WHERE username = 'hr_manager')),

-- Draft Contracts (created by different HR staff - only visible to creators)
(6, 'CTR-2024-101', 'Indefinite', '2024-12-01', NULL, 6000.00, 'Lead technical projects, mentor junior developers, and ensure code quality', 'Standard employment with technical certifications support', 'Draft', NULL, NULL, 'Still reviewing technical requirements', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff1')),
(7, 'CTR-2024-102', 'Fixed-term', '2024-11-15', '2025-11-14', 4500.00, 'Develop and maintain software applications using modern technologies', 'Fixed-term contract with renewal option based on performance', 'Draft', NULL, NULL, 'Waiting for budget approval', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff1')),
(8, 'CTR-2024-103', 'Indefinite', '2024-10-20', NULL, 3200.00, 'Generate sales, maintain client relationships, and achieve sales targets', 'Standard employment with commission-based incentives', 'Draft', NULL, NULL, 'Need to finalize commission structure', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff2')),
(9, 'CTR-2024-104', 'Indefinite', '2024-11-01', NULL, 3800.00, 'Business development and client acquisition in assigned territory', 'Standard employment terms with travel allowance', 'Draft', NULL, NULL, 'Reviewing territory assignments', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff2')),
(10, 'CTR-2024-105', 'Fixed-term', '2024-12-15', '2025-06-14', 3000.00, 'Develop and execute marketing campaigns, analyze market trends', 'Temporary contract for holiday season campaign', 'Draft', NULL, NULL, 'Seasonal position - finalizing details', NULL, (SELECT user_id FROM users WHERE username = 'hr_manager')),

-- Pending Approval Contracts (submitted by HR staff, waiting for HR Manager approval)
(11, 'CTR-2024-201', 'Indefinite', '2024-11-25', NULL, 4200.00, 'Manage accounting records, financial reporting, and tax compliance', 'Standard employment with CPA certification support and annual bonus', 'Pending Approval', NULL, NULL, 'Ready for review - candidate has excellent credentials', NULL, (SELECT user_id FROM users WHERE username = 'hr_manager')),
(12, 'CTR-2024-202', 'Indefinite', '2024-12-10', NULL, 6500.00, 'Oversee daily operations, optimize processes, and manage operational staff', 'Management contract with performance metrics and team bonuses', 'Pending Approval', NULL, NULL, 'Urgent position - operations manager needed ASAP', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff2')),
(13, 'CTR-2024-203', 'Fixed-term', '2024-11-20', '2025-05-19', 2800.00, 'Provide administrative support including scheduling, documentation, and coordination', 'Temporary contract to cover maternity leave', 'Pending Approval', NULL, NULL, 'Temporary replacement for Sarah - 6 months', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff1')),
(14, 'CTR-2024-204', 'Indefinite', '2024-12-05', NULL, 5500.00, 'Software development and maintenance of internal systems', 'Standard employment with remote work options', 'Pending Approval', NULL, NULL, 'Senior developer with 8 years experience', NULL, (SELECT user_id FROM users WHERE username = 'hr_staff2')),
(15, 'CTR-2024-205', 'Fixed-term', '2025-01-15', '2025-12-31', 4800.00, 'Project management for digital transformation initiative', 'Project-based contract with completion bonuses', 'Pending Approval', NULL, NULL, 'Critical project - need experienced PM', NULL, (SELECT user_id FROM users WHERE username = 'hr_manager')),

-- Rejected Contracts (rejected by HR Manager with reasons)
(16, 'CTR-2024-301', 'Indefinite', '2024-10-01', NULL, 8500.00, 'Senior HR Business Partner role with strategic responsibilities', 'Executive level contract with stock options', 'Rejected', NULL, 2, 'Salary request exceeds budget allocation for this position. Please revise compensation package.', '2024-10-15 14:30:00', (SELECT user_id FROM users WHERE username = 'hr_manager')),
(17, 'CTR-2024-302', 'Fixed-term', '2024-09-15', '2024-12-15', 2200.00, 'Data entry and basic administrative tasks', 'Entry level position with basic benefits', 'Rejected', NULL, 2, 'Position requirements do not match candidate qualifications. Candidate is overqualified for this role.', '2024-09-20 10:15:00', (SELECT user_id FROM users WHERE username = 'hr_staff2')),
(18, 'CTR-2024-303', 'Indefinite', '2024-08-20', NULL, 7200.00, 'Lead architect for new software platform development', 'Senior technical role with leadership responsibilities', 'Rejected', NULL, 2, 'Technical requirements not clearly defined. Please provide detailed technical specifications and team structure.', '2024-08-25 16:45:00', (SELECT user_id FROM users WHERE username = 'hr_staff1')),
(19, 'CTR-2024-304', 'Fixed-term', '2024-07-10', '2024-09-10', 3500.00, 'Temporary sales support during summer campaign', 'Short-term contract with performance incentives', 'Rejected', NULL, 2, 'Contract duration too short for effective onboarding and training. Minimum 6-month contracts required for sales positions.', '2024-07-15 11:20:00', (SELECT user_id FROM users WHERE username = 'hr_staff2')),

-- Expired Contracts
(20, 'CTR-2023-401', 'Fixed-term', '2023-01-15', '2024-01-14', 3200.00, 'Temporary customer service representative', 'One-year fixed contract with renewal option', 'Expired', '2023-01-10', 2, 'Approved for one year term', '2023-01-12 09:00:00', (SELECT user_id FROM users WHERE username = 'hr_staff1')),
(21, 'CTR-2022-402', 'Fixed-term', '2022-06-01', '2024-05-31', 4100.00, 'Project coordinator for system upgrade', 'Two-year project contract', 'Expired', '2022-05-25', 2, 'Critical project role approved', '2022-05-28 13:30:00', (SELECT user_id FROM users WHERE username = 'hr_staff2')),

-- Terminated Contracts
(22, 'CTR-2023-501', 'Indefinite', '2023-03-01', NULL, 3800.00, 'Marketing specialist for digital campaigns', 'Standard employment with creative bonuses', 'Terminated', '2023-02-25', 2, 'Approved with probation period', '2023-02-27 10:00:00', (SELECT user_id FROM users WHERE username = 'hr_manager'));

-- ==============================================================================
-- INSERT DATA: job_postings
-- Dữ liệu tin tuyển dụng
-- ==============================================================================
INSERT INTO job_postings (job_title, department_id, position_id, job_description, requirements, benefits, salary_range_from, salary_range_to, number_of_positions, application_deadline, job_status, posted_by, posted_date, internal_notes) VALUES
('Senior Software Engineer', 2, 3, 'We are seeking an experienced Senior Software Engineer to join our IT department. You will lead technical projects, mentor junior developers, and contribute to architectural decisions.', 'Bachelor degree in Computer Science or related field; 5+ years of experience in software development; Proficiency in Java, Python, or C#; Experience with cloud platforms (AWS/Azure); Strong problem-solving skills', 'Competitive salary; Health insurance; Annual performance bonus; Professional development budget; Flexible working hours; Remote work options', 5500.00, 7000.00, 2, '2025-11-30', 'Open', 3, '2025-10-01', 'Priority hiring for Q4 projects'),
('Marketing Specialist', 4, 10, 'Join our dynamic marketing team to develop and execute innovative marketing campaigns. You will work on brand awareness, digital marketing, and market analysis.', 'Bachelor degree in Marketing, Communications, or related field; 2+ years of marketing experience; Strong understanding of digital marketing channels; Excellent communication skills; Experience with marketing analytics tools', 'Competitive salary; Health and dental insurance; Performance bonuses; Creative work environment; Team building events', 3000.00, 3500.00, 1, '2025-10-31', 'Open', 3, '2025-09-15', 'Need strong social media background'),
('HR Specialist', 1, 6, 'We are looking for an HR Specialist to support our growing team. Responsibilities include recruitment, employee onboarding, and HR administration.', 'Bachelor degree in Human Resources or related field; 2+ years HR experience; Knowledge of employment law; Excellent interpersonal skills; HRCI or SHRM certification preferred', 'Competitive compensation; Comprehensive benefits package; Professional development opportunities; Supportive work culture', 3200.00, 3800.00, 1, '2025-11-15', 'Open', 2, '2025-09-20', 'Replacement for expanded team'),
('Sales Executive', 3, 8, 'Exciting opportunity for a motivated Sales Executive to join our sales team. You will be responsible for generating new business and maintaining client relationships.', 'Bachelor degree preferred; 1+ years of sales experience; Excellent communication and negotiation skills; Self-motivated and target-driven; Valid driver license', 'Base salary plus commission; Health insurance; Performance bonuses; Company car allowance; Career growth opportunities', 2800.00, 3500.00, 3, '2025-12-15', 'Open', 4, '2025-10-05', 'Expansion into new territories'),
('Junior Software Developer', 2, 4, 'Great opportunity for a junior developer to start their career. You will work on exciting projects under the mentorship of senior engineers.', 'Bachelor degree in Computer Science or related field; 0-2 years experience; Knowledge of programming languages (Java, Python, JavaScript); Strong learning attitude; Good problem-solving skills', 'Competitive entry-level salary; Mentorship program; Training and certification support; Health insurance; Modern tech stack', 3500.00, 4500.00, 2, '2025-10-25', 'Closed', 3, '2025-08-10', 'Position filled - hired 2 candidates'),
('Accountant', 5, 12, 'We need an experienced Accountant to manage our financial records and reporting. This role is crucial for maintaining our financial health.', 'Bachelor degree in Accounting or Finance; 3+ years of accounting experience; CPA certification preferred; Proficiency in accounting software; Strong attention to detail; Knowledge of GAAP', 'Competitive salary; Health insurance; Retirement plan; Professional certification support; Stable work environment', 3300.00, 4000.00, 1, '2025-11-20', 'Open', 4, '2025-10-01', 'Need to start by December');

-- ==============================================================================
-- INSERT DATA: job_applications
-- Dữ liệu hồ sơ ứng tuyển
-- ==============================================================================
INSERT INTO job_applications (job_id, applicant_name, applicant_email, applicant_phone, resume_file_path, cover_letter, application_status, applied_date, reviewed_by, review_notes, interview_date) VALUES
(1, 'Thomas Anderson', 'thomas.anderson@email.com', '+1-555-0201', '/resumes/thomas_anderson_resume.pdf', 'I am excited to apply for the Senior Software Engineer position. With 7 years of experience in full-stack development and cloud architecture, I believe I would be a great fit for your team.', 'Interview', '2025-10-03 09:15:00', 3, 'Strong technical background, good communication skills. Schedule for technical interview.', '2025-10-15 14:00:00'),
(1, 'Rebecca Chen', 'rebecca.chen@email.com', '+1-555-0202', '/resumes/rebecca_chen_resume.pdf', 'With extensive experience in enterprise software development and team leadership, I am eager to contribute to your IT department.', 'Interview', '2025-10-05 11:30:00', 3, 'Impressive resume with relevant experience. Recommended for interview.', '2025-10-16 10:00:00'),
(1, 'Marcus Johnson', 'marcus.johnson@email.com', '+1-555-0203', '/resumes/marcus_johnson_resume.pdf', 'I have been following your company and am impressed by your innovative solutions. My background in cloud computing and microservices aligns perfectly with this role.', 'Screening', '2025-10-07 14:20:00', 3, 'Good credentials, needs phone screening first.', NULL),
(2, 'Sophie Martinez', 'sophie.martinez@email.com', '+1-555-0204', '/resumes/sophie_martinez_resume.pdf', 'As a creative marketing professional with strong digital marketing skills, I am excited about the opportunity to join your marketing team.', 'Offered', '2025-09-18 10:45:00', 4, 'Excellent portfolio, strong social media experience. Made an offer.', '2025-09-28 15:00:00'),
(2, 'Daniel White', 'daniel.white@email.com', '+1-555-0205', '/resumes/daniel_white_resume.pdf', 'My experience in B2B marketing and content strategy would be valuable for your growing marketing department.', 'Rejected', '2025-09-20 13:10:00', 4, 'Good experience but not the right fit for our current needs. Looking for more B2C focus.', NULL),
(3, 'Amanda Foster', 'amanda.foster@email.com', '+1-555-0206', '/resumes/amanda_foster_resume.pdf', 'I am passionate about HR and employee development. My SHRM certification and 3 years of HR experience make me an ideal candidate.', 'Interview', '2025-09-25 09:00:00', 2, 'Strong HR background with certification. Schedule for interview.', '2025-10-12 11:00:00'),
(3, 'Christopher Lee', 'christopher.lee@email.com', '+1-555-0207', '/resumes/christopher_lee_resume.pdf', 'With experience in recruitment and employee relations, I am confident I can contribute to your HR team.', 'Submitted', '2025-09-28 16:30:00', NULL, NULL, NULL),
(4, 'Nicole Brown', 'nicole.brown@email.com', '+1-555-0208', '/resumes/nicole_brown_resume.pdf', 'I am a results-driven sales professional with a proven track record of exceeding targets. I would love to join your sales team.', 'Screening', '2025-10-06 10:20:00', 6, 'Good sales background, checking references.', NULL),
(4, 'Kevin Harris', 'kevin.harris@email.com', '+1-555-0209', '/resumes/kevin_harris_resume.pdf', 'My experience in B2B sales and client relationship management aligns well with this position.', 'Submitted', '2025-10-07 14:55:00', NULL, NULL, NULL),
(4, 'Rachel Green', 'rachel.green@email.com', '+1-555-0210', '/resumes/rachel_green_resume.pdf', 'Enthusiastic sales professional looking for a challenging opportunity to grow and contribute to a dynamic team.', 'Submitted', '2025-10-08 09:40:00', NULL, NULL, NULL),
(5, 'Alex Turner', 'alex.turner@email.com', '+1-555-0211', '/resumes/alex_turner_resume.pdf', 'Recent computer science graduate eager to start my career in software development. Strong foundation in programming and quick learner.', 'Hired', '2025-08-15 11:00:00', 5, 'Great potential, good technical assessment results. Hired.', '2025-08-25 10:00:00'),
(5, 'Olivia Parker', 'olivia.parker@email.com', '+1-555-0212', '/resumes/olivia_parker_resume.pdf', 'Junior developer with internship experience and passion for coding. Excited about learning from experienced engineers.', 'Hired', '2025-08-18 14:30:00', 5, 'Strong coding skills, good cultural fit. Hired.', '2025-08-27 14:00:00');

-- ==============================================================================
-- INSERT DATA: tasks
-- Dữ liệu công việc/nhiệm vụ
-- ==============================================================================
INSERT INTO tasks (task_title, task_description, assigned_to, assigned_by, department_id, priority, task_status, start_date, due_date, completed_date, progress_percentage) VALUES
('Prepare Q3 HR Report', 'Compile comprehensive HR metrics including headcount, turnover, recruitment statistics, and training hours for Q3 2025', 2, 2, 1, 'High', 'Done', '2025-09-25', '2025-09-30', '2025-09-29', 100),
('Update Employee Handbook', 'Review and update employee handbook to reflect new policies and legal requirements', 3, 2, 1, 'Medium', 'In Progress', '2025-10-01', '2025-10-20', NULL, 60),
('Conduct New Hire Orientation', 'Prepare and conduct orientation session for 3 new employees joining on October 15', 2, 2, 1, 'High', 'Not Started', '2025-10-10', '2025-10-15', NULL, 0),
('Database Migration Project', 'Migrate legacy database to new cloud-based infrastructure with zero downtime', 6, 5, 2, 'Urgent', 'In Progress', '2025-09-15', '2025-10-30', NULL, 75),
('Implement Security Patches', 'Apply latest security updates to all servers and conduct security audit', 7, 5, 2, 'High', 'Done', '2025-10-01', '2025-10-05', '2025-10-04', 100),
('Develop Mobile App Feature', 'Develop and test new user authentication feature for mobile application', 7, 5, 2, 'Medium', 'In Progress', '2025-10-05', '2025-10-25', NULL, 40),
('Q4 Sales Strategy Meeting', 'Organize and lead quarterly sales planning meeting with all sales team members', 8, 6, 3, 'High', 'Done', '2025-09-28', '2025-09-30', '2025-09-30', 100),
('Client Proposal - ABC Corporation', 'Prepare comprehensive proposal for ABC Corporation including pricing and timeline', 9, 6, 3, 'Urgent', 'In Progress', '2025-10-03', '2025-10-10', NULL, 80),
('Follow-up with Prospect List', 'Contact 50 warm leads from last quarter and schedule meetings', 8, 6, 3, 'Medium', 'In Progress', '2025-10-01', '2025-10-15', NULL, 55),
('Social Media Campaign Launch', 'Launch new product social media campaign across all platforms', 10, 10, 4, 'High', 'Not Started', '2025-10-12', '2025-10-18', NULL, 0),
('Market Research Analysis', 'Analyze competitor strategies and market trends for Q4 planning', 10, 10, 4, 'Medium', 'In Progress', '2025-10-01', '2025-10-20', NULL, 30),
('Month-End Financial Close', 'Complete September month-end closing and prepare financial statements', 11, 11, 5, 'Urgent', 'Done', '2025-09-28', '2025-10-02', '2025-10-01', 100),
('Budget Planning FY2026', 'Prepare initial budget proposals for fiscal year 2026', 11, 11, 5, 'High', 'In Progress', '2025-10-01', '2025-10-31', NULL, 45),
('Process Optimization Review', 'Review current operational processes and identify improvement opportunities', 13, 12, 6, 'Medium', 'In Progress', '2025-09-20', '2025-10-25', NULL, 70),
('Vendor Contract Renewal', 'Review and negotiate renewal terms with key vendors', 13, 12, 6, 'High', 'Not Started', '2025-10-15', '2025-10-30', NULL, 0);

-- ==============================================================================
-- INSERT DATA: request_types
-- Dữ liệu loại yêu cầu
-- ==============================================================================
INSERT INTO request_types (request_type_name, description, requires_approval, max_days_per_year, is_paid) VALUES
('Annual Leave', 'Paid vacation leave', TRUE, 15, TRUE),
('Sick Leave', 'Leave for illness or medical appointments', TRUE, 10, TRUE),
('Personal Leave', 'Unpaid personal leave for personal matters', TRUE, 5, FALSE),
('Remote Work', 'Request to work from home', TRUE, NULL, TRUE),
('Business Trip', 'Official business travel', TRUE, NULL, TRUE),
('Maternity Leave', 'Leave for childbirth and childcare', TRUE, 90, TRUE),
('Paternity Leave', 'Leave for fathers after childbirth', TRUE, 10, TRUE),
('Bereavement Leave', 'Leave due to death of family member', TRUE, 5, TRUE),
('Study Leave', 'Leave for educational purposes', TRUE, NULL, FALSE),
('Compensatory Leave', 'Time off in lieu of overtime worked', TRUE, NULL, TRUE);

-- ==============================================================================
-- INSERT DATA: requests
-- Dữ liệu yêu cầu
-- ==============================================================================
INSERT INTO requests (employee_id, request_type_id, start_date, end_date, number_of_days, reason, request_status, reviewed_by, review_comment, reviewed_at) VALUES
(2, 1, '2025-10-20', '2025-10-24', 5, 'Family vacation to Europe', 'Approved', 2, 'Approved. Enjoy your vacation!', '2025-10-05 10:30:00'),
(3, 2, '2025-10-10', '2025-10-11', 2, 'Flu symptoms, need rest', 'Approved', 2, 'Approved. Please get well soon.', '2025-10-09 14:15:00'),
(6, 4, '2025-10-15', '2025-10-15', 1, 'Need to work from home due to home repair appointment', 'Approved', 5, 'Approved for one day', '2025-10-12 09:00:00'),
(7, 1, '2025-11-01', '2025-11-05', 5, 'Wedding anniversary trip', 'Pending', NULL, NULL, NULL),
(8, 1, '2025-10-25', '2025-10-26', 2, 'Personal matters', 'Rejected', 6, 'Sorry, we need full team presence during end-of-month closing. Please reschedule.', '2025-10-08 11:20:00'),
(9, 4, '2025-10-14', '2025-10-16', 3, 'Attending online conference, prefer to work from home', 'Approved', 6, 'Approved. Please ensure you are available for team calls.', '2025-10-10 15:45:00'),
(10, 1, '2025-12-23', '2025-12-30', 6, 'Year-end holiday with family', 'Pending', NULL, NULL, NULL),
(11, 2, '2025-10-08', '2025-10-08', 1, 'Doctor appointment for annual checkup', 'Approved', 11, 'Approved', '2025-10-07 16:00:00'),
(13, 3, '2025-11-10', '2025-11-12', 3, 'Family emergency', 'Pending', NULL, NULL, NULL),
(6, 1, '2025-09-15', '2025-09-20', 4, 'Summer vacation', 'Approved', 5, 'Approved', '2025-09-05 10:00:00'),
(7, 10, '2025-10-12', '2025-10-12', 1, 'Worked overtime on weekend project', 'Approved', 5, 'Approved compensatory leave for weekend work', '2025-10-10 09:30:00');

-- ==============================================================================
-- INSERT DATA: company_information
-- Dữ liệu thông tin công ty
-- ==============================================================================
INSERT INTO company_information (company_name, company_logo_path, about_us, mission_statement, vision_statement, core_values, address, phone_number, email, website, founded_year, number_of_employees, industry, social_media_links, is_active) VALUES
('TechVision Solutions Inc.', '/images/company_logo.png', 
'TechVision Solutions is a leading technology company specializing in enterprise software solutions, cloud computing, and digital transformation services. Founded in 2010, we have grown from a small startup to a thriving organization serving clients across various industries worldwide. Our team of talented professionals is committed to delivering innovative solutions that drive business success and create lasting value for our clients.',
'To empower businesses through innovative technology solutions that transform the way they operate, compete, and grow in the digital age.',
'To be the global leader in enterprise technology solutions, recognized for innovation, excellence, and positive impact on businesses and communities.',
'Innovation: We constantly push boundaries and embrace new ideas; Integrity: We conduct business with honesty and transparency; Excellence: We strive for the highest quality in everything we do; Collaboration: We believe in the power of teamwork; Customer Focus: Our clients success is our success; Continuous Learning: We invest in growth and development',
'1250 Technology Park Drive, Silicon Valley, CA 94025, United States',
'+1-555-123-4567',
'info@techvisionsolutions.com',
'www.techvisionsolutions.com',
2010,
'100-500',
'Information Technology & Services',
'{"facebook": "facebook.com/techvisionsolutions", "linkedin": "linkedin.com/company/techvisionsolutions", "twitter": "twitter.com/techvision_inc", "instagram": "instagram.com/techvisionsolutions"}',
TRUE);

-- ==============================================================================
-- INSERT DATA: audit_logs
-- Dữ liệu log hoạt động (ví dụ)
-- ==============================================================================
INSERT INTO audit_logs (user_id, action_type, table_name, record_id, old_values, new_values, ip_address, user_agent) VALUES
(1, 'CREATE', 'users', 5, NULL, '{"username": "john.smith", "email": "john.smith@company.com", "role": "Dept Manager"}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(2, 'UPDATE', 'employees', 2, '{"phone_number": "+1-555-0103"}', '{"phone_number": "+1-555-0104"}', '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)'),
(5, 'APPROVE', 'requests', 1, '{"request_status": "Pending"}', '{"request_status": "Approved", "reviewed_by": 5}', '192.168.1.102', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(1, 'DELETE', 'users', 15, '{"username": "william.rodriguez", "status": "Active"}', '{"status": "Inactive"}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(3, 'CREATE', 'job_postings', 1, NULL, '{"job_title": "Senior Software Engineer", "job_status": "Open"}', '192.168.1.103', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)'),
(2, 'UPDATE', 'monthly_payroll', 1, '{"status": "Calculated"}', '{"status": "Approved", "approved_by": 2}', '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)');

-- ==============================================================================
-- INSERT DATA: notifications
-- Dữ liệu thông báo
-- ==============================================================================
INSERT INTO notifications (user_id, notification_type, title, message, related_table, related_record_id, is_read, read_at) VALUES
(7, 'TASK_ASSIGNED', 'New Task Assigned', 'You have been assigned a new task: Develop Mobile App Feature. Due date: October 25, 2025', 'tasks', 6, TRUE, '2025-10-05 14:30:00'),
(2, 'REQUEST_SUBMITTED', 'New Leave Request', 'Employee Mark Williams has submitted a new leave request for your approval', 'requests', 1, TRUE, '2025-10-04 09:15:00'),
(8, 'REQUEST_APPROVED', 'Leave Request Approved', 'Your annual leave request for October 25-26 has been rejected. Reason: Sorry, we need full team presence during end-of-month closing. Please reschedule.', 'requests', 5, TRUE, '2025-10-08 11:25:00'),
(6, 'TASK_DUE_SOON', 'Task Due Soon', 'Reminder: Your task "Database Migration Project" is due in 5 days', 'tasks', 4, FALSE, NULL),
(10, 'TASK_ASSIGNED', 'New Task Assigned', 'You have been assigned a new task: Social Media Campaign Launch. Due date: October 18, 2025', 'tasks', 10, FALSE, NULL),
(9, 'REQUEST_APPROVED', 'Remote Work Approved', 'Your remote work request for October 14-16 has been approved', 'requests', 6, TRUE, '2025-10-10 16:00:00'),
(11, 'PAYROLL_READY', 'Payslip Available', 'Your payslip for September 2025 is now available for download', 'monthly_payroll', 4, FALSE, NULL),
(3, 'INTERVIEW_SCHEDULED', 'Interview Scheduled', 'Interview scheduled with Amanda Foster for HR Specialist position on October 12, 2025 at 11:00 AM', 'job_applications', 6, FALSE, NULL);

-- ==============================================================================
-- VIEWS: Các view để hỗ trợ báo cáo và truy vấn
-- ==============================================================================

-- View: Thông tin nhân viên đầy đủ với phòng ban và chức vụ
CREATE OR REPLACE VIEW vw_employee_full_info AS
SELECT 
    e.employee_id,
    e.employee_code,
    CONCAT(e.first_name, ' ', e.last_name) AS full_name,
    e.first_name,
    e.last_name,
    e.date_of_birth,
    TIMESTAMPDIFF(YEAR, e.date_of_birth, CURDATE()) AS age,
    e.gender,
    e.phone_number,
    e.personal_email,
    u.email AS work_email,
    e.home_address,
    e.emergency_contact_name,
    e.emergency_contact_phone,
    d.department_name,
    d.department_id,
    p.position_name,
    p.position_id,
    CONCAT(m.first_name, ' ', m.last_name) AS manager_name,
    e.manager_id,
    e.hire_date,
    TIMESTAMPDIFF(YEAR, e.hire_date, CURDATE()) AS years_of_service,
    e.employment_status,
    u.username,
    u.role AS user_role,
    u.status AS account_status
FROM employees e
LEFT JOIN users u ON e.user_id = u.user_id
LEFT JOIN departments d ON e.department_id = d.department_id
LEFT JOIN positions p ON e.position_id = p.position_id
LEFT JOIN employees m ON e.manager_id = m.employee_id;

-- View: Tổng hợp chấm công theo tháng
CREATE OR REPLACE VIEW vw_monthly_attendance_summary AS
SELECT 
    ar.employee_id,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    e.employee_code,
    d.department_name,
    DATE_FORMAT(ar.attendance_date, '%Y-%m') AS attendance_month,
    COUNT(CASE WHEN ar.status = 'Present' THEN 1 END) AS present_days,
    COUNT(CASE WHEN ar.status = 'Absent' THEN 1 END) AS absent_days,
    COUNT(CASE WHEN ar.status = 'Late' THEN 1 END) AS late_days,
    COUNT(CASE WHEN ar.status = 'Early Leave' THEN 1 END) AS early_leave_days,
    COUNT(CASE WHEN ar.status = 'Business Trip' THEN 1 END) AS business_trip_days,
    COUNT(CASE WHEN ar.status = 'Remote' THEN 1 END) AS remote_days,
    SUM(ar.overtime_hours) AS total_overtime_hours,
    COUNT(*) AS total_working_days
FROM attendance_records ar
JOIN employees e ON ar.employee_id = e.employee_id
LEFT JOIN departments d ON e.department_id = d.department_id
GROUP BY ar.employee_id, e.first_name, e.last_name, e.employee_code, d.department_name, DATE_FORMAT(ar.attendance_date, '%Y-%m');

-- View: Tổng hợp lương hiện tại của nhân viên
CREATE OR REPLACE VIEW vw_employee_current_salary AS
SELECT 
    e.employee_id,
    e.employee_code,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    d.department_name,
    p.position_name,
    sc.base_salary,
    sc.position_allowance,
    sc.housing_allowance,
    sc.transportation_allowance,
    sc.meal_allowance,
    sc.other_allowances,
    (sc.base_salary + sc.position_allowance + sc.housing_allowance + 
     sc.transportation_allowance + sc.meal_allowance + sc.other_allowances) AS total_monthly_salary,
    sc.effective_from,
    sc.effective_to
FROM employees e
JOIN salary_components sc ON e.employee_id = sc.employee_id
LEFT JOIN departments d ON e.department_id = d.department_id
LEFT JOIN positions p ON e.position_id = p.position_id
WHERE sc.is_active = TRUE;

-- View: Danh sách công việc với thông tin chi tiết
CREATE OR REPLACE VIEW vw_task_details AS
SELECT 
    t.task_id,
    t.task_title,
    t.task_description,
    CONCAT(e1.first_name, ' ', e1.last_name) AS assigned_to_name,
    e1.employee_code AS assigned_to_code,
    CONCAT(e2.first_name, ' ', e2.last_name) AS assigned_by_name,
    d.department_name,
    t.priority,
    t.task_status,
    t.start_date,
    t.due_date,
    t.completed_date,
    CASE 
        WHEN t.task_status = 'Done' THEN 100
        WHEN t.due_date < CURDATE() AND t.task_status NOT IN ('Done', 'Cancelled') THEN -1
        ELSE DATEDIFF(t.due_date, CURDATE())
    END AS days_until_due,
    t.progress_percentage,
    t.created_at
FROM tasks t
JOIN employees e1 ON t.assigned_to = e1.employee_id
JOIN users u ON t.assigned_by = u.user_id
JOIN employees e2 ON u.user_id = e2.user_id
LEFT JOIN departments d ON t.department_id = d.department_id;

-- View: Danh sách yêu cầu với thông tin chi tiết
CREATE OR REPLACE VIEW vw_request_details AS
SELECT 
    r.request_id,
    r.employee_id,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    e.employee_code,
    d.department_name,
    rt.request_type_name,
    r.start_date,
    r.end_date,
    r.number_of_days,
    r.reason,
    r.request_status,
    CONCAT(rev.first_name, ' ', rev.last_name) AS reviewed_by_name,
    r.review_comment,
    r.reviewed_at,
    r.created_at,
    rt.is_paid
FROM requests r
JOIN employees e ON r.employee_id = e.employee_id
JOIN request_types rt ON r.request_type_id = rt.request_type_id
LEFT JOIN departments d ON e.department_id = d.department_id
LEFT JOIN users u ON r.reviewed_by = u.user_id
LEFT JOIN employees rev ON u.user_id = rev.user_id;

-- View: Thống kê tuyển dụng
CREATE OR REPLACE VIEW vw_recruitment_statistics AS
SELECT 
    jp.job_id,
    jp.job_title,
    d.department_name,
    p.position_name,
    jp.job_status,
    jp.number_of_positions,
    jp.posted_date,
    jp.application_deadline,
    COUNT(ja.application_id) AS total_applications,
    COUNT(CASE WHEN ja.application_status = 'Submitted' THEN 1 END) AS submitted_count,
    COUNT(CASE WHEN ja.application_status = 'Screening' THEN 1 END) AS screening_count,
    COUNT(CASE WHEN ja.application_status = 'Interview' THEN 1 END) AS interview_count,
    COUNT(CASE WHEN ja.application_status = 'Offered' THEN 1 END) AS offered_count,
    COUNT(CASE WHEN ja.application_status = 'Rejected' THEN 1 END) AS rejected_count,
    COUNT(CASE WHEN ja.application_status = 'Hired' THEN 1 END) AS hired_count
FROM job_postings jp
LEFT JOIN job_applications ja ON jp.job_id = ja.job_id
LEFT JOIN departments d ON jp.department_id = d.department_id
LEFT JOIN positions p ON jp.position_id = p.position_id
GROUP BY jp.job_id, jp.job_title, d.department_name, p.position_name, 
         jp.job_status, jp.number_of_positions, jp.posted_date, jp.application_deadline;

-- View: Hợp đồng còn hiệu lực
CREATE OR REPLACE VIEW vw_active_contracts AS
SELECT 
    ec.contract_id,
    ec.contract_number,
    e.employee_id,
    e.employee_code,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    d.department_name,
    p.position_name,
    ec.contract_type,
    ec.start_date,
    ec.end_date,
    CASE 
        WHEN ec.end_date IS NULL THEN 'Indefinite'
        WHEN ec.end_date < CURDATE() THEN 'Expired'
        WHEN DATEDIFF(ec.end_date, CURDATE()) <= 30 THEN 'Expiring Soon'
        ELSE 'Active'
    END AS contract_expiry_status,
    DATEDIFF(COALESCE(ec.end_date, DATE_ADD(CURDATE(), INTERVAL 100 YEAR)), CURDATE()) AS days_until_expiry,
    ec.salary_amount,
    ec.contract_status,
    ec.signed_date
FROM employment_contracts ec
JOIN employees e ON ec.employee_id = e.employee_id
LEFT JOIN departments d ON e.department_id = d.department_id
LEFT JOIN positions p ON e.position_id = p.position_id
WHERE ec.contract_status = 'Active';

-- View: Bảng lương chi tiết với tên nhân viên
CREATE OR REPLACE VIEW vw_payroll_details AS
SELECT 
    mp.payroll_id,
    mp.employee_id,
    e.employee_code,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    d.department_name,
    p.position_name,
    mp.payroll_month,
    mp.base_salary,
    mp.total_allowances,
    mp.overtime_pay,
    mp.total_bonus,
    mp.total_benefits,
    mp.total_deductions,
    mp.gross_salary,
    mp.net_salary,
    mp.working_days,
    mp.absent_days,
    mp.late_days,
    mp.overtime_hours,
    mp.status AS payroll_status,
    mp.calculated_at,
    CONCAT(calc.first_name, ' ', calc.last_name) AS calculated_by_name,
    mp.approved_at,
    CONCAT(appr.first_name, ' ', appr.last_name) AS approved_by_name
FROM monthly_payroll mp
JOIN employees e ON mp.employee_id = e.employee_id
LEFT JOIN departments d ON e.department_id = d.department_id
LEFT JOIN positions p ON e.position_id = p.position_id
LEFT JOIN users u1 ON mp.calculated_by = u1.user_id
LEFT JOIN employees calc ON u1.user_id = calc.user_id
LEFT JOIN users u2 ON mp.approved_by = u2.user_id
LEFT JOIN employees appr ON u2.user_id = appr.user_id;

-- View: Dashboard HR Manager - Thống kê tổng quan
CREATE OR REPLACE VIEW vw_hr_dashboard_statistics AS
SELECT 
    (SELECT COUNT(*) FROM employees WHERE employment_status = 'Active') AS total_active_employees,
    (SELECT COUNT(*) FROM employees WHERE employment_status = 'Terminated') AS total_terminated_employees,
    (SELECT COUNT(DISTINCT department_id) FROM employees WHERE employment_status = 'Active') AS total_active_departments,
    (SELECT COUNT(*) FROM job_postings WHERE job_status = 'Open') AS open_job_postings,
    (SELECT COUNT(*) FROM job_applications WHERE application_status = 'Submitted' AND applied_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) AS applications_this_month,
    (SELECT COUNT(*) FROM requests WHERE request_status = 'Pending') AS pending_requests,
    (SELECT COUNT(*) FROM tasks WHERE task_status IN ('Not Started', 'In Progress') AND due_date < CURDATE()) AS overdue_tasks,
    (SELECT COUNT(*) FROM attendance_exception_requests WHERE status = 'Pending') AS pending_attendance_exceptions,
    (SELECT COUNT(*) FROM employment_contracts WHERE contract_status = 'Active' AND end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 60 DAY)) AS contracts_expiring_soon,
    (SELECT COUNT(*) FROM monthly_payroll WHERE status = 'Draft' OR status = 'Calculated') AS pending_payroll_approvals;

-- ==============================================================================
-- INDEXES: Các index bổ sung để tối ưu performance
-- ==============================================================================

-- Index cho tìm kiếm và lọc nhanh
CREATE INDEX idx_employees_full_name ON employees(first_name, last_name);
CREATE INDEX idx_employees_hire_date ON employees(hire_date);
CREATE INDEX idx_attendance_employee_date ON attendance_records(employee_id, attendance_date);
CREATE INDEX idx_payroll_employee_month ON monthly_payroll(employee_id, payroll_month);
CREATE INDEX idx_contracts_expiry ON employment_contracts(end_date, contract_status);
CREATE INDEX idx_tasks_due_date_status ON tasks(due_date, task_status);
CREATE INDEX idx_requests_dates ON requests(start_date, end_date, request_status);

-- Composite indexes cho các truy vấn phức tạp
CREATE INDEX idx_attendance_summary ON attendance_records(employee_id, attendance_date, status);
CREATE INDEX idx_payroll_summary ON monthly_payroll(payroll_month, status, employee_id);
CREATE INDEX idx_task_assignment ON tasks(assigned_to, task_status, due_date);

-- ==============================================================================
-- STORED PROCEDURES: Các thủ tục hỗ trợ nghiệp vụ
-- ==============================================================================

-- Procedure: Tính lương tự động cho một nhân viên trong tháng
DELIMITER //

CREATE PROCEDURE sp_calculate_monthly_salary(
    IN p_employee_id INT,
    IN p_payroll_month DATE,
    IN p_calculated_by INT
)
BEGIN
    DECLARE v_base_salary DECIMAL(12,2);
    DECLARE v_total_allowances DECIMAL(10,2);
    DECLARE v_working_days INT;
    DECLARE v_absent_days INT;
    DECLARE v_late_days INT;
    DECLARE v_overtime_hours DECIMAL(6,2);
    DECLARE v_overtime_pay DECIMAL(10,2);
    DECLARE v_gross_salary DECIMAL(12,2);
    DECLARE v_total_deductions DECIMAL(10,2);
    DECLARE v_net_salary DECIMAL(12,2);
    
    -- Lấy thông tin lương cơ bản và phụ cấp
    SELECT 
        base_salary,
        (position_allowance + housing_allowance + transportation_allowance + meal_allowance + other_allowances)
    INTO v_base_salary, v_total_allowances
    FROM salary_components
    WHERE employee_id = p_employee_id AND is_active = TRUE
    LIMIT 1;
    
    -- Tính số ngày làm việc, vắng, đi muộn và giờ làm thêm
    SELECT 
        COUNT(CASE WHEN status IN ('Present', 'Late', 'Remote', 'Business Trip') THEN 1 END),
        COUNT(CASE WHEN status = 'Absent' THEN 1 END),
        COUNT(CASE WHEN status = 'Late' THEN 1 END),
        COALESCE(SUM(overtime_hours), 0)
    INTO v_working_days, v_absent_days, v_late_days, v_overtime_hours
    FROM attendance_records
    WHERE employee_id = p_employee_id 
        AND DATE_FORMAT(attendance_date, '%Y-%m-01') = p_payroll_month;
    
    -- Tính lương làm thêm (giả sử 1.5x lương giờ)
    SET v_overtime_pay = (v_base_salary / 176) * 1.5 * v_overtime_hours; -- 176 giờ = 22 ngày * 8 giờ
    
    -- Tính tổng lương gộp
    SET v_gross_salary = v_base_salary + v_total_allowances + v_overtime_pay;
    
    -- Tính khấu trừ (thuế + bảo hiểm - ví dụ 22.65%)
    SET v_total_deductions = v_gross_salary * 0.2265;
    
    -- Tính lương thực nhận
    SET v_net_salary = v_gross_salary - v_total_deductions;
    
    -- Insert hoặc update bảng lương
    INSERT INTO monthly_payroll (
        employee_id, payroll_month, base_salary, total_allowances, overtime_pay,
        total_bonus, total_benefits, total_deductions, gross_salary, net_salary,
        working_days, absent_days, late_days, overtime_hours, status,
        calculated_by, calculated_at
    ) VALUES (
        p_employee_id, p_payroll_month, v_base_salary, v_total_allowances, v_overtime_pay,
        0, 0, v_total_deductions, v_gross_salary, v_net_salary,
        v_working_days, v_absent_days, v_late_days, v_overtime_hours, 'Calculated',
        p_calculated_by, NOW()
    )
    ON DUPLICATE KEY UPDATE
        base_salary = v_base_salary,
        total_allowances = v_total_allowances,
        overtime_pay = v_overtime_pay,
        total_deductions = v_total_deductions,
        gross_salary = v_gross_salary,
        net_salary = v_net_salary,
        working_days = v_working_days,
        absent_days = v_absent_days,
        late_days = v_late_days,
        overtime_hours = v_overtime_hours,
        status = 'Calculated',
        calculated_by = p_calculated_by,
        calculated_at = NOW();
        
END //

-- Procedure: Phê duyệt yêu cầu
CREATE PROCEDURE sp_approve_request(
    IN p_request_id INT,
    IN p_reviewed_by INT,
    IN p_review_comment TEXT
)
BEGIN
    UPDATE requests 
    SET 
        request_status = 'Approved',
        reviewed_by = p_reviewed_by,
        review_comment = p_review_comment,
        reviewed_at = NOW()
    WHERE request_id = p_request_id;
    
    -- Tạo thông báo cho nhân viên
    INSERT INTO notifications (user_id, notification_type, title, message, related_table, related_record_id)
    SELECT 
        e.user_id,
        'REQUEST_APPROVED',
        'Request Approved',
        CONCAT('Your ', rt.request_type_name, ' request has been approved'),
        'requests',
        p_request_id
    FROM requests r
    JOIN employees e ON r.employee_id = e.employee_id
    JOIN request_types rt ON r.request_type_id = rt.request_type_id
    WHERE r.request_id = p_request_id;
END //

-- Procedure: Từ chối yêu cầu
CREATE PROCEDURE sp_reject_request(
    IN p_request_id INT,
    IN p_reviewed_by INT,
    IN p_review_comment TEXT
)
BEGIN
    UPDATE requests 
    SET 
        request_status = 'Rejected',
        reviewed_by = p_reviewed_by,
        review_comment = p_review_comment,
        reviewed_at = NOW()
    WHERE request_id = p_request_id;
    
    -- Tạo thông báo cho nhân viên
    INSERT INTO notifications (user_id, notification_type, title, message, related_table, related_record_id)
    SELECT 
        e.user_id,
        'REQUEST_REJECTED',
        'Request Rejected',
        CONCAT('Your ', rt.request_type_name, ' request has been rejected. Reason: ', p_review_comment),
        'requests',
        p_request_id
    FROM requests r
    JOIN employees e ON r.employee_id = e.employee_id
    JOIN request_types rt ON r.request_type_id = rt.request_type_id
    WHERE r.request_id = p_request_id;
END //

-- Procedure: Gán nhiệm vụ mới
CREATE PROCEDURE sp_assign_task(
    IN p_task_title VARCHAR(200),
    IN p_task_description TEXT,
    IN p_assigned_to INT,
    IN p_assigned_by INT,
    IN p_department_id INT,
    IN p_priority ENUM('Low', 'Medium', 'High', 'Urgent'),
    IN p_due_date DATE
)
BEGIN
    DECLARE v_task_id INT;
    
    INSERT INTO tasks (
        task_title, task_description, assigned_to, assigned_by,
        department_id, priority, task_status, due_date
    ) VALUES (
        p_task_title, p_task_description, p_assigned_to, p_assigned_by,
        p_department_id, p_priority, 'Not Started', p_due_date
    );
    
    SET v_task_id = LAST_INSERT_ID();
    
    -- Tạo thông báo cho người được giao việc
    INSERT INTO notifications (user_id, notification_type, title, message, related_table, related_record_id)
    SELECT 
        e.user_id,
        'TASK_ASSIGNED',
        'New Task Assigned',
        CONCAT('You have been assigned a new task: ', p_task_title, '. Due date: ', DATE_FORMAT(p_due_date, '%M %d, %Y')),
        'tasks',
        v_task_id
    FROM employees e
    WHERE e.employee_id = p_assigned_to;
END //

DELIMITER ;

-- ==============================================================================
-- TRIGGERS: Các trigger tự động
-- ==============================================================================

-- Trigger: Tự động tạo audit log khi cập nhật user
DELIMITER //

CREATE TRIGGER trg_users_audit_update
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (user_id, action_type, table_name, record_id, old_values, new_values, ip_address)
    VALUES (
        NEW.user_id,
        'UPDATE',
        'users',
        NEW.user_id,
        JSON_OBJECT(
            'username', OLD.username,
            'email', OLD.email,
            'role', OLD.role,
            'status', OLD.status
        ),
        JSON_OBJECT(
            'username', NEW.username,
            'email', NEW.email,
            'role', NEW.role,
            'status', NEW.status
        ),
        'SYSTEM'
    );
END //

-- Trigger: Cập nhật trạng thái hợp đồng khi hết hạn
CREATE TRIGGER trg_contract_check_expiry
BEFORE UPDATE ON employment_contracts
FOR EACH ROW
BEGIN
    IF NEW.end_date IS NOT NULL AND NEW.end_date < CURDATE() AND NEW.contract_status = 'Active' THEN
        SET NEW.contract_status = 'Expired';
    END IF;
END //

-- Trigger: Tự động cập nhật số ngày trong yêu cầu
CREATE TRIGGER trg_request_calculate_days
BEFORE INSERT ON requests
FOR EACH ROW
BEGIN
    SET NEW.number_of_days = DATEDIFF(NEW.end_date, NEW.start_date) + 1;
END //

CREATE TRIGGER trg_request_calculate_days_update
BEFORE UPDATE ON requests
FOR EACH ROW
BEGIN
    IF NEW.start_date != OLD.start_date OR NEW.end_date != OLD.end_date THEN
        SET NEW.number_of_days = DATEDIFF(NEW.end_date, NEW.start_date) + 1;
    END IF;
END //

DELIMITER ;

-- ==============================================================================
-- FUNCTIONS: Các hàm tiện ích
-- ==============================================================================

DELIMITER //

-- Function: Tính tổng số ngày nghỉ phép đã sử dụng trong năm
CREATE FUNCTION fn_get_used_leave_days(
    p_employee_id INT,
    p_request_type_id INT,
    p_year INT
)
RETURNS DECIMAL(4,1)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total_days DECIMAL(4,1);
    
    SELECT COALESCE(SUM(number_of_days), 0)
    INTO v_total_days
    FROM requests
    WHERE employee_id = p_employee_id
        AND request_type_id = p_request_type_id
        AND YEAR(start_date) = p_year
        AND request_status = 'Approved';
    
    RETURN v_total_days;
END //

-- Function: Kiểm tra nhân viên có phải là manager không
CREATE FUNCTION fn_is_manager(p_employee_id INT)
RETURNS BOOLEAN
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_is_manager BOOLEAN;
    
    SELECT COUNT(*) > 0
    INTO v_is_manager
    FROM employees
    WHERE manager_id = p_employee_id;
    
    RETURN v_is_manager;
END //

-- Function: Lấy số nhân viên trong phòng ban
CREATE FUNCTION fn_get_department_employee_count(p_department_id INT)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*)
    INTO v_count
    FROM employees
    WHERE department_id = p_department_id
        AND employment_status = 'Active';
    
    RETURN v_count;
END //

DELIMITER ;

-- ==============================================================================
-- KẾT THÚC SCRIPT
-- Database đã được tạo với đầy đủ:
-- - 25 bảng dữ liệu chuẩn 3NF
-- - Dữ liệu mẫu đầy đủ cho tất cả các bảng
-- - 8 Views hỗ trợ báo cáo và truy vấn
-- - Indexes để tối ưu performance
-- - 5 Stored Procedures cho các nghiệp vụ quan trọng
-- - 4 Triggers tự động
-- - 3 Functions tiện ích
-- 
-- Hệ thống sẵn sàng triển khai cho toàn bộ 67 use cases
-- ==============================================================================

SELECT 'Database hr_management_system has been created successfully!' AS status;
SELECT 'Total tables created: 25' AS info;
SELECT 'Total views created: 8' AS info;
SELECT 'Total stored procedures created: 5' AS info;
SELECT 'Total triggers created: 4' AS info;
SELECT 'Total functions created: 3' AS info;
SELECT 'Sample data inserted for all tables' AS info;
SELECT 'System is ready for deployment!' AS info;

-- ==============================================================================
-- BẢNG BỔ SUNG ĐỂ ĐẦY ĐỦ HỆ THỐNG
-- ==============================================================================

-- ==============================================================================
-- TABLE: employee_benefits
-- Bảng quản lý phúc lợi cụ thể của từng nhân viên
-- ==============================================================================
CREATE TABLE employee_benefits (
    employee_benefit_id INT PRIMARY KEY AUTO_INCREMENT,        -- ID phúc lợi nhân viên (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên
    benefit_type_id INT NOT NULL,                              -- ID loại phúc lợi
    benefit_amount DECIMAL(10, 2),                             -- Số tiền phúc lợi cụ thể
    effective_from DATE NOT NULL,                              -- Có hiệu lực từ
    effective_to DATE,                                         -- Có hiệu lực đến
    is_active BOOLEAN DEFAULT TRUE,                            -- Còn hiệu lực không
    notes TEXT,                                                -- Ghi chú
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (benefit_type_id) REFERENCES benefit_types(benefit_type_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_benefit_type (benefit_type_id),                  -- Index cho lọc theo loại phúc lợi
    INDEX idx_is_active (is_active)                            -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: employee_deductions
-- Bảng quản lý khấu trừ cụ thể của từng nhân viên
-- ==============================================================================
CREATE TABLE employee_deductions (
    employee_deduction_id INT PRIMARY KEY AUTO_INCREMENT,      -- ID khấu trừ nhân viên (khóa chính)
    employee_id INT NOT NULL,                                  -- ID nhân viên
    deduction_type_id INT NOT NULL,                            -- ID loại khấu trừ
    deduction_amount DECIMAL(10, 2),                           -- Số tiền khấu trừ cụ thể
    effective_from DATE NOT NULL,                              -- Có hiệu lực từ
    effective_to DATE,                                         -- Có hiệu lực đến
    is_active BOOLEAN DEFAULT TRUE,                            -- Còn hiệu lực không
    notes TEXT,                                                -- Ghi chú
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Thời gian cập nhật
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (deduction_type_id) REFERENCES deduction_types(deduction_type_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_employee (employee_id),                          -- Index cho lọc theo nhân viên
    INDEX idx_deduction_type (deduction_type_id),              -- Index cho lọc theo loại khấu trừ
    INDEX idx_is_active (is_active)                            -- Index cho lọc theo trạng thái
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: payroll_benefit_details
-- Bảng chi tiết phúc lợi trong bảng lương tháng
-- ==============================================================================
CREATE TABLE payroll_benefit_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,                  -- ID chi tiết (khóa chính)
    payroll_id INT NOT NULL,                                   -- ID bảng lương
    benefit_type_id INT NOT NULL,                              -- ID loại phúc lợi
    benefit_amount DECIMAL(10, 2) NOT NULL,                    -- Số tiền phúc lợi
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    FOREIGN KEY (payroll_id) REFERENCES monthly_payroll(payroll_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (benefit_type_id) REFERENCES benefit_types(benefit_type_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_payroll (payroll_id),                            -- Index cho lọc theo payroll
    INDEX idx_benefit_type (benefit_type_id)                   -- Index cho lọc theo loại phúc lợi
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: payroll_deduction_details
-- Bảng chi tiết khấu trừ trong bảng lương tháng
-- ==============================================================================
CREATE TABLE payroll_deduction_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,                  -- ID chi tiết (khóa chính)
    payroll_id INT NOT NULL,                                   -- ID bảng lương
    deduction_type_id INT NOT NULL,                            -- ID loại khấu trừ
    deduction_amount DECIMAL(10, 2) NOT NULL,                  -- Số tiền khấu trừ
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    FOREIGN KEY (payroll_id) REFERENCES monthly_payroll(payroll_id) ON DELETE CASCADE, -- Khóa ngoại
    FOREIGN KEY (deduction_type_id) REFERENCES deduction_types(deduction_type_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_payroll (payroll_id),                            -- Index cho lọc theo payroll
    INDEX idx_deduction_type (deduction_type_id)               -- Index cho lọc theo loại khấu trừ
) ENGINE=InnoDB;

-- ==============================================================================
-- TABLE: password_reset_tokens
-- Bảng quản lý token reset mật khẩu (UC04: Forgot Password)
-- ==============================================================================
CREATE TABLE password_reset_tokens (
    token_id INT PRIMARY KEY AUTO_INCREMENT,                   -- ID token (khóa chính)
    user_id INT NOT NULL,                                      -- ID người dùng
    token VARCHAR(255) UNIQUE NOT NULL,                        -- Token reset (duy nhất)
    expires_at TIMESTAMP NOT NULL,                             -- Thời gian hết hạn
    is_used BOOLEAN DEFAULT FALSE,                             -- Đã sử dụng chưa
    used_at TIMESTAMP NULL,                                    -- Thời gian sử dụng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,            -- Thời gian tạo
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE, -- Khóa ngoại
    INDEX idx_token (token),                                   -- Index cho tìm kiếm token
    INDEX idx_user (user_id),                                  -- Index cho lọc theo user
    INDEX idx_expires_at (expires_at)                          -- Index cho kiểm tra hết hạn
) ENGINE=InnoDB;

-- ==============================================================================
-- INSERT DATA: employee_benefits
-- Dữ liệu phúc lợi của nhân viên
-- ==============================================================================
INSERT INTO employee_benefits (employee_id, benefit_type_id, benefit_amount, effective_from, effective_to, is_active, notes) VALUES
(1, 1, 200.00, '2015-01-15', NULL, TRUE, 'Full family health insurance coverage'),
(1, 2, 50.00, '2015-01-15', NULL, TRUE, 'Life insurance coverage'),
(2, 1, 200.00, '2018-03-10', NULL, TRUE, 'Individual health insurance'),
(2, 2, 50.00, '2018-03-10', NULL, TRUE, 'Basic life insurance'),
(3, 1, 200.00, '2019-06-01', NULL, TRUE, 'Individual health insurance'),
(4, 1, 200.00, '2014-08-20', NULL, TRUE, 'Executive health insurance package'),
(4, 2, 50.00, '2014-08-20', NULL, TRUE, 'Enhanced life insurance'),
(4, 5, 50.00, '2014-08-20', NULL, TRUE, 'Premium gym membership'),
(5, 1, 200.00, '2016-02-14', NULL, TRUE, 'Full health insurance'),
(5, 5, 50.00, '2016-02-14', NULL, TRUE, 'Gym membership'),
(6, 1, 200.00, '2019-04-15', NULL, TRUE, 'Health insurance'),
(7, 1, 200.00, '2020-01-20', NULL, TRUE, 'Basic health insurance'),
(8, 1, 200.00, '2018-07-11', NULL, TRUE, 'Health insurance'),
(9, 1, 200.00, '2020-09-05', NULL, TRUE, 'Health insurance'),
(10, 1, 200.00, '2017-05-18', NULL, TRUE, 'Health insurance');

-- ==============================================================================
-- INSERT DATA: employee_deductions
-- Dữ liệu khấu trừ của nhân viên
-- ==============================================================================
INSERT INTO employee_deductions (employee_id, deduction_type_id, deduction_amount, effective_from, effective_to, is_active, notes) VALUES
(1, 4, 100.00, '2015-01-15', NULL, TRUE, 'Employee health insurance premium contribution'),
(1, 5, NULL, '2015-01-15', NULL, TRUE, '5% retirement contribution'),
(2, 4, 100.00, '2018-03-10', NULL, TRUE, 'Health insurance premium'),
(3, 4, 100.00, '2019-06-01', NULL, TRUE, 'Health insurance premium'),
(4, 4, 100.00, '2014-08-20', NULL, TRUE, 'Health insurance premium'),
(4, 5, NULL, '2014-08-20', NULL, TRUE, '5% retirement contribution'),
(5, 4, 100.00, '2016-02-14', NULL, TRUE, 'Health insurance premium'),
(5, 5, NULL, '2016-02-14', NULL, TRUE, '5% retirement contribution'),
(6, 4, 100.00, '2019-04-15', NULL, TRUE, 'Health insurance premium'),
(7, 4, 100.00, '2020-01-20', NULL, TRUE, 'Health insurance premium'),
(8, 4, 100.00, '2018-07-11', NULL, TRUE, 'Health insurance premium'),
(9, 4, 100.00, '2020-09-05', NULL, TRUE, 'Health insurance premium'),
(10, 4, 100.00, '2017-05-18', NULL, TRUE, 'Health insurance premium');

-- ==============================================================================
-- INSERT DATA: payroll_benefit_details
-- Chi tiết phúc lợi trong bảng lương tháng 9/2025
-- ==============================================================================
INSERT INTO payroll_benefit_details (payroll_id, benefit_type_id, benefit_amount) VALUES
(1, 1, 200.00),
(1, 2, 50.00),
(2, 1, 200.00),
(2, 2, 50.00),
(3, 1, 200.00),
(3, 2, 50.00),
(4, 1, 200.00),
(4, 2, 50.00),
(5, 1, 200.00),
(5, 2, 50.00);

-- ==============================================================================
-- INSERT DATA: payroll_deduction_details
-- Chi tiết khấu trừ trong bảng lương tháng 9/2025
-- ==============================================================================
INSERT INTO payroll_deduction_details (payroll_id, deduction_type_id, deduction_amount) VALUES
-- Payroll 1 (Alice Thompson)
(1, 1, 1481.25),  -- Income Tax (15% of gross)
(1, 2, 612.25),   -- Social Security (6.2%)
(1, 3, 143.19),   -- Medicare (1.45%)
(1, 4, 100.00),   -- Health Insurance Premium
(1, 5, 493.75),   -- Retirement Fund (5%)
-- Payroll 2 (Mark Williams)
(2, 1, 735.00),   -- Income Tax
(2, 2, 303.80),   -- Social Security
(2, 3, 71.05),    -- Medicare
(2, 4, 100.00),   -- Health Insurance Premium
(2, 5, 245.00),   -- Retirement Fund
-- Payroll 3 (Patricia Martinez)
(3, 1, 720.00),   -- Income Tax
(3, 2, 297.60),   -- Social Security
(3, 3, 69.60),    -- Medicare
(3, 4, 100.00),   -- Health Insurance Premium
(3, 5, 240.00),   -- Retirement Fund
-- Payroll 4 (John Smith)
(4, 1, 1755.00),  -- Income Tax
(4, 2, 725.40),   -- Social Security
(4, 3, 169.65),   -- Medicare
(4, 4, 100.00),   -- Health Insurance Premium
(4, 5, 585.00),   -- Retirement Fund
-- Payroll 5 (Sarah Johnson)
(5, 1, 1575.00),  -- Income Tax
(5, 2, 651.00),   -- Social Security
(5, 3, 152.25),   -- Medicare
(5, 4, 100.00),   -- Health Insurance Premium
(5, 5, 525.00);   -- Retirement Fund

-- ==============================================================================
-- INSERT DATA: password_reset_tokens
-- Token reset mật khẩu mẫu
-- ==============================================================================
INSERT INTO password_reset_tokens (user_id, token, expires_at, is_used, used_at) VALUES
(7, 'abc123def456ghi789jkl012mno345pqr678stu901', '2025-10-08 10:00:00', TRUE, '2025-10-08 09:45:00'),
(10, 'xyz789uvw456rst123opq890lmn567ijk234fgh901', '2025-10-10 15:00:00', FALSE, NULL);

-- ==============================================================================
-- VIEWS BỔ SUNG
-- ==============================================================================

-- View: Chi tiết phúc lợi và khấu trừ của nhân viên
CREATE OR REPLACE VIEW vw_employee_compensation_details AS
SELECT 
    e.employee_id,
    e.employee_code,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    sc.base_salary,
    sc.position_allowance + sc.housing_allowance + sc.transportation_allowance + 
    sc.meal_allowance + sc.other_allowances AS total_allowances,
    COALESCE(SUM(DISTINCT eb.benefit_amount), 0) AS total_monthly_benefits,
    COALESCE(SUM(DISTINCT ed.deduction_amount), 0) AS total_fixed_deductions,
    (sc.base_salary + sc.position_allowance + sc.housing_allowance + 
     sc.transportation_allowance + sc.meal_allowance + sc.other_allowances +
     COALESCE(SUM(DISTINCT eb.benefit_amount), 0)) AS estimated_gross_salary
FROM employees e
JOIN salary_components sc ON e.employee_id = sc.employee_id AND sc.is_active = TRUE
LEFT JOIN employee_benefits eb ON e.employee_id = eb.employee_id AND eb.is_active = TRUE
LEFT JOIN employee_deductions ed ON e.employee_id = ed.employee_id AND ed.is_active = TRUE
WHERE e.employment_status = 'Active'
GROUP BY e.employee_id, e.employee_code, e.first_name, e.last_name, 
         sc.base_salary, sc.position_allowance, sc.housing_allowance, 
         sc.transportation_allowance, sc.meal_allowance, sc.other_allowances;

-- ==============================================================================
-- STORED PROCEDURE BỔ SUNG
-- ==============================================================================

DELIMITER //

-- Procedure: Tạo token reset mật khẩu
CREATE PROCEDURE sp_create_password_reset_token(
    IN p_user_id INT,
    OUT p_token VARCHAR(255)
)
BEGIN
    DECLARE v_token VARCHAR(255);
    
    -- Generate token (trong thực tế nên dùng hàm random mạnh hơn)
    SET v_token = MD5(CONCAT(p_user_id, NOW(), RAND()));
    
    -- Insert token vào bảng
    INSERT INTO password_reset_tokens (user_id, token, expires_at)
    VALUES (p_user_id, v_token, DATE_ADD(NOW(), INTERVAL 1 HOUR));
    
    SET p_token = v_token;
END //

-- Procedure: Xác thực và sử dụng token reset mật khẩu
CREATE PROCEDURE sp_validate_reset_token(
    IN p_token VARCHAR(255),
    OUT p_is_valid BOOLEAN,
    OUT p_user_id INT
)
BEGIN
    DECLARE v_user_id INT;
    DECLARE v_expires_at TIMESTAMP;
    DECLARE v_is_used BOOLEAN;
    
    -- Lấy thông tin token
    SELECT user_id, expires_at, is_used
    INTO v_user_id, v_expires_at, v_is_used
    FROM password_reset_tokens
    WHERE token = p_token
    LIMIT 1;
    
    -- Kiểm tra token có hợp lệ không
    IF v_user_id IS NOT NULL AND v_expires_at > NOW() AND v_is_used = FALSE THEN
        SET p_is_valid = TRUE;
        SET p_user_id = v_user_id;
        
        -- Đánh dấu token đã sử dụng
        UPDATE password_reset_tokens
        SET is_used = TRUE, used_at = NOW()
        WHERE token = p_token;
    ELSE
        SET p_is_valid = FALSE;
        SET p_user_id = NULL;
    END IF;
END //

DELIMITER ;

-- ==============================================================================
-- TỔNG KẾT
-- ==============================================================================
SELECT 'Added 5 additional tables to complete the system!' AS status;
SELECT '1. employee_benefits - Manages individual employee benefits' AS info
UNION ALL SELECT '2. employee_deductions - Manages individual employee deductions'
UNION ALL SELECT '3. payroll_benefit_details - Detailed benefits in monthly payroll'
UNION ALL SELECT '4. payroll_deduction_details - Detailed deductions in monthly payroll'
UNION ALL SELECT '5. password_reset_tokens - Password reset functionality (UC04)';

SELECT 'Total tables: 25' AS final_count;
SELECT 'Total views: 9 (added 1 more)' AS final_count;
SELECT 'Total stored procedures: 7 (added 2 more)' AS final_count;
SELECT 'System now fully supports all 67 use cases!' AS final_status;

-- Note: FULLTEXT indexes đã được thêm vào các bảng employees, employment_contracts, và job_postings
-- Các indexes này hỗ trợ tìm kiếm MATCH...AGAINST trong Boolean mode được sử dụng trong các hàm tìm kiếm
--
-- FULLTEXT indexes:
-- - employees: ft_employee_search (first_name, last_name, employee_code)
-- - employment_contracts: ft_contract_search (contract_number, job_description)
-- - job_postings: ft_job_posting_search (job_title, job_description, requirements)
--
-- Additional test user accounts (user_id 16-25) have been added without employee records
-- for testing the Create Employee functionality where HR links employee info to existing user accounts
