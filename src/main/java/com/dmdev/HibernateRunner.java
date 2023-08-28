package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.entity.User;
import com.dmdev.interceptor.GlobalInterceptor;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.DataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//                DataImporter.importData(sessionFactory);
            User user = null;
            try (Session session = sessionFactory.openSession()) {

                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                User userAgain = session.find(User.class, 1L);

                List<Payment> payments = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", user.getId())
                        .setCacheable(true)                     // 1 way
//                        .setCacheRegion("queries")
                        //.setHint(QueryHints.CACHEABLE, true)  // 2 way
                        .getResultList();

                System.out.println(sessionFactory.getStatistics());
                sessionFactory.getStatistics().getCacheRegionStatistics("Companies");

                session.getTransaction().commit();
            }

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                User user2 = session2.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();

                List<Payment> payments2 = session2.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", user2.getId())
                        .setCacheable(true)             // required!!!
//                        .setCacheRegion("queries")
                        .getResultList();

                session2.getTransaction().commit();
            }
        }
    }
}
