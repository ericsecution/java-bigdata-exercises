package com.myapp;

import com.myapp.dao.CourseDao;
import com.myapp.dao.StudentDao;
import com.myapp.models.Course;
import com.myapp.models.Student;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        Scanner scanner = new Scanner(System.in);

        CourseDao courseDao = new CourseDao();
        List<Course> courses = courseDao.getAllCourses();



        if (studentDao.isDatabaseEmpty()) {
            runImportSqlScript();


            System.out.println("Welcome to the Student Management System!");
            System.out.println("Please enter your details.");

            System.out.print("Enter your Full Name: ");
            String name = scanner.nextLine();

            int age = -1;
            while (age == -1) {
                System.out.print("Enter your current Age: ");
                try {
                    age = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    logger.error("Invalid input. Please enter a valid number for your age.", e);
                    System.out.println("Invalid input. Please enter a valid number for your age.");
                }
            }

            System.out.print("Enter the Email that you'd like to use for this Course: ");
            String email = scanner.nextLine();

            // Show Course & Instructor List
            System.out.println("Great, and there's no need to signup just yet if you're not ready to decide.");
            System.out.println("However, let's have you select the course that you're most interested in: ");
            displayCourseList(courses);
            Course selectedCourse = selectCourse(courses, scanner);

            // Get the course name from the selected course
            String courseName = selectedCourse.getName();

            // Pass the course name to the Student constructor
            Student currentUser = new Student(name, age, email, courseName);



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
                        studentDao.saveStudent(currentUser);
                        System.out.println("You've been added to the Class!");


                        break;
                    case "C":
                        // Code to create a new student
                        System.out.println("Sounds like a plan! Let's get this Student's info entered in.");
                        System.out.println("What's their Full Name?");
                        String studentName = scanner.nextLine();
                        System.out.println("What's their current Age?");
                        int studentAge = Integer.parseInt(scanner.nextLine());
                        System.out.println("What's the best Email to contact them at about this Course?");
                        String studentEmail = scanner.nextLine();

                        displayCourseList(courses);
                        Course studentCourse = selectCourse(courses, scanner);

                        // Get the course name from the selected course
                        String studentCourseName = studentCourse.getName();

                        // Pass the course name to the Student constructor
                        Student addedStudent = new Student(studentName, studentAge,
                                studentEmail, studentCourseName);

                        studentDao.saveStudent(addedStudent);
                        break;
                    case "R":
                        // Code to read info on a current student
                        System.out.println("Please enter the ID of the student that you want to view?");
                        int id = Integer.parseInt(scanner.nextLine());
                        Student student = studentDao.getStudent(id);
                        System.out.println(student);
                        break;
                    case "U":
                        // Code to update a student's records
                        System.out.println("Enter the ID of the student you want to update:");
                        int updateId = Integer.parseInt(scanner.nextLine());
                        Student updateStudent = studentDao.getStudent(updateId);

                        System.out.println("What's their updated Full Name?");
                        updateStudent.setFullName(scanner.nextLine());

                        System.out.println("What's their updated Age?");
                        updateStudent.setAge(Integer.parseInt(scanner.nextLine()));

                        System.out.println("What's their updated Email?");
                        updateStudent.setEmail(scanner.nextLine());

                        // Display the Courses again, in lieu of user's selection for an updatedCourse
                        displayCourseList(courses);
                        Course updatedCourse = selectCourse(courses, scanner);
                        updateStudent.setCourse(updatedCourse.getName());
                        studentDao.updateStudent(updateStudent);
                        break;
                    case "D":
                        // Code to delete a student record completely
                        System.out.println("Enter the ID of the student you want to delete:");
                        int deleteId = Integer.parseInt(scanner.nextLine());
                        studentDao.deleteStudent(deleteId);
                        break;
                    case "E":
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter A (to add-in your info), "
                                + "C, R, U, D, or E (to Exit).");
                } // close `switch(choice)` statement

            } // close `while(true)` loop

        } // close `if (studentDao.isDatabaseEmpty())` statement

        scanner.close();
    } // close of my 'main' method


    private static Course selectCourse(List<Course> courses, Scanner scanner) {
        int courseNumber = -1;
        while (courseNumber == -1) {
            System.out.print("Enter the number of the course you're interested in: ");
            try {
                courseNumber = Integer.parseInt(scanner.nextLine());
                if (courseNumber < 1 || courseNumber > courses.size()) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input. Please enter a valid course number.", e);
                System.out.println("Invalid input. Please enter a valid course number.");
                courseNumber = -1; // reset courseNumber to -1 so the loop continues
            }
        }
        return courses.get(courseNumber - 1); // subtract 1 because list is 0-indexed
    }


    private static void displayCourseList(List<Course> courses) {
        System.out.println("Here are the available courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
    }

    private static Student createStudentFromUserInput(List<Course> courses, Scanner scanner) {
        System.out.print("Enter your Full Name: ");
        String name = scanner.nextLine();

        int age = -1;
        while (age == -1) {
            System.out.print("Enter your current Age: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                logger.error("Invalid input. Please enter a valid number for your age.", e);
                System.out.println("Invalid input. Please enter a valid number for your age.");
            }
        }

        System.out.print("Enter the Email that you'd like to use for this Course: ");
        String email = scanner.nextLine();

        // Show Course & Instructor List
        System.out.println("Great, and there's no need to signup just yet if you're not ready to decide.");
        System.out.println("However, let's have you select the course that you're most interested in: ");
        displayCourseList(courses);
        Course selectedCourse = selectCourse(courses, scanner);

        // Get the course name from the selected course
        String courseName = selectedCourse.getName();

        // Pass the course name to the Student constructor
        return new Student(name, age, email, courseName);
    }

    private static void runImportSqlScript() {
        // code to run the import.sql script
        try {
            // get the file's path
            URL resource = Main.class.getClassLoader().getResource("import.sql");
            if (resource == null) {
                throw new FileNotFoundException("Could not find file: 'import.sql'.");
            }
            String path = resource.getPath();

            // read the file's content
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