package com.myapp.dao;

import com.myapp.models.Student;
import com.myapp.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StudentDao {

    public boolean isDatabaseEmpty() {
        return true;
    }
    private static final Logger logger = LogManager.getLogger(StudentDao.class);
    public void saveStudent(Student student) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // save the student object
            session.save(student);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to save a student.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    public void updateStudent(Student student) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.update(student);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("An error occurred while trying to update a student.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteStudent(int id) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
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
        } finally {
            if (session != null) {
                session.close();
            }
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
}
