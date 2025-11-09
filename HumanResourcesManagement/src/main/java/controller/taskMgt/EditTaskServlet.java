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
 * Servlet for editing task details
 * UC42: Edit Task Details
 * Manager (or assignee, if allowed) updates task information
 * @author admin
 */
@WebServlet(name = "EditTaskServlet", urlPatterns = {"/task/edit"})
public class EditTaskServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the edit task form
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

        try {
            // Get task ID
            String taskIdStr = request.getParameter("id");
            if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Task ID is required.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            int taskId = Integer.parseInt(taskIdStr);
            Task task = taskDAO.getTaskById(taskId);

            if (task == null) {
                session.setAttribute("errorMessage", "Task not found.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Check if user has permission to edit
            String userRole = user.getRole();
            boolean canEdit = false;
            boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
            boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);

            if (isHRManager) {
                canEdit = true; // HR Manager can edit any task
            } else if (isDeptManager) {
                // Dept Manager can edit tasks they assigned
                if (task.getAssignedBy() == user.getUserId()) {
                    canEdit = true;
                }
            } else {
                // Regular employees can edit tasks assigned to them (if allowed)
                Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
                if (employee != null && employee.getEmployeeID() == task.getAssignedTo()) {
                    // Employees can only update certain fields (handled in POST)
                    canEdit = true;
                }
            }

            if (!canEdit) {
                session.setAttribute("errorMessage", "You don't have permission to edit this task.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Check if task can be edited
            if (!task.canBeEdited()) {
                session.setAttribute("errorMessage", "This task cannot be edited (status: " + task.getTaskStatus() + ").");
                response.sendRedirect(request.getContextPath() + "/task/detail?id=" + taskId);
                return;
            }

            // Get list of employees for reassignment (managers only)
            if (isHRManager || isDeptManager) {
                List<Employee> employees;
                if (isDeptManager) {
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                        employees = employeeDAO.getEmployeesByDepartment(managerEmployee.getDepartmentID());
                    } else {
                        employees = employeeDAO.getAllActiveEmployees();
                    }
                } else {
                    employees = employeeDAO.getAllActiveEmployees();
                }
                request.setAttribute("employees", employees);
            }

            // Get list of departments
            List<Department> departments = employeeDAO.getAllDepartments();

            // Set attributes for JSP
            request.setAttribute("task", task);
            request.setAttribute("departments", departments);
            request.setAttribute("userRole", userRole);

            // Forward to JSP page
            request.getRequestDispatcher("/task-mgt/edit-task.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing task ID: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid task ID.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        } catch (Exception e) {
            System.err.println("Error in EditTaskServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the task.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        }
    }

    /**
     * Handle POST request - update the task
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

        try {
            // Get form parameters
            String taskIdStr = request.getParameter("taskId");
            String taskTitle = request.getParameter("taskTitle");
            String taskDescription = request.getParameter("taskDescription");
            String assignedToStr = request.getParameter("assignedTo");
            String departmentIdStr = request.getParameter("departmentId");
            String priority = request.getParameter("priority");
            String startDateStr = request.getParameter("startDate");
            String dueDateStr = request.getParameter("dueDate");

            // Validate required fields
            if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Task ID is required.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            int taskId = Integer.parseInt(taskIdStr);
            Task existingTask = taskDAO.getTaskById(taskId);

            if (existingTask == null) {
                session.setAttribute("errorMessage", "Task not found.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Check permissions
            String userRole = user.getRole();
            boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
            boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);
            boolean isManager = isHRManager || isDeptManager;
            boolean isAssignee = false;
            
            Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (employee != null && employee.getEmployeeID() == existingTask.getAssignedTo()) {
                isAssignee = true;
            }

            if (!isManager && !isAssignee) {
                session.setAttribute("errorMessage", "You don't have permission to edit this task.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Validate required fields
            if (taskTitle == null || taskTitle.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Task title is required.");
                response.sendRedirect(request.getContextPath() + "/task/edit?id=" + taskId);
                return;
            }

            if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Due date is required.");
                response.sendRedirect(request.getContextPath() + "/task/edit?id=" + taskId);
                return;
            }

            // Parse parameters
            int assignedTo = existingTask.getAssignedTo(); // Default to existing
            if (isManager && assignedToStr != null && !assignedToStr.trim().isEmpty()) {
                assignedTo = Integer.parseInt(assignedToStr);
            }

            Integer departmentId = existingTask.getDepartmentId();
            if (isManager && departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                departmentId = Integer.parseInt(departmentIdStr);
            }

            // Parse dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = existingTask.getStartDate();
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = new Date(dateFormat.parse(startDateStr).getTime());
            }
            Date dueDate = new Date(dateFormat.parse(dueDateStr).getTime());

            // Validate dates
            if (startDate != null && dueDate.before(startDate)) {
                session.setAttribute("errorMessage", "Due date cannot be before start date.");
                response.sendRedirect(request.getContextPath() + "/task/edit?id=" + taskId);
                return;
            }

            // Update task object
            existingTask.setTaskTitle(taskTitle.trim());
            existingTask.setTaskDescription(taskDescription != null ? taskDescription.trim() : "");
            existingTask.setAssignedTo(assignedTo);
            existingTask.setDepartmentId(departmentId);
            existingTask.setPriority(priority != null && !priority.isEmpty() ? priority : "Medium");
            existingTask.setStartDate(startDate);
            existingTask.setDueDate(dueDate);

            // Update in database
            boolean success = taskDAO.updateTask(existingTask);

            if (success) {
                session.setAttribute("successMessage", "Task updated successfully!");
                response.sendRedirect(request.getContextPath() + "/task/detail?id=" + taskId);
            } else {
                session.setAttribute("errorMessage", "Failed to update task. Please try again.");
                response.sendRedirect(request.getContextPath() + "/task/edit?id=" + taskId);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid input format. Please check your entries.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        } catch (Exception e) {
            System.err.println("Error in EditTaskServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating the task.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Task Servlet - UC42";
    }
}

