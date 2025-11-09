package controller.taskMgt;

import dal.TaskDAO;
import dal.EmployeeDAO;
import model.Task;
import model.Employee;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

/**
 * Servlet for updating task status and progress
 * UC41: Update Task Status
 * The person assigned the task can update its progress
 * @author admin
 */
@WebServlet(name = "UpdateTaskStatusServlet", urlPatterns = {"/task/update-status"})
public class UpdateTaskStatusServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle POST request - update task status
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
            String status = request.getParameter("status");
            String progressStr = request.getParameter("progress");

            // Validate required fields
            if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Task ID is required.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            if (status == null || status.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Status is required.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            int taskId = Integer.parseInt(taskIdStr);
            int progress = 0;
            if (progressStr != null && !progressStr.trim().isEmpty()) {
                progress = Integer.parseInt(progressStr);
                // Validate progress range
                if (progress < 0 || progress > 100) {
                    session.setAttribute("errorMessage", "Progress must be between 0 and 100.");
                    response.sendRedirect(request.getContextPath() + "/task/list");
                    return;
                }
            }

            // Get the task
            Task task = taskDAO.getTaskById(taskId);
            if (task == null) {
                session.setAttribute("errorMessage", "Task not found.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Check if user is assigned to this task
            Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (employee == null || employee.getEmployeeID() != task.getAssignedTo()) {
                // Allow managers to update status too
                String userRole = user.getRole();
                boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
                boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);
                
                if (!isHRManager && !isDeptManager) {
                    session.setAttribute("errorMessage", "You can only update status of tasks assigned to you.");
                    response.sendRedirect(request.getContextPath() + "/task/list");
                    return;
                }
            }

            // Check if task can be updated
            if (task.isCancelled()) {
                session.setAttribute("errorMessage", "Cannot update status of a cancelled task.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Set completed date if status is Done
            Date completedDate = null;
            if ("Done".equals(status)) {
                completedDate = new Date(System.currentTimeMillis());
                progress = 100; // Automatically set progress to 100% when done
            } else if ("Not Started".equals(status)) {
                progress = 0; // Reset progress when not started
            }

            // Update task status
            boolean success = taskDAO.updateTaskStatus(taskId, status, progress, completedDate);

            if (success) {
                session.setAttribute("successMessage", "Task status updated successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to update task status. Please try again.");
            }

            // Redirect back to task list or detail page
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && redirectUrl.contains("detail")) {
                response.sendRedirect(request.getContextPath() + "/task/detail?id=" + taskId);
            } else {
                response.sendRedirect(request.getContextPath() + "/task/list");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid input format. Please check your entries.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        } catch (Exception e) {
            System.err.println("Error in UpdateTaskStatusServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating task status.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        }
    }

    /**
     * Handle GET request - redirect to task list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/task/list");
    }

    @Override
    public String getServletInfo() {
        return "Update Task Status Servlet - UC41";
    }
}

