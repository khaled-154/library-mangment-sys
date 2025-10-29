package database;
import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:~/librarydb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS Books (id INT PRIMARY KEY, title VARCHAR(255), author VARCHAR(255), isAvailable BOOLEAN)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Members (id INT PRIMARY KEY, name VARCHAR(255))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Borrowed (memberId INT, bookId INT)");

            System.out.println("✅ Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
