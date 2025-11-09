package controller.attendanceMgt;

import dal.AttendanceDAO;
import dal.EmployeeDAO;
import model.AttendanceRecord;
import model.AttendanceSummary;
import model.Department;
import model.Employee;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing attendance summary with filters
 * Supports filtering by employee, department, and date range
 */
@WebServlet("/attendance/summary")
public class ViewAttendanceSummaryServlet extends HttpServlet {
    
    private AttendanceDAO attendanceDAO;
    private EmployeeDAO employeeDAO;
    private static final int PAGE_SIZE = 20;
    
    @Override
    public void init() throws ServletException {
        super.init();
        attendanceDAO = new AttendanceDAO();
        employeeDAO = new EmployeeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in and has appropriate role
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Get user role
        String userRole = (String) session.getAttribute("userRole");
        model.User user = (model.User) session.getAttribute("user");
        
        // Get role from user object if not in session (more reliable)
        String actualRole = null;
        if (user != null) {
            actualRole = user.getRole(); // This is the actual role from database
        }
        if (actualRole == null && userRole != null) {
            actualRole = userRole; // Fallback to session role
        }
        
        // Normalize role for comparison (handle both formats)
        if (actualRole != null) {
            actualRole = actualRole.trim();
        }
        
        // Check if user has permission (HR, HR Manager, Dept Manager, or Employee)
        // Handle both "Dept Manager" and "Department Manager" (from getRoleDisplayName)
        boolean isHR = "HR".equals(actualRole);
        boolean isHRManager = "HR_MANAGER".equals(actualRole) || "HR Manager".equals(actualRole);
        boolean isDeptManager = "DEPT_MANAGER".equals(actualRole) || "Dept Manager".equals(actualRole) || "Department Manager".equals(actualRole);
        boolean isEmployee = "EMPLOYEE".equals(actualRole) || "Employee".equals(actualRole);
        
        if (!isHR && !isHRManager && !isDeptManager && !isEmployee) {
            // Redirect to dashboard with error message instead of non-existent access-denied.jsp
            session.setAttribute("errorMessage", "Access denied. You don't have permission to view attendance records.");
            if (user != null && "Admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/dashboard/admin");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
            return;
        }
        
        // Get employee information for Employee and Dept Manager roles
        Employee currentEmployee = null;
        String employeeCode = null;
        Integer managerDepartmentId = null;
        
        if (isEmployee && user != null) {
            currentEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (currentEmployee == null) {
                session.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
                response.sendRedirect(request.getContextPath() + "/dashboard/employee");
                return;
            }
            // Auto-filter by current employee's code
            employeeCode = currentEmployee.getEmployeeCode();
        } else if (isDeptManager && user != null) {
            // Get manager's employee record to get their department
            Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (managerEmployee == null) {
                session.setAttribute("errorMessage", "Manager record not found. Please contact HR.");
                response.sendRedirect(request.getContextPath() + "/dashboard/dept-manager");
                return;
            }
            // Auto-filter by manager's department
            managerDepartmentId = managerEmployee.getDepartmentID();
        }
        
        try {
            // Get filter parameters
            // For Employee: force filter by their own employee code
            // For Dept Manager: force filter by their department
            Integer departmentId = managerDepartmentId; // Default to manager's department for Dept Manager
            
            if (!isEmployee && !isDeptManager) {
                employeeCode = request.getParameter("employeeCode");
                String departmentIdStr = request.getParameter("departmentId");
                if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                    try {
                        departmentId = Integer.parseInt(departmentIdStr);
                    } catch (NumberFormatException e) {
                        // Invalid department ID, ignore
                    }
                }
            } else if (!isEmployee) {
                // For Dept Manager, allow employee filter within their department
                employeeCode = request.getParameter("employeeCode");
            }
            
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String pageStr = request.getParameter("page");
            
            // Parse page number
            int currentPage = 1;
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageStr);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            
            // Get attendance records with filters
            List<AttendanceRecord> attendanceRecords = attendanceDAO.getAttendanceWithFilters(
                employeeCode, departmentId, startDate, endDate, currentPage, PAGE_SIZE
            );
            
            // Get total count for pagination
            int totalRecords = attendanceDAO.getTotalAttendanceWithFilters(
                employeeCode, departmentId, startDate, endDate
            );
            
            // Get summary statistics
            AttendanceSummary summary = attendanceDAO.getAttendanceSummary(
                employeeCode, departmentId, startDate, endDate
            );
            
            // Calculate pagination info
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
            
            // Get filter options
            List<Employee> employees = null;
            List<Department> departments = null;
            if (isHR || isHRManager) {
                employees = employeeDAO.getAllActiveEmployees();
                departments = employeeDAO.getAllDepartments();
            } else if (isDeptManager && managerDepartmentId != null) {
                // For Dept Manager, only show employees in their department
                employees = employeeDAO.getEmployeesByDepartment(managerDepartmentId);
                // Get department info for display
                departments = employeeDAO.getAllDepartments();
            }
            
            // Set attributes for display in JSP
            request.setAttribute("attendanceRecords", attendanceRecords);
            request.setAttribute("summary", summary);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("employees", employees);
            request.setAttribute("departments", departments);
            request.setAttribute("currentEmployee", currentEmployee);
            request.setAttribute("isEmployee", isEmployee);
            request.setAttribute("isHR", isHR);
            request.setAttribute("isHRManager", isHRManager);
            request.setAttribute("isDeptManager", isDeptManager);
            
            // Preserve filter parameters
            request.setAttribute("filterEmployeeCode", employeeCode);
            request.setAttribute("filterDepartmentId", departmentId);
            request.setAttribute("filterStartDate", startDate);
            request.setAttribute("filterEndDate", endDate);
            
            // Forward to JSP
            request.getRequestDispatcher("/attendance-mgt/view-attendance-summary.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ViewAttendanceSummaryServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading attendance summary.");
            response.sendRedirect(request.getContextPath() + "/attendance/import");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect POST to GET
        doGet(request, response);
    }
}

