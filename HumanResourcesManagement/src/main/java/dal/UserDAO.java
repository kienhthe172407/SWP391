package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

/**
 * Data Access Object for users table
 */
public class UserDAO extends DBContext {

    public User getByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, email, role, status, created_at FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserID(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getString("status"));
                    u.setCreatedAt(rs.getTimestamp("created_at"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getByUsername: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Basic credential check. Assumes passwordHash stores plain or hashed password.
     * TODO: Use a secure password hashing (BCrypt) and compare properly.
     */
    public boolean validateCredentials(String username, String password) {
        User u = getByUsername(username);
        if (u == null) return false;
        String stored = u.getPasswordHash();
        if (stored == null) return false;
        // For now, compare directly (if stored as plain) or you can adapt to hash compare.
        return stored.equals(password);
    }
}
