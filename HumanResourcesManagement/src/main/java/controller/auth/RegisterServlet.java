package controller.auth;

import dal.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");

        if (firstName == null || lastName == null || email == null || username == null || password == null || confirm == null) {
            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirm)) {
            request.setAttribute("errorMessage", "Mật khẩu và xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        // Minimal password validation
        if (password.length() < 8) {
            request.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 8 ký tự.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            User created = dao.createUser(username.trim(), password, email.trim(), "Employee", firstName.trim(), lastName.trim());
            if (created != null) {
                request.setAttribute("successMessage", "Đăng ký thành công. Bạn có thể đăng nhập ngay bây giờ.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("errorMessage", "Đăng ký thất bại. Tên đăng nhập hoặc email có thể đã tồn tại.");
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                return;
            }
        } catch (RuntimeException ex) {
            // DB connection or query failed
            request.setAttribute("errorMessage", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
    }
}
