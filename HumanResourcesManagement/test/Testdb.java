import dal.DBContext;
import java.sql.Connection;
import java.sql.SQLException;

public class Testdb extends DBContext {

    public static void main(String[] args) {
        Testdb test = new Testdb();
        test.checkConnection();
    }

    public void checkConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    System.out.println("Connection is alive!");
                } else {
                    System.out.println("Connection is closed.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Connection is null.");
        }
    }
}
