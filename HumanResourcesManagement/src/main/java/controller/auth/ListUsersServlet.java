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
        String pageParam = request.getParameter("page");
        
        // Xử lý phân trang
        int page = 1;
        int pageSize = 10;
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        List<User> users;
        int totalUsers;
        int totalPages;
        
        try {
            // Lấy tất cả người dùng theo filter
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                users = userDAO.searchUsers(searchTerm.trim());
            } else if (roleFilter != null && !roleFilter.trim().isEmpty()) {
                users = userDAO.getUsersByRole(roleFilter);
            } else {
                users = userDAO.getAllUsers();
            }
            
            // Lọc theo trạng thái nếu có
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !"all".equals(statusFilter)) {
                users = users.stream()
                    .filter(user -> statusFilter.equals(user.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Tính tổng số và phân trang trong memory
            totalUsers = users.size();
            totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            // Phân trang kết quả
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalUsers);
            if (fromIndex < totalUsers) {
                users = users.subList(fromIndex, toIndex);
            } else {
                users = new java.util.ArrayList<>();
            }
            
            // Đặt dữ liệu vào request
            request.setAttribute("users", users);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("roleFilter", roleFilter);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalUsers", totalUsers);
            
            // Hiển thị trang danh sách
            request.getRequestDispatcher("/auth/list-users.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Xử lý lỗi
            request.setAttribute("errorMessage", "An error occurred while loading user list: " + e.getMessage());
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
                            "Account " + ("Active".equals(newStatus) ? "activated" : "deactivated") + " successfully!" :
                            "An error occurred while updating account status!";
                    } else {
                        message = "User not found!";
                    }
                    break;
                    
                case "change_role":
                    // Thay đổi vai trò người dùng
                    String newRole = request.getParameter("newRole");
                    if (newRole != null && !newRole.trim().isEmpty()) {
                        success = userDAO.updateUserRole(userId, newRole);
                        message = success ? 
                            "Role updated successfully!" :
                            "An error occurred while updating role!";
                    } else {
                        message = "Invalid role!";
                    }
                    break;
                    
                case "delete":
                    // Xóa người dùng (soft delete)
                    success = userDAO.deleteUser(userId);
                    message = success ? 
                        "Account deleted successfully!" :
                        "An error occurred while deleting account!";
                    break;
                    
                default:
                    message = "Invalid action!";
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
                java.net.URLEncoder.encode("An error occurred: " + e.getMessage(), "UTF-8"));
        }
    }
}
