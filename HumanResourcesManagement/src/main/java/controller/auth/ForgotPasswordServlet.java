package controller.auth;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.sql.Date;

/**
 * Yêu cầu nhân viên điền đủ thông tin profile rồi reset mật khẩu về 12345678
 */
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");

        // Validate cơ bản
        if (email == null || !email.contains("@")) {
            request.setAttribute("errorMessage", "Email không hợp lệ");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ họ và tên");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập số điện thoại");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }

        User user = userDAO.getByEmail(email.trim());
        if (user == null) {
            request.setAttribute("errorMessage", "Không tìm thấy tài khoản với email này");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }

        // Cập nhật thông tin profile
        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        user.setPhone(phone.trim());
        if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
            try {
                user.setDateOfBirth(Date.valueOf(dateOfBirthStr));
            } catch (IllegalArgumentException ignore) {}
        }
        user.setGender(gender);
        userDAO.updateProfile(user);

        // Reset mật khẩu về 12345678
        boolean ok = userDAO.updatePassword(user.getUserID(), "12345678");
        if (ok) {
            request.setAttribute("successMessage", "Đặt lại mật khẩu thành công. Mật khẩu mới: 12345678");
        } else {
            request.setAttribute("errorMessage", "Không thể đặt lại mật khẩu. Vui lòng thử lại");
        }

        request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
    }
}


