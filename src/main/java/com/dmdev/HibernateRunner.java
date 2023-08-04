package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = session.get(User.class, 1L);
//            System.out.println(user.getPayments().size());      // LAZY
//            System.out.println(user.getCompany().getName());    // LAZY

            List<User> users = session.createQuery("select u from User u ", User.class)
                    .list();

//            users.forEach(user -> System.out.println(user.getPayments().size()));   // Будет сформировано N + 1 запросов.
            // 1 - запрос на получение юзеров, и по отдельному запросу на каждого юзера на получение платежей.
            users.forEach(user -> System.out.println(user.getCompany().getName()));


            session.getTransaction().commit();
        }
    }

    private static void generatedTables() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            TestDataImporter.importData(sessionFactory);
        }
    }
}
