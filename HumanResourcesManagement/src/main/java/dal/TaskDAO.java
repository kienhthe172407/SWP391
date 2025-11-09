package dal;

import model.Task;
import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for tasks table
 * Handles all database operations related to task management
 * @author admin
 */
public class TaskDAO extends DBContext {

    /**
     * Create a new task
     * @param task Task object to insert
     * @return Generated task ID, or -1 if failed
     */
    public int createTask(Task task) {
        String sql = "INSERT INTO tasks (task_title, task_description, assigned_to, assigned_by, " +
                     "department_id, priority, task_status, start_date, due_date, progress_percentage) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        System.out.println("TaskDAO.createTask - Input values:");
        System.out.println("  - Title: " + task.getTaskTitle());
        System.out.println("  - Description: " + (task.getTaskDescription() != null ? task.getTaskDescription().substring(0, Math.min(50, task.getTaskDescription().length())) : "null"));
        System.out.println("  - AssignedTo (Employee ID): " + task.getAssignedTo());
        System.out.println("  - AssignedBy (User ID): " + task.getAssignedBy());
        System.out.println("  - DepartmentId: " + task.getDepartmentId());
        System.out.println("  - Priority: " + task.getPriority());
        System.out.println("  - TaskStatus: " + task.getTaskStatus());
        System.out.println("  - StartDate: " + task.getStartDate());
        System.out.println("  - DueDate: " + task.getDueDate());
        System.out.println("  - ProgressPercentage: " + task.getProgressPercentage());
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getTaskTitle());
            ps.setString(2, task.getTaskDescription());
            ps.setInt(3, task.getAssignedTo());
            ps.setInt(4, task.getAssignedBy());
            
            if (task.getDepartmentId() != null) {
                ps.setInt(5, task.getDepartmentId());
                System.out.println("TaskDAO.createTask - Setting department_id: " + task.getDepartmentId());
            } else {
                ps.setNull(5, Types.INTEGER);
                System.out.println("TaskDAO.createTask - Setting department_id: NULL");
            }
            
            String taskStatus = task.getTaskStatus() != null ? task.getTaskStatus() : "Not Started";
            ps.setString(6, task.getPriority());
            ps.setString(7, taskStatus);
            ps.setDate(8, task.getStartDate());
            ps.setDate(9, task.getDueDate());
            ps.setInt(10, task.getProgressPercentage());
            
            System.out.println("TaskDAO.createTask - Executing INSERT with:");
            System.out.println("  - assigned_by: " + task.getAssignedBy());
            System.out.println("  - task_status: " + taskStatus);
            
            int affectedRows = ps.executeUpdate();
            System.out.println("TaskDAO.createTask - INSERT affected rows: " + affectedRows);
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int taskId = generatedKeys.getInt(1);
                        System.out.println("TaskDAO.createTask - Task created successfully with ID: " + taskId);
                        return taskId;
                    } else {
                        System.err.println("TaskDAO.createTask - No generated key returned!");
                    }
                }
            } else {
                System.err.println("TaskDAO.createTask - INSERT failed! No rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Error in createTask: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }

    /**
     * Get task by ID with full details
     * @param taskId Task ID
     * @return Task if found, null otherwise
     */
    public Task getTaskById(int taskId) {
        String sql = "SELECT t.task_id, t.task_title, t.task_description, t.assigned_to, t.assigned_by, " +
                     "t.department_id, t.priority, t.task_status, t.start_date, t.due_date, t.completed_date, " +
                     "t.progress_percentage, t.cancellation_reason, t.cancelled_by, t.cancelled_at, " +
                     "t.created_at, t.updated_at, t.is_deleted, " +
                     "TRIM(CONCAT(COALESCE(e.first_name, ''), ' ', COALESCE(e.last_name, ''))) AS assigned_to_name, " +
                     "e.employee_code AS assigned_to_code, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, ''))), ''), u.username) AS assigned_by_name, " +
                     "d.department_name, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(cu.first_name, ''), ' ', COALESCE(cu.last_name, ''))), ''), cu.username) AS cancelled_by_name " +
                     "FROM tasks t " +
                     "INNER JOIN employees e ON t.assigned_to = e.employee_id " +
                     "INNER JOIN users u ON t.assigned_by = u.user_id " +
                     "LEFT JOIN departments d ON t.department_id = d.department_id " +
                     "LEFT JOIN users cu ON t.cancelled_by = cu.user_id " +
                     "WHERE t.task_id = ? AND t.is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractTaskFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTaskById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get all tasks assigned to a specific employee
     * @param employeeId Employee ID
     * @return List of tasks
     */
    public List<Task> getTasksByAssignedTo(int employeeId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.task_id, t.task_title, t.task_description, t.assigned_to, t.assigned_by, " +
                     "t.department_id, t.priority, t.task_status, t.start_date, t.due_date, t.completed_date, " +
                     "t.progress_percentage, t.cancellation_reason, t.cancelled_by, t.cancelled_at, " +
                     "t.created_at, t.updated_at, t.is_deleted, " +
                     "TRIM(CONCAT(COALESCE(e.first_name, ''), ' ', COALESCE(e.last_name, ''))) AS assigned_to_name, " +
                     "e.employee_code AS assigned_to_code, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, ''))), ''), u.username) AS assigned_by_name, " +
                     "d.department_name, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(cu.first_name, ''), ' ', COALESCE(cu.last_name, ''))), ''), cu.username) AS cancelled_by_name " +
                     "FROM tasks t " +
                     "INNER JOIN employees e ON t.assigned_to = e.employee_id " +
                     "INNER JOIN users u ON t.assigned_by = u.user_id " +
                     "LEFT JOIN departments d ON t.department_id = d.department_id " +
                     "LEFT JOIN users cu ON t.cancelled_by = cu.user_id " +
                     "WHERE t.assigned_to = ? AND t.is_deleted = FALSE " +
                     "ORDER BY t.due_date ASC, t.priority DESC, t.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTasksByAssignedTo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tasks;
    }

    /**
     * Get all tasks assigned by a specific user (manager)
     * @param userId User ID
     * @return List of tasks
     */
    public List<Task> getTasksByAssignedBy(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.task_id, t.task_title, t.task_description, t.assigned_to, t.assigned_by, " +
                     "t.department_id, t.priority, t.task_status, t.start_date, t.due_date, t.completed_date, " +
                     "t.progress_percentage, t.cancellation_reason, t.cancelled_by, t.cancelled_at, " +
                     "t.created_at, t.updated_at, t.is_deleted, " +
                     "TRIM(CONCAT(COALESCE(e.first_name, ''), ' ', COALESCE(e.last_name, ''))) AS assigned_to_name, " +
                     "e.employee_code AS assigned_to_code, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, ''))), ''), u.username) AS assigned_by_name, " +
                     "d.department_name, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(cu.first_name, ''), ' ', COALESCE(cu.last_name, ''))), ''), cu.username) AS cancelled_by_name " +
                     "FROM tasks t " +
                     "INNER JOIN employees e ON t.assigned_to = e.employee_id " +
                     "INNER JOIN users u ON t.assigned_by = u.user_id " +
                     "LEFT JOIN departments d ON t.department_id = d.department_id " +
                     "LEFT JOIN users cu ON t.cancelled_by = cu.user_id " +
                     "WHERE t.assigned_by = ? AND t.is_deleted = FALSE " +
                     "ORDER BY t.due_date ASC, t.priority DESC, t.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTasksByAssignedBy: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tasks;
    }

    /**
     * Get tasks by department
     * @param departmentId Department ID
     * @return List of tasks
     */
    public List<Task> getTasksByDepartment(int departmentId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.task_id, t.task_title, t.task_description, t.assigned_to, t.assigned_by, " +
                     "t.department_id, t.priority, t.task_status, t.start_date, t.due_date, t.completed_date, " +
                     "t.progress_percentage, t.cancellation_reason, t.cancelled_by, t.cancelled_at, " +
                     "t.created_at, t.updated_at, t.is_deleted, " +
                     "TRIM(CONCAT(COALESCE(e.first_name, ''), ' ', COALESCE(e.last_name, ''))) AS assigned_to_name, " +
                     "e.employee_code AS assigned_to_code, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, ''))), ''), u.username) AS assigned_by_name, " +
                     "d.department_name, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(cu.first_name, ''), ' ', COALESCE(cu.last_name, ''))), ''), cu.username) AS cancelled_by_name " +
                     "FROM tasks t " +
                     "INNER JOIN employees e ON t.assigned_to = e.employee_id " +
                     "INNER JOIN users u ON t.assigned_by = u.user_id " +
                     "LEFT JOIN departments d ON t.department_id = d.department_id " +
                     "LEFT JOIN users cu ON t.cancelled_by = cu.user_id " +
                     "WHERE t.department_id = ? AND t.is_deleted = FALSE " +
                     "ORDER BY t.due_date ASC, t.priority DESC, t.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTasksByDepartment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tasks;
    }

    /**
     * Get all tasks in the system (for HR Manager)
     * @return List of all tasks
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.task_id, t.task_title, t.task_description, t.assigned_to, t.assigned_by, " +
                     "t.department_id, t.priority, t.task_status, t.start_date, t.due_date, t.completed_date, " +
                     "t.progress_percentage, t.cancellation_reason, t.cancelled_by, t.cancelled_at, " +
                     "t.created_at, t.updated_at, t.is_deleted, " +
                     "TRIM(CONCAT(COALESCE(e.first_name, ''), ' ', COALESCE(e.last_name, ''))) AS assigned_to_name, " +
                     "e.employee_code AS assigned_to_code, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, ''))), ''), u.username) AS assigned_by_name, " +
                     "d.department_name, " +
                     "COALESCE(NULLIF(TRIM(CONCAT(COALESCE(cu.first_name, ''), ' ', COALESCE(cu.last_name, ''))), ''), cu.username) AS cancelled_by_name " +
                     "FROM tasks t " +
                     "INNER JOIN employees e ON t.assigned_to = e.employee_id " +
                     "INNER JOIN users u ON t.assigned_by = u.user_id " +
                     "LEFT JOIN departments d ON t.department_id = d.department_id " +
                     "LEFT JOIN users cu ON t.cancelled_by = cu.user_id " +
                     "WHERE t.is_deleted = FALSE " +
                     "ORDER BY t.due_date ASC, t.priority DESC, t.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllTasks: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tasks;
    }

    /**
     * Search tasks with filters
     * @param assignedTo Employee ID (null for all)
     * @param assignedBy User ID (null for all)
     * @param status Task status (null for all)
     * @param priority Priority (null for all)
     * @param departmentId Department ID (null for all)
     * @return List of tasks
     */
    public List<Task> searchTasks(Integer assignedTo, Integer assignedBy, String status, 
                                   String priority, Integer departmentId) {
        List<Task> tasks = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.task_id, t.task_title, t.task_description, t.assigned_to, t.assigned_by, ");
        sql.append("t.department_id, t.priority, t.task_status, t.start_date, t.due_date, t.completed_date, ");
        sql.append("t.progress_percentage, t.cancellation_reason, t.cancelled_by, t.cancelled_at, ");
        sql.append("t.created_at, t.updated_at, t.is_deleted, ");
        sql.append("CONCAT(COALESCE(e.first_name, ''), ' ', COALESCE(e.last_name, '')) AS assigned_to_name, ");
        sql.append("e.employee_code AS assigned_to_code, ");
        sql.append("CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, '')) AS assigned_by_name, ");
        sql.append("d.department_name, ");
        sql.append("CONCAT(COALESCE(cu.first_name, ''), ' ', COALESCE(cu.last_name, '')) AS cancelled_by_name ");
        sql.append("FROM tasks t ");
        sql.append("INNER JOIN employees e ON t.assigned_to = e.employee_id ");
        sql.append("INNER JOIN users u ON t.assigned_by = u.user_id ");
        sql.append("LEFT JOIN departments d ON t.department_id = d.department_id ");
        sql.append("LEFT JOIN users cu ON t.cancelled_by = cu.user_id ");
        sql.append("WHERE t.is_deleted = FALSE ");
        
        if (assignedTo != null) {
            sql.append("AND t.assigned_to = ? ");
        }
        if (assignedBy != null) {
            sql.append("AND t.assigned_by = ? ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append("AND t.task_status = ? ");
        }
        if (priority != null && !priority.isEmpty()) {
            sql.append("AND t.priority = ? ");
        }
        if (departmentId != null) {
            sql.append("AND t.department_id = ? ");
        }
        
        sql.append("ORDER BY t.due_date ASC, t.priority DESC, t.created_at DESC");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (assignedTo != null) {
                ps.setInt(paramIndex++, assignedTo);
            }
            if (assignedBy != null) {
                ps.setInt(paramIndex++, assignedBy);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            if (priority != null && !priority.isEmpty()) {
                ps.setString(paramIndex++, priority);
            }
            if (departmentId != null) {
                ps.setInt(paramIndex++, departmentId);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchTasks: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tasks;
    }

    /**
     * Update task details
     * @param task Task object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET task_title = ?, task_description = ?, assigned_to = ?, " +
                     "department_id = ?, priority = ?, start_date = ?, due_date = ? " +
                     "WHERE task_id = ? AND is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, task.getTaskTitle());
            ps.setString(2, task.getTaskDescription());
            ps.setInt(3, task.getAssignedTo());
            
            if (task.getDepartmentId() != null) {
                ps.setInt(4, task.getDepartmentId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            
            ps.setString(5, task.getPriority());
            ps.setDate(6, task.getStartDate());
            ps.setDate(7, task.getDueDate());
            ps.setInt(8, task.getTaskId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateTask: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Update task status and progress
     * @param taskId Task ID
     * @param status New status
     * @param progressPercentage Progress percentage (0-100)
     * @param completedDate Completed date (null if not done)
     * @return true if successful, false otherwise
     */
    public boolean updateTaskStatus(int taskId, String status, int progressPercentage, Date completedDate) {
        String sql = "UPDATE tasks SET task_status = ?, progress_percentage = ?, completed_date = ? " +
                     "WHERE task_id = ? AND is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, progressPercentage);
            
            if (completedDate != null) {
                ps.setDate(3, completedDate);
            } else {
                ps.setNull(3, Types.DATE);
            }
            
            ps.setInt(4, taskId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateTaskStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Cancel a task
     * @param taskId Task ID
     * @param cancelledBy User ID who cancelled
     * @param cancellationReason Reason for cancellation
     * @return true if successful, false otherwise
     */
    public boolean cancelTask(int taskId, int cancelledBy, String cancellationReason) {
        String sql = "UPDATE tasks SET task_status = 'Cancelled', cancelled_by = ?, " +
                     "cancellation_reason = ?, cancelled_at = CURRENT_TIMESTAMP " +
                     "WHERE task_id = ? AND is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cancelledBy);
            ps.setString(2, cancellationReason);
            ps.setInt(3, taskId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in cancelTask: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Soft delete a task
     * @param taskId Task ID
     * @return true if successful, false otherwise
     */
    public boolean deleteTask(int taskId) {
        String sql = "UPDATE tasks SET is_deleted = TRUE WHERE task_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteTask: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get count of tasks by status for a specific employee
     * @param employeeId Employee ID
     * @return Array of counts [Not Started, In Progress, Done, Blocked, Cancelled]
     */
    public int[] getTaskCountsByStatus(int employeeId) {
        int[] counts = new int[5]; // Not Started, In Progress, Done, Blocked, Cancelled
        String sql = "SELECT task_status, COUNT(*) as count FROM tasks " +
                     "WHERE assigned_to = ? AND is_deleted = FALSE " +
                     "GROUP BY task_status";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getString("task_status");
                    int count = rs.getInt("count");
                    
                    switch (status) {
                        case "Not Started":
                            counts[0] = count;
                            break;
                        case "In Progress":
                            counts[1] = count;
                            break;
                        case "Done":
                            counts[2] = count;
                            break;
                        case "Blocked":
                            counts[3] = count;
                            break;
                        case "Cancelled":
                            counts[4] = count;
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTaskCountsByStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        return counts;
    }

    /**
     * Extract Task object from ResultSet
     * @param rs ResultSet
     * @return Task object
     * @throws SQLException if error occurs
     */
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setTaskTitle(rs.getString("task_title"));
        task.setTaskDescription(rs.getString("task_description"));
        task.setAssignedTo(rs.getInt("assigned_to"));
        int assignedByUserId = rs.getInt("assigned_by");
        task.setAssignedBy(assignedByUserId);
        
        int deptId = rs.getInt("department_id");
        if (!rs.wasNull()) {
            task.setDepartmentId(deptId);
        }
        
        task.setPriority(rs.getString("priority"));
        String taskStatusRaw = rs.getString("task_status");
        boolean taskStatusWasNull = rs.wasNull();
        
        // Debug logging for task_status (before setting default)
        System.out.println("TaskDAO.extractTaskFromResultSet - Task Status Debug:");
        System.out.println("  - task_id: " + task.getTaskId());
        System.out.println("  - task_status (raw from DB): " + (taskStatusRaw != null ? taskStatusRaw : "NULL"));
        System.out.println("  - task_status wasNull(): " + taskStatusWasNull);
        
        // Ensure task_status is never null - default to "Not Started"
        if (taskStatusRaw == null || taskStatusRaw.isEmpty() || taskStatusWasNull) {
            System.out.println("TaskDAO.extractTaskFromResultSet - ⚠️ WARNING: task_status is NULL/EMPTY, setting default to 'Not Started'");
            taskStatusRaw = "Not Started";
        }
        task.setTaskStatus(taskStatusRaw);
        System.out.println("  - task.taskStatus (final): " + task.getTaskStatus());
        task.setStartDate(rs.getDate("start_date"));
        task.setDueDate(rs.getDate("due_date"));
        task.setCompletedDate(rs.getDate("completed_date"));
        task.setProgressPercentage(rs.getInt("progress_percentage"));
        task.setCancellationReason(rs.getString("cancellation_reason"));
        
        int cancelledBy = rs.getInt("cancelled_by");
        if (!rs.wasNull()) {
            task.setCancelledBy(cancelledBy);
        }
        
        task.setCancelledAt(rs.getTimestamp("cancelled_at"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        task.setDeleted(rs.getBoolean("is_deleted"));
        
        // Additional display fields
        task.setAssignedToName(rs.getString("assigned_to_name"));
        task.setAssignedToCode(rs.getString("assigned_to_code"));
        
        // Check raw values from ResultSet before setting
        String assignedByName = rs.getString("assigned_by_name");
        boolean assignedByNameWasNull = rs.wasNull();
        
        // Try to get user info directly from ResultSet if available
        try {
            String userFirstName = rs.getString("u.first_name");
            String userLastName = rs.getString("u.last_name");
            System.out.println("TaskDAO.extractTaskFromResultSet - Raw ResultSet values:");
            System.out.println("  - assigned_by (User ID): " + assignedByUserId);
            System.out.println("  - u.first_name (raw): " + (userFirstName != null ? userFirstName : "NULL"));
            System.out.println("  - u.last_name (raw): " + (userLastName != null ? userLastName : "NULL"));
            System.out.println("  - assigned_by_name (raw): " + (assignedByName != null ? assignedByName : "NULL"));
            System.out.println("  - assigned_by_name wasNull(): " + assignedByNameWasNull);
            System.out.println("  - task_status (raw): " + (taskStatusRaw != null ? taskStatusRaw : "NULL"));
        } catch (Exception e) {
            // Column not available in ResultSet, skip
            System.out.println("TaskDAO.extractTaskFromResultSet - Raw ResultSet values:");
            System.out.println("  - assigned_by (User ID): " + assignedByUserId);
            System.out.println("  - assigned_by_name (raw): " + (assignedByName != null ? assignedByName : "NULL"));
            System.out.println("  - assigned_by_name wasNull(): " + assignedByNameWasNull);
            System.out.println("  - task_status (raw): " + (taskStatusRaw != null ? taskStatusRaw : "NULL"));
        }
        
        task.setAssignedByName(assignedByName);
        task.setDepartmentName(rs.getString("department_name"));
        task.setCancelledByName(rs.getString("cancelled_by_name"));
        
        // Debug logging - log all values for newly created tasks
        System.out.println("TaskDAO.extractTaskFromResultSet - Extracted task:");
        System.out.println("  - task_id: " + task.getTaskId());
        System.out.println("  - task_title: " + task.getTaskTitle());
        System.out.println("  - assigned_to (Employee ID): " + task.getAssignedTo());
        System.out.println("  - assigned_to_name: " + task.getAssignedToName());
        System.out.println("  - assigned_by (User ID): " + task.getAssignedBy());
        System.out.println("  - assigned_by_name: " + (assignedByName != null ? assignedByName : "NULL"));
        System.out.println("  - task_status: " + (task.getTaskStatus() != null ? task.getTaskStatus() : "NULL"));
        System.out.println("  - priority: " + task.getPriority());
        System.out.println("  - department_id: " + task.getDepartmentId());
        System.out.println("  - department_name: " + task.getDepartmentName());
        
        // Warning logging
        if (assignedByName == null || assignedByName.isEmpty()) {
            System.err.println("TaskDAO.extractTaskFromResultSet - ⚠️ WARNING: assigned_by_name is NULL/EMPTY!");
            System.err.println("  - task_id: " + task.getTaskId());
            System.err.println("  - assigned_by (User ID from DB): " + task.getAssignedBy());
            System.err.println("  - assigned_by_name from ResultSet: " + assignedByName);
            System.err.println("  - This means JOIN with users table failed or user not found!");
        }
        if (task.getTaskStatus() == null || task.getTaskStatus().isEmpty()) {
            System.err.println("TaskDAO.extractTaskFromResultSet - ⚠️ WARNING: task_status is NULL/EMPTY!");
            System.err.println("  - task_id: " + task.getTaskId());
            System.err.println("  - task_status from ResultSet: " + task.getTaskStatus());
        }
        
        return task;
    }
}

