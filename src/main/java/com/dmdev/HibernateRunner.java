package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.DataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            DataImporter.importData(sessionFactory);

            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);
            session.save(payment);

            session.getTransaction().commit();
        }
    }

}
