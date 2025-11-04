package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {
    private String smtpHost;
    private String smtpPort;
    private String senderEmail;
    private String senderPassword;
    private String senderName;
    
    public EmailService() {
        loadConfig();
    }
    
    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (input != null) {
                props.load(input);
                this.senderEmail = props.getProperty("mail.sender.email", "your-email@gmail.com");
                this.senderPassword = props.getProperty("mail.sender.password", "your-app-password");
                this.senderName = props.getProperty("mail.sender.name", "HR Management System");
                this.smtpHost = props.getProperty("mail.smtp.host", "smtp.gmail.com");
                this.smtpPort = props.getProperty("mail.smtp.port", "587");
            } else {
                // Default values nếu không tìm thấy file
                this.senderEmail = "your-email@gmail.com";
                this.senderPassword = "your-app-password";
                this.senderName = "HR Management System";
                this.smtpHost = "smtp.gmail.com";
                this.smtpPort = "587";
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Use default values
            this.senderEmail = "your-email@gmail.com";
            this.senderPassword = "your-app-password";
            this.senderName = "HR Management System";
            this.smtpHost = "smtp.gmail.com";
            this.smtpPort = "587";
        }
    }
    
    public boolean sendNewPassword(String recipientEmail, String newPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, senderName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mật khẩu mới cho tài khoản của bạn");
            
            String htmlContent = createEmailContent(newPassword);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String createEmailContent(String newPassword) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head><body style='font-family: Arial, sans-serif;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>" +
                "<h1 style='margin: 0;'>🔐 Đặt lại mật khẩu</h1>" +
                "</div>" +
                "<div style='background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px;'>" +
                "<p style='font-size: 16px;'>Xin chào,</p>" +
                "<p style='font-size: 16px;'>Mật khẩu mới của bạn là:</p>" +
                "<div style='background: white; padding: 20px; text-align: center; border-radius: 8px; margin: 20px 0;'>" +
                "<h2 style='color: #667eea; font-size: 28px; margin: 0; letter-spacing: 2px;'>" + newPassword + "</h2>" +
                "</div>" +
                "<p style='font-size: 16px;'>Vui lòng sử dụng mật khẩu này để đăng nhập.</p>" +
                "<div style='background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 4px;'>" +
                "<p style='margin: 0; color: #856404;'><strong>⚠️ Lưu ý:</strong> Vì lý do bảo mật, bạn nên đổi mật khẩu ngay sau khi đăng nhập.</p>" +
                "</div>" +
                "<p style='font-size: 14px; color: #6c757d;'>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng liên hệ quản trị viên.</p>" +
                "</div>" +
                "<div style='text-align: center; padding: 20px; color: #6c757d; font-size: 12px;'>" +
                "<p>© 2024 HR Management System. All rights reserved.</p>" +
                "</div>" +
                "</div></body></html>";
    }
}
