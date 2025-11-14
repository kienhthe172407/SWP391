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

        // Check permission
        if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.TASK_VIEW_LIST)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xem danh sách nhiệm vụ");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }

        String userRole = user.getRole();
        List<Task> tasks = null;
        
        // Get filter parameters
        String statusFilter = request.getParameter("status");
        String priorityFilter = request.getParameter("priority");
        String viewParam = request.getParameter("view");

        // Determine default view based on role
        boolean isHRManager = "HR_MANAGER".equals(userRole) || "HR Manager".equals(userRole);
        boolean isDeptManager = "DEPT_MANAGER".equals(userRole) || "Dept Manager".equals(userRole);
        
        String viewType;
        if (viewParam != null && !viewParam.isEmpty()) {
            viewType = viewParam;
        } else {
            // Set default view based on role
            if (isHRManager) {
                viewType = "all_tasks"; // HR Manager sees all tasks by default
            } else if (isDeptManager) {
                viewType = "my_department"; // Dept Manager sees department tasks by default
            } else {
                viewType = "assigned_to_me"; // Employee sees only their tasks
            }
        }
        
        System.out.println("ViewTaskListServlet - Role: " + userRole + ", ViewType: " + viewType);

        try {
            if (isHRManager) {
                // HR Manager can view all tasks or filter by specific criteria
                if ("assigned_by_me".equals(viewType)) {
                    // Tasks assigned by this HR manager
                    if (statusFilter != null || priorityFilter != null) {
                        tasks = taskDAO.searchTasks(null, user.getUserId(), statusFilter, priorityFilter, null);
                    } else {
                        tasks = taskDAO.getTasksByAssignedBy(user.getUserId());
                    }
                } else if ("all_tasks".equals(viewType)) {
                    // All tasks in the system
                    if (statusFilter != null || priorityFilter != null) {
                        tasks = taskDAO.searchTasks(null, null, statusFilter, priorityFilter, null);
                    } else {
                        tasks = taskDAO.getAllTasks();
                    }
                } else {
                    // Default: all tasks
                    if (statusFilter != null || priorityFilter != null) {
                        tasks = taskDAO.searchTasks(null, null, statusFilter, priorityFilter, null);
                    } else {
                        tasks = taskDAO.getAllTasks();
                    }
                }
                System.out.println("ViewTaskListServlet - HR Manager loaded " + (tasks != null ? tasks.size() : 0) + " tasks");
                
            } else if (isDeptManager) {
                // Dept Manager can view department tasks or tasks assigned by them
                if ("assigned_by_me".equals(viewType)) {
                    // Tasks assigned by this manager
                    if (statusFilter != null || priorityFilter != null) {
                        tasks = taskDAO.searchTasks(null, user.getUserId(), statusFilter, priorityFilter, null);
                    } else {
                        tasks = taskDAO.getTasksByAssignedBy(user.getUserId());
                    }
                } else if ("my_department".equals(viewType)) {
                    // Tasks in manager's department
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                        if (statusFilter != null || priorityFilter != null) {
                            tasks = taskDAO.searchTasks(null, null, statusFilter, priorityFilter, managerEmployee.getDepartmentID());
                        } else {
                            tasks = taskDAO.getTasksByDepartment(managerEmployee.getDepartmentID());
                        }
                        System.out.println("ViewTaskListServlet - Dept Manager loaded " + (tasks != null ? tasks.size() : 0) + " tasks for department " + managerEmployee.getDepartmentID());
                    } else {
                        System.err.println("ViewTaskListServlet - Dept Manager has no department assigned");
                        tasks = List.of();
                    }
                } else {
                    // Default: department tasks
                    Employee managerEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
                    if (managerEmployee != null && managerEmployee.getDepartmentID() != null) {
                        if (statusFilter != null || priorityFilter != null) {
                            tasks = taskDAO.searchTasks(null, null, statusFilter, priorityFilter, managerEmployee.getDepartmentID());
                        } else {
                            tasks = taskDAO.getTasksByDepartment(managerEmployee.getDepartmentID());
                        }
                        System.out.println("ViewTaskListServlet - Dept Manager (default) loaded " + (tasks != null ? tasks.size() : 0) + " tasks for department " + managerEmployee.getDepartmentID());
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

            // Auto-start tasks if start date has passed and status is still "Not Started"
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    if (task.shouldAutoStart()) {
                        System.out.println("ViewTaskListServlet - Auto-starting task " + task.getTaskId() + 
                                         " (start date: " + task.getStartDate() + " has passed)");
                        boolean updated = taskDAO.updateTaskStatus(task.getTaskId(), "In Progress", 0, null);
                        if (updated) {
                            // Update task status in the list
                            task.setTaskStatus("In Progress");
                            System.out.println("ViewTaskListServlet - Task " + task.getTaskId() + " status auto-updated to: In Progress");
                        }
                    }
                }
            }

            // Get task statistics based on current view
            int notStartedCount = 0, inProgressCount = 0, doneCount = 0, blockedCount = 0, cancelledCount = 0;
            
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    String status = task.getTaskStatus();
                    if (status != null) {
                        switch (status) {
                            case "Not Started":
                                notStartedCount++;
                                break;
                            case "In Progress":
                                inProgressCount++;
                                break;
                            case "Done":
                                doneCount++;
                                break;
                            case "Blocked":
                                blockedCount++;
                                break;
                            case "Cancelled":
                                cancelledCount++;
                                break;
                        }
                    }
                }
            }
            
            request.setAttribute("notStartedCount", notStartedCount);
            request.setAttribute("inProgressCount", inProgressCount);
            request.setAttribute("doneCount", doneCount);
            request.setAttribute("blockedCount", blockedCount);
            request.setAttribute("cancelledCount", cancelledCount);

            // Set attributes for JSP
            request.setAttribute("tasks", tasks != null ? tasks : List.of());
            request.setAttribute("viewType", viewType);
            request.setAttribute("statusFilter", statusFilter != null ? statusFilter : "");
            request.setAttribute("priorityFilter", priorityFilter != null ? priorityFilter : "");
            request.setAttribute("userRole", userRole);
            
            System.out.println("ViewTaskListServlet - Statistics: Not Started=" + notStartedCount + 
                             ", In Progress=" + inProgressCount + ", Done=" + doneCount + 
                             ", Blocked=" + blockedCount + ", Cancelled=" + cancelledCount + 
                             ", Total=" + (tasks != null ? tasks.size() : 0));

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

