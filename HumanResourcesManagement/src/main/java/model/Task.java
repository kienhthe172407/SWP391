package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Task model class - mapped to tasks table
 * Represents tasks assigned to employees by managers
 * @author admin
 */
public class Task {
    // Primary fields from tasks table
    private int taskId;                         // task_id
    private String taskTitle;                   // task_title
    private String taskDescription;             // task_description
    private int assignedTo;                     // assigned_to (employee_id)
    private int assignedBy;                     // assigned_by (user_id)
    private Integer departmentId;               // department_id
    private String priority;                    // priority (Low, Medium, High, Urgent)
    private String taskStatus;                  // task_status (Not Started, In Progress, Done, Blocked, Cancelled)
    private Date startDate;                     // start_date
    private Date dueDate;                       // due_date
    private Date completedDate;                 // completed_date
    private int progressPercentage;             // progress_percentage (0-100)
    private String cancellationReason;          // cancellation_reason
    private Integer cancelledBy;                // cancelled_by (user_id)
    private Timestamp cancelledAt;              // cancelled_at
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                // updated_at
    private boolean isDeleted;                  // is_deleted
    
    // Additional fields for display (from JOINs)
    private String assignedToName;              // Employee name who is assigned
    private String assignedToCode;              // Employee code
    private String assignedByName;              // User name who assigned
    private String departmentName;              // Department name
    private String cancelledByName;             // User name who cancelled
    
    // Default constructor
    public Task() {
        this.progressPercentage = 0;
        this.taskStatus = "Not Started";
        this.priority = "Medium";
        this.isDeleted = false;
    }
    
    // Constructor for creating new task
    public Task(String taskTitle, String taskDescription, int assignedTo, int assignedBy,
                Integer departmentId, String priority, Date startDate, Date dueDate) {
        this();
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.departmentId = departmentId;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }
    
    // Full constructor
    public Task(int taskId, String taskTitle, String taskDescription, int assignedTo, int assignedBy,
                Integer departmentId, String priority, String taskStatus, Date startDate, Date dueDate,
                Date completedDate, int progressPercentage, String cancellationReason, Integer cancelledBy,
                Timestamp cancelledAt, Timestamp createdAt, Timestamp updatedAt, boolean isDeleted) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.departmentId = departmentId;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        this.progressPercentage = progressPercentage;
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(int assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Integer getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(Integer cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Timestamp getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Timestamp cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public String getAssignedToCode() {
        return assignedToCode;
    }

    public void setAssignedToCode(String assignedToCode) {
        this.assignedToCode = assignedToCode;
    }

    public String getAssignedByName() {
        return assignedByName;
    }

    public void setAssignedByName(String assignedByName) {
        this.assignedByName = assignedByName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCancelledByName() {
        return cancelledByName;
    }

    public void setCancelledByName(String cancelledByName) {
        this.cancelledByName = cancelledByName;
    }

    // Helper methods
    public boolean isNotStarted() {
        return "Not Started".equals(taskStatus);
    }

    public boolean isInProgress() {
        return "In Progress".equals(taskStatus);
    }

    public boolean isDone() {
        return "Done".equals(taskStatus);
    }

    public boolean isBlocked() {
        return "Blocked".equals(taskStatus);
    }

    public boolean isCancelled() {
        return "Cancelled".equals(taskStatus);
    }

    public boolean canBeEdited() {
        return !isCancelled() && !isDone();
    }

    public boolean canBeCancelled() {
        return !isCancelled() && !isDone();
    }

    public boolean isOverdue() {
        if (dueDate == null || isDone() || isCancelled()) {
            return false;
        }
        return dueDate.before(new Date(System.currentTimeMillis()));
    }

    public String getStatusBadgeClass() {
        switch (taskStatus) {
            case "Not Started":
                return "badge-secondary";
            case "In Progress":
                return "badge-pending"; // Yellow
            case "Done":
                return "badge-active"; // Green
            case "Blocked":
                return "badge-expired"; // Red
            case "Cancelled":
                return "badge-secondary";
            default:
                return "badge-secondary";
        }
    }

    public String getPriorityBadgeClass() {
        switch (priority) {
            case "Low":
                return "badge-secondary";
            case "Medium":
                return "badge-pending";
            case "High":
                return "badge-warning";
            case "Urgent":
                return "badge-expired";
            default:
                return "badge-secondary";
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", assignedTo=" + assignedTo +
                ", assignedBy=" + assignedBy +
                ", priority='" + priority + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", dueDate=" + dueDate +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}

