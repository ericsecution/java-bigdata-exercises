package com.myapp.dao;

import com.myapp.models.Course;
import com.myapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDao {
    public List<Course> getAllCourses() {
        Transaction transaction = null;
        List<Course> listOfCourse = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // get a list of all courses
            listOfCourse = session.createQuery("from Course").getResultList();
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listOfCourse;
    }
}
