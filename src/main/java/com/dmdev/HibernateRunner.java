package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

//            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 1);

            session.getTransaction().commit();
        }
    }

}
