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
import java.util.List;

/**
 * Servlet for viewing task list
 * UC40: View Assigned Task List
 * Shows tasks assigned to the user (if employee) or tasks assigned by the user (if manager)
 * @author admin
 */
@WebServlet(name = "ViewTaskListServlet", urlPatterns = {"/task/list"})
public class ViewTaskListServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the task list
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

        String userRole = user.getRole();
        List<Task> tasks = null;
        String viewType = "assigned_to_me"; // Default view

        // Get filter parameters
        String statusFilter = request.getParameter("status");
        String priorityFilter = request.getParameter("priority");
        String viewParam = request.getParameter("view");

        if (viewParam != null && !viewParam.isEmpty()) {
            viewType = viewParam;
        }

        try {
            // Determine which tasks to show based on role and view type
            if ("HR_MANAGER".equals(userRole) || "DEPT_MANAGER".equals(userRole)) {
                // Managers can view tasks they assigned or tasks in their department
                if ("assigned_by_me".equals(viewType)) {
                    // Tasks assigned by this manager
                    if (statusFilter != null || priorityFilter != null) {
                        tasks = taskDAO.searchTasks(null, user.getUserId(), statusFilter, priorityFilter, null);
                    } else {
                        tasks = taskDAO.getTasksByAssignedBy(user.getUserId());
                    }
                } else if ("my_department".equals(viewType) && "DEPT_MANAGER".equals(userRole)) {
                    // Tasks in manager's department
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                        if (statusFilter != null || priorityFilter != null) {
                            tasks = taskDAO.searchTasks(null, null, statusFilter, priorityFilter, managerEmployee.getDepartmentID());
                        } else {
                            tasks = taskDAO.getTasksByDepartment(managerEmployee.getDepartmentID());
                        }
                    }
                } else {
                    // Default: tasks assigned to me (if manager also has employee record)
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null) {
                        if (statusFilter != null || priorityFilter != null) {
                            tasks = taskDAO.searchTasks(managerEmployee.getEmployeeID(), null, statusFilter, priorityFilter, null);
                        } else {
                            tasks = taskDAO.getTasksByAssignedTo(managerEmployee.getEmployeeID());
                        }
                    }
                }
            } else {
                // Regular employees can only view tasks assigned to them
                Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
                if (employee != null) {
                    if (statusFilter != null || priorityFilter != null) {
                        tasks = taskDAO.searchTasks(employee.getEmployeeID(), null, statusFilter, priorityFilter, null);
                    } else {
                        tasks = taskDAO.getTasksByAssignedTo(employee.getEmployeeID());
                    }
                } else {
                    request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
            }

            // Get task statistics if viewing own tasks
            if ("assigned_to_me".equals(viewType)) {
                Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
                if (employee != null) {
                    int[] taskCounts = taskDAO.getTaskCountsByStatus(employee.getEmployeeID());
                    request.setAttribute("notStartedCount", taskCounts[0]);
                    request.setAttribute("inProgressCount", taskCounts[1]);
                    request.setAttribute("doneCount", taskCounts[2]);
                    request.setAttribute("blockedCount", taskCounts[3]);
                    request.setAttribute("cancelledCount", taskCounts[4]);
                }
            }

            // Set attributes for JSP
            request.setAttribute("tasks", tasks != null ? tasks : List.of());
            request.setAttribute("viewType", viewType);
            request.setAttribute("statusFilter", statusFilter != null ? statusFilter : "");
            request.setAttribute("priorityFilter", priorityFilter != null ? priorityFilter : "");
            request.setAttribute("userRole", userRole);

            // Forward to JSP page
            request.getRequestDispatcher("/task-mgt/list-tasks.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in ViewTaskListServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading tasks.");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    /**
     * Handle POST request - same as GET
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Task List Servlet - UC40";
    }
}

