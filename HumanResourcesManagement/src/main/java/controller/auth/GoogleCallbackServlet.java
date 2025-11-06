package controller.auth;

import config.GoogleAuthConfig;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet xử lý callback từ Google OAuth
 */
@WebServlet(name = "GoogleCallbackServlet", urlPatterns = {"/google-callback"})
public class GoogleCallbackServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(GoogleCallbackServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Verify state parameter to prevent CSRF
        String state = request.getParameter("state");
        String sessionState = (String) session.getAttribute("oauth_state");
        
        if (state == null || !state.equals(sessionState)) {
            LOGGER.warning("Invalid state parameter - possible CSRF attack");
            response.sendRedirect(request.getContextPath() + "/login?error=" + 
                URLEncoder.encode("Invalid state parameter", "UTF-8"));
            return;
        }
        
        // Remove state from session
        session.removeAttribute("oauth_state");
        
        // Get authorization code
        String code = request.getParameter("code");
        String error = request.getParameter("error");
        
        if (error != null) {
            LOGGER.warning("Google OAuth error: " + error);
            response.sendRedirect(request.getContextPath() + "/login?error=" + 
                URLEncoder.encode("Google authentication failed", "UTF-8"));
            return;
        }
        
        if (code == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=" + 
                URLEncoder.encode("No authorization code received", "UTF-8"));
            return;
        }
        
        try {
            // Exchange authorization code for access token
            String accessToken = exchangeCodeForToken(code);
            
            if (accessToken == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=" + 
                    URLEncoder.encode("Failed to get access token", "UTF-8"));
                return;
            }
            
            // Get user info from Google
            JSONObject userInfo = getUserInfo(accessToken);
            
            if (userInfo == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=" + 
                    URLEncoder.encode("Failed to get user information", "UTF-8"));
                return;
            }
            
            // Extract user information
            String googleId = userInfo.getString("sub");
            String email = userInfo.optString("email", "");
            String firstName = userInfo.optString("given_name", "");
            String lastName = userInfo.optString("family_name", "");
            String profilePicture = userInfo.optString("picture", "");
            
            // Find user by email (must be pre-created by admin)
            User user = userDAO.findOrCreateGoogleUser(googleId, email, firstName, lastName, profilePicture);
            
            if (user == null) {
                LOGGER.warning("Google OAuth login failed - email not found in system: " + email);
                response.sendRedirect(request.getContextPath() + "/login?error=" + 
                    URLEncoder.encode("Email does not exist in the system. Please contact your administrator.", "UTF-8"));
                return;
            }
            
            // Check if user is active
            if (!user.isActive()) {
                LOGGER.warning("Google OAuth login failed - account inactive: " + email);
                response.sendRedirect(request.getContextPath() + "/login?error=" + 
                    URLEncoder.encode("Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.", "UTF-8"));
                return;
            }
            
            // Set user in session
            session.setAttribute("user", user);
            
            // Redirect based on role
            String role = user.getRole();
            LOGGER.info("Google OAuth login successful for user: " + email + " with role: " + role);
            
            if ("Admin".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/dashboard/admin-dashboard.jsp");
            } else if ("HR Manager".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/dashboard/hr-manager-dashboard.jsp");
            } else if ("HR".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/dashboard/hr-dashboard.jsp");
            } else if ("Employee".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/dashboard/employee-dashboard.jsp");
            } else if ("Dept Manager".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/dashboard/dept-manager-dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/manager/home.jsp");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during Google OAuth callback", e);
            response.sendRedirect(request.getContextPath() + "/login?error=" + 
                URLEncoder.encode("Authentication error: " + e.getMessage(), "UTF-8"));
        }
    }
    
    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String code) throws IOException {
        URL url = new URL(GoogleAuthConfig.getTokenUri());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        
        String postData = "code=" + URLEncoder.encode(code, "UTF-8") +
                "&client_id=" + URLEncoder.encode(GoogleAuthConfig.getClientId(), "UTF-8") +
                "&client_secret=" + URLEncoder.encode(GoogleAuthConfig.getClientSecret(), "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(GoogleAuthConfig.getRedirectUri(), "UTF-8") +
                "&grant_type=authorization_code";
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes("UTF-8"));
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.optString("access_token", null);
        } else {
            LOGGER.warning("Failed to exchange code for token. Response code: " + responseCode);
            return null;
        }
    }
    
    /**
     * Get user information from Google
     */
    private JSONObject getUserInfo(String accessToken) throws IOException {
        URL url = new URL(GoogleAuthConfig.getUserInfoUri());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            
            return new JSONObject(response.toString());
        } else {
            LOGGER.warning("Failed to get user info. Response code: " + responseCode);
            return null;
        }
    }
}
