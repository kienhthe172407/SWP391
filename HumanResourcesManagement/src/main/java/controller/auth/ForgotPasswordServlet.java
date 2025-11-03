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
            request.setAttribute("errorMessage", "Invalid email");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your full name");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your phone number");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }

        User user = userDAO.getByEmail(email.trim());
        if (user == null) {
            request.setAttribute("errorMessage", "No account found with this email");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra tất cả thông tin phải khớp chính xác
        boolean isValid = true;
        StringBuilder errorMsg = new StringBuilder();
        
        // Kiểm tra firstName
        if (!firstName.trim().equalsIgnoreCase(user.getFirstName())) {
            isValid = false;
            errorMsg.append("First name does not match. ");
        }
        
        // Kiểm tra lastName
        if (!lastName.trim().equalsIgnoreCase(user.getLastName())) {
            isValid = false;
            errorMsg.append("Last name does not match. ");
        }
        
        // Kiểm tra phone
        if (!phone.trim().equals(user.getPhone())) {
            isValid = false;
            errorMsg.append("Phone number does not match. ");
        }
        
        // Kiểm tra dateOfBirth
        if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
            try {
                Date inputDate = Date.valueOf(dateOfBirthStr.trim());
                if (user.getDateOfBirth() == null || !inputDate.equals(user.getDateOfBirth())) {
                    isValid = false;
                    errorMsg.append("Date of birth does not match. ");
                }
            } catch (IllegalArgumentException ex) {
                isValid = false;
                errorMsg.append("Invalid date format. ");
            }
        } else if (user.getDateOfBirth() != null) {
            isValid = false;
            errorMsg.append("Date of birth is required. ");
        }
        
        // Kiểm tra gender
        if (gender != null && !gender.trim().isEmpty()) {
            if (user.getGender() == null || !gender.trim().equalsIgnoreCase(user.getGender())) {
                isValid = false;
                errorMsg.append("Gender does not match. ");
            }
        } else if (user.getGender() != null && !user.getGender().isEmpty()) {
            isValid = false;
            errorMsg.append("Gender is required. ");
        }
        
        // Nếu có thông tin không khớp, hiển thị lỗi
        if (!isValid) {
            request.setAttribute("errorMessage", "Information does not match our records. " + errorMsg.toString());
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }

        // Reset mật khẩu về 12345678
        boolean ok = userDAO.updatePassword(user.getUserID(), "12345678");
        if (ok) {
            request.setAttribute("successMessage", "Password reset successful. New password: 12345678");
        } else {
            request.setAttribute("errorMessage", "Unable to reset password. Please try again");
        }

        request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
    }
}


