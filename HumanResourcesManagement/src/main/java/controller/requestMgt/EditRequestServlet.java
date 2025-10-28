package controller.requestMgt;

import dal.RequestDAO;
import dal.EmployeeDAO;
import model.Request;
import model.RequestType;
import model.Employee;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

/**
 * Servlet for editing employee requests (only pending requests can be edited)
 */
@WebServlet("/request/edit")
public class EditRequestServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the edit request form
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

        // Get employee information for the logged-in user
        Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (employee == null) {
            request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Get request ID from parameter
        String requestIdStr = request.getParameter("id");
        if (requestIdStr == null || requestIdStr.isEmpty()) {
            session.setAttribute("errorMessage", "Request ID is required.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Get the request from database
        Request req = requestDAO.getRequestById(requestId);
        if (req == null) {
            session.setAttribute("errorMessage", "Request not found.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Verify that the request belongs to the logged-in employee
        if (req.getEmployeeID() != employee.getEmployeeID()) {
            session.setAttribute("errorMessage", "You don't have permission to edit this request.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Verify that the request is still pending
        if (!req.isPending()) {
            session.setAttribute("errorMessage", "Only pending requests can be edited.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Get all request types for the dropdown
        List<RequestType> requestTypes = requestDAO.getAllRequestTypes();

        // Set attributes for JSP
        request.setAttribute("request", req);
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("employee", employee);

        // Forward to the edit request view
        request.getRequestDispatcher("/request-mgt/edit-request.jsp").forward(request, response);
    }

    /**
     * Handle POST request - update the request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        // Get employee information for the logged-in user
        Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (employee == null) {
            request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try {
            // Get form parameters
            String requestIdStr = request.getParameter("requestId");
            if (requestIdStr == null || requestIdStr.isEmpty()) {
                request.setAttribute("errorMessage", "Request ID is required.");
                doGet(request, response);
                return;
            }
            
            int requestId = Integer.parseInt(requestIdStr);
            int requestTypeId = Integer.parseInt(request.getParameter("requestTypeId"));
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String numberOfDaysStr = request.getParameter("numberOfDays");
            String reason = request.getParameter("reason");

            // Validate required fields
            if (startDateStr == null || startDateStr.isEmpty() ||
                endDateStr == null || endDateStr.isEmpty() ||
                numberOfDaysStr == null || numberOfDaysStr.isEmpty() ||
                reason == null || reason.trim().isEmpty()) {
                
                request.setAttribute("errorMessage", "All fields are required.");
                doGet(request, response);
                return;
            }

            // Verify that the request belongs to the logged-in employee
            Request existingRequest = requestDAO.getRequestById(requestId);
            if (existingRequest == null || existingRequest.getEmployeeID() != employee.getEmployeeID()) {
                session.setAttribute("errorMessage", "You don't have permission to edit this request.");
                response.sendRedirect(request.getContextPath() + "/request/list");
                return;
            }

            // Verify that the request is still pending
            if (!existingRequest.isPending()) {
                session.setAttribute("errorMessage", "Only pending requests can be edited.");
                response.sendRedirect(request.getContextPath() + "/request/list");
                return;
            }

            // Parse dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilStartDate = dateFormat.parse(startDateStr);
            java.util.Date utilEndDate = dateFormat.parse(endDateStr);

            // Convert to java.sql.Date
            Date startDate = new Date(utilStartDate.getTime());
            Date endDate = new Date(utilEndDate.getTime());

            // Validate date range
            if (endDate.before(startDate)) {
                request.setAttribute("errorMessage", "End date must be after or equal to start date.");
                doGet(request, response);
                return;
            }

            // Parse number of days
            BigDecimal numberOfDays = new BigDecimal(numberOfDaysStr);
            if (numberOfDays.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("errorMessage", "Number of days must be greater than 0.");
                doGet(request, response);
                return;
            }

            // Validate reason length
            if (reason.trim().length() < 10) {
                request.setAttribute("errorMessage", "Reason must be at least 10 characters long.");
                doGet(request, response);
                return;
            }

            // Get request type to check max days limit
            RequestType requestType = requestDAO.getRequestTypeById(requestTypeId);
            if (requestType == null) {
                request.setAttribute("errorMessage", "Invalid request type selected.");
                doGet(request, response);
                return;
            }

            // Validate against max days per year limit (if applicable)
            if (requestType.getMaxDaysPerYear() > 0) {
                // Check if single request exceeds max days
                if (numberOfDays.compareTo(new BigDecimal(requestType.getMaxDaysPerYear())) > 0) {
                    request.setAttribute("errorMessage",
                        "The number of days requested (" + numberOfDays + ") exceeds the maximum allowed for " +
                        requestType.getRequestTypeName() + " (" + requestType.getMaxDaysPerYear() + " days per year).");
                    doGet(request, response);
                    return;
                }

                // Calculate total days already used in current year (approved + pending), excluding this request
                BigDecimal approvedDays = requestDAO.getTotalApprovedDaysInCurrentYear(
                    employee.getEmployeeID(), requestTypeId, requestId);
                BigDecimal pendingDays = requestDAO.getTotalPendingDaysInCurrentYear(
                    employee.getEmployeeID(), requestTypeId, requestId);
                BigDecimal totalUsedDays = approvedDays.add(pendingDays);
                BigDecimal totalWithNewRequest = totalUsedDays.add(numberOfDays);

                // Check if total exceeds annual quota
                if (totalWithNewRequest.compareTo(new BigDecimal(requestType.getMaxDaysPerYear())) > 0) {
                    BigDecimal remainingDays = new BigDecimal(requestType.getMaxDaysPerYear()).subtract(totalUsedDays);

                    request.setAttribute("errorMessage",
                        "This request would exceed your annual quota for " + requestType.getRequestTypeName() + ". " +
                        "Maximum allowed: " + requestType.getMaxDaysPerYear() + " days/year. " +
                        "Already used/pending: " + totalUsedDays + " days. " +
                        "Remaining: " + remainingDays + " days. " +
                        "You are requesting: " + numberOfDays + " days.");
                    doGet(request, response);
                    return;
                }
            }

            // Create updated request object
            Request updatedRequest = new Request();
            updatedRequest.setRequestID(requestId);
            updatedRequest.setRequestTypeID(requestTypeId);
            updatedRequest.setStartDate(startDate);
            updatedRequest.setEndDate(endDate);
            updatedRequest.setNumberOfDays(numberOfDays);
            updatedRequest.setReason(reason.trim());

            // Update the request in database
            boolean success = requestDAO.updateRequest(updatedRequest);

            if (success) {
                session.setAttribute("successMessage", "Request updated successfully!");
                response.sendRedirect(request.getContextPath() + "/request/list");
            } else {
                request.setAttribute("errorMessage", "Failed to update request. The request may no longer be pending.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format: " + e.getMessage());
            doGet(request, response);
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD format.");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            e.printStackTrace();
            doGet(request, response);
        }
    }
}

