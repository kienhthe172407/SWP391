package model;

import java.sql.Timestamp;

/**
 * User model class
 * @author admin
 */
public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private String status; // 'Active' or 'Inactive'
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;
    private int failedLoginAttempts;
    private Timestamp lockedUntil;
    
    // Constructors
    public User() {}
    
    public User(int userId, String username, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return "Active".equals(status);
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
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
    
    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }
    
    public Timestamp getLockedUntil() {
        return lockedUntil;
    }
    
    public void setLockedUntil(Timestamp lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    // Helper methods
    public String getDisplayName() {
        return username + " (" + email + ")";
    }
    
    public String getRoleDisplayName() {
        switch (role) {
            case "HR Manager": return "HR Manager";
            case "HR": return "HR Staff";
            case "Dept Manager": return "Department Manager";
            case "Employee": return "Employee";
            case "Admin": return "Administrator";
            case "Guest": return "Guest";
            default: return role;
        }
    }
}
