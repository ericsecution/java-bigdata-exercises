package com.myapp.database.handlers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.myapp.models.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseHandler {
    private static final Logger logger = LogManager.getLogger(DatabaseHandler.class);
    private Connection conn;

    public void connect() {
        String url = "jdbc:postgresql://localhost/test";
        Properties props = new Properties();
        System.out.println("USER: " + "postgres");
        System.out.println("DB_PASSWORD: " + "••••••••••");
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

    // Create (from CRUD) Command
    public void addStudent(String name, int age, String email, String course) {
        String query = "INSERT INTO students(name, age, email, course) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, course);
            pstmt.executeUpdate();
            logger.info("Successfully added a new student: " + name);
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                logger.error("Error: A student with the same ID already exists.", e);
            } else if (e instanceof SQLDataException) {
                logger.error("Error: Invalid data format.", e);
            } else {
                logger.error("An error occurred while adding the student.", e);
            }
        }
    }


    // Read (from CRUD) command
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
            if (e instanceof SQLSyntaxErrorException) {
                logger.error("Error: There is a syntax error in the SQL query.", e);
            } else if (e instanceof SQLDataException) {
                logger.error("Error: Invalid data format.", e);
            } else {
                logger.error("An error occurred while retrieving the students.", e);
            }
        }

        return students;
    }

    // Update (from CRUD) command
    public void updateStudent(int id, String name, int age, String email, String course) {
        String query = "UPDATE students SET name = ?, age = ?, email = ?, course = ? WHERE id = ?";
        // 'try' to update using a PreparedStatement
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, course);
            pstmt.setInt(5, id);

            // Check the Rows to see if the UPDATE changed anything
            int checkRows = pstmt.executeUpdate();
            // if not, I know that the ID doesn't exist (possibly bc it was Deleted prior)
            if (checkRows == 0) {
                // and I can notify whoever's working with the code in the future
                logger.info("No Student found with ID #" + id);
            } // otherwise, run through exceptions
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                logger.error("Error: Unable to update student. " +
                        "Please ensure the student ID is correct.", e);
            } else if (e instanceof SQLDataException) {
                logger.error("Error: Invalid data format.", e);
            } else {
                logger.error("An error occurred while updating the student.", e);
            }
        }
    }

    public void updateStudentEmail(int id, String newEmail) {
        String query = "UPDATE students SET email = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, id);
            // Check the Rows to see if the UPDATE changed anything
            int checkRows = pstmt.executeUpdate();
            // if not, I know that the ID doesn't exist (possibly bc it was Deleted prior)
            if (checkRows == 0) {
                // and I can notify whoever's working with the code in the future
                logger.info("No Student found with ID #" + id);
            } // otherwise, run through exceptions
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                logger.error("Error: Unable to update student. " +
                        "Please ensure the student ID is correct.", e);
            } else if (e instanceof SQLDataException) {
                logger.error("Error: Invalid data format.", e);
            } else {
                logger.error("An error occurred while updating the student.", e);
            }
        }
    }

    // Delete (from CRUD) command
    public void deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            int checkRows = pstmt.executeUpdate();
            if (checkRows == 0) {
                logger.info("No Student was Deleted at ID #" + id + ". " +
                        "Check your ID #, and first make sure a "+
                        "Student with that ID exists to Delete.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                logger.error("Error: Unable to delete student. " +
                        "Please ensure the student ID is correct", e);
            } else if (e instanceof SQLDataException) {
                logger.error("Error: Invalid data format.", e);
            } else {
                logger.error("An error occurred while deleting the student.", e);
            }
        }
    }


    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.connect();
        // comment this out for now, to prevent addt'l 'John Doe's until code amended
//        dbHandler.addStudent("Johnny Eight", 36,
//                "jgone@goodbye.com", "Johnny Gonny");
//      dbHandler.addStudent("Eric Apple", 32, "eric.apple@phonefruit.com", "Computer Science");

        // Post 'addStudent()' method and/or the Database as it currently stands
        System.out.println("==================");
        System.out.println("====  START  ====");
        System.out.println("** List of Current Students in Database **");
        List<Student> students = dbHandler.getStudents();
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("==============");
        System.out.println();

        // Calling / Checking the 'updateStudent()' method
        dbHandler.updateStudent(7, "Jeanluc Hewlitt Packard", 42,
                "newguy1@notarealemail.com", "Shipwide Analytics");
        System.out.println("==============");
        System.out.println("** List of Current Students--post-UPDATE--in Database **");
        students = dbHandler.getStudents();
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("==============");
        System.out.println();

        // Calling / Checking the 'updateStudent()' method
        // Calling it again but with a dif / non-existing ID.
        dbHandler.updateStudent(3, "Hunter Shazam", 28,
                "hunters@themoviesemailsys.com", "Superhero Survival Skills");
        System.out.println("==============");
        System.out.println("** List of Current Students--post-UPDATE #2--in Database **");
        students = dbHandler.getStudents();
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println();
        dbHandler.updateStudentEmail(6, "jDynasty1@morenewemails.com");

        // commented out for now, so Students don't continually get deleted.
//        dbHandler.deleteStudent(8);
        System.out.println("==============");
        System.out.println("** List of Current Students after Updating "
                + "a Student's Email &/or Deleting a Student **");
        students = dbHandler.getStudents();
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("=================");
        System.out.println("==== THE END ====");
        System.out.println();
    }

}

