package com.myapp.dao;

import com.myapp.models.Course;
import com.myapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

// Java generics don't support runtime type checking--meaning:
// there's risk of a 'ClassCastException' because my generic's
// type information isn't available at runtime. However, in this
// case, I know that the 'createQuery()' method will only return
// a 'List' of 'Course' objects, so I'm going to suppress the warning.
@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
// Also looking to force Hibernate to load the data from the database
// while the session is still open with "ResultOfMethodCallIgnored"

public class CourseDao {

    private static final Logger logger = LogManager.getLogger(CourseDao.class);

    public List<Course> getAllCourses() {
        Transaction transaction = null;
        List<Course> listOfCourse = new ArrayList<>();  // Initialize to an empty lis
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            // start a transaction
            transaction = session.beginTransaction();
            // get a list of all courses
            listOfCourse = session.createQuery("from Course").getResultList();
            // Eagerly load the listOfCourse
            listOfCourse.size();
            // commit transaction
            transaction.commit();

            // Debug print statement
            System.out.println("Retrieved " + listOfCourse.size() + " courses from the database.");
            for (Course course : listOfCourse) {
                System.out.println("Course ID: " + course.getId() + ", Name: " + course.getName() +
                        ", Instructor: " + course.getInstructor());
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Error getting the Courses.");
            logger.error("Error getting all courses: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return listOfCourse;
    }

}
