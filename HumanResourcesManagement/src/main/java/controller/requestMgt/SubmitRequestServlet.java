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
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Servlet for handling employee request submission
 * Allows employees to create and submit requests for leave, remote work, etc.
 * @author admin
 */
@WebServlet(name = "SubmitRequestServlet", urlPatterns = {"/request/submit"})
public class SubmitRequestServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the submit request form
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

        // Get all request types for the dropdown
        List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("employee", employee);

        // Forward to the submit request form
        request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
    }

    /**
     * Handle POST request - process the submitted request
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
            String requestTypeIdStr = request.getParameter("requestTypeId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String numberOfDaysStr = request.getParameter("numberOfDays");
            String reason = request.getParameter("reason");

            // Validate required fields
            if (requestTypeIdStr == null || requestTypeIdStr.trim().isEmpty() ||
                startDateStr == null || startDateStr.trim().isEmpty() ||
                endDateStr == null || endDateStr.trim().isEmpty() ||
                numberOfDaysStr == null || numberOfDaysStr.trim().isEmpty() ||
                reason == null || reason.trim().isEmpty()) {

                request.setAttribute("errorMessage", "All fields are required. Please fill in all information.");
                
                // Reload request types for the form
                List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                request.setAttribute("requestTypes", requestTypes);
                request.setAttribute("employee", employee);
                
                // Preserve form data
                request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("numberOfDays", numberOfDaysStr);
                request.setAttribute("reason", reason);
                
                request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
                return;
            }

            // Parse and validate data
            int requestTypeId = Integer.parseInt(requestTypeIdStr);
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            BigDecimal numberOfDays = new BigDecimal(numberOfDaysStr);

            // Validate dates
            LocalDate start = startDate.toLocalDate();
            LocalDate end = endDate.toLocalDate();

            if (end.isBefore(start)) {
                request.setAttribute("errorMessage", "End date cannot be before start date.");
                
                // Reload request types for the form
                List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                request.setAttribute("requestTypes", requestTypes);
                request.setAttribute("employee", employee);
                
                // Preserve form data
                request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("numberOfDays", numberOfDaysStr);
                request.setAttribute("reason", reason);
                
                request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
                return;
            }

            // Validate number of days
            if (numberOfDays.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("errorMessage", "Number of days must be greater than 0.");

                // Reload request types for the form
                List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                request.setAttribute("requestTypes", requestTypes);
                request.setAttribute("employee", employee);

                // Preserve form data
                request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("numberOfDays", numberOfDaysStr);
                request.setAttribute("reason", reason);

                request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
                return;
            }

            // Get request type to check max days limit
            RequestType requestType = requestDAO.getRequestTypeById(requestTypeId);
            if (requestType == null) {
                request.setAttribute("errorMessage", "Invalid request type selected.");

                // Reload request types for the form
                List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                request.setAttribute("requestTypes", requestTypes);
                request.setAttribute("employee", employee);

                // Preserve form data
                request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("numberOfDays", numberOfDaysStr);
                request.setAttribute("reason", reason);

                request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
                return;
            }

            // Validate against max days per year limit (if applicable)
            if (requestType.getMaxDaysPerYear() > 0) {
                // Check if single request exceeds max days
                if (numberOfDays.compareTo(new BigDecimal(requestType.getMaxDaysPerYear())) > 0) {
                    request.setAttribute("errorMessage",
                        "The number of days requested (" + numberOfDays + ") exceeds the maximum allowed for " +
                        requestType.getRequestTypeName() + " (" + requestType.getMaxDaysPerYear() + " days per year).");

                    // Reload request types for the form
                    List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                    request.setAttribute("requestTypes", requestTypes);
                    request.setAttribute("employee", employee);

                    // Preserve form data
                    request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                    request.setAttribute("startDate", startDateStr);
                    request.setAttribute("endDate", endDateStr);
                    request.setAttribute("numberOfDays", numberOfDaysStr);
                    request.setAttribute("reason", reason);

                    request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
                    return;
                }

                // Calculate total days already used in current year (approved + pending)
                BigDecimal approvedDays = requestDAO.getTotalApprovedDaysInCurrentYear(
                    employee.getEmployeeID(), requestTypeId, 0);
                BigDecimal pendingDays = requestDAO.getTotalPendingDaysInCurrentYear(
                    employee.getEmployeeID(), requestTypeId, 0);
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

                    // Reload request types for the form
                    List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                    request.setAttribute("requestTypes", requestTypes);
                    request.setAttribute("employee", employee);

                    // Preserve form data
                    request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                    request.setAttribute("startDate", startDateStr);
                    request.setAttribute("endDate", endDateStr);
                    request.setAttribute("numberOfDays", numberOfDaysStr);
                    request.setAttribute("reason", reason);

                    request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
                    return;
                }
            }

            // Create request object
            Request newRequest = new Request();
            newRequest.setEmployeeID(employee.getEmployeeID());
            newRequest.setRequestTypeID(requestTypeId);
            newRequest.setStartDate(startDate);
            newRequest.setEndDate(endDate);
            newRequest.setNumberOfDays(numberOfDays);
            newRequest.setReason(reason.trim());
            newRequest.setRequestStatus("Pending");

            // Save to database
            int requestId = requestDAO.createRequest(newRequest);

            if (requestId > 0) {
                // Success - redirect to request list or detail page
                session.setAttribute("successMessage", "Request submitted successfully! Your request is pending approval.");
                response.sendRedirect(request.getContextPath() + "/request/list");
            } else {
                // Failed to save
                request.setAttribute("errorMessage", "Failed to submit request. Please try again.");
                
                // Reload request types for the form
                List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
                request.setAttribute("requestTypes", requestTypes);
                request.setAttribute("employee", employee);
                
                // Preserve form data
                request.setAttribute("selectedRequestTypeId", requestTypeIdStr);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("numberOfDays", numberOfDaysStr);
                request.setAttribute("reason", reason);
                
                request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format. Please check your entries.");
            
            // Reload request types for the form
            List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
            request.setAttribute("requestTypes", requestTypes);
            request.setAttribute("employee", employee);
            
            request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD format.");
            
            // Reload request types for the form
            List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
            request.setAttribute("requestTypes", requestTypes);
            request.setAttribute("employee", employee);
            
            request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in SubmitRequestServlet: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            
            // Reload request types for the form
            List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
            request.setAttribute("requestTypes", requestTypes);
            request.setAttribute("employee", employee);
            
            request.getRequestDispatcher("/request-mgt/submit-request.jsp").forward(request, response);
        }
    }
}

