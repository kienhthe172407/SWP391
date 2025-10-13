package dao;

import .model.Task;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    
    // Assign new task
    public boolean assignTask(Task task) {
        String sql = "INSERT INTO tasks (task_name, description, assigned_to, assigned_by, " +
                     "status, priority, assigned_date, due_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getAssignedTo());
            pstmt.setInt(4, task.getAssignedBy());
            pstmt.setString(5, "PENDING");
            pstmt.setString(6, task.getPriority());
            pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(8, task.getDueDate());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all tasks assigned by a manager
    public List<Task> getTasksByManager(int managerId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, e.name as employee_name, m.name as manager_name " +
                     "FROM tasks t " +
                     "LEFT JOIN employees e ON t.assigned_to = e.employee_id " +
                     "LEFT JOIN employees m ON t.assigned_by = m.employee_id " +
                     "WHERE t.assigned_by = ? ORDER BY t.assigned_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tasks;
    }
    
    // Get all tasks assigned to an employee
    public List<Task> getTasksByEmployee(int employeeId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, e.name as employee_name, m.name as manager_name " +
                     "FROM tasks t " +
                     "LEFT JOIN employees e ON t.assigned_to = e.employee_id " +
                     "LEFT JOIN employees m ON t.assigned_by = m.employee_id " +
                     "WHERE t.assigned_to = ? ORDER BY t.assigned_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tasks;
    }
    
    // Get task by ID
    public Task getTaskById(int taskId) {
        String sql = "SELECT t.*, e.name as employee_name, m.name as manager_name " +
                     "FROM tasks t " +
                     "LEFT JOIN employees e ON t.assigned_to = e.employee_id " +
                     "LEFT JOIN employees m ON t.assigned_by = m.employee_id " +
                     "WHERE t.task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Update task status
    public boolean updateTaskStatus(int taskId, String status) {
        String sql = "UPDATE tasks SET status = ?";
        
        if ("COMPLETED".equals(status)) {
            sql += ", completed_date = ?";
        }
        
        sql += " WHERE task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            if ("COMPLETED".equals(status)) {
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setInt(3, taskId);
            } else {
                pstmt.setInt(2, taskId);
            }
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update task details
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET task_name = ?, description = ?, assigned_to = ?, " +
                     "priority = ?, due_date = ? WHERE task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getAssignedTo());
            pstmt.setString(4, task.getPriority());
            pstmt.setTimestamp(5, task.getDueDate());
            pstmt.setInt(6, task.getTaskId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cancel task
    public boolean cancelTask(int taskId) {
        return updateTaskStatus(taskId, "CANCELLED");
    }
    
    // Helper method to extract Task from ResultSet
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setTaskName(rs.getString("task_name"));
        task.setDescription(rs.getString("description"));
        task.setAssignedTo(rs.getInt("assigned_to"));
        task.setAssignedBy(rs.getInt("assigned_by"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setAssignedDate(rs.getTimestamp("assigned_date"));
        task.setDueDate(rs.getTimestamp("due_date"));
        task.setCompletedDate(rs.getTimestamp("completed_date"));
        task.setEmployeeName(rs.getString("employee_name"));
        task.setManagerName(rs.getString("manager_name"));
        return task;
    }
    
    // Get all employees for dropdown
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, name FROM employees WHERE status = 'ACTIVE' ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setName(rs.getString("name"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    // Inner class for Employee (basic info only)
    public static class Employee {
        private int employeeId;
        private String name;
        
        public int getEmployeeId() { return employeeId; }
        public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}