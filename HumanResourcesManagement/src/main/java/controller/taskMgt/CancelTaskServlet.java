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

/**
 * Servlet for cancelling a task
 * UC43: Cancel Task
 * Manager cancels a task if it's no longer needed
 * @author admin
 */
@WebServlet(name = "CancelTaskServlet", urlPatterns = {"/task/cancel"})
public class CancelTaskServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle POST request - cancel the task
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
            String cancellationReason = request.getParameter("cancellationReason");

            // Validate required fields
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

            // Check permission
            if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.TASK_CANCEL)) {
                request.setAttribute("errorMessage", "Bạn không có quyền hủy nhiệm vụ");
                request.getRequestDispatcher("/error/403.jsp").forward(request, response);
                return;
            }
            
            String userRole = user.getRole();
            boolean canCancel = false;
            boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
            boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);

            if (isHRManager) {
                canCancel = true;
            } else if (isDeptManager) {
                if (task.getAssignedBy() == user.getUserId()) {
                    canCancel = true;
                } else {
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                        if (task.getDepartmentId() != null && 
                            task.getDepartmentId().equals(managerEmployee.getDepartmentID())) {
                            canCancel = true;
                        }
                    }
                }
            }

            if (!canCancel) {
                request.setAttribute("errorMessage", "Bạn không có quyền hủy nhiệm vụ này");
                request.getRequestDispatcher("/error/403.jsp").forward(request, response);
                return;
            }

            // Check if task can be cancelled
            if (!task.canBeCancelled()) {
                session.setAttribute("errorMessage", "This task cannot be cancelled (status: " + task.getTaskStatus() + ").");
                response.sendRedirect(request.getContextPath() + "/task/detail?id=" + taskId);
                return;
            }

            // Cancel the task
            String reason = (cancellationReason != null && !cancellationReason.trim().isEmpty()) 
                            ? cancellationReason.trim() 
                            : "No reason provided";
            
            boolean success = taskDAO.cancelTask(taskId, user.getUserId(), reason);

            if (success) {
                session.setAttribute("successMessage", "Task cancelled successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to cancel task. Please try again.");
            }

            // Redirect back to task list or detail page
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && redirectUrl.contains("detail")) {
                response.sendRedirect(request.getContextPath() + "/task/detail?id=" + taskId);
            } else {
                response.sendRedirect(request.getContextPath() + "/task/list");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing task ID: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid task ID.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        } catch (Exception e) {
            System.err.println("Error in CancelTaskServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while cancelling the task.");
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
        return "Cancel Task Servlet - UC43";
    }
}

