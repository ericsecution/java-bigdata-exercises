import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost/test";
    private static final String USER = "postgres";
    private static final String PASS = System.getenv("DB_PASSWORD");
    /* replace above line with either your own local / 'env' DB_PASSWORD
            or set it on your server (if deployed to your Server), or just use:

            private static final String PASS = "<your-actual-password";
    */

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

}
