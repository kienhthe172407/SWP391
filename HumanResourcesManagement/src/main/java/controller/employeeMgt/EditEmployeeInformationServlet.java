package controller.employeeMgt;

import dal.EmployeeDAO;
import model.Employee;
import model.Department;
import model.Position;
import model.User;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle editing employee information
 * Only accessible by HR and HR Manager roles
 */
@WebServlet(name = "EditEmployeeInformationServlet", urlPatterns = {"/employees/edit"})
public class EditEmployeeInformationServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display edit employee information form
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
        User currentUser = (User) session.getAttribute("user");
        
        // Check authentication
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check permission using PermissionChecker
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.EMPLOYEE_EDIT)) {
            request.setAttribute("errorMessage", "Bạn không có quyền chỉnh sửa thông tin nhân viên");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        // Get employee ID from request
        String employeeIdStr = request.getParameter("employeeId");
        if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Employee ID is required.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
            return;
        }
        
        try {
            // Parse employee ID
            int employeeId = Integer.parseInt(employeeIdStr);
            
            // Get employee data
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            
            if (employee == null) {
                session.setAttribute("errorMessage", "Employee not found.");
                response.sendRedirect(request.getContextPath() + "/employees/list");
                return;
            }
            
            // Get departments and positions for dropdowns
            List<Department> departments = employeeDAO.getAllDepartments();
            List<Position> positions = employeeDAO.getAllPositions();
            List<Employee> managers = employeeDAO.getAllEmployeesForManagerDropdown(employeeId);
            
            // Set attributes for display in JSP
            request.setAttribute("employee", employee);
            request.setAttribute("departments", departments);
            request.setAttribute("positions", positions);
            request.setAttribute("managers", managers);
            
            // Forward to JSP page
            request.getRequestDispatcher("/employee-mgt/edit-employee-information.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid employee ID.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
        } catch (Exception e) {
            System.err.println("Error in EditEmployeeInformationServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the edit employee information form.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
        }
    }

    /**
     * Handle POST request - process updating employee information
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        // Check if user has permission to edit employee records
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can edit employee information.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
            return;
        }

        try {
            // Get employee ID from request
            String employeeIdStr = request.getParameter("employeeId");
            if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Employee ID is required.");
                response.sendRedirect(request.getContextPath() + "/employees/list");
                return;
            }
            
            int employeeId = Integer.parseInt(employeeIdStr);
            
            // Get current employee data
            Employee currentEmployee = employeeDAO.getEmployeeById(employeeId);
            if (currentEmployee == null) {
                session.setAttribute("errorMessage", "Employee not found.");
                response.sendRedirect(request.getContextPath() + "/employees/list");
                return;
            }
            
            // Get form data
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String phoneNumber = request.getParameter("phoneNumber");
            String personalEmail = request.getParameter("personalEmail");
            String homeAddress = request.getParameter("homeAddress");
            String emergencyContactName = request.getParameter("emergencyContactName");
            String emergencyContactPhone = request.getParameter("emergencyContactPhone");
            String departmentIdStr = request.getParameter("departmentId");
            String positionIdStr = request.getParameter("positionId");
            String managerIdStr = request.getParameter("managerId");
            String hireDateStr = request.getParameter("hireDate");
            String employmentStatus = request.getParameter("employmentStatus");

            // Validate required fields
            if (firstName == null || firstName.trim().isEmpty()) {
                session.setAttribute("errorMessage", "First name is required.");
                response.sendRedirect(request.getContextPath() + "/employees/edit?employeeId=" + employeeId);
                return;
            }

            if (lastName == null || lastName.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Last name is required.");
                response.sendRedirect(request.getContextPath() + "/employees/edit?employeeId=" + employeeId);
                return;
            }

            // Create Employee object with updated data
            Employee employee = new Employee();
            employee.setEmployeeID(employeeId);
            employee.setUserID(currentEmployee.getUserID()); // Keep the same user ID
            employee.setEmployeeCode(currentEmployee.getEmployeeCode()); // Keep the same employee code
            employee.setFirstName(firstName.trim());
            employee.setLastName(lastName.trim());
            
            // Gender can be null, set it only if provided
            if (gender != null && !gender.trim().isEmpty()) {
                employee.setGender(gender);
            }
            
            employee.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
            employee.setPersonalEmail(personalEmail != null ? personalEmail.trim() : null);
            employee.setHomeAddress(homeAddress != null ? homeAddress.trim() : null);
            employee.setEmergencyContactName(emergencyContactName != null ? emergencyContactName.trim() : null);
            employee.setEmergencyContactPhone(emergencyContactPhone != null ? emergencyContactPhone.trim() : null);
            employee.setEmploymentStatus(employmentStatus != null && !employmentStatus.trim().isEmpty() ? employmentStatus.trim() : "Active");

            // Parse and set date of birth
            if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
                employee.setDateOfBirth(Date.valueOf(dateOfBirthStr));
            }

            // Parse and set hire date
            if (hireDateStr != null && !hireDateStr.trim().isEmpty()) {
                employee.setHireDate(Date.valueOf(hireDateStr));
            } else {
                // Keep existing hire date if not provided
                employee.setHireDate(currentEmployee.getHireDate());
            }

            // Parse and set department ID
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    employee.setDepartmentID(Integer.parseInt(departmentIdStr));
                } catch (NumberFormatException e) {
                    // Invalid department ID, leave as null
                }
            }

            // Parse and set position ID
            if (positionIdStr != null && !positionIdStr.trim().isEmpty()) {
                try {
                    employee.setPositionID(Integer.parseInt(positionIdStr));
                } catch (NumberFormatException e) {
                    // Invalid position ID, leave as null
                }
            }

            // Parse and set manager ID
            if (managerIdStr != null && !managerIdStr.trim().isEmpty()) {
                try {
                    int managerId = Integer.parseInt(managerIdStr);
                    // Ensure manager is not the employee themselves
                    if (managerId != employeeId) {
                        employee.setManagerID(managerId);
                    }
                } catch (NumberFormatException e) {
                    // Invalid manager ID, leave as null
                }
            }

            // Update employee information
            boolean success = employeeDAO.updateEmployeeInformation(employee);

            if (success) {
                session.setAttribute("successMessage", "Employee information for '" + employee.getFirstName() + " " + employee.getLastName() + "' has been successfully updated.");
                response.sendRedirect(request.getContextPath() + "/employees/list");
            } else {
                session.setAttribute("errorMessage", "Failed to update employee information. Please try again.");
                response.sendRedirect(request.getContextPath() + "/employees/edit?employeeId=" + employeeId);
            }

        } catch (IllegalArgumentException e) {
            // Handle date format errors
            session.setAttribute("errorMessage", "Invalid date format: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/employees/list");
        } catch (Exception e) {
            System.err.println("Error in EditEmployeeInformationServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating employee information.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Employee Information Servlet";
    }
}
