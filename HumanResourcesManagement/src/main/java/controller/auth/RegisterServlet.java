package controller.auth;

import dal.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet xử lý đăng ký tài khoản người dùng mới
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin từ form đăng ký
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dateOfBirth"); // yyyy-MM-dd
        String gender = request.getParameter("gender");

        if (firstName == null || lastName == null || email == null || username == null || password == null || confirm == null || phone == null || dob == null || gender == null) {
            request.setAttribute("errorMessage", "Please fill in all required fields.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirm)) {
            request.setAttribute("errorMessage", "Password and confirmation password do not match.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra độ dài mật khẩu tối thiểu
        if (password.length() < 8) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters long.");
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        try {
            java.sql.Date dateOfBirth = null;
            try {
                dateOfBirth = java.sql.Date.valueOf(dob.trim());
            } catch (IllegalArgumentException ex) {
                request.setAttribute("errorMessage", "Invalid date of birth. Format: yyyy-MM-dd.");
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                return;
            }

            UserDAO dao = new UserDAO();
            User created = dao.createUser(
                    username.trim(),
                    password,
                    email.trim(),
                    "Employee",
                    firstName.trim(),
                    lastName.trim(),
                    phone.trim(),
                    dateOfBirth,
                    gender.trim()
            );
            if (created != null) {
                request.setAttribute("successMessage", "Registration successful. You can now log in.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("errorMessage", "Registration failed. Username or email may already exist.");
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                return;
            }
        } catch (RuntimeException ex) {
            // Lỗi kết nối database hoặc truy vấn thất bại - ghi log để chẩn đoán
            ex.printStackTrace();
            String detail = ex.getMessage() == null ? "" : (": " + ex.getMessage());
            request.setAttribute("errorMessage", "System error. Please try again later" + detail + ". (See server log for details)");
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
