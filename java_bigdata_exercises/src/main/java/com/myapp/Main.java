package com.myapp;

import com.myapp.dao.CourseDao;
import com.myapp.dao.StudentDao;
import com.myapp.models.Course;
import com.myapp.models.Student;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        Scanner scanner = new Scanner(System.in);

        CourseDao courseDao = new CourseDao();
        List<Course> courses = courseDao.getAllCourses();

        boolean isUserInfoEntered = false;




        if (studentDao.isDatabaseEmpty()) {
            runImportSqlScript();
        } // close `if (studentDao.isDatabaseEmpty())` statement

            System.out.println();
            System.out.println("╔═══════════════════════════════════════════" +
                                "══════════════════════════════════════════╗");
            System.out.println("║                      Welcome to the Student Management System!"
                              +"                      ║");
            System.out.println("║                                                               "
                              +"                      ║");
            System.out.println("║                    » Would you like to enter your details now? «"
                +"                    ║");
            System.out.println("║                                                               "
                +"                      ║");
            System.out.println("║                                    (Y)es / (N)o               "
                              +"                      ║");
            System.out.println("╚═══════════════════════════════════════════" +
                                "══════════════════════════════════════════╝");
            System.out.printf("%-30s %n", "↳");
            System.out.printf("»»        ");

            String userChoice = scanner.nextLine().toUpperCase();

            Student currentUser = null;
            if (userChoice.equals("Y")) {
                currentUser = createStudentFromUserInput(courses, scanner);
                isUserInfoEntered = true;
            }

            boolean running = true;
            while (running) {
                System.out.println();
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
                System.out.println("Current date and time: " + now.format(formatter));
                System.out.println("╔═══════════════════════════════════════════" +
                                    "══════════════════════════════════════════╗");
                System.out.println("║                              Student Management System"+
                                   "                              ║");
                System.out.println("║                                       MAIN MENU"+
                                   "                                     ║");
                System.out.println("╚═══════════════════════════════════════════" +
                                    "══════════════════════════════════════════╝");
                System.out.println();
                System.out.println("────────────────────────────────────────────"+
                        "───────────────────────────────────────────");
                System.out.printf("%-30s %-30s %-30s %n", "▫", "▪", "▫");
                System.out.println("What would you like to do?");
                System.out.println("--------------------------------------------"+
                                    "-------------------------------------------");
                System.out.printf("%-30s %-30s %-30s %n", "(A)dd Yourself",
                        "(C)reate a new Student", "(R)ead info for a Student");
                System.out.printf("%-30s %-30s %-30s %n", "(U)pdate a Student's records",
                        "(D)elete a Student's records", "(S)how entire Course List");
                System.out.printf("%-30s", "(V)iew Attending Students");
                System.out.printf("%38s %n", "(E)xit");
                System.out.println("--------------------------------------------"+
                                    "-------------------------------------------");
                System.out.printf("%-30s %-30s %-30s %n", "▫", "▪", "▫");
                System.out.println("────────────────────────────────────────────"+
                        "───────────────────────────────────────────");
                System.out.println("↳  ⇣");
                System.out.println("»»          ");

                String choice = scanner.nextLine().toUpperCase();


                // IntelliJ recommended using the 'enhanced switch statement'
                // to both make it more concise and easier to read, and also
                // to avoid any potential fall-through (as per Java 12 Docs).
                switch (choice) {
                    case "A" -> {
                        // check if they've already entered in their info
                        if (!isUserInfoEntered) {
                            currentUser = createStudentFromUserInput(courses, scanner);
                            isUserInfoEntered = true;
                        }
                        // Code to add the current user's info
                        studentDao.saveStudent(currentUser);
                        System.out.println("You've been added to the Class!");
                    }
                    case "C" -> {
                        // Code to create a new student
                        System.out.println("────────────────────────────────────────────"+
                                "───────────────────────────────────────────");
                        System.out.println("Sounds like a plan! Let's get this Student's info entered in.");
                        System.out.println("What's their Full Name?");
                        String fullName = scanner.nextLine();

                        System.out.println("What's their current Age?");
                        int age = Integer.parseInt(scanner.nextLine());

                        System.out.println("What's the best Email to contact them at about this Course?");
                        String email = scanner.nextLine();

                        // Display the Courses again, in lieu of user's selection for an updatedCourse
                        displayCourseList(courses);
                        Course selectedCourse = selectCourse(courses, scanner);

                        Student newStudent = new Student(fullName, age, email, selectedCourse.getName());
                        studentDao.saveStudent(newStudent);

                        // Display a success message
                        System.out.printf("%-35s %-45s\n", "Full Name:", newStudent.getFullName());
                        System.out.printf("%-35s %-45s\n", "Age:", newStudent.getAge());
                        System.out.printf("%-35s %-45s\n", "Email:", newStudent.getEmail());
                        System.out.printf("%-35s %-45s\n", "Course:", newStudent.getCourse());

                        System.out.println();
                        System.out.println("The new student has been successfully created!");
                        System.out.println("────────────────────────────────────────────"+
                                "───────────────────────────────────────────");
                    }

                    case "R" -> {
                        // Code to read info on a current student
                        System.out.println("════════════════════════════════════════════" +
                                "═══════════════════════════════════════════");
                        System.out.println("Please enter the ID of the student that you want to view?");
                        System.out.println("↳            ⇣");
                        int id = Integer.parseInt(scanner.nextLine());
                        Student student = studentDao.getStudent(id);
                        System.out.println(student);
                    }
                    case "U" -> {
                        // Code to update a student's records
                        System.out.println("Enter the ID of the student you want to update:");
                        int updateId = Integer.parseInt(scanner.nextLine());
                        Student updateStudent = studentDao.getStudent(updateId);
                        System.out.println();
                        System.out.println();
                        System.out.println();
                        System.out.println("════════════════════════════════════════════" +
                                "═══════════════════════════════════════════");
                        System.out.println("You've selected Student ID#: " + updateStudent.getId()
                                +", " + updateStudent.getFullName()
                                + ".\nIs this correct?  (Y)es / (N)o ?");
                        System.out.println("↳");
                        System.out.println("»»          ");
                        String confirm = scanner.nextLine().toUpperCase();
                        if (confirm.equals("Y")) {
                            // Store the original details of the student
                            String originalFullName = updateStudent.getFullName();
                            int originalAge = updateStudent.getAge();
                            String originalEmail = updateStudent.getEmail();
                            String originalCourse = updateStudent.getCourse();
                            System.out.println("════════════════════════════════════════════" +
                                    "═══════════════════════════════════════════");
                            System.out.println("What's their updated Full Name?");
                            System.out.println("↳            ⇣");
                            updateStudent.setFullName(scanner.nextLine());

                            System.out.println("What's their updated Age?");
                            System.out.println("↳            ⇣");
                            updateStudent.setAge(Integer.parseInt(scanner.nextLine()));

                            System.out.println("What's their updated Email?");
                            System.out.println("↳            ⇣");
                            updateStudent.setEmail(scanner.nextLine());

                            // Display the Courses again, in lieu of user's selection for an updatedCourse
                            System.out.println("════════════════════════════════════════════" +
                                    "═══════════════════════════════════════════");
                            displayCourseList(courses);
                            Course updatedCourse = selectCourse(courses, scanner);
                            updateStudent.setCourse(updatedCourse.getName());
                            studentDao.updateStudent(updateStudent);
                            System.out.println("════════════════════════════════════════════" +
                                    "═══════════════════════════════════════════");
                            // Display the changes
                            System.out.println();
                            System.out.println("You've updated ⇢ 'Student ID # : "
                                    + updateStudent.getId() + ", " + originalFullName + "' ⇠");
                            if (!originalFullName.equals(updateStudent.getFullName())) {
                                System.out.printf("%-45s %-55s\n", "Previous Full Name: "
                                        + originalFullName, "⇨ Updated Full Name: "
                                        + updateStudent.getFullName() + " ⇦");
                            }
                            if (originalAge != updateStudent.getAge()) {
                                System.out.printf("%-45s %-55s\n", "Previous Age: "
                                        + originalAge, "⇨ Updated Age: "
                                        + updateStudent.getAge() +" ⇦");
                            }
                            if (!originalEmail.equals(updateStudent.getEmail())) {
                                System.out.printf("%-45s %-55s\n", "Previous Email: "
                                        + originalEmail, "⇨ Updated Email: "
                                        + updateStudent.getEmail() + " ⇦");
                            }
                            if (!originalCourse.equals(updateStudent.getCourse())) {
                                System.out.printf("%-45s %-55s\n", "Previous Course: "
                                        + originalCourse, "⇨ Updated Course: "
                                        + updateStudent.getCourse() + " ⇦");
                            }
                            System.out.println("↳ All Set!");
                            System.out.println("════════════════════════════════════════════" +
                                    "═══════════════════════════════════════════");

                        } // end 'if(confirm.equals("Y"))' statement
                    }

                    case "D" -> {
                        // Code to delete a student record completely
                        System.out.println("Enter the ID of the student you want to delete:");
                        System.out.println("--------------------------------------------"+
                                "-------------------------------------------");
                        System.out.print(">> ");
                        int deleteId = Integer.parseInt(scanner.nextLine());
                        Student exitingStudent = studentDao.getStudent(deleteId);
                        String possibleDeletedStudent = exitingStudent.getFullName();
                        System.out.print("Do you really want to Delete: ");
                        System.out.print(possibleDeletedStudent + " from the SMS? (Y)es / (N)o"+"\n»       ");
                        String deleteChoice = scanner.nextLine().toUpperCase();
                        if (deleteChoice.equals("Y")) {
                            String deletedOutStudent = exitingStudent.toString();
                            studentDao.deleteStudent(deleteId);
                            System.out.println(deletedOutStudent + " »» DELETED ««");
                        }
                    }
                    case "S" -> {
                        displayCourseList(courses);
                        System.out.println("Would you like to:");
                        System.out.println("(I)nclude Instructors with Courses List");
                        System.out.println("(S)how Students in the Instructors with Courses List");
                        System.out.println("(B)ack to Main Menu");
                        System.out.print("Enter your choice: ");
                        String subChoice = scanner.nextLine().toUpperCase();
                        switch (subChoice) {
                            case "I" -> {
                                // Code to display courses with their instructors
                                for (Course course : courses) {
                                    System.out.println(course.getName() + " - Instructor: " + course.getInstructor());
                                }
                            }
                            case "S" -> {
                                // Code to show students in the instructors with courses list

                                System.out.println("Students in the Instructors with Courses List:");
                                studentDao.showAllStudentsCoursesAndInstructors();
                            }
                            case "B" -> {
                                // Simply go back to the main menu
                            }
                            default -> {
                                System.out.println("Invalid choice. Returning to the main menu.");
                            }
                        }
                    }

                    case "V" -> studentDao.displayAllStudents();
                    case "E" -> {
                        System.out.println("Do you really want to Exit the entire "
                                + "Student Management System? (Y)es / (N)o");
                        String exitChoice = scanner.nextLine().toUpperCase();
                        if (exitChoice.equals("Y")) {
                            running = false;
                        }
                    }
                    default -> {
                        System.out.println("Invalid choice. Please enter A (to add-in your info), "
                                + "C, R, U, D, or E (to Exit).");
                        logger.info("Invalid choice. Please enter A (to add-in your info), "
                                + "C, R, U, D, or E (to Exit).");
                    }
                } // close `switch(choice)` statement


            } // close `while(running)` loop

        scanner.close(); // close that scanner!
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
        String name = getValidatedInput("Enter your Full Name: ", scanner);
        String ageInput = getValidatedInput("Enter your current Age: ", scanner, true);
        int age = Integer.parseInt(ageInput);
        String email = getValidatedInput("Enter the Email that you'd like to use for this Course: ", scanner);

        System.out.println("Great, and there's no need to signup just yet if you're not ready to decide.");
        System.out.println("However, let's have you select the course that you're most interested in: ");
        displayCourseList(courses);
        Course selectedCourse = selectCourse(courses, scanner);

        String courseName = selectedCourse != null ? selectedCourse.getName() : "";

        return new Student(name, age, email, courseName);
    }

    private static String getValidatedInput(String prompt, Scanner scanner) {
        return getValidatedInput(prompt, scanner, false);
    }

    private static String getValidatedInput(String prompt, Scanner scanner, boolean isNumeric) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (isNumeric && !isNumeric(input)) {
                System.out.println("Invalid input. Please enter a valid number.");
            } else {
                break;
            }
        }
        return input;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static void runImportSqlScript() {
        // code to run the import.sql script
        try {
            // get the file's path
            URL url = Main.class.getClassLoader().getResource("import.sql");
            if (url == null) {
                throw new FileNotFoundException("Could not find the file: 'import.sql'.");
            }
            Path resPath = Paths.get(url.toURI());
            String sql = new String(Files.readAllBytes(resPath), "UTF8");

            // log the sql commands
            logger.info("SQL commands: " + sql);

            // Get connection and create a statement
            Connection connection = DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/universe_city",
                            "postgres", System.getenv("DB_PASSWORD"));
            Statement statement = connection.createStatement();

            // Execute the SQL commands
            statement.executeUpdate(sql);

            // log a message indicating that the SQL commands were executed
            logger.info("SQL commands executed successfully.");

            // Close the statement and connection
            statement.close();
            connection.close();
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        } catch (IOException e) {
            logger.error("File error: ", e);
        } catch (URISyntaxException e) {
            logger.error("URI Syntax error: ", e);
        }
    }

}