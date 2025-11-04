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
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Servlet tạo tài khoản người dùng mới bởi admin
 * Chỉ admin mới có thể truy cập chức năng này
 */
@WebServlet(name = "CreateUserServlet", urlPatterns = {"/create-user"})
public class CreateUserServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
            // Nếu không phải admin, chuyển về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Hiển thị form tạo tài khoản
        request.getRequestDispatcher("/auth/create-user.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set encoding UTF-8 cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // Kiểm tra quyền admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        
        // Biến lưu thông báo lỗi
        String errorMessage = "";
        String successMessage = "";
        
        try {
            // Kiểm tra dữ liệu đầu vào
            if (username == null || username.trim().isEmpty()) {
                errorMessage += "Username cannot be empty.<br>";
            }
            if (password == null || password.trim().isEmpty()) {
                errorMessage += "Password cannot be empty.<br>";
            }
            if (confirmPassword == null || !confirmPassword.equals(password)) {
                errorMessage += "Password confirmation does not match.<br>";
            }
            if (email == null || email.trim().isEmpty()) {
                errorMessage += "Email cannot be empty.<br>";
            }
            if (role == null || role.trim().isEmpty()) {
                errorMessage += "Role cannot be empty.<br>";
            }
            
            // Kiểm tra độ dài mật khẩu
            if (password != null && password.length() < 6) {
                errorMessage += "Password must be at least 6 characters long.<br>";
            }
            
            // Kiểm tra định dạng email (kiểm tra cơ bản)
            if (email != null && !email.contains("@")) {
                errorMessage += "Email format is invalid.<br>";
            }
            
            // Nếu có lỗi, hiển thị lại form với thông báo lỗi
            if (!errorMessage.isEmpty()) {
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);
                request.setAttribute("phone", phone);
                request.setAttribute("dateOfBirth", dateOfBirthStr);
                request.setAttribute("gender", gender);
                request.getRequestDispatcher("/auth/create-user.jsp").forward(request, response);
                return;
            }
            
            // Kiểm tra username đã tồn tại chưa
            User existingUser = userDAO.getByUsername(username.trim());
            if (existingUser != null) {
                errorMessage = "Username already exists. Please choose a different username.";
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);
                request.setAttribute("phone", phone);
                request.setAttribute("dateOfBirth", dateOfBirthStr);
                request.setAttribute("gender", gender);
                request.getRequestDispatcher("/auth/create-user.jsp").forward(request, response);
                return;
            }
            
            // Chuyển đổi ngày sinh
            Date dateOfBirth = null;
            if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilDate = sdf.parse(dateOfBirthStr);
                    dateOfBirth = new Date(utilDate.getTime());
                } catch (ParseException e) {
                    errorMessage += "Date of birth format is invalid.<br>";
                }
            }
            
            // Tạo tài khoản mới
            User newUser = userDAO.createUser(
                username.trim(),
                password,
                email.trim(),
                role,
                firstName != null ? firstName.trim() : null,
                lastName != null ? lastName.trim() : null,
                phone != null ? phone.trim() : null,
                dateOfBirth,
                gender
            );
            
            if (newUser != null) {
                successMessage = "Account created successfully! Username: " + newUser.getUsername();
                request.setAttribute("successMessage", successMessage);
            } else {
                errorMessage = "An error occurred while creating the account. Please try again.";
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);
                request.setAttribute("phone", phone);
                request.setAttribute("dateOfBirth", dateOfBirthStr);
                request.setAttribute("gender", gender);
            }
            
        } catch (Exception e) {
            errorMessage = "An error occurred: " + e.getMessage();
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("phone", phone);
            request.setAttribute("dateOfBirth", dateOfBirthStr);
            request.setAttribute("gender", gender);
        }
        
        // Hiển thị lại form
        request.getRequestDispatcher("/auth/create-user.jsp").forward(request, response);
    }
}
