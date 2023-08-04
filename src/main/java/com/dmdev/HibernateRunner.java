package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
            session.enableFetchProfile("withCompanyAndPayments");

            User user = session.get(User.class, 1L);
            System.out.println(user.getCompany().getName());

//            users.forEach(user -> System.out.println(user.getPayments().size()));
//            users.forEach(user -> System.out.println(user.getCompany().getName()));


            session.getTransaction().commit();
        }
    }

    private static void generatedTables() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            TestDataImporter.importData(sessionFactory);
        }
    }
}
