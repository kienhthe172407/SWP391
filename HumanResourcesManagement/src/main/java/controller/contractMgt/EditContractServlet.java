package controller.contractMgt;

import dal.ContractDAO;
import dal.EmployeeDAO;
import model.Contract;
import model.Employee;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle editing existing employment contracts.
 * Reuses the create contract form and front-end validation.
 */
@WebServlet(name = "EditContractServlet", urlPatterns = {"/contracts/edit"})
public class EditContractServlet extends HttpServlet {

    private ContractDAO contractDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        contractDAO = new ContractDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Show edit form with existing contract data (only if Draft and owned by current user)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer currentUserId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        if (role == null) {
            Object userObj = session.getAttribute("user");
            if (userObj instanceof model.User) {
                role = ((model.User) userObj).getRole();
                session.setAttribute("userRole", role);
                if (currentUserId == null) {
                    currentUserId = ((model.User) userObj).getUserId();
                    session.setAttribute("userId", currentUserId);
                }
            }
        }
        // role is now ensured if user present
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }

        try {
            int contractId = Integer.parseInt(idParam);
            Contract contract = contractDAO.getContractById(contractId);

            if (contract == null) {
                session.setAttribute("errorMessage", "Contract not found");
                response.sendRedirect(request.getContextPath() + "/contracts/list");
                return;
            }

            // Check permission
            model.User user = (model.User) session.getAttribute("user");
            if (user != null && !util.PermissionChecker.hasPermission(user, util.PermissionConstants.CONTRACT_EDIT)) {
                request.setAttribute("errorMessage", "Bạn không có quyền chỉnh sửa hợp đồng");
                request.getRequestDispatcher("/error/403.jsp").forward(request, response);
                return;
            }

            // Check Draft contract ownership - only creator can edit Draft contracts
            if ("Draft".equals(contract.getContractStatus())) {
                if (!contract.getCreatedBy().equals(currentUserId)) {
                    session.setAttribute("errorMessage", "You can only edit Draft contracts that you created.");
                    response.sendRedirect(request.getContextPath() + "/contracts/detail?id=" + contractId);
                    return;
                }
            }

            // Populate employees for dropdown
            List<Employee> employees = employeeDAO.getAllActiveEmployees();
            request.setAttribute("employees", employees);

            // Provide contract to prefill form and a flag to switch to edit mode in JSP
            request.setAttribute("contract", contract);
            request.setAttribute("editMode", true);

            request.getRequestDispatcher("/contract-mgt/create-contract.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid contract ID");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
        }
    }

    /**
     * Handle update submission.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer currentUserId = (Integer) session.getAttribute("userId");
        String rolePost = (String) session.getAttribute("userRole");
        if (rolePost == null) {
            Object userObj = session.getAttribute("user");
            if (userObj instanceof model.User) {
                rolePost = ((model.User) userObj).getRole();
                session.setAttribute("userRole", rolePost);
                if (currentUserId == null) {
                    currentUserId = ((model.User) userObj).getUserId();
                    session.setAttribute("userId", currentUserId);
                }
            }
        }
        String idParam = request.getParameter("id");

        if (currentUserId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (idParam == null) {
            session.setAttribute("errorMessage", "Missing contract ID");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }

        try {
            int contractId = Integer.parseInt(idParam);
            Contract existing = contractDAO.getContractById(contractId);

            if (existing == null) {
                session.setAttribute("errorMessage", "Contract not found");
                response.sendRedirect(request.getContextPath() + "/contracts/list");
                return;
            }

            // Permission: HR and HR Manager can edit contracts
            if (!"HR".equals(rolePost) && !"HR Manager".equals(rolePost)) {
                session.setAttribute("errorMessage", "Access denied.");
                response.sendRedirect(request.getContextPath() + "/contracts/list");
                return;
            }

            // Check Draft contract ownership - only creator can edit Draft contracts
            if ("Draft".equals(existing.getContractStatus())) {
                if (!existing.getCreatedBy().equals(currentUserId)) {
                    session.setAttribute("errorMessage", "You can only edit Draft contracts that you created.");
                    response.sendRedirect(request.getContextPath() + "/contracts/detail?id=" + contractId);
                    return;
                }
            }

            // Read form values
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            String contractNumber = request.getParameter("contractNumber");
            String contractType = request.getParameter("contractType");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String salaryAmountStr = request.getParameter("salaryAmount");
            String jobDescription = request.getParameter("jobDescription");
            String approvalComment = request.getParameter("approvalComment");
            String saveDraft = request.getParameter("saveDraft");

            // Build updated contract
            Contract updated = new Contract();
            updated.setContractID(contractId);
            updated.setCreatedBy(existing.getCreatedBy());
            updated.setEmployeeID(employeeId);
            updated.setContractNumber(contractNumber);
            updated.setContractType(contractType);

            if (startDateStr != null && !startDateStr.isEmpty()) {
                updated.setStartDate(Date.valueOf(startDateStr));
            } else {
                updated.setStartDate(null);
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                updated.setEndDate(Date.valueOf(endDateStr));
            } else {
                updated.setEndDate(null);
            }

            if (salaryAmountStr != null && !salaryAmountStr.isEmpty()) {
                try {
                    updated.setSalaryAmount(new BigDecimal(salaryAmountStr));
                } catch (NumberFormatException ex) {
                    updated.setSalaryAmount(null);
                }
            } else {
                updated.setSalaryAmount(null);
            }

            updated.setJobDescription(jobDescription);

            // Set contract status based on role and draft checkbox
            if ("true".equals(saveDraft)) {
                updated.setContractStatus("Draft");
            } else {
                // HR Manager can directly activate contracts, HR needs approval
                if ("HR Manager".equals(rolePost)) {
                    updated.setContractStatus("Active");
                } else {
                    updated.setContractStatus("Pending Approval");
                }
            }

            updated.setApprovalComment(approvalComment);

            boolean success = contractDAO.updateContract(updated);

            if (success) {
                if ("Draft".equals(updated.getContractStatus())) {
                    session.setAttribute("successMessage", "Contract saved as draft successfully!");
                } else if ("Active".equals(updated.getContractStatus())) {
                    session.setAttribute("successMessage", "Contract activated successfully!");
                } else {
                    session.setAttribute("successMessage", "Contract submitted for HR Manager approval!");
                }
                response.sendRedirect(request.getContextPath() + "/contracts/detail?id=" + contractId);
            } else {
                // reload form with errors
                request.setAttribute("errorMessage", "Failed to update contract. Please check your permissions.");
                List<Employee> employees = employeeDAO.getAllActiveEmployees();
                request.setAttribute("employees", employees);
                request.setAttribute("contract", updated);
                request.setAttribute("editMode", true);
                request.getRequestDispatcher("/contract-mgt/create-contract.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            HttpSession session2 = request.getSession();
            session2.setAttribute("errorMessage", "Invalid contract ID");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Contract Servlet";
    }
}


