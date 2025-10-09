package dal;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DBContext {
    protected Connection connection;

    public DBContext() {
        try {
            // Thông tin kết nối MySQL
            String user = "root";
            String pass = "123456";
            String url = "jdbc:mysql://127.0.0.1:3306/hr_management_system?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo connection
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to MySQL successfully.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "MySQL Driver not found.", ex);
            throw new RuntimeException("MySQL JDBC Driver not found. Ensure mysql-connector-j is on the classpath.", ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Connection failed.", ex);
            throw new RuntimeException("Failed to establish database connection. Check URL/credentials and that MySQL is running.", ex);
        }
    }
    
    
}
