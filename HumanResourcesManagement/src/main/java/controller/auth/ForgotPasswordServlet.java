package controller.auth;

import dal.UserDAO;
import model.User;
import service.EmailService;
import util.PasswordGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/auth/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập địa chỉ email");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        
        email = email.trim().toLowerCase();
        
        try {
            UserDAO userDAO = new UserDAO();
            
            // Kiểm tra email có tồn tại không
            User user = userDAO.getByEmail(email);
            
            if (user == null) {
                request.setAttribute("errorMessage", "Email không tồn tại trong hệ thống");
                request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Kiểm tra trạng thái tài khoản
            if (!"Active".equals(user.getStatus())) {
                request.setAttribute("errorMessage", "Tài khoản đã bị vô hiệu hóa");
                request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Tạo mật khẩu mới
            String newPassword = PasswordGenerator.generate();
            
            // Cập nhật mật khẩu trong database (UserDAO tự động hash bằng BCrypt)
            boolean updated = userDAO.updatePassword(user.getUserID(), newPassword);
            
            if (!updated) {
                request.setAttribute("errorMessage", "Lỗi hệ thống. Vui lòng thử lại sau");
                request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Gửi email
            EmailService emailService = new EmailService();
            boolean emailSent = emailService.sendNewPassword(email, newPassword);
            
            if (!emailSent) {
                request.setAttribute("errorMessage", "Không thể gửi email. Vui lòng thử lại sau");
                request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Thành công
            request.setAttribute("successMessage", "Mật khẩu mới đã được gửi đến email của bạn");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống. Vui lòng thử lại sau");
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
        }
    }
}
