package controller.attendanceMgt;

import dal.AttendanceDAO;
import dal.EmployeeDAO;
import model.Department;
import model.Employee;
import model.MonthlyAttendanceSummary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet to handle monthly attendance report display
 * Shows aggregated monthly attendance data per employee
 * Supports filtering by month/year, department, and employee
 */
@WebServlet("/attendance/monthly-report")
public class MonthlyAttendanceReportServlet extends HttpServlet {
    
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
        
        // Check user authentication and authorization
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Get filter parameters
            String monthStr = request.getParameter("month");
            String yearStr = request.getParameter("year");
            String employeeCode = request.getParameter("employeeCode");
            String departmentIdStr = request.getParameter("departmentId");
            String pageStr = request.getParameter("page");
            
            // Parse month and year
            Integer month = null;
            Integer year = null;
            
            if (monthStr != null && !monthStr.trim().isEmpty()) {
                try {
                    month = Integer.parseInt(monthStr);
                    if (month < 1 || month > 12) month = null;
                } catch (NumberFormatException e) {
                    // Invalid month, ignore
                }
            }
            
            if (yearStr != null && !yearStr.trim().isEmpty()) {
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    // Invalid year, ignore
                }
            }
            
            // If no year specified, default to current year
            if (year == null) {
                year = LocalDate.now().getYear();
            }
            
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
            
            // Get monthly attendance records with filters
            List<MonthlyAttendanceSummary> monthlyRecords = attendanceDAO.getMonthlyAttendanceRecords(
                month, year, departmentId, employeeCode, currentPage, PAGE_SIZE
            );
            
            // Get total count for pagination
            int totalRecords = attendanceDAO.getTotalMonthlyAttendanceRecords(
                month, year, departmentId, employeeCode
            );
            
            // Calculate pagination info
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
            
            // Get filter options
            List<Employee> employees = employeeDAO.getAllActiveEmployees();
            List<Department> departments = employeeDAO.getAllDepartments();
            
            // Set attributes for display in JSP
            request.setAttribute("monthlyRecords", monthlyRecords);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("employees", employees);
            request.setAttribute("departments", departments);
            
            // Preserve filter parameters
            request.setAttribute("filterMonth", month);
            request.setAttribute("filterYear", year);
            request.setAttribute("filterEmployeeCode", employeeCode);
            request.setAttribute("filterDepartmentId", departmentId);
            
            // Forward to JSP
            request.getRequestDispatcher("/attendance-mgt/monthly-attendance-report.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in MonthlyAttendanceReportServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading monthly attendance report.");
            response.sendRedirect(request.getContextPath() + "/attendance/summary");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect POST to GET
        doGet(request, response);
    }
}

