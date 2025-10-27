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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet để quản lý Role & Permissions
 */
@WebServlet(name = "RoleManagementServlet", urlPatterns = {"/role-management"})
public class RoleManagementServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            String action = request.getParameter("action");
            String roleFilter = request.getParameter("role");
            String searchTerm = request.getParameter("search");
            
            // Pagination
            int page = 1;
            int pageSize = 10;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e) {
                page = 1;
            }
            
            List<User> users;
            int totalUsers = 0;
            
            if ("filter".equals(action)) {
                // Tìm kiếm theo username
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    users = userDAO.searchUsers(searchTerm);
                    totalUsers = users.size();
                    
                    // Lọc thêm theo role nếu có
                    if (roleFilter != null && !roleFilter.trim().isEmpty()) {
                        users = users.stream()
                            .filter(u -> u.getRole().equals(roleFilter))
                            .collect(java.util.stream.Collectors.toList());
                        totalUsers = users.size();
                        request.setAttribute("selectedRole", roleFilter);
                    }
                } 
                // Chỉ lọc theo role
                else if (roleFilter != null && !roleFilter.trim().isEmpty()) {
                    totalUsers = userDAO.countUsersByRole(roleFilter);
                    users = userDAO.getUsersByRoleAndPage(roleFilter, page, pageSize);
                    request.setAttribute("selectedRole", roleFilter);
                } 
                // Hiển thị tất cả
                else {
                    totalUsers = userDAO.getTotalUsers();
                    users = userDAO.getUsersByPage(page, pageSize);
                }
            } else {
                // Hiển thị tất cả users với phân trang
                totalUsers = userDAO.getTotalUsers();
                users = userDAO.getUsersByPage(page, pageSize);
            }
            
            request.setAttribute("users", users);
            
            // Pagination info
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalUsers", totalUsers);
            
            // Đếm số lượng user theo role
            Map<String, Integer> roleStats = getRoleStatistics();
            request.setAttribute("roleStats", roleStats);
            
            // Forward đến JSP
            request.getRequestDispatcher("/auth/role-management.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/auth/role-management.jsp").forward(request, response);
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
        
        try {
            if ("updateRole".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String newRole = request.getParameter("role");
                
                boolean success = userDAO.updateUserRole(userId, newRole);
                
                if (success) {
                    session.setAttribute("successMessage", "Cập nhật vai trò thành công!");
                } else {
                    session.setAttribute("errorMessage", "Không thể cập nhật vai trò!");
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/role-management");
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/role-management");
        }
    }
    
    private Map<String, Integer> getRoleStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        String[] roles = {"Admin", "HR", "HR Manager", "Employee", "Dept Manager"};
        
        for (String role : roles) {
            List<User> users = userDAO.getUsersByRole(role);
            stats.put(role, users.size());
        }
        
        return stats;
    }
}
