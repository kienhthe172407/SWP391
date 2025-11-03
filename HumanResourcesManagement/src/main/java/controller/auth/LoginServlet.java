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
            User authenticated = dao.authenticate(username.trim(), password);
            if (authenticated != null && authenticated.isActive()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", authenticated);
                String role = authenticated.getRole();
                LOGGER.info("User role: " + role); // Ghi log debug
                if ("Admin".equals(role)) {
                    LOGGER.info("Redirecting Admin to: /dashboard/admin-dashboard.jsp");
                    response.sendRedirect(request.getContextPath() + "/dashboard/admin-dashboard.jsp");
                    return;
                } else if ("HR Manager".equals(role)) {
                    LOGGER.info("Redirecting HR Manager to: /dashboard/hr-manager-dashboard.jsp");
                    response.sendRedirect(request.getContextPath() + "/dashboard/hr-manager-dashboard.jsp");
                    return;
                } else if ("HR".equals(role)) {
                    LOGGER.info("Redirecting HR to: /dashboard/hr-dashboard.jsp");
                    response.sendRedirect(request.getContextPath() + "/dashboard/hr-dashboard.jsp");
                    return;
                } else if ("Employee".equals(role)) {
                    LOGGER.info("Redirecting Employee to: /dashboard/employee-dashboard.jsp");
                    response.sendRedirect(request.getContextPath() + "/dashboard/employee-dashboard.jsp");
                    return;
                } else if ("Dept Manager".equals(role)) {
                    LOGGER.info("Redirecting Dept Manager to: /dashboard/dept-manager-dashboard.jsp");
                    response.sendRedirect(request.getContextPath() + "/dashboard/dept-manager-dashboard.jsp");
                    return;
                } else {
                    LOGGER.info("Redirecting other role (" + role + ") to: /manager/home.jsp");
                    response.sendRedirect(request.getContextPath() + "/manager/home.jsp");
                    return;
                }
            } else {
                request.setAttribute("errorMessage", "Incorrect username or password.");
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
