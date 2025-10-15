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
 * Login servlet: authenticate against users table using BCrypt password hashes.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            request.setAttribute("errorMessage", "Vui lòng nhập tên đăng nhập và mật khẩu.");
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
                if ("HR Manager".equals(role)) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/hr-manager-dashboard.jsp");
                    return;
                } else if ("HR".equals(role)) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/hr-dashboard.jsp");
                    return;
                } else {
                    response.sendRedirect(request.getContextPath() + "/manager/home.jsp");
                    return;
                }
            } else {
                request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                return;
            }
        } catch (RuntimeException ex) {
            // DB connection failed or other runtime errors. Log and show generic error to user.
            LOGGER.log(Level.SEVERE, "DB/auth error: {0}", ex.getMessage());
            request.setAttribute("errorMessage", "Lỗi hệ thống (không thể kết nối đến cơ sở dữ liệu). Vui lòng thử lại sau.");
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
