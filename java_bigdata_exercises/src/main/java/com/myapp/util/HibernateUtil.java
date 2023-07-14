package com.myapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);

    private static SessionFactory buildSessionFactory() {
        // switched this method to a simpler, high-level API with 'Configuration()'
        // Using the docs' low-level 'StandardServiceRegistry' and 'MetadataSources'
        // Hibernate APIs was "technically" correct, but also too complex for my purposes here.
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            // Logging the exception
            logger.error("Initial SessionFactory creation failed.", e);
            // re-throwing (as I was swallowing the exception before)
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}


