package tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import model.User;
import util.PermissionChecker;

/**
 * Custom JSP Tag để kiểm tra permission
 * Sử dụng: <permission:check permission="USER_CREATE">...</permission:check>
 */
public class PermissionTag extends TagSupport {
    private String permission;
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    @Override
    public int doStartTag() throws JspException {
        User user = (User) pageContext.getSession().getAttribute("user");
        
        if (user != null && PermissionChecker.hasPermission(user, permission)) {
            return EVAL_BODY_INCLUDE; // Hiển thị nội dung bên trong tag
        }
        
        return SKIP_BODY; // Bỏ qua nội dung
    }
}
