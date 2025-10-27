package controller.jobPostingMgt;

import dal.JobApplicationDAO;
import dal.JobPostingDAO;
import model.JobApplication;
import model.JobPosting;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Servlet for handling job application submissions from guests
 * Supports file upload for resumes (PDF, DOC, DOCX)
 */
@WebServlet(name = "SubmitJobApplicationServlet", urlPatterns = {"/jobs/apply"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 5,        // 5 MB
    maxRequestSize = 1024 * 1024 * 10     // 10 MB
)
public class SubmitJobApplicationServlet extends HttpServlet {
    
    private static final String UPLOAD_DIR = "resumes";
    
    /**
     * Handle GET request - display application form
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get job ID parameter
        String jobIdParam = request.getParameter("jobId");
        
        if (jobIdParam == null || jobIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        try {
            int jobId = Integer.parseInt(jobIdParam);
            
            // Create DAO instance
            JobPostingDAO jobPostingDAO = new JobPostingDAO();
            
            // Get job posting details
            JobPosting jobPosting = jobPostingDAO.getJobPostingById(jobId);
            
            if (jobPosting == null) {
                request.setAttribute("errorMessage", "Job posting not found.");
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            
            // Check if job is still open
            if (!"Open".equals(jobPosting.getJobStatus())) {
                request.setAttribute("errorMessage", "This job posting is no longer accepting applications.");
                response.sendRedirect(request.getContextPath() + "/jobs/detail?jobId=" + jobId);
                return;
            }
            
            // Check deadline
            if (jobPosting.getApplicationDeadline() != null) {
                java.sql.Date deadline = jobPosting.getApplicationDeadline();
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                if (deadline.before(today)) {
                    request.setAttribute("errorMessage", "The application deadline has passed.");
                    response.sendRedirect(request.getContextPath() + "/jobs/detail?jobId=" + jobId);
                    return;
                }
            }
            
            // Set attributes for JSP
            request.setAttribute("jobPosting", jobPosting);
            
            // Forward to application form
            request.getRequestDispatcher("/job-posting-mgt/apply-job.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid job ID format: " + jobIdParam);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            System.err.println("Error in SubmitJobApplicationServlet doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
    
    /**
     * Handle POST request - process application submission
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get form parameters
            String jobIdParam = request.getParameter("jobId");
            String applicantName = request.getParameter("applicantName");
            String applicantEmail = request.getParameter("applicantEmail");
            String applicantPhone = request.getParameter("applicantPhone");
            String coverLetter = request.getParameter("coverLetter");
            
            // Validate required fields
            if (jobIdParam == null || applicantName == null || applicantEmail == null ||
                jobIdParam.trim().isEmpty() || applicantName.trim().isEmpty() || applicantEmail.trim().isEmpty()) {
                request.setAttribute("error", "Please fill in all required fields.");
                doGet(request, response);
                return;
            }
            
            int jobId = Integer.parseInt(jobIdParam);
            
            // Handle file upload
            Part filePart = request.getPart("resumeFile");
            String resumeFilePath = null;
            
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getSubmittedFileName(filePart);
                
                // Validate file type
                if (!isValidResumeFile(fileName)) {
                    request.setAttribute("error", "Please upload a valid resume file (PDF, DOC, or DOCX).");
                    doGet(request, response);
                    return;
                }
                
                // Save file
                resumeFilePath = saveFile(filePart, fileName);
            }
            
            // Create JobApplication object
            JobApplication application = new JobApplication();
            application.setJobId(jobId);
            application.setApplicantName(applicantName.trim());
            application.setApplicantEmail(applicantEmail.trim());
            application.setApplicantPhone(applicantPhone != null ? applicantPhone.trim() : null);
            application.setResumeFilePath(resumeFilePath);
            application.setCoverLetter(coverLetter != null ? coverLetter.trim() : null);
            application.setApplicationStatus("Submitted");
            
            // Save application to database
            JobApplicationDAO applicationDAO = new JobApplicationDAO();
            boolean success = applicationDAO.createJobApplication(application);
            
            if (success) {
                session.setAttribute("successMessage", 
                    "Your application has been submitted successfully! We will contact you soon.");
                response.sendRedirect(request.getContextPath() + "/jobs/apply?jobId=" + jobId);
            } else {
                request.setAttribute("error", "Failed to submit application. Please try again.");
                doGet(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error in SubmitJobApplicationServlet doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while submitting your application.");
            doGet(request, response);
        }
    }
    
    /**
     * Get submitted file name from Part
     */
    private String getSubmittedFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
    
    /**
     * Validate if file is a valid resume format
     */
    private boolean isValidResumeFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        String lowerFileName = fileName.toLowerCase();
        return lowerFileName.endsWith(".pdf") || 
               lowerFileName.endsWith(".doc") || 
               lowerFileName.endsWith(".docx");
    }
    
    /**
     * Save uploaded file to server
     */
    private String saveFile(Part filePart, String fileName) throws IOException {
        // Get application path
        String applicationPath = getServletContext().getRealPath("");
        String uploadPath = applicationPath + File.separator + UPLOAD_DIR;
        
        // Create directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Generate unique file name
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = uploadPath + File.separator + uniqueFileName;
        
        // Save file
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Return relative path for database storage
        return UPLOAD_DIR + "/" + uniqueFileName;
    }
}

