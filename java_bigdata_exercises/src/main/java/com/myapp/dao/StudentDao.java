package com.myapp.dao;

import com.myapp.models.Course;
import com.myapp.models.Student;
import com.myapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class StudentDao {

    private static final Logger logger = LogManager.getLogger(StudentDao.class);

    public boolean isDatabaseEmpty() {
        // create the query using the case-insensitive HQL (Hibernate Query Language), ftw
        // Adding some extra notes to remind myself of how HQL works

        // initialize my transaction obj and my isEmpty variable
        Transaction transaction = null;
        boolean isEmpty = true;


        // Sessions are lightweight and can be opened and closed throughout.
        // The SessionFactory is thread-safe and usually application-scoped, s
        // there's only a need to call to the Hibernate utility class one time.

        // Opening up the conversation with a try/catch btwn my App and the "persistent store"...
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // ...aka the database =). I'm creating a short-lived session object, in order to...

            // ...create a query object, in order to retrieve a persistent object from the db

            // testing out the case-insensitivity of HQL to make query easier to read.
            Query<Long> query = session.createQuery("select count(s.id) from Student s",
                    Long.class);

            // If database is empty, no results (i.e. "0" is put into 'count')
            // execute query, get our single-value result (and only checks 'students' table)

            Long count = query.uniqueResult();

            // check if count > 0
            // if it is greater than '0', then there's obv info in there
            // ...and no need to update database with "dummy data"
            if (count > 0) {
                isEmpty = false;
            }

            // As a good practice, I'm including 'commit' & 'rollback' (regardless of
            // whether changes have been made--just so it's more 'future proof').
            transaction.commit();
            logger.info("Database has records inside, so no changes made.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.info("Database filled with initial Student, Course, and Instructor info.");
            }
            // log the error
            logger.error
                    ("An error occurred while trying " +
                            "to check if the database is empty.", e);
        }
        // return the true or false result
        return isEmpty;
    } // close isDatabaseEmpty() method


    // For 'save', 'update', and 'delete', IntelliJ recommends that I use the
    // 'try-with-resources' (aka 'automatic resource management') statement. Hmm...

    // Just a 'yellow' warning, but after researching it more... eh, ok. Makes sense.

    // Reason: a 'resource' object must be closed after the program is done with it.
    // So, these added-in try-with-resources statements ensure each resource is closed
    // at the end of their statement, which is better. Plus, now there's no need to
    // manually close my session with a 'finally', as they're closed in the 'try' block.

    // Took me a few 'tries' but I 'finally' got the new block working correctly =)
    public void saveStudent(Student student) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // save the student object
            session.save(student);

            transaction.commit();
            logger.info("Student saved successfully.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to save a student.", e);
        }
    }

    public void updateStudent(Student student) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Fetch the existing student from the database
            Student existingStudent = session.get(Student.class, student.getId());

            // Update the details of the existing student
            existingStudent.setFullName(student.getFullName());
            existingStudent.setAge(student.getAge());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setCourse(student.getCourse());

            // Save the updated student back to the database
            session.update(existingStudent);

            transaction.commit();
            logger.info("Student updated successfully.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to update a student.", e);
        }
    }


    public void deleteStudent(int id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, id);
            if (student != null) {
                session.delete(student);
                System.out.println("ID #: " + student.getId() + " - " + student.getFullName() + "   »» DELETED ««");
                logger.info("Student with ID #" + id + " was deleted.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to delete a student.", e);
        }
    }


    public Student getStudent(int id) {
        Transaction transaction = null;
        Student student = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

    /*        <>COMMENTED OUT<>
            student = session.get(Student.class, id);

            Access the courses field while the session is still open
               student.getCourses().size();
              <>COMMENTED OUT<>
    */

            // Updated to now use the 'JOIN FETCH' to eagerly fetch the courses for the student
            student = session.createQuery("from Student s JOIN FETCH s.courses where s.id = :studentId",
                            Student.class)
                    .setParameter("studentId", id)
                    .uniqueResult();

            transaction.commit();
            logger.info("Student ID#: " + id + "'s info (below) requested by User, " +
                    "and successfully printed to screen.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to retrieve a student.", e);
        }

        // Print the student's details after the session has been closed
        System.out.printf("%-10s %-20s %-30s %n", "Student ID", "Full Name", "Enrolled in Course(s)");
        System.out.printf("%-10d %-20s %n", student.getId(), student.getFullName());
        for (Course course : student.getCourses()) {
            System.out.printf("%-30s ", course.getName());
        }

        return student;
    }


    /*
       Explanation (and reminder to myself) about how Hibernate can be customized:

       Hmm, ok, this is interesting (and I'll prob leave in my original 'displayAllStudents()' method)
       Hibernate, by default, uses a 'lazy load' when it goes to fetch collections
          >> I discovered this when my program, though *looking* ok, was *not* functioning properly
          >> Hibernate, in order to optimize performance, uses lazy-loading, batch processing,
             second-level cache, etc. However, Hibernate (likely) won't get offended if I say,
             "Thanks, but no thanks," on the lazy-loading default business for my 'courses' set.

     [note:
       • the 'courses' HashSet from my Student class, referenced in both the above 'getStudent()',
         and the below 'displayAllStudents()' methods by the call to 'student.getCourses()'--which
         is markedly different than the single-purposes 'getCourse()'--was not showing anything
       • this lazy-loading default of collections--if I'm to keep the default in place (not a bad idea),
         means that the data isn't actually fetched from the database until I try to access it

       • I've had issues arise with NFT Marketplaces that do the same thing with their images, for
         speed reasons (i.e. they don't want the bog down their whole site by constantly calling the
         'database', usually the slow blockchain, in that case, to query for the NFT's image each time).
         Well, that's ok, unless you've got an NFT collection that's dynamic and allows the users to
         'equip' and 'unequip' other NFTs via your Project's website, then the Owner goes to a Market
         to sell and sees all this work and probably investment that they put into their NFT display
         as though they hadn't done anythng to the 'base-model' NFT

       • Anyway, in this particular instance, there's a couple ways to approach a "fix" for this:
              º 1) requires that I initialize the 'courses' collection while the session is
                still open (ex: `student.getCourses().size()` does this in 'getStudent()' and
                'displayAllStudents()')
              º 2) could be to switch the default 'FetchType' for a @ManyToMany relationship
                annotation from lazy to eager >> `@ManyToMany(fetch = FetchType.EAGER)`
                ↳ in Hibernate, there are two types of fetching strategies: 'EAGER' and 'LAZY
                ↳ if it's eager, all related entities will also be loaded whenever the entity
                  that owns the relationship is loaded (i.e. 'Student' has a relationship with
                  'Course', and so if 'Student' is loaded, and the 'FetchType' is set to 'EAGER',
                  then all the 'Course' entities are called at the same time, from the database).
                    ↳ The default fetch type for:
                       >> `@ManyToMany` and `@OneToMany` is `LAZY` and for:
                       >> `@OneToOne` and `@ManyToOne` it's `EAGER`.
       • So what I'll try here is to use a clause in Hibernate that allows me to eagerly fetch
         associations--but to specify which ones first:
              • this clause is the `JOIN FETCH` clause, and here's the HQL:
                ↳ The HQL query "from Student s JOIN FETCH s.courses"
                  ↳ so the whole line (after opening the session) would be:
                  `List<Student> students = session.createQuery("from Student s JOIN FETCH s.courses",
                   Student.class).list();`
    */

    public void displayAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use JOIN FETCH to eagerly fetch the courses for each student
            List<Student> students = session.createQuery("from Student s JOIN FETCH s.courses",
                    Student.class).list();

            students.forEach(System.out::println);
            logger.info("User requested list of all Students, and was successfully shown Student List.");
        } catch (Exception e) {
            logger.error("An error occurred while trying to display all students.", e);
        }
    } // close of 'displayAllStudents()'

    /*                    <>COMMENTED OUT<>
    // using `student.getCourses().size();` introduces an 'N+1 select problem'
    // meaning: when Hibernate retrieves an object and its associated collections,
    //          it does so with one SQL statement to retrieve the object and then
    //          'N' additional SQL statements to retrieve the 'N' number of associated
    //          collections.

    public void displayAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Student> students = session.createQuery("from Student", Student.class).list();

            for (Student student : students) {
                // Initialize the courses collection for each student
                student.getCourses().size();
            }

            students.forEach(System.out::println);
            logger.info("User requested list of all Students, and was successfully shown Student List.");
        } catch (Exception e) {
            logger.error("An error occurred while trying to display all students.", e);
        }
    }
    ////////////////////  <>COMMENTED OUT<>  ///////////////////
    */



    public void showStudentCoursesAndInstructors(int studentId) {
        Student student = getStudent(studentId);

        System.out.printf("%-10s %-20s %-30s %-30s %n", "Student ID", "Full Name", "Enrolled in Course(s)", "Instructor");
        System.out.printf("%-10d %-20s %n", student.getId(), student.getFullName());
        for (Course course : student.getCourses()) {
            System.out.printf("%-30s %-30s %n", course.getName(), course.getInstructor());
        }
    }

    public void showAllStudentsCoursesAndInstructors() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                // Use JOIN FETCH to eagerly fetch the courses for each student
                List<Student> students = session.createQuery("from Student s JOIN FETCH s.courses",
                        Student.class).list();

                System.out.printf("%-10s %-20s %-30s %-30s %n", "Student ID", "Full Name", "Enrolled in Course(s)", "Instructor");

                for (Student student : students) {
                    System.out.printf("%-10d %-20s %n", student.getId(), student.getFullName());
                    for (Course course : student.getCourses()) {
                        System.out.printf("%-30s %-30s %n", course.getName(), course.getInstructor());
                    }
                }

                logger.info("User requested list of all Students, Courses, and Instructors, and was successfully shown the list.");
            } catch (Exception e) {
                logger.error("An error occurred while trying to display all students, their courses, and instructors.", e);
            }
        }



    } // close of 'StudentDao' class
