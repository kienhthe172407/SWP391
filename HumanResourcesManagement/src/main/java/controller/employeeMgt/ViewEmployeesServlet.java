package controller.employeeMgt;

import dal.EmployeeDAO;
import model.Employee;
import model.Department;
import model.Position;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing employee records list
 * Only accessible by HR and HR Manager roles
 */
public class ViewEmployeesServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display employees list
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get user session information
        HttpSession session = request.getSession();
        model.User currentUser = (model.User) session.getAttribute("user");
        
        // Check authentication
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check permission using PermissionChecker
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.EMPLOYEE_VIEW)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xem danh sách nhân viên");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }

        // Pagination parameters
        final int PAGE_SIZE = 10;
        int currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Get search parameters from request
        String keyword = request.getParameter("keyword");
        String department = request.getParameter("department");
        String position = request.getParameter("position");
        String status = request.getParameter("status");
        
        // Handle encoding/decoding of special characters in search parameters
        if (keyword != null) {
            keyword = keyword.trim();
        }

        // Always set search params to preserve them in pagination links
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("department", department != null ? department : "");
        request.setAttribute("position", position != null ? position : "");
        request.setAttribute("status", status != null ? status : "");
        
        // Create a search query string for pagination links
        StringBuilder searchQueryString = new StringBuilder();
        try {
            if (keyword != null && !keyword.isEmpty()) {
                searchQueryString.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
            }
            if (department != null && !department.isEmpty()) {
                searchQueryString.append("&department=").append(department);
            }
            if (position != null && !position.isEmpty()) {
                searchQueryString.append("&position=").append(position);
            }
            if (status != null && !status.isEmpty()) {
                searchQueryString.append("&status=").append(status);
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error encoding search parameters: " + e.getMessage());
            // Fallback to non-encoded parameters
            if (keyword != null && !keyword.isEmpty()) {
                searchQueryString.append("&keyword=").append(keyword);
            }
            if (department != null && !department.isEmpty()) {
                searchQueryString.append("&department=").append(department);
            }
            if (position != null && !position.isEmpty()) {
                searchQueryString.append("&position=").append(position);
            }
            if (status != null && !status.isEmpty()) {
                searchQueryString.append("&status=").append(status);
            }
        }
        
        request.setAttribute("searchQueryString", searchQueryString.toString());

        List<Employee> employees;
        int totalRecords;
        
        try {
            // If search parameters exist, call searchEmployees with pagination
            if ((keyword != null && !keyword.trim().isEmpty()) ||
                (department != null && !department.trim().isEmpty()) ||
                (position != null && !position.trim().isEmpty()) ||
                (status != null && !status.trim().isEmpty())) {
                employees = employeeDAO.searchEmployees(keyword, department, position, status, currentPage, PAGE_SIZE);
                totalRecords = employeeDAO.getTotalSearchResults(keyword, department, position, status);
            } else {
                // Otherwise, get all employees with pagination
                employees = employeeDAO.getAllEmployees(currentPage, PAGE_SIZE);
                totalRecords = employeeDAO.getTotalEmployees();
            }
            
            // Calculate pagination info
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
            
            // Get filter options
            List<String> employmentStatuses = employeeDAO.getAllEmploymentStatuses();
            List<Department> departments = employeeDAO.getAllDepartments();
            List<Position> positions = employeeDAO.getAllPositions();
            
            // Set attributes for display in JSP
            request.setAttribute("employees", employees);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("employmentStatuses", employmentStatuses);
            request.setAttribute("departments", departments);
            request.setAttribute("positions", positions);
            
            // Forward to JSP page
            request.getRequestDispatcher("/employee-mgt/list-employees.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ViewEmployeesServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading employee records.");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    /**
     * Handle POST request - search employees
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST handles same as GET
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Employee Records List Servlet";
    }
}
