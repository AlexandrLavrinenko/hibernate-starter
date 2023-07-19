package com.dmdev;

import com.dmdev.entity.Birthday;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class HibernateRunner {
//    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {
//        Configuration configuration = getConfiguration();
//        saveUser(configuration);
//        updateUser(configuration);
//        deleteUser(configuration);
//        getUser(configuration);
        // Transient entity state
        User user = User.builder()
                .username("12345a@gmail.com")
                .firstname("Taras")
                .lastname("Shevchenko")
                .build();
        log.info("User entity is in a TRANSIENT state, object: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created, {}", transaction);

                session1.saveOrUpdate(user);    // Transient -> Persistent
                log.trace("User is in PERSISTENT state: {}, session: {}", user, session1);

                session1.getTransaction().commit();
            }
            log.warn("User is in DETACHED state: {}, session is closed: {}", user, session1);
        } catch (Exception exception) {
            log.error("Exception occured", exception);
            throw exception;
        }


    }

    private static void getUser(Configuration configuration) {
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // DB row -> User.class
            User user = session.get(User.class, "new_admin@gmail.com");
            user.setLastname("Tarasyao");

            /**
             * Force this session to flush. Must be called at the end of a
             * unit of work, before committing the transaction and closing the
             * session (depending on {@link #setFlushMode(FlushMode)},
             * {@link Transaction#commit()} calls this method).
             * <p/>
             * <i>Flushing</i> is the process of synchronizing the underlying persistent
             * store with persistable state held in memory.
             *
             * @throws HibernateException Indicates problems flushing the session or
             * talking to the database.
             */
            session.flush();

            /**
             * Does this session contain any changes which must be synchronized with
             * the database?  In other words, would any DML operations be executed if
             * we flushed this session?
             *
             * @return True if the session contains pending changes; false otherwise.
             * @throws HibernateException could not perform dirtying checking
             */
            System.out.println(session.isDirty());
            session.getTransaction().commit();
            System.out.println(user);
        }
    }

    private static void deleteUser(Configuration configuration) {
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("new_admin10@gmail.com")
                    .firstname("New10")
                    .lastname("Admin10")
                    .birthDate(new Birthday(LocalDate.of(2015, 4, 20)))
                    .role(Role.ADMIN)
                    .info("""
                            {
                            "name": "Test",
                            "id": 25
                            }
                            """)
                    .build();

            // pending delete request
            // first, the presence of this entity is checked
            session.delete(user);

            session.getTransaction().commit();
        }
    }

    private static void updateUser(Configuration configuration) {
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("new_admin10@gmail.com")
                    .firstname("New10")
                    .lastname("Admin10")
                    .birthDate(new Birthday(LocalDate.of(2015, 4, 20)))
                    .role(Role.ADMIN)
                    .info("""
                            {
                            "name": "Test",
                            "id": 25
                            }
                            """)
                    .build();

//            session.update(user);
            // pending delete request
            // first, the presence of this entity is checked
            session.saveOrUpdate(user);

            session.getTransaction().commit();
        }
    }

    private static void saveUser(Configuration configuration) {
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
//            System.out.println("Connection is OK");
            session.beginTransaction();

            User user = User.builder()
                    .username("new_admin@gmail.com")
                    .firstname("New")
                    .lastname("Admin")
                    .birthDate(new Birthday(LocalDate.of(2015, 4, 20)))
                    .role(Role.ADMIN)
                    .info("""
                            {
                            "name": "Test",
                            "id": 25
                            }
                            """)
                    .build();

//            session.save(user);   // @Deprecated
            session.persist(user);  // Current method

            session.getTransaction().commit();
//            session.getTransaction().rollback();
        }
    }

    private static Configuration getConfiguration() {
        //      In Hibernate:
//        ConnectionPool -> SessionFactory
//        Connection -> Session

        Configuration configuration = new Configuration();
        // XML in hibernate.cfg.hml
//        configuration.addClass(User.class);           // old variant
        configuration.addAnnotatedClass(User.class);    // modern variant

        // birth_day (in DB) -> birthDay (in object)
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // one of the methods

        // set converter without annotation way
//        configuration.addAttributeConverter(new BirthdayConverter(), true);

        // registry Type
        configuration.registerTypeOverride(new JsonBinaryType());

        configuration.configure();
        return configuration;
    }

    private static void beforeHibernate() throws SQLException {
        BlockingDeque<Connection> pool = new LinkedBlockingDeque<>(10);
        Connection connection = DriverManager.getConnection("db.url", "db.username", "db.password");
        pool.add(connection);
    }
}
