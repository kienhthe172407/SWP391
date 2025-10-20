package controller.auth;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.List;

/**
 * Servlet để hiển thị danh sách người dùng và quản lý tài khoản
 * Chỉ admin mới có thể truy cập chức năng này
 */
@WebServlet(name = "ListUsersServlet", urlPatterns = {"/list-users"})
public class ListUsersServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
            // Nếu không phải admin, chuyển về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Lấy tham số tìm kiếm và lọc
        String searchTerm = request.getParameter("search");
        String roleFilter = request.getParameter("role");
        String statusFilter = request.getParameter("status");
        
        List<User> users;
        
        try {
            // Lấy danh sách người dùng dựa trên các bộ lọc
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Tìm kiếm theo từ khóa
                users = userDAO.searchUsers(searchTerm.trim());
            } else if (roleFilter != null && !roleFilter.trim().isEmpty()) {
                // Lọc theo vai trò
                users = userDAO.getUsersByRole(roleFilter);
            } else {
                // Lấy tất cả người dùng
                users = userDAO.getAllUsers();
            }
            
            // Lọc theo trạng thái nếu có
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !"all".equals(statusFilter)) {
                users = users.stream()
                    .filter(user -> statusFilter.equals(user.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Đặt dữ liệu vào request
            request.setAttribute("users", users);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("roleFilter", roleFilter);
            request.setAttribute("statusFilter", statusFilter);
            
            // Hiển thị trang danh sách
            request.getRequestDispatcher("/auth/list-users.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Xử lý lỗi
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách người dùng: " + e.getMessage());
            request.getRequestDispatcher("/auth/list-users.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");
        
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/list-users?error=invalid_user_id");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            boolean success = false;
            String message = "";
            
            switch (action) {
                case "toggle_status":
                    // Thay đổi trạng thái người dùng
                    User user = userDAO.getUserById(userId);
                    if (user != null) {
                        String newStatus = "Active".equals(user.getStatus()) ? "Inactive" : "Active";
                        success = userDAO.updateUserStatus(userId, newStatus);
                        message = success ? 
                            "Đã " + ("Active".equals(newStatus) ? "kích hoạt" : "vô hiệu hóa") + " tài khoản thành công!" :
                            "Có lỗi xảy ra khi cập nhật trạng thái tài khoản!";
                    } else {
                        message = "Không tìm thấy người dùng!";
                    }
                    break;
                    
                case "change_role":
                    // Thay đổi vai trò người dùng
                    String newRole = request.getParameter("newRole");
                    if (newRole != null && !newRole.trim().isEmpty()) {
                        success = userDAO.updateUserRole(userId, newRole);
                        message = success ? 
                            "Đã cập nhật vai trò thành công!" :
                            "Có lỗi xảy ra khi cập nhật vai trò!";
                    } else {
                        message = "Vai trò không hợp lệ!";
                    }
                    break;
                    
                case "delete":
                    // Xóa người dùng (soft delete)
                    success = userDAO.deleteUser(userId);
                    message = success ? 
                        "Đã xóa tài khoản thành công!" :
                        "Có lỗi xảy ra khi xóa tài khoản!";
                    break;
                    
                default:
                    message = "Hành động không hợp lệ!";
            }
            
            // Chuyển hướng với thông báo
            String redirectUrl = request.getContextPath() + "/list-users";
            if (success) {
                redirectUrl += "?success=" + java.net.URLEncoder.encode(message, "UTF-8");
            } else {
                redirectUrl += "?error=" + java.net.URLEncoder.encode(message, "UTF-8");
            }
            
            response.sendRedirect(redirectUrl);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/list-users?error=invalid_user_id");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/list-users?error=" + 
                java.net.URLEncoder.encode("Có lỗi xảy ra: " + e.getMessage(), "UTF-8"));
        }
    }
}
