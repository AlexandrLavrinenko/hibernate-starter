package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.entity.Profile;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;
import org.hibernate.jdbc.Work;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

//            session.doWork(connection -> connection.setAutoCommit(true));

            Profile profile = Profile.builder()
                    .user(session.find(User.class, 1L))
                    .language("ua")
                    .street("Banderi, 28")
                    .build();
            session.save(profile);

//            session.beginTransaction();
            List<Payment> payments = session.createQuery("select p from Payment p", Payment.class)
                    .list();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);        // 3
//            session.save(payment);                            // 4
//            session.flush();

//            session.getTransaction().commit();
        }
    }

}
