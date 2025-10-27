package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Data Access Object for users table
 */
public class UserDAO extends DBContext {

    public User getByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users WHERE username = ?";
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
                    try { u.setPhone(rs.getString("phone")); } catch (SQLException ignore) {}
                    try { u.setDateOfBirth(rs.getDate("date_of_birth")); } catch (SQLException ignore) {}
                    try { u.setGender(rs.getString("gender")); } catch (SQLException ignore) {}
                    return u;
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getByUsername: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public User getByEmail(String email) {
        String sql = "SELECT user_id, username, password_hash, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
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
                    try { u.setPhone(rs.getString("phone")); } catch (SQLException ignore) {}
                    try { u.setDateOfBirth(rs.getDate("date_of_birth")); } catch (SQLException ignore) {}
                    try { u.setGender(rs.getString("gender")); } catch (SQLException ignore) {}
                    return u;
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getByEmail: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Update user's password with a new BCrypt-hashed password. Returns true if updated.
     */
    public boolean updatePassword(int userId, String newPlainPassword) {
        if (newPlainPassword == null || newPlainPassword.length() < 8) {
            return false;
        }
        String hashed = BCrypt.hashpw(newPlainPassword, BCrypt.gensalt(10));
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashed);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException ex) {
            System.err.println("UserDAO.updatePassword: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
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
    public User createUser(String username, String plainPassword, String email, String role, String firstName, String lastName, String phone, java.sql.Date dateOfBirth, String gender) {
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        String sql = "INSERT INTO users (username, password_hash, email, role, status, created_by, first_name, last_name, phone, date_of_birth, gender) VALUES (?,?,?,?, 'Active', NULL, ?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.setString(3, email);
            ps.setString(4, role == null ? "Employee" : role);
            ps.setString(5, firstName);
            ps.setString(6, lastName);
            ps.setString(7, phone);
            ps.setDate(8, dateOfBirth);
            ps.setString(9, gender);
            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        User u = new User(id, username, email, role == null ? "Employee" : role);
                        u.setFirstName(firstName);
                        u.setLastName(lastName);
                        u.setPhone(phone);
                        u.setDateOfBirth(dateOfBirth);
                        u.setGender(gender);
                        return u;
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.createUser: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Update basic profile fields for a user. Returns true if one row affected.
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET email = ?, first_name = ?, last_name = ?, phone = ?, date_of_birth = ?, gender = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getPhone());
            ps.setDate(5, user.getDateOfBirth());
            ps.setString(6, user.getGender());
            ps.setInt(7, user.getUserID());
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException ex) {
            System.err.println("UserDAO.updateProfile: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy danh sách tất cả người dùng trong hệ thống
     * @return List<User> danh sách người dùng
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users ORDER BY user_id ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhone(rs.getString("phone"));
                user.setDateOfBirth(rs.getDate("date_of_birth"));
                user.setGender(rs.getString("gender"));
                users.add(user);
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getAllUsers: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return users;
    }
    
    /**
     * Lấy danh sách người dùng theo vai trò
     * @param role vai trò cần lọc
     * @return List<User> danh sách người dùng
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users WHERE role = ? ORDER BY user_id ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setDateOfBirth(rs.getDate("date_of_birth"));
                    user.setGender(rs.getString("gender"));
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getUsersByRole: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return users;
    }
    
    /**
     * Cập nhật trạng thái của người dùng (Active/Inactive)
     * @param userId ID của người dùng
     * @param status trạng thái mới
     * @return true nếu cập nhật thành công
     */
    public boolean updateUserStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException ex) {
            System.err.println("UserDAO.updateUserStatus: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật vai trò của người dùng
     * @param userId ID của người dùng
     * @param role vai trò mới
     * @return true nếu cập nhật thành công
     */
    public boolean updateUserRole(int userId, String role) {
        String sql = "UPDATE users SET role = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException ex) {
            System.err.println("UserDAO.updateUserRole: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa người dùng (soft delete - chỉ đổi status thành Inactive)
     * @param userId ID của người dùng
     * @return true nếu xóa thành công
     */
    public boolean deleteUser(int userId) {
        return updateUserStatus(userId, "Inactive");
    }
    
    /**
     * Tìm kiếm người dùng theo tên hoặc email
     * @param searchTerm từ khóa tìm kiếm
     * @return List<User> danh sách người dùng tìm được
     */
    public List<User> searchUsers(String searchTerm) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender " +
                    "FROM users WHERE username LIKE ? OR email LIKE ? OR first_name LIKE ? OR last_name LIKE ? " +
                    "ORDER BY created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setDateOfBirth(rs.getDate("date_of_birth"));
                    user.setGender(rs.getString("gender"));
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.searchUsers: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return users;
    }
    
    /**
     * Lấy thông tin người dùng theo ID
     * @param userId ID của người dùng
     * @return User object hoặc null nếu không tìm thấy
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users WHERE user_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setDateOfBirth(rs.getDate("date_of_birth"));
                    user.setGender(rs.getString("gender"));
                    return user;
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getUserById: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Đếm tổng số người dùng
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getTotalUsers: " + ex.getMessage());
            ex.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Lấy danh sách người dùng theo trang
     */
    public List<User> getUsersByPage(int page, int pageSize) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender " +
                    "FROM users ORDER BY user_id ASC LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setDateOfBirth(rs.getDate("date_of_birth"));
                    user.setGender(rs.getString("gender"));
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getUsersByPage: " + ex.getMessage());
            ex.printStackTrace();
        }
        return users;
    }
    
    /**
     * Lấy danh sách người dùng theo role với phân trang
     */
    public List<User> getUsersByRoleAndPage(String role, int page, int pageSize) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender " +
                    "FROM users WHERE role = ? ORDER BY user_id ASC LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setDateOfBirth(rs.getDate("date_of_birth"));
                    user.setGender(rs.getString("gender"));
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getUsersByRoleAndPage: " + ex.getMessage());
            ex.printStackTrace();
        }
        return users;
    }
    
    /**
     * Đếm số lượng users theo role
     */
    public int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.countUsersByRole: " + ex.getMessage());
            ex.printStackTrace();
        }
        return 0;
    }
}
