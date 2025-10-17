package controller.attendanceMgt;

import util.AttendanceImportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Servlet for importing attendance records from Excel files
 * Handles file upload and delegates to AttendanceImportService
 */
@WebServlet(name = "ImportAttendanceServlet", urlPatterns = {"/attendance/import"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class ImportAttendanceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Display import form
        request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get uploaded file
            Part filePart = request.getPart("attendanceFile");
            
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "Please select a file to upload");
                request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
                return;
            }
            
            // Validate file type
            String fileName = filePart.getSubmittedFileName();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                request.setAttribute("error", "Please upload an Excel file (.xlsx or .xls)");
                request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
                return;
            }
            
            // Generate unique batch ID for this import
            String importBatchID = "BATCH_" + UUID.randomUUID().toString();
            
            // Get input stream from uploaded file
            InputStream inputStream = filePart.getInputStream();
            
            // Perform import
            AttendanceImportService importService = new AttendanceImportService();
            boolean success = importService.importFromExcel(inputStream, importBatchID);
            
            // Prepare response
            if (success) {
                request.setAttribute("importBatchID", importBatchID);
                request.setAttribute("successCount", importService.getSuccessCount());
                request.setAttribute("errorCount", importService.getErrorCount());
                request.setAttribute("successMessages", importService.getSuccessMessages());
                request.setAttribute("errorMessages", importService.getErrorMessages());
                
                if (importService.getErrorCount() > 0) {
                    request.setAttribute("message", 
                        "Import completed with " + importService.getSuccessCount() + 
                        " successful and " + importService.getErrorCount() + " failed records");
                } else {
                    request.setAttribute("message", 
                        "Import completed successfully! " + importService.getSuccessCount() + 
                        " records imported");
                }
            } else {
                request.setAttribute("error", "Import failed: " + 
                    (importService.getErrorMessages().isEmpty() ? 
                    "Unknown error" : importService.getErrorMessages().get(0)));
            }
            
            request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ImportAttendanceServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing file: " + e.getMessage());
            request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
        }
    }
}

