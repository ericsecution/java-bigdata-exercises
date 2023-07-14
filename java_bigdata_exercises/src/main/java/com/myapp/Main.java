package com.myapp;

import com.myapp.dao.StudentDao;
import com.myapp.models.Student;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

import static com.mysql.cj.conf.PropertyKey.logger;

public class Main {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        Scanner scanner = new Scanner(System.in);

        if (studentDao.isDatabaseEmpty()) {
            runImportSqlScript();


            System.out.println("Welcome to the Student Management System!");
            System.out.println("Please enter your details.");

            System.out.print("Enter your Full Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your current Age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter the Email that you'd like to use for this Course: ");
            String email = scanner.nextLine();

            System.out.print("Enter the Name of the Course that you're interested in: ");
            String course = scanner.nextLine();

            Student currentUser = new Student(name, age, email, course);

            while (true) {

                System.out.println("\nWhat would you like to do?");
                System.out.println("(A)dd yourself to the class that you're interested in?");
                System.out.println("(C)reate a new Student?");
                System.out.println("(R)ead info on a current Student?");
                System.out.println("(U)pdate a Students records?");
                System.out.println("(D)elete a Student record completely?");
                System.out.println("(E)xit");

                String choice = scanner.nextLine().toUpperCase();

                switch (choice) {
                    case "A":
                        // Code to add the current user's info
                        break;
                    case "C":
                        // Code to create a new student
                        break;
                    case "R":
                        // Code to read info on a current student
                        break;
                    case "U":
                        // Code to update a student's records
                        break;
                    case "D":
                        // Code to delete a student record completely
                        break;
                    case "E":
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter A (to add-in your info), "
                                + "C, R, U, D, or E (to Exit).");
                }

//                Student student = new Student("Yogi Bear", 22,
//                        "yogi@jellystonepark.com", "Hibernation");
//                studentDao.saveStudent(student);
            }

        }
    }

    private static void runImportSqlScript() {
        // code to run the import.sql script
        try {
            // get the file's path (added a null check to avoid potential NullPointerException
            URL resource = Main.class.getClassLoader().getResource("import.sql");
            if (resource == null) {
                throw new FileNotFoundException("Could not find file: 'import.sql'.");
            }
            String path = resource.getPath();

            // read the file's content (replacing the way I learned with the newer 'Files.readString()'
//             String sql = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String sql = Files.readString(Paths.get(path), StandardCharsets.UTF_8);

            // Get connection and create a statement
            Connection connection = DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/universe_city",
                            "postgres", System.getenv("DB_PASSWORD"));
            Statement statement = connection.createStatement();

            // Execute the SQL commands
            statement.executeUpdate(sql);

            // Close the statement and connection
            statement.close();
            connection.close();
        } catch (Exception e) {
            // Print out exception message
            System.out.println("Error running the 'import.sql' script: " + e.getMessage());
        }
    }
}
