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
        if (userRole == null && session.getAttribute("user") != null) {
            model.User user = (model.User) session.getAttribute("user");
            userRole = user.getRole();
        }
        
        // Only HR and HR Manager can access
        if (!"HR".equals(userRole) && !"HR_MANAGER".equals(userRole) && 
            !"HR Manager".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }
        
        try {
            // Get filter parameters
            String employeeCode = request.getParameter("employeeCode");
            String departmentIdStr = request.getParameter("departmentId");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String pageStr = request.getParameter("page");
            
            // Parse department ID
            Integer departmentId = null;
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    departmentId = Integer.parseInt(departmentIdStr);
                } catch (NumberFormatException e) {
                    // Invalid department ID, ignore
                }
            }
            
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
            List<Employee> employees = employeeDAO.getAllActiveEmployees();
            List<Department> departments = employeeDAO.getAllDepartments();
            
            // Set attributes for display in JSP
            request.setAttribute("attendanceRecords", attendanceRecords);
            request.setAttribute("summary", summary);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("employees", employees);
            request.setAttribute("departments", departments);
            
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

