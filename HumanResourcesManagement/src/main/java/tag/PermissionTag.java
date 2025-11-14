package tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import model.User;
import util.PermissionChecker;

/**
 * Custom JSP Tag để check permission
 * Sử dụng: <permission:check code="EMPLOYEE_VIEW">...</permission:check>
 */
public class PermissionTag extends TagSupport {
    
    private String code;
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public int doStartTag() throws JspException {
        User user = (User) pageContext.getSession().getAttribute("user");
        
        if (user != null && PermissionChecker.hasPermission(user, code)) {
            return EVAL_BODY_INCLUDE; // Hiển thị nội dung bên trong tag
        }
        
        return SKIP_BODY; // Bỏ qua nội dung bên trong tag
    }
}
