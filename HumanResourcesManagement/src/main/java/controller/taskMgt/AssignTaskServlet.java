package controller.taskMgt;

import dal.TaskDAO;
import dal.EmployeeDAO;
import model.Task;
import model.Employee;
import model.Department;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for creating and assigning tasks to employees
 * UC39: Assign Task
 * Only accessible by HR Manager and Department Manager roles
 * @author admin
 */
@WebServlet(name = "AssignTaskServlet", urlPatterns = {"/task/assign"})
public class AssignTaskServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the assign task form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        // Check if user has permission to assign tasks (HR Manager or Dept Manager)
        String userRole = user.getRole();
        boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
        boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);
        
        if (!isHRManager && !isDeptManager) {
            session.setAttribute("errorMessage", "Access denied. Only managers can assign tasks.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Get list of employees for assignment
        List<Employee> employees = new ArrayList<>();
        try {
            if (isDeptManager) {
                // Department manager can only assign to employees in their department
                Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                System.out.println("Dept Manager - User ID: " + user.getUserId());
                System.out.println("Dept Manager - Manager Employee: " + (managerEmployee != null ? "Found" : "Not Found"));
                
                if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                    System.out.println("Dept Manager - Department ID: " + managerEmployee.getDepartmentID());
                    employees = employeeDAO.getEmployeesByDepartment(managerEmployee.getDepartmentID());
                    
                    // Remove the manager themselves from the list (managers shouldn't assign tasks to themselves)
                    final int managerEmployeeID = managerEmployee.getEmployeeID();
                    employees.removeIf(emp -> emp.getEmployeeID() == managerEmployeeID);
                    
                    System.out.println("Dept Manager - Found " + employees.size() + " employees in department " + managerEmployee.getDepartmentID());
                    
                    if (employees.isEmpty()) {
                        session.setAttribute("errorMessage", "No employees found in your department. Please contact HR to add employees to your department.");
                    }
                } else {
                    // If manager doesn't have employee record or department, show empty list
                    employees = new ArrayList<>();
                    System.out.println("Dept Manager - No employee record or department found for user " + user.getUserId());
                    session.setAttribute("errorMessage", "Unable to load employees. Please ensure your employee record is linked to a department.");
                }
            } else if (isHRManager) {
                // HR Manager can assign to any employee
                employees = employeeDAO.getAllActiveEmployees();
                System.out.println("HR Manager - Found " + employees.size() + " active employees");
            } else {
                // Fallback: empty list
                employees = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error loading employees in AssignTaskServlet: " + e.getMessage());
            e.printStackTrace();
            employees = new ArrayList<>();
            session.setAttribute("errorMessage", "Error loading employee list. Please try again.");
        }

        // Ensure employees list is not null
        if (employees == null) {
            employees = new ArrayList<>();
        }

        // Debug: Log employees before setting attribute
        System.out.println("AssignTaskServlet - Setting employees attribute: " + employees.size() + " employees");
        if (!employees.isEmpty()) {
            Employee firstEmp = employees.get(0);
            System.out.println("AssignTaskServlet - First employee ID: " + firstEmp.getEmployeeID());
        }

        // Get list of departments
        List<Department> departments = employeeDAO.getAllDepartments();

        // Set attributes for JSP
        request.setAttribute("employees", employees);
        request.setAttribute("departments", departments != null ? departments : new ArrayList<>());
        
        System.out.println("AssignTaskServlet - Employees attribute set. Size: " + employees.size());

        // Forward to JSP page
        try {
            request.getRequestDispatcher("/task-mgt/assign-task.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error forwarding to JSP: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error loading assign task form. Please try again.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        }
    }

    /**
     * Handle POST request - create and assign the task
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        // Check if user has permission
        String userRole = user.getRole();
        boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
        boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);
        
        if (!isHRManager && !isDeptManager) {
            session.setAttribute("errorMessage", "Access denied. Only managers can assign tasks.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            // Get form parameters
            String taskTitle = request.getParameter("taskTitle");
            String taskDescription = request.getParameter("taskDescription");
            String assignedToStr = request.getParameter("assignedTo");
            String departmentIdStr = request.getParameter("departmentId");
            String priority = request.getParameter("priority");
            String startDateStr = request.getParameter("startDate");
            String dueDateStr = request.getParameter("dueDate");

            // Validate required fields
            if (taskTitle == null || taskTitle.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Task title is required.");
                response.sendRedirect(request.getContextPath() + "/task/assign");
                return;
            }

            if (assignedToStr == null || assignedToStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Please select an employee to assign the task.");
                response.sendRedirect(request.getContextPath() + "/task/assign");
                return;
            }

            if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Due date is required.");
                response.sendRedirect(request.getContextPath() + "/task/assign");
                return;
            }

            // Parse parameters
            int assignedTo = Integer.parseInt(assignedToStr);
            Integer departmentId = null;
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                departmentId = Integer.parseInt(departmentIdStr);
            }

            // Parse dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = new Date(dateFormat.parse(startDateStr).getTime());
            }
            Date dueDate = new Date(dateFormat.parse(dueDateStr).getTime());

            // Validate dates
            if (startDate != null && dueDate.before(startDate)) {
                session.setAttribute("errorMessage", "Due date cannot be before start date.");
                response.sendRedirect(request.getContextPath() + "/task/assign");
                return;
            }

            // Get assigned employee details
            Employee assignedEmployee = employeeDAO.getEmployeeById(assignedTo);
            if (assignedEmployee == null) {
                session.setAttribute("errorMessage", "Selected employee not found.");
                response.sendRedirect(request.getContextPath() + "/task/assign");
                return;
            }

            // Additional validation for department managers
            if (isDeptManager) {
                Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                
                if (managerEmployee != null) {
                    if (managerEmployee.getDepartmentID() != null && 
                        !managerEmployee.getDepartmentID().equals(assignedEmployee.getDepartmentID())) {
                        session.setAttribute("errorMessage", "You can only assign tasks to employees in your department.");
                        response.sendRedirect(request.getContextPath() + "/task/assign");
                        return;
                    }
                }
            }

            // If department_id is not specified in form, use the assigned employee's department
            if (departmentId == null && assignedEmployee.getDepartmentID() != null) {
                departmentId = assignedEmployee.getDepartmentID();
                System.out.println("AssignTaskServlet - Auto-setting department_id to: " + departmentId + " from assigned employee");
            }

            // Create task object
            Task task = new Task();
            task.setTaskTitle(taskTitle.trim());
            task.setTaskDescription(taskDescription != null ? taskDescription.trim() : "");
            task.setAssignedTo(assignedTo);
            int assignedByUserId = user.getUserId();
            task.setAssignedBy(assignedByUserId);
            task.setDepartmentId(departmentId);
            task.setPriority(priority != null && !priority.isEmpty() ? priority : "Medium");
            task.setStartDate(startDate);
            task.setDueDate(dueDate);
            task.setTaskStatus("Not Started");
            task.setProgressPercentage(0);

            System.out.println("AssignTaskServlet - Creating task:");
            System.out.println("  - Title: " + taskTitle);
            System.out.println("  - AssignedTo (Employee ID): " + assignedTo);
            System.out.println("  - AssignedBy (User ID): " + assignedByUserId);
            System.out.println("  - AssignedBy (User Name): " + user.getFirstName() + " " + user.getLastName());
            System.out.println("  - AssignedBy (User Email): " + user.getEmail());
            System.out.println("  - DepartmentId: " + departmentId);
            System.out.println("  - Priority: " + task.getPriority());
            System.out.println("  - TaskStatus: " + task.getTaskStatus());
            System.out.println("  - DueDate: " + dueDate);

            // Save to database
            int taskId = taskDAO.createTask(task);
            
            System.out.println("AssignTaskServlet - Task created with ID: " + taskId);

            if (taskId > 0) {
                session.setAttribute("successMessage", "Task assigned successfully!");
                response.sendRedirect(request.getContextPath() + "/task/list");
            } else {
                session.setAttribute("errorMessage", "Failed to assign task. Please try again.");
                response.sendRedirect(request.getContextPath() + "/task/assign");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid input format. Please check your entries.");
            response.sendRedirect(request.getContextPath() + "/task/assign");
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD.");
            response.sendRedirect(request.getContextPath() + "/task/assign");
        } catch (Exception e) {
            System.err.println("Error in AssignTaskServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while assigning the task.");
            response.sendRedirect(request.getContextPath() + "/task/assign");
        }
    }

    @Override
    public String getServletInfo() {
        return "Assign Task Servlet - UC39";
    }
}

