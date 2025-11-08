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
        if (!"HR_MANAGER".equals(userRole) && !"DEPT_MANAGER".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only managers can assign tasks.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Get list of employees for assignment
        List<Employee> employees;
        if ("DEPT_MANAGER".equals(userRole)) {
            // Department manager can only assign to employees in their department
            Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                employees = employeeDAO.getEmployeesByDepartment(managerEmployee.getDepartmentID());
            } else {
                employees = employeeDAO.getAllActiveEmployees();
            }
        } else {
            // HR Manager can assign to any employee
            employees = employeeDAO.getAllActiveEmployees();
        }

        // Get list of departments
        List<Department> departments = employeeDAO.getAllDepartments();

        // Set attributes for JSP
        request.setAttribute("employees", employees);
        request.setAttribute("departments", departments);

        // Forward to JSP page
        request.getRequestDispatcher("/task-mgt/assign-task.jsp").forward(request, response);
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
        if (!"HR_MANAGER".equals(userRole) && !"DEPT_MANAGER".equals(userRole)) {
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

            // Additional validation for department managers
            if ("DEPT_MANAGER".equals(userRole)) {
                Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                Employee assignedEmployee = employeeDAO.getEmployeeById(assignedTo);
                
                if (managerEmployee != null && assignedEmployee != null) {
                    if (managerEmployee.getDepartmentID() != null && 
                        !managerEmployee.getDepartmentID().equals(assignedEmployee.getDepartmentID())) {
                        session.setAttribute("errorMessage", "You can only assign tasks to employees in your department.");
                        response.sendRedirect(request.getContextPath() + "/task/assign");
                        return;
                    }
                }
            }

            // Create task object
            Task task = new Task();
            task.setTaskTitle(taskTitle.trim());
            task.setTaskDescription(taskDescription != null ? taskDescription.trim() : "");
            task.setAssignedTo(assignedTo);
            task.setAssignedBy(user.getUserId());
            task.setDepartmentId(departmentId);
            task.setPriority(priority != null && !priority.isEmpty() ? priority : "Medium");
            task.setStartDate(startDate);
            task.setDueDate(dueDate);
            task.setTaskStatus("Not Started");
            task.setProgressPercentage(0);

            // Save to database
            int taskId = taskDAO.createTask(task);

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

