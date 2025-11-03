package controller.auth;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.sql.Date;

/**
 * Dễ hiểu, dễ sửa: Servlet chỉnh sửa thông tin người dùng cho Admin
 * - GET: hiển thị form với dữ liệu hiện tại
 * - POST: cập nhật thông tin cơ bản (email, tên, điện thoại, ngày sinh, giới tính, vai trò, trạng thái)
 */
@WebServlet(name = "EditUserServlet", urlPatterns = {"/edit-user"})
public class EditUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        Object u = session.getAttribute("user");
        if (u instanceof User) {
            return "Admin".equals(((User) u).getRole());
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/list-users?error=Missing+id+parameter");
            return;
        }
        try {
            int userId = Integer.parseInt(id);
            User user = userDAO.getUserById(userId);
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/list-users?error=User+not+found");
                return;
            }
            request.setAttribute("editUser", user);
            request.getRequestDispatcher("/auth/edit-user.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/list-users?error=Invalid+ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy dữ liệu từ form
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        String role = request.getParameter("role");
        String status = request.getParameter("status");

        String error = null;
        try {
            int userId = Integer.parseInt(id);
            User user = userDAO.getUserById(userId);
            if (user == null) {
                error = "User not found";
            } else {
                // Validate đơn giản
                if (email == null || !email.contains("@")) {
                    error = "Invalid email";
                }
                if (error == null) {
                    // Cập nhật các trường cơ bản
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPhone(phone);
                    if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                        try {
                            user.setDateOfBirth(Date.valueOf(dateOfBirthStr));
                        } catch (IllegalArgumentException ignore) {}
                    } else {
                        user.setDateOfBirth(null);
                    }
                    user.setGender(gender);
                    if (role != null && !role.isEmpty()) user.setRole(role);
                    if (status != null && !status.isEmpty()) user.setStatus(status);

                    boolean ok = userDAO.updateProfile(user);
                    if (ok) {
                        // Nếu đổi role/status, cập nhật riêng để đảm bảo
                        if (role != null && !role.isEmpty()) {
                            userDAO.updateUserRole(userId, role);
                        }
                        if (status != null && !status.isEmpty()) {
                            userDAO.updateUserStatus(userId, status);
                        }
                        response.sendRedirect(request.getContextPath() + "/list-users?success=Update+successful");
                        return;
                    } else {
                        error = "Update failed";
                    }
                }
            }
        } catch (NumberFormatException e) {
            error = "Invalid ID";
        }

        // Quay lại form với thông báo lỗi
        request.setAttribute("errorMessage", error);
        doGet(request, response);
    }
}


