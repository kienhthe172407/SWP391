package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Data Access Object for users table
 */
public class UserDAO extends DBContext {

    public User getByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, email, role, status, created_at, first_name, last_name FROM users WHERE username = ?";
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
                    u.setFirstName(rs.getString("first_name"));
                    u.setLastName(rs.getString("last_name"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getByUsername: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Validate credentials using BCrypt hash stored in password_hash.
     * Returns the authenticated User on success, otherwise null.
     */
    public User authenticate(String username, String password) {
        User u = getByUsername(username);
        if (u == null) return null;
        String storedHash = u.getPasswordHash();
        if (storedHash == null || storedHash.isEmpty()) return null;
        try {
            if (BCrypt.checkpw(password, storedHash)) {
                return u;
            }
        } catch (IllegalArgumentException ex) {
            // In case stored hash is not a BCrypt hash, fall back to plain comparison
            if (storedHash.equals(password)) return u;
        }
        return null;
    }
}
