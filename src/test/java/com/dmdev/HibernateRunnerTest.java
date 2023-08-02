package com.dmdev;

import com.dmdev.entity.*;
import com.dmdev.util.HibernateTestUtil;
import com.dmdev.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.management.MemoryNotificationInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkHql() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // SQL: select u.* from users as u where u.firstname = 'Ivan';
            // 1
//            Query<User> query = session.createQuery(
//                    "select u from User u where u.personalInfo.firstname = 'Ivan'", User.class);
//            List<User> result = query.list();

            // 2
//            List<User> result = session.createQuery(
//                    "select u from User u where u.personalInfo.firstname = 'Ivan'", User.class)
//                    .setParameter(1, "Ivan")
//                    .list();

            // 3
//            List<User> result = session.createQuery(
//                            "select u from User u where u.personalInfo.firstname = ?1", User.class)
//                    .setParameter(1, "Ivan")
//                    .list();

            // 4
//            List<User> result = session.createQuery(
//                            "select u from User u where u.personalInfo.firstname = :firstname", User.class)
//                    .setParameter("firstname", "Ivan")
//                    .list();

            // 5
//            List<User> result = session.createQuery(
//                            "select u from User u " +
//                            "join u.company c " +
//                            "where u.personalInfo.firstname = :firstname and c.name = :companyName", User.class)
//                    .setParameter("firstname", "Ivan")
//                    .setParameter("companyName", "Google")
//                    .list();

            // 6
//            List<User> result = session.createQuery(
//                            "select u from User u " +
//                            "where u.personalInfo.firstname = :firstname and u.company.name = :companyName ", User.class)
//                    .setParameter("firstname", "Ivan")
//                    .setParameter("companyName", "Google")
//                    .list();

            Company company = Company.builder()
                    .name("Google")
                    .build();
            session.save(company);

            User user = User.builder()
                    .personalInfo(PersonalInfo.builder()
                            .firstname("Ivan")
                            .build())
                    .company(company)
                    .build();
            session.save(user);
            session.clear();

            // 7
            // Select query - with class-parameters
            List<User> result = session.createNamedQuery("findUserByName", User.class)
                    .setParameter("firstname", "Ivan")
                    .setParameter("companyName", "Google")
                    .setFlushMode(FlushMode.COMMIT)
                    .setHint(QueryHints.FLUSH_MODE, "commit")
                    .list();
            System.out.println(result);

            // Update query (without class-parameters)
            int countUpdUsers = session.createQuery("update User u set u.role = 'USER' " +
                                                    "where u.id = :userId")
                    .setParameter("userId", 1L)
                    .setFlushMode(FlushMode.COMMIT)
                    .executeUpdate();

            // Native query with simple SQL-query
            User userGet = session.createNativeQuery("update users as u set role = 'ADMIN' where u.id = :userId returning * ", User.class)
                    .setParameter("userId", 1L)
                    .uniqueResult();
            session.flush();
            session.refresh(userGet);

            session.getTransaction().commit();
        }
    }
}