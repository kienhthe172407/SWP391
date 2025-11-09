package controller.salaryMgt;

import model.SalaryComponent;
import model.SalaryPreviewData;
import model.SalaryPreviewData.SalaryPreviewRecord;
import dal.SalaryComponentDAO;
import dal.EmployeeDAO;
import model.Employee;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.UUID;

// Apache POI for Excel parsing
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Servlet for importing salary components from Excel files
 * Handles file upload and preview before saving
 * @author admin
 */
@WebServlet(name = "ImportSalaryServlet", urlPatterns = {"/salary/import"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class ImportSalaryServlet extends HttpServlet {
    
    private final SalaryComponentDAO salaryComponentDAO = new SalaryComponentDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Check authorization (HR and HR Manager only)
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        if (!"HR".equals(role) && !"HR Manager".equals(role) && !"HR_MANAGER".equals(role)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can import salary data.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Check if there are existing salary components in the database
        try {
            List<model.SalaryComponent> existingComponents = salaryComponentDAO.getAllActiveSalaryComponents();
            boolean hasExistingData = existingComponents != null && !existingComponents.isEmpty();
            request.setAttribute("hasExistingSalaryData", hasExistingData);
            request.setAttribute("totalSalaryComponents", hasExistingData ? existingComponents.size() : 0);
        } catch (Exception e) {
            System.err.println("Error checking existing salary components: " + e.getMessage());
            request.setAttribute("hasExistingSalaryData", false);
            request.setAttribute("totalSalaryComponents", 0);
        }

        // Display import form
        request.getRequestDispatcher("/salary-mgt/import-salary.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Check authorization (HR and HR Manager only)
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        if (!"HR".equals(role) && !"HR Manager".equals(role) && !"HR_MANAGER".equals(role)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can import salary data.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            // Get uploaded file
            Part filePart = request.getPart("salaryFile");

            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "Please select a file to upload");
                request.getRequestDispatcher("/salary-mgt/import-salary.jsp").forward(request, response);
                return;
            }

            // Validate file type
            String fileName = filePart.getSubmittedFileName();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                request.setAttribute("error", "Please upload an Excel file (.xlsx or .xls)");
                request.getRequestDispatcher("/salary-mgt/import-salary.jsp").forward(request, response);
                return;
            }

            // Generate unique batch ID for this import
            String importBatchID = "SALARY_BATCH_" + UUID.randomUUID().toString();

            // Get input stream from uploaded file
            InputStream inputStream = filePart.getInputStream();

            // Parse Excel file for preview
            SalaryPreviewData previewData = parseExcelForPreview(inputStream, importBatchID);

            // Store preview data in session for later confirmation
            session.setAttribute("salaryPreviewData", previewData);
            session.setAttribute("uploadedFileName", fileName);

            // Set attributes for display
            request.setAttribute("previewData", previewData);
            request.setAttribute("message", "File parsed successfully. Please review the data below and click 'Save' to confirm.");

            request.getRequestDispatcher("/salary-mgt/import-salary.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in ImportSalaryServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing file: " + e.getMessage());
            request.getRequestDispatcher("/salary-mgt/import-salary.jsp").forward(request, response);
        }
    }

    /**
     * Parse Excel file and create preview data
     * @param inputStream Excel file input stream
     * @param importBatchID Unique batch ID for this import
     * @return SalaryPreviewData with parsed records
     * @throws Exception if parsing fails
     */
    private SalaryPreviewData parseExcelForPreview(InputStream inputStream, String importBatchID) throws Exception {
        SalaryPreviewData previewData = new SalaryPreviewData(importBatchID);
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return previewData;
            }
            
            // Start from row 1 (skip header row 0)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                SalaryPreviewRecord previewRecord = new SalaryPreviewRecord(rowIndex + 1);
                
                try {
                    // Employee code for display
                    Cell employeeCodeCell = row.getCell(0);
                    String employeeCode = getCellValueAsString(employeeCodeCell);
                    previewRecord.setEmployeeCode(employeeCode);

                    // Parse row to SalaryComponent
                    SalaryComponent component = parseRowToSalaryComponent(row);
                    
                    if (component != null) {
                        previewRecord.setSalaryComponent(component);
                        
                        // Get employee name
                        Employee employee = employeeDAO.getEmployeeById(component.getEmployeeID());
                        if (employee != null) {
                            previewRecord.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
                        }
                        
                        // Check if employee already has active salary component
                        SalaryComponent existing = salaryComponentDAO.getActiveSalaryComponent(component.getEmployeeID());
                        if (existing != null) {
                            previewRecord.setValid(true);
                            previewRecord.setWillUpdate(true);
                            // Note: We will deactivate the old one and create a new one
                        } else {
                            previewRecord.setValid(true);
                            previewRecord.setWillUpdate(false);
                        }
                    }
                } catch (Exception e) {
                    previewRecord.setErrorMessage(e.getMessage());
                }
                
                previewData.addRecord(previewRecord);
            }
        }
        
        return previewData;
    }

    /**
     * Parse a single Excel row to SalaryComponent object
     * Expected columns: Employee Code, Base Salary, Position Allowance, Housing Allowance,
     *                   Transportation Allowance, Meal Allowance, Other Allowances, Effective From
     * @param row Excel row
     * @return SalaryComponent object
     * @throws Exception if parsing fails
     */
    private SalaryComponent parseRowToSalaryComponent(Row row) throws Exception {
        // Column 0: Employee Code
        Cell employeeCodeCell = row.getCell(0);
        String employeeCode = getCellValueAsString(employeeCodeCell);
        if (employeeCode.isEmpty()) {
            throw new Exception("Employee Code is required");
        }
        
        Employee employee = employeeDAO.getEmployeeByCode(employeeCode);
        if (employee == null) {
            throw new Exception("Employee with code '" + employeeCode + "' not found");
        }

        // Column 1: Base Salary (required)
        Cell baseSalaryCell = row.getCell(1);
        if (baseSalaryCell == null || baseSalaryCell.getCellType() == CellType.BLANK) {
            throw new Exception("Base Salary is required");
        }
        BigDecimal baseSalary = getCellValueAsBigDecimal(baseSalaryCell);
        if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Base Salary cannot be negative");
        }

        // Column 2: Position Allowance
        BigDecimal positionAllowance = getCellValueAsBigDecimal(row.getCell(2));
        
        // Column 3: Housing Allowance
        BigDecimal housingAllowance = getCellValueAsBigDecimal(row.getCell(3));
        
        // Column 4: Transportation Allowance
        BigDecimal transportationAllowance = getCellValueAsBigDecimal(row.getCell(4));
        
        // Column 5: Meal Allowance
        BigDecimal mealAllowance = getCellValueAsBigDecimal(row.getCell(5));
        
        // Column 6: Other Allowances
        BigDecimal otherAllowances = getCellValueAsBigDecimal(row.getCell(6));
        
        // Column 7: Effective From (required)
        Cell effectiveFromCell = row.getCell(7);
        if (effectiveFromCell == null || effectiveFromCell.getCellType() == CellType.BLANK) {
            throw new Exception("Effective From date is required");
        }
        Date effectiveFrom = getCellValueAsDate(effectiveFromCell);

        // Create SalaryComponent object
        SalaryComponent component = new SalaryComponent();
        component.setEmployeeID(employee.getEmployeeID());
        component.setBaseSalary(baseSalary);
        component.setPositionAllowance(positionAllowance);
        component.setHousingAllowance(housingAllowance);
        component.setTransportationAllowance(transportationAllowance);
        component.setMealAllowance(mealAllowance);
        component.setOtherAllowances(otherAllowances);
        component.setEffectiveFrom(effectiveFrom);
        component.setEffectiveTo(null); // Open-ended
        component.setActive(true);

        return component;
    }

    /**
     * Get cell value as String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.format("%d", (long) numValue);
                    } else {
                        return String.format("%s", numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Get cell value as BigDecimal
     */
    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return BigDecimal.ZERO;
        }
        
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            String value = cell.getStringCellValue().trim();
            if (value.isEmpty()) {
                return BigDecimal.ZERO;
            }
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }
        
        return BigDecimal.ZERO;
    }

    /**
     * Get cell value as Date
     */
    private Date getCellValueAsDate(Cell cell) throws Exception {
        if (cell == null) {
            throw new Exception("Date cell is null");
        }
        
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return new Date(cell.getDateCellValue().getTime());
        } else if (cell.getCellType() == CellType.STRING) {
            String dateStr = cell.getStringCellValue().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return new Date(sdf.parse(dateStr).getTime());
        }
        
        throw new Exception("Invalid date format");
    }
}

