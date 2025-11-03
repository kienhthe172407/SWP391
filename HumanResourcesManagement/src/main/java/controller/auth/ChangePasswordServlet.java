package controller.auth;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * Servlet xử lý đổi mật khẩu người dùng
 */
public class ChangePasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Hiển thị trang đổi mật khẩu
        req.getRequestDispatcher("/auth/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Kiểm tra dữ liệu đầu vào
        if (newPassword == null || newPassword.length() < 8) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters long.");
            request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
            return;
        }
        if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Password confirmation does not match.");
            request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
            return;
        }

        // Xác thực lại với mật khẩu hiện tại
        if (currentUser.getPasswordHash() != null) {
            try {
                if (!BCrypt.checkpw(currentPassword, currentUser.getPasswordHash())) {
                    request.setAttribute("errorMessage", "Current password is incorrect.");
                    request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
                    return;
                }
            } catch (IllegalArgumentException e) {
                // Dự phòng nếu hash không phải BCrypt (legacy)
                if (!currentPassword.equals(currentUser.getPasswordHash())) {
request.setAttribute("errorMessage", "Current password is incorrect.");
                    request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
                    return;
                }
            }
        }

        // Cập nhật mật khẩu
        boolean updated = userDAO.updatePassword(currentUser.getUserID(), newPassword);
        if (updated) {
            // Cập nhật hash trong session để tránh phải đăng nhập lại
            currentUser.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
            session.setAttribute("user", currentUser);
            request.setAttribute("successMessage", "Password updated successfully.");
        } else {
            request.setAttribute("errorMessage", "Password update failed. Please try again.");
        }

        request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
    }
}


