import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseHandler {
    private Connection conn;

    public void connect() {
        String url = "jdbc:postgresql://localhost/test";
        Properties props = new Properties();
        System.out.println("USER: " + "postgres");
        System.out.println("DB_PASSWORD: " + System.getenv("DB_PASSWORD"));
        props.setProperty("password", System.getenv("DB_PASSWORD"));

        props.setProperty("user", "postgres");
        props.setProperty("password", System.getenv("DB_PASSWORD"));
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        1. CRUD - Build out my CRUD (Create, Read, Update, and Delete) methods.
        2. Data Validation - ensure only valid data is entered into the database
        3. Exception Handling - Make my program more robust, and easier to debug
        4. Logging - Remove the Sys-Outs and implement the 'Log4J' library
        5. Unit Testing - Write unit tests to make sure they're working as expected
        6. Integration with a Web Framework - Once data access layer is solid, begin to
                build a web interface for my app using the Java web framework, Spring Boot.
     */
    public void addStudent(String name, int age, String email, String course) {
        String query = "INSERT INTO students(name, age, email, course) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, course);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                String course = rs.getString("course");
                students.add(new Student(id, name, age, email, course));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.connect();
        dbHandler.addStudent("John Doe", 22, "john.doe@example.com", "Computer Science");

        List<Student> students = dbHandler.getStudents();
        for (Student student : students) {
            System.out.println(student);
        }

    }

}

