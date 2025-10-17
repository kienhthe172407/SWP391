package controller.attendanceMgt;

import model.AttendancePreviewData;
import model.AttendancePreviewData.AttendancePreviewRecord;
import model.AttendanceRecord;
import dal.AttendanceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for saving previewed attendance records to database
 * Called after HR/HR Manager confirms the preview data
 */
@WebServlet(name = "SaveAttendanceServlet", urlPatterns = {"/attendance/save"})
public class SaveAttendanceServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get preview data from session
            AttendancePreviewData previewData = (AttendancePreviewData) session.getAttribute("attendancePreviewData");
            
            if (previewData == null) {
                request.setAttribute("error", "No preview data found. Please upload a file first.");
                request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
                return;
            }
            
            // Save to database (inlined save logic)
            boolean success = savePreviewData(previewData);
            
            // Clear preview data from session
            session.removeAttribute("attendancePreviewData");
            session.removeAttribute("uploadedFileName");
            
            // Prepare minimal response (only show a simple success/failure message)
            if (success) {
                request.setAttribute("message", "Import completed successfully!");
            } else {
                request.setAttribute("error", "Import failed: No records were saved");
            }
            
            request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in SaveAttendanceServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error saving data: " + e.getMessage());
            request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect to import page if accessed via GET
        response.sendRedirect(request.getContextPath() + "/attendance/import");
    }

    // ---------------- Inlined save logic from AttendanceImportService ----------------
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    private boolean savePreviewData(AttendancePreviewData previewData) {
        boolean anyFailure = false;
        for (AttendancePreviewRecord previewRecord : previewData.getRecords()) {
            if (!previewRecord.isValid()) {
                anyFailure = true;
                continue;
            }
            try {
                AttendanceRecord record = previewRecord.getRecord();
                boolean inserted = attendanceDAO.insertAttendanceRecord(record);
                if (!inserted) {
                    anyFailure = true;
                }
            } catch (Exception e) {
                anyFailure = true;
            }
        }
        return !anyFailure;
    }
}

