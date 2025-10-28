package controller.requestMgt;

import dal.RequestDAO;
import dal.EmployeeDAO;
import model.Request;
import model.Employee;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for viewing request details
 * Accessible by the employee who created the request and their manager
 * @author admin
 */
@WebServlet(name = "RequestDetailServlet", urlPatterns = {"/request/detail"})
public class RequestDetailServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display request details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        try {
            // Get request ID from parameter
            int requestID = Integer.parseInt(request.getParameter("id"));

            // Get request details
            Request req = requestDAO.getRequestById(requestID);
            if (req == null) {
                request.setAttribute("errorMessage", "Request not found.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Get current user's employee record
            Employee currentEmployee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (currentEmployee == null) {
                request.setAttribute("errorMessage", "Employee record not found.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Get the employee who made the request
            Employee requestEmployee = employeeDAO.getEmployeeById(req.getEmployeeID());

            // Check access permissions
            boolean isOwnRequest = currentEmployee.getEmployeeID() == req.getEmployeeID();
            boolean isManager = "Dept Manager".equals(user.getRole()) && 
                               requestEmployee != null && 
                               requestEmployee.getDepartmentID() == currentEmployee.getDepartmentID();
            boolean isHRManager = "HR Manager".equals(user.getRole());

            if (!isOwnRequest && !isManager && !isHRManager) {
                request.setAttribute("errorMessage", "You don't have permission to view this request.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Set attributes for JSP
            request.setAttribute("request", req);
            request.setAttribute("requestEmployee", requestEmployee);
            request.setAttribute("canApproveReject", (isManager || isHRManager) && req.isPending());
            request.setAttribute("isOwnRequest", isOwnRequest);

            // Forward to request detail view
            request.getRequestDispatcher("/request-mgt/request-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid request ID.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in RequestDetailServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading the request details.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}

