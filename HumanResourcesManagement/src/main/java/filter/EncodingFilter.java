package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter để đảm bảo encoding UTF-8 cho tất cả request/response
 * Tự động áp dụng cho mọi URL trong ứng dụng
 */
@WebFilter(urlPatterns = {"/*"})
public class EncodingFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Set encoding UTF-8 cho request và response
        httpRequest.setCharacterEncoding("UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");
        
        // Only set content type for HTML responses, not for static resources (CSS, JS, images, etc.)
        String requestURI = httpRequest.getRequestURI();
        
        if (!requestURI.endsWith(".css") && !requestURI.endsWith(".js") && 
            !requestURI.endsWith(".png") && !requestURI.endsWith(".jpg") && 
            !requestURI.endsWith(".jpeg") && !requestURI.endsWith(".gif") && 
            !requestURI.endsWith(".svg") && !requestURI.endsWith(".ico") &&
            !requestURI.endsWith(".woff") && !requestURI.endsWith(".woff2") &&
            !requestURI.endsWith(".ttf") && !requestURI.endsWith(".eot")) {
            httpResponse.setContentType("text/html; charset=UTF-8");
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}

