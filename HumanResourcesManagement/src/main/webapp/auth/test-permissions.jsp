<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="util.PermissionChecker" %>
<%@ page import="model.User" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Permissions - Debug</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding: 20px; }
        .permission-item { 
            padding: 8px; 
            margin: 2px 0; 
            border-left: 3px solid #28a745;
            background: #f8f9fa;
        }
        .no-permission { 
            border-left-color: #dc3545;
            background: #fff5f5;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>🔍 Test Permissions - Debug Page</h2>
        
        <%
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                String role = currentUser.getRole();
                Set<String> userPermissions = PermissionChecker.getUserPermissions(currentUser);
        %>
        
        <div class="card mt-3">
            <div class="card-header bg-primary text-white">
                <h5>Current User Information</h5>
            </div>
            <div class="card-body">
                <p><strong>Username:</strong> <%= currentUser.getUsername() %></p>
                <p><strong>Role:</strong> <%= role %></p>
                <p><strong>Number of permissions:</strong> <%= userPermissions.size() %></p>
            </div>
        </div>
        
        <div class="card mt-3">
            <div class="card-header bg-success text-white">
                <h5>Permissions List for Role: <%= role %></h5>
            </div>
            <div class="card-body">
                <% if (userPermissions.isEmpty()) { %>
                    <div class="alert alert-warning">
                        <i class="fas fa-exclamation-triangle"></i>
                        This role has NO permissions!
                    </div>
                <% } else { %>
                    <div class="row">
                        <% for (String perm : userPermissions) { %>
                            <div class="col-md-6">
                                <div class="permission-item">
                                    ✅ <%= perm %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>
        </div>
        
        <div class="card mt-3">
            <div class="card-header bg-info text-white">
                <h5>Test Important Permissions</h5>
            </div>
            <div class="card-body">
                <% 
                    String[] testPermissions = {
                        "USER_VIEW", "USER_CREATE", "USER_EDIT",
                        "EMPLOYEE_VIEW", "EMPLOYEE_CREATE", "EMPLOYEE_EDIT",
                        "SYSTEM_CONFIG", "PERMISSION_MANAGE"
                    };
                    
                    for (String testPerm : testPermissions) {
                        boolean hasPerm = PermissionChecker.hasPermission(currentUser, testPerm);
                        String cssClass = hasPerm ? "permission-item" : "permission-item no-permission";
                        String icon = hasPerm ? "✅" : "❌";
                %>
                    <div class="<%= cssClass %>">
                        <%= icon %> <%= testPerm %> - <%= hasPerm ? "HAS PERMISSION" : "NO PERMISSION" %>
                    </div>
                <% } %>
            </div>
        </div>
        
        <div class="card mt-3">
            <div class="card-header bg-secondary text-white">
                <h5>All Roles and Permissions in System</h5>
            </div>
            <div class="card-body">
                <% 
                    Set<String> allRoles = PermissionChecker.getAllRoles();
                    for (String r : allRoles) {
                        Set<String> rolePerms = PermissionChecker.getRolePermissions(r);
                %>
                    <div class="mb-3">
                        <h6><strong><%= r %></strong> (<%= rolePerms.size() %> permissions)</h6>
                        <div class="ms-3">
                            <% for (String p : rolePerms) { %>
                                <span class="badge bg-secondary me-1"><%= p %></span>
                            <% } %>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
        
        <% } else { %>
            <div class="alert alert-danger">
                You are not logged in!
            </div>
        <% } %>
        
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/permission-settings" class="btn btn-primary">
                ← Back to Permission Matrix
            </a>
        </div>
    </div>
</body>
</html>
