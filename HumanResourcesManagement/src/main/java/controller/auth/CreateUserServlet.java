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
 * Servlet để tạo tài khoản người dùng mới từ admin
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
        
        // Biến để lưu thông báo lỗi
        String errorMessage = "";
        String successMessage = "";
        
        try {
            // Kiểm tra dữ liệu đầu vào
            if (username == null || username.trim().isEmpty()) {
                errorMessage += "Tên đăng nhập không được để trống.<br>";
            }
            if (password == null || password.trim().isEmpty()) {
                errorMessage += "Mật khẩu không được để trống.<br>";
            }
            if (confirmPassword == null || !confirmPassword.equals(password)) {
                errorMessage += "Xác nhận mật khẩu không khớp.<br>";
            }
            if (email == null || email.trim().isEmpty()) {
                errorMessage += "Email không được để trống.<br>";
            }
            if (role == null || role.trim().isEmpty()) {
                errorMessage += "Vai trò không được để trống.<br>";
            }
            
            // Kiểm tra độ dài mật khẩu
            if (password != null && password.length() < 6) {
                errorMessage += "Mật khẩu phải có ít nhất 6 ký tự.<br>";
            }
            
            // Kiểm tra email có đúng định dạng không (kiểm tra cơ bản)
            if (email != null && !email.contains("@")) {
                errorMessage += "Email không đúng định dạng.<br>";
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
                errorMessage = "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.";
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
                    errorMessage += "Ngày sinh không đúng định dạng.<br>";
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
                successMessage = "Tạo tài khoản thành công! Tên đăng nhập: " + newUser.getUsername();
                request.setAttribute("successMessage", successMessage);
            } else {
                errorMessage = "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại.";
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
            errorMessage = "Có lỗi xảy ra: " + e.getMessage();
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
