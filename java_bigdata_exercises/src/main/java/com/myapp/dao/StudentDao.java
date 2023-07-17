package com.myapp.dao;

import com.myapp.models.Student;
import com.myapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

            // execute query, get our single-value result
            Long count = query.uniqueResult();

            // check if count < 0
            if (count > 0) {
                isEmpty = false;
            }

            // as a good practice, including 'commit' & 'rollback'
            // (even though obv no changes made)
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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

            session.update(student);

            transaction.commit();
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
        Session session = null;
        Transaction transaction = null;
        Student student = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            student = session.get(Student.class, id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to retrieve a student.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return student;
    }

    public void displayAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Student> students = session.createQuery("from Student", Student.class).list();
            students.forEach(System.out::println);
        } catch (Exception e) {
            logger.error("An error occurred while trying to display all students.", e);
        }
    }

}
