package com.dmdev;

import com.dmdev.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class HibernateRunner {
    public static void main(String[] args) throws SQLException {
//      In Hibernate:
//        ConnectionPool -> SessionFactory
//        Connection -> Session

        Configuration configuration = new Configuration();
        // XML in hibernate.cfg.hml
//        configuration.addClass(User.class);           // old variant
        configuration.addAnnotatedClass(User.class);    // modern variant

        // birth_day (in DB) -> birthDay (in object)
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // one of the methods

        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
//            System.out.println("Connection is OK");
            session.beginTransaction();

            User user = User.builder()
                    .username("new_user@gmail.com")
                    .firstname("New")
                    .lastname("User")
                    .birthDate(LocalDate.of(2020, 4, 19))
                    .age(20)
                    .build();

//            session.save(user);   // @Deprecated
            session.persist(user);  // Current method

            session.getTransaction().commit();
//            session.getTransaction().rollback();
        }
    }

    private static void beforeHibernate() throws SQLException {
        BlockingDeque<Connection> pool = new LinkedBlockingDeque<>(10);
        Connection connection = DriverManager.getConnection("db.url", "db.username", "db.password");
        pool.add(connection);
    }
}
