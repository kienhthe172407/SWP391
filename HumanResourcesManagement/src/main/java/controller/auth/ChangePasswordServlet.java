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

public class ChangePasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Show page
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

        // Validate inputs
        if (newPassword == null || newPassword.length() < 8) {
            request.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 8 ký tự.");
            request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
            return;
        }
        if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
            return;
        }

        // Re-authenticate with current password
        if (currentUser.getPasswordHash() != null) {
            try {
                if (!BCrypt.checkpw(currentPassword, currentUser.getPasswordHash())) {
                    request.setAttribute("errorMessage", "Mật khẩu hiện tại không đúng.");
                    request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
                    return;
                }
            } catch (IllegalArgumentException e) {
                // Fallback in case stored hash isn't BCrypt (legacy)
                if (!currentPassword.equals(currentUser.getPasswordHash())) {
                    request.setAttribute("errorMessage", "Mật khẩu hiện tại không đúng.");
                    request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
                    return;
                }
            }
        }

        // Update password
        boolean updated = userDAO.updatePassword(currentUser.getUserID(), newPassword);
        if (updated) {
            // Update session user hash to avoid re-login issues
            currentUser.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
            session.setAttribute("user", currentUser);
            request.setAttribute("successMessage", "Cập nhật mật khẩu thành công.");
        } else {
            request.setAttribute("errorMessage", "Cập nhật mật khẩu thất bại. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("/auth/change-password.jsp").forward(request, response);
    }
}


