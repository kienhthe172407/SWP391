<%@ page import="org.mindrot.jbcrypt.BCrypt" %>
<%
    // Generate BCrypt hash for admin password
    String password = "admin123"; // Change this to your desired password
    String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
    
    out.println("<h2>Admin Password Hash Generator</h2>");
    out.println("<p><strong>Password:</strong> " + password + "</p>");
    out.println("<p><strong>BCrypt Hash:</strong> " + hash + "</p>");
    out.println("<hr>");
    out.println("<p>Run this SQL to update admin password:</p>");
    out.println("<pre>UPDATE users SET password_hash = '" + hash + "' WHERE username = 'admin';</pre>");
%>
