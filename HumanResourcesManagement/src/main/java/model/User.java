package model;

import java.sql.Timestamp;

public class User {
    private int userID;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private String status;
    private Timestamp createdAt;
    // Optional display names
    private String firstName;
    private String lastName;

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // Helper for JSP: ${user.fullName}
    public String getFullName() {
        if (firstName != null && !firstName.isEmpty()) {
            if (lastName != null && !lastName.isEmpty()) {
                return firstName + " " + lastName;
            }
            return firstName;
        }
        if (username != null && !username.isEmpty()) return username;
        return "Người dùng";
    }
}
