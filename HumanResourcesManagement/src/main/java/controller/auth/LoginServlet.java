package controller.auth;

import dal.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet đăng nhập: xác thực người dùng sử dụng BCrypt password hashes
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    // Đăng nhập bằng tài khoản và password
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            request.setAttribute("errorMessage", "Please enter username and password.");
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            return;
        }

        UserDAO dao = null;
        try {
            dao = new UserDAO();
            
            // Thử đăng nhập bằng username trước
            User authenticated = dao.authenticate(username.trim(), password);
            
            // Nếu không thành công, thử đăng nhập bằng email
            if (authenticated == null) {
                User userByEmail = dao.getByEmail(username.trim());
                if (userByEmail != null) {
                    authenticated = dao.authenticate(userByEmail.getUsername(), password);
                }
            }
            
            if (authenticated != null) {
                // Kiểm tra xem tài khoản có bị vô hiệu hóa không
                if (!authenticated.isActive()) {
                    request.setAttribute("errorMessage", "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.");
                    request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                    return;
                }
                
                // Tài khoản hợp lệ và đang hoạt động
                HttpSession session = request.getSession(true);
                session.setAttribute("user", authenticated);
                
                // Also set userRole for backward compatibility
                session.setAttribute("userRole", authenticated.getRoleDisplayName());
                
                String role = authenticated.getRole();
                LOGGER.info("User logged in - Username: " + authenticated.getUsername() + ", Role: " + role);
                
                // Redirect based on role
                if ("Admin".equals(role)) {
                    LOGGER.info("Redirecting Admin to: /dashboard/admin");
                    response.sendRedirect(request.getContextPath() + "/dashboard/admin");
                    return;
                } else if ("HR Manager".equals(role)) {
                    LOGGER.info("Redirecting HR Manager to: /dashboard/hr-manager");
                    response.sendRedirect(request.getContextPath() + "/dashboard/hr-manager");
                    return;
                } else if ("HR".equals(role)) {
                    LOGGER.info("Redirecting HR to: /dashboard/hr");
                    response.sendRedirect(request.getContextPath() + "/dashboard/hr");
                    return;
                } else if ("Employee".equals(role)) {
                    LOGGER.info("Redirecting Employee to: /dashboard/employee");
                    response.sendRedirect(request.getContextPath() + "/dashboard/employee");
                    return;
                } else if ("Dept Manager".equals(role)) {
                    LOGGER.info("Redirecting Dept Manager to: /dashboard/dept-manager");
                    response.sendRedirect(request.getContextPath() + "/dashboard/dept-manager");
                    return;
                } else {
                    LOGGER.warning("Unknown role (" + role + "), redirecting to home page");
                    response.sendRedirect(request.getContextPath() + "/");
                    return;
                }
            } else {
                request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                return;
            }
        } catch (RuntimeException ex) {
            // Lỗi kết nối database hoặc lỗi runtime khác. Ghi log và hiển thị lỗi chung cho người dùng
            LOGGER.log(Level.SEVERE, "DB/auth error: {0}", ex.getMessage());
            request.setAttribute("errorMessage", "System error (unable to connect to database). Please try again later.");
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
    }
}
