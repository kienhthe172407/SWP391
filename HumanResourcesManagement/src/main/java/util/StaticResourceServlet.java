package util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Servlet to serve static resources (CSS, JS) with correct MIME type
 * This ensures proper content-type headers for static files
 */
@WebServlet(name = "StaticResourceServlet", urlPatterns = {"/css/*", "/js/*"})
public class StaticResourceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String resourcePath = requestURI.substring(contextPath.length());
        
        // Set correct content type based on file extension
        if (resourcePath.endsWith(".css")) {
            response.setContentType("text/css; charset=UTF-8");
        } else if (resourcePath.endsWith(".js")) {
            response.setContentType("application/javascript; charset=UTF-8");
        }
        
        // Get the resource from webapp directory
        InputStream resourceStream = getServletContext().getResourceAsStream(resourcePath);
        
        if (resourceStream == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Copy the resource to response
        try (OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = resourceStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            resourceStream.close();
        }
    }
}

