package controller.auth;

import config.GoogleAuthConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Servlet khởi tạo Google OAuth flow
 */
@WebServlet(name = "GoogleLoginServlet", urlPatterns = {"/google-login"})
public class GoogleLoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Generate state parameter để prevent CSRF attacks
        String state = generateState();
        HttpSession session = request.getSession();
        session.setAttribute("oauth_state", state);
        
        // Build Google OAuth authorization URL
        String authUrl = GoogleAuthConfig.getAuthUri() +
                "?client_id=" + URLEncoder.encode(GoogleAuthConfig.getClientId(), "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(GoogleAuthConfig.getRedirectUri(), "UTF-8") +
                "&response_type=code" +
                "&scope=" + URLEncoder.encode(GoogleAuthConfig.getScope(), "UTF-8") +
                "&state=" + URLEncoder.encode(state, "UTF-8") +
                "&access_type=offline" +
                "&prompt=consent";
        
        // Redirect user to Google OAuth consent screen
        response.sendRedirect(authUrl);
    }
    
    /**
     * Generate random state parameter for CSRF protection
     */
    private String generateState() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
