package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Data Access Object cho bảng users
 */
public class UserDAO extends DBContext {

    /**
     * Lấy thông tin người dùng theo username
     * @param username tên đăng nhập
     * @return User object hoặc null nếu không tìm thấy
     */
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

    /**
     * Lấy thông tin người dùng theo email
     * @param email địa chỉ email
     * @return User object hoặc null nếu không tìm thấy
     */
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
     * Cập nhật mật khẩu người dùng với mã hóa BCrypt
     * @param userId ID của người dùng
     * @param newPlainPassword mật khẩu mới (chưa mã hóa)
     * @return true nếu cập nhật thành công
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
     * Xác thực thông tin đăng nhập sử dụng BCrypt
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return User object nếu xác thực thành công, ngược lại null
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
            // Trường hợp hash không phải BCrypt, so sánh trực tiếp
            if (storedHash.equals(password)) return u;
        }
        return null;
    }

    /**
     * Tạo người dùng mới với mật khẩu được mã hóa BCrypt
     * @return User object với userId đã được set, hoặc null nếu thất bại
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
     * Cập nhật thông tin cơ bản của người dùng
     * @param user đối tượng User chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công
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
     * Lấy danh sách tất cả người dùng trong hệ thống (không bao gồm người dùng đã xóa)
     * @return List<User> danh sách người dùng
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users WHERE status != 'Deleted' ORDER BY user_id ASC";
        
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
     * Lấy danh sách người dùng theo vai trò (không bao gồm người dùng đã xóa)
     * @param role vai trò cần lọc
     * @return List<User> danh sách người dùng
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender FROM users WHERE role = ? AND status != 'Deleted' ORDER BY user_id ASC";
        
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
     * Xóa người dùng vĩnh viễn khỏi database và sắp xếp lại ID (hard delete)
     * Tự động xử lý foreign key constraints
     * @param userId ID của người dùng
     * @return true nếu xóa thành công
     */
    public boolean deleteUser(int userId) {
        try {
            // Tắt foreign key checks tạm thời
            try (PreparedStatement ps = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                ps.executeUpdate();
            }
            
            // Soft delete user
            String deleteSql = "UPDATE users SET is_deleted = TRUE WHERE user_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
                ps.setInt(1, userId);
                int affected = ps.executeUpdate();
                if (affected != 1) {
                    // Bật lại foreign key checks
                    try (PreparedStatement ps2 = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                        ps2.executeUpdate();
                    }
                    return false;
                }
            }
            
            // Cập nhật lại ID của các user có ID lớn hơn user vừa xóa
            String updateSql = "UPDATE users SET user_id = user_id - 1 WHERE user_id > ?";
            try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }
            
            // Bước 3: Cập nhật foreign keys trong các bảng liên quan
            // Cập nhật created_by trong bảng users
            String updateCreatedBySql = "UPDATE users SET created_by = created_by - 1 WHERE created_by > ?";
            try (PreparedStatement ps = connection.prepareStatement(updateCreatedBySql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            } catch (SQLException ex) {
                // Bỏ qua nếu cột không tồn tại
            }
            
            // Bước 4: Reset AUTO_INCREMENT về giá trị tiếp theo
            String getMaxIdSql = "SELECT MAX(user_id) FROM users";
            int maxId = 0;
            try (PreparedStatement ps = connection.prepareStatement(getMaxIdSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maxId = rs.getInt(1);
                }
            }
            
            String resetAutoIncrementSql = "ALTER TABLE users AUTO_INCREMENT = ?";
            try (PreparedStatement ps = connection.prepareStatement(resetAutoIncrementSql)) {
                ps.setInt(1, maxId + 1);
                ps.executeUpdate();
            }
            
            // Bật lại foreign key checks
            try (PreparedStatement ps = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                ps.executeUpdate();
            }
            
            return true;
            
        } catch (SQLException ex) {
            System.err.println("UserDAO.deleteUser: " + ex.getMessage());
            ex.printStackTrace();
            
            // Đảm bảo bật lại foreign key checks ngay cả khi có lỗi
            try (PreparedStatement ps = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return false;
        }
    }
    
    /**
     * Tìm kiếm người dùng theo tên hoặc email (không bao gồm người dùng đã xóa)
     * @param searchTerm từ khóa tìm kiếm
     * @return List<User> danh sách người dùng tìm được
     */
    public List<User> searchUsers(String searchTerm) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender " +
                    "FROM users WHERE (username LIKE ? OR email LIKE ? OR first_name LIKE ? OR last_name LIKE ?) AND status != 'Deleted' " +
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
     * Đếm tổng số người dùng (không bao gồm người dùng đã xóa)
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE status != 'Deleted'";
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
     * Lấy danh sách người dùng theo trang (không bao gồm người dùng đã xóa)
     */
    public List<User> getUsersByPage(int page, int pageSize) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender " +
                    "FROM users WHERE status != 'Deleted' ORDER BY user_id ASC LIMIT ? OFFSET ?";
        
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
     * Lấy danh sách người dùng theo role với phân trang (không bao gồm người dùng đã xóa)
     */
    public List<User> getUsersByRoleAndPage(String role, int page, int pageSize) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT user_id, username, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender " +
                    "FROM users WHERE role = ? AND status != 'Deleted' ORDER BY user_id ASC LIMIT ? OFFSET ?";
        
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
     * Đếm số lượng users theo role (không bao gồm người dùng đã xóa)
     */
    public int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ? AND status != 'Deleted'";
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
    
    /**
     * Tìm user theo Google ID
     * @param googleId Google user ID
     * @return User object hoặc null nếu không tìm thấy
     */
    public User getByGoogleId(String googleId) {
        String sql = "SELECT user_id, username, password_hash, email, role, status, created_at, first_name, last_name, phone, date_of_birth, gender, google_id, auth_provider, profile_picture FROM users WHERE google_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, googleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("UserDAO.getByGoogleId: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm và link user với Google OAuth (không tự động tạo user mới)
     * Chỉ cho phép login nếu email đã được admin tạo trước
     * @param googleId Google user ID
     * @param email Email từ Google
     * @param firstName First name từ Google
     * @param lastName Last name từ Google
     * @param profilePicture URL ảnh đại diện từ Google
     * @return User object nếu email tồn tại, null nếu email chưa được tạo
     */
    public User findOrCreateGoogleUser(String googleId, String email, String firstName, String lastName, String profilePicture) {
        // Kiểm tra xem user đã có Google ID chưa
        User existingUser = getByGoogleId(googleId);
        if (existingUser != null) {
            // Cập nhật thông tin nếu có thay đổi
            updateGoogleUserInfo(existingUser.getUserID(), firstName, lastName, profilePicture);
            return getByGoogleId(googleId);
        }
        
        // Kiểm tra xem có user với email này chưa (phải được admin tạo trước)
        User userByEmail = getByEmail(email);
        if (userByEmail != null) {
            // Link Google account với existing user
            linkGoogleAccount(userByEmail.getUserID(), googleId, profilePicture);
            return getByGoogleId(googleId);
        }
        
        // Không tự động tạo user mới - trả về null nếu email chưa tồn tại
        // Admin phải tạo user trước khi họ có thể login bằng Google
        return null;
    }
    
    /**
     * Link Google account với existing user
     */
    private void linkGoogleAccount(int userId, String googleId, String profilePicture) {
        String sql = "UPDATE users SET google_id = ?, auth_provider = 'google', profile_picture = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, googleId);
            ps.setString(2, profilePicture);
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("UserDAO.linkGoogleAccount: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Cập nhật thông tin Google user
     */
    private void updateGoogleUserInfo(int userId, String firstName, String lastName, String profilePicture) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, profile_picture = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, profilePicture);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("UserDAO.updateGoogleUserInfo: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Helper method để map ResultSet sang User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
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
        try { u.setGoogleId(rs.getString("google_id")); } catch (SQLException ignore) {}
        try { u.setAuthProvider(rs.getString("auth_provider")); } catch (SQLException ignore) {}
        try { u.setProfilePicture(rs.getString("profile_picture")); } catch (SQLException ignore) {}
        return u;
    }
}
