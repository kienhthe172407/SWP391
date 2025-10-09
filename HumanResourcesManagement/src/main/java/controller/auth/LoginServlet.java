package controller.auth;

import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Simple login servlet. Mapped in web.xml to /login as well.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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

        // Development fallback mode: accept any non-empty username/password and create a temporary user
        if (!username.trim().isEmpty() && !password.trim().isEmpty()) {
            User temp = new User();
            temp.setUserID(-1);
            temp.setUsername(username);
            temp.setRole("HR Manager"); // default role for testing
            HttpSession session = request.getSession(true);
            session.setAttribute("user", temp);
            response.sendRedirect(request.getContextPath() + "/manager/home.jsp");
            return;
        } else {
            request.setAttribute("errorMessage", "Vui lòng nhập tên đăng nhập và mật khẩu hợp lệ.");
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
