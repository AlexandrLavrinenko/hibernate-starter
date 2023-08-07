package com.dmdev;

import com.dmdev.entity.Payment;
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

//            session.setDefaultReadOnly(true);                   // для всех сущностей
//            session.setReadOnly(Payment.class, true);           // для конкретной сущности
            session.beginTransaction();

            session.createNativeQuery("SET TRANSACTION READ ONLY;")
                    .executeUpdate();                             // READ ONLY на уровне БД

//            TestDataImporter.importData(sessionFactory);

//            List<Payment> payments = session.createQuery("select p from Payment p", Payment.class)
//                    .setReadOnly(true)
//                    .setHint(QueryHints.READ_ONLY, true)
//                    .list();

            Payment payment = session.find(Payment.class, 1L);

            // случайная попытка изменения
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }

}
