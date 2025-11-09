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
 * Servlet for viewing task details
 * Shows detailed information about a specific task
 * @author admin
 */
@WebServlet(name = "ViewTaskDetailServlet", urlPatterns = {"/task/detail"})
public class ViewTaskDetailServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display task details
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

            // Check if user has permission to view this task
            String userRole = user.getRole();
            boolean canView = false;
            boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
            boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);

            if (isHRManager) {
                canView = true; // HR Manager can view any task
            } else if (isDeptManager) {
                // Dept Manager can view tasks they assigned or in their department
                if (task.getAssignedBy() == user.getUserId()) {
                    canView = true;
                } else {
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                        if (task.getDepartmentId() != null && 
                            task.getDepartmentId().equals(managerEmployee.getDepartmentID())) {
                            canView = true;
                        }
                    }
                }
            } else {
                // Regular employees can view tasks assigned to them
                Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
                if (employee != null && employee.getEmployeeID() == task.getAssignedTo()) {
                    canView = true;
                }
            }

            if (!canView) {
                session.setAttribute("errorMessage", "You don't have permission to view this task.");
                response.sendRedirect(request.getContextPath() + "/task/list");
                return;
            }

            // Auto-start task if start date has passed and status is still "Not Started"
            if (task.shouldAutoStart()) {
                System.out.println("ViewTaskDetailServlet - Auto-starting task " + taskId + 
                                 " (start date: " + task.getStartDate() + " has passed)");
                boolean updated = taskDAO.updateTaskStatus(taskId, "In Progress", 0, null);
                if (updated) {
                    // Reload task to get updated status
                    task = taskDAO.getTaskById(taskId);
                    System.out.println("ViewTaskDetailServlet - Task status auto-updated to: " + task.getTaskStatus());
                }
            }

            // Determine user's permissions for this task
            boolean canEdit = false;
            boolean canCancel = false;
            boolean canUpdateStatus = false;

            Employee currentEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
            
            if (isHRManager) {
                canEdit = task.canBeEdited();
                canCancel = task.canBeCancelled();
                canUpdateStatus = !task.isCancelled();
            } else if (isDeptManager) {
                if (task.getAssignedBy() == user.getUserId()) {
                    canEdit = task.canBeEdited();
                    canCancel = task.canBeCancelled();
                }
                canUpdateStatus = !task.isCancelled();
            } else {
                // Regular employee assigned to the task
                if (currentEmployee != null && currentEmployee.getEmployeeID() == task.getAssignedTo()) {
                    canUpdateStatus = !task.isCancelled() && !task.isDone();
                }
            }

            // Set attributes for JSP
            request.setAttribute("task", task);
            request.setAttribute("canEdit", canEdit);
            request.setAttribute("canCancel", canCancel);
            request.setAttribute("canUpdateStatus", canUpdateStatus);
            request.setAttribute("userRole", userRole);

            // Forward to JSP page
            request.getRequestDispatcher("/task-mgt/task-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing task ID: " + e.getMessage());
            session.setAttribute("errorMessage", "Invalid task ID.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        } catch (Exception e) {
            System.err.println("Error in ViewTaskDetailServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the task.");
            response.sendRedirect(request.getContextPath() + "/task/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Task Detail Servlet";
    }
}

