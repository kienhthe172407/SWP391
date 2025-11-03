package controller.auth;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

/**
 * Servlet quản lý hồ sơ cá nhân người dùng
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        request.getRequestDispatcher("/auth/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        User current = (User) session.getAttribute("user");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");

        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();
        if (email != null) email = email.trim();
        if (phone != null) phone = phone.trim();
        java.sql.Date dateOfBirth = null;
        if (dob != null && !dob.trim().isEmpty()) {
            try {
                dateOfBirth = java.sql.Date.valueOf(dob.trim());
            } catch (IllegalArgumentException ex) {
                request.setAttribute("errorMessage", "Invalid date of birth. Format: yyyy-MM-dd.");
                request.getRequestDispatcher("/auth/profile.jsp").forward(request, response);
                return;
            }
        }
        if (gender != null) gender = gender.trim();

        UserDAO dao = new UserDAO();
        current.setFirstName(firstName);
        current.setLastName(lastName);
        current.setEmail(email);
        current.setPhone(phone);
        current.setDateOfBirth(dateOfBirth);
        current.setGender(gender);
        boolean ok = dao.updateProfile(current);
        if (ok) {
            session.setAttribute("user", current);
            request.setAttribute("successMessage", "Profile updated successfully.");
        } else {
            request.setAttribute("errorMessage", "Unable to update profile. Please try again.");
        }
        request.getRequestDispatcher("/auth/profile.jsp").forward(request, response);
    }
}


