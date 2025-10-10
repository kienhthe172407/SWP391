package controller.employeeMgt;

import dal.EmployeeDAO;
import model.Employee;
import model.Department;
import model.Position;
import model.User;
import util.EmployeeCodeGenerator;
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
 * Servlet to handle adding employee information
 * Only accessible by HR and HR Manager roles
 */
@WebServlet(name = "AddEmployeeInformationServlet", urlPatterns = {"/employees/addInformation"})
public class AddEmployeeInformationServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display add employee information form
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
        
        // Set default role as HR Manager for full access (for development)
        if (session.getAttribute("userRole") == null) {
            session.setAttribute("userRole", "HR Manager");
            session.setAttribute("userId", 1);
        }
        
        // Check if user has permission to create employee records
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can add employee information.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
            return;
        }

        try {
            // Get departments and positions for dropdowns
            List<Department> departments = employeeDAO.getAllDepartments();
            List<Position> positions = employeeDAO.getAllPositions();
            List<Employee> managers = employeeDAO.getAllEmployeesForManagerDropdown(null);

            // Get users without employee records (created by Admin)
            List<User> availableUsers = employeeDAO.getUsersWithoutEmployeeRecords();

            // Generate a default employee code
            String defaultEmployeeCode = EmployeeCodeGenerator.generateEmployeeCode();

            // Set attributes for display in JSP
            request.setAttribute("departments", departments);
            request.setAttribute("positions", positions);
            request.setAttribute("managers", managers);
            request.setAttribute("availableUsers", availableUsers);
            request.setAttribute("defaultEmployeeCode", defaultEmployeeCode);
            
            // Forward to JSP page
            request.getRequestDispatcher("/employee-mgt/add-employee-information.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in AddEmployeeInformationServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the add employee information form.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
        }
    }

    /**
     * Handle POST request - process adding employee information
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        // Check if user has permission to create employee records
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can add employee information.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
            return;
        }

        try {
            // Get form data
            String userIdStr = request.getParameter("userId");
            String employeeCode = request.getParameter("employeeCode");
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
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Please select a user account to link with this employee.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            if (employeeCode == null || employeeCode.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Employee code is required.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            if (firstName == null || firstName.trim().isEmpty()) {
                session.setAttribute("errorMessage", "First name is required.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            if (lastName == null || lastName.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Last name is required.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            // Parse and validate user ID
            int userId;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid user selection.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            // Check if user already has an employee record
            if (employeeDAO.isUserAlreadyLinkedToEmployee(userId)) {
                session.setAttribute("errorMessage", "The selected user already has an employee record.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            // Check if employee code already exists
            if (employeeDAO.isEmployeeCodeExists(employeeCode.trim())) {
                session.setAttribute("errorMessage", "Employee code '" + employeeCode.trim() + "' already exists. Please use a different code.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
                return;
            }

            // Create Employee object
            Employee employee = new Employee();
            employee.setUserID(userId); // Link to the selected user account
            employee.setEmployeeCode(employeeCode.trim());
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
                // Default to current date if not provided
                employee.setHireDate(new Date(System.currentTimeMillis()));
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
                    employee.setManagerID(Integer.parseInt(managerIdStr));
                } catch (NumberFormatException e) {
                    // Invalid manager ID, leave as null
                }
            }

            // Add employee information
            boolean success = employeeDAO.addEmployeeInformation(employee);

            if (success) {
                session.setAttribute("successMessage", "Employee information for '" + employee.getFirstName() + " " + employee.getLastName() + "' has been successfully added.");
                response.sendRedirect(request.getContextPath() + "/employees/list");
            } else {
                session.setAttribute("errorMessage", "Failed to add employee information. Please try again.");
                response.sendRedirect(request.getContextPath() + "/employees/addInformation");
            }

        } catch (IllegalArgumentException e) {
            // Handle date format errors
            session.setAttribute("errorMessage", "Invalid date format: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/employees/addInformation");
        } catch (Exception e) {
            System.err.println("Error in AddEmployeeInformationServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while adding employee information.");
            response.sendRedirect(request.getContextPath() + "/employees/addInformation");
        }
    }

    @Override
    public String getServletInfo() {
        return "Add Employee Information Servlet";
    }
}
