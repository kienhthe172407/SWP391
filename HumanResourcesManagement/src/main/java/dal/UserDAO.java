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

    /**
     * Create a new user with a BCrypt-hashed password. Returns created User with userId set, or null on failure.
     */
    public User createUser(String username, String plainPassword, String email, String role, String firstName, String lastName) {
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        String sql = "INSERT INTO users (username, password_hash, email, role, status, created_by, first_name, last_name) VALUES (?,?,?,?, 'Active', NULL, ?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.setString(3, email);
            ps.setString(4, role == null ? "Employee" : role);
            ps.setString(5, firstName);
            ps.setString(6, lastName);
            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        User u = new User(id, username, email, role == null ? "Employee" : role);
                        u.setFirstName(firstName);
                        u.setLastName(lastName);
                        return u;
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.createUser: " + ex.getMessage());
        }
        return null;
    }
}
