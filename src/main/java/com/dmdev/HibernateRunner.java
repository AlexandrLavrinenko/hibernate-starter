package com.dmdev;

import com.dmdev.entity.Birthday;
import com.dmdev.entity.Company;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
//        Company company = Company.builder()
//                .name("Google")
//                .build();

//        User user = User.builder()
//                .username("Ostap@gmail.com")
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Ostap")
//                        .lastname("Vishnya")
//                        .birthDate(new Birthday(LocalDate.of(2001, 1, 1)))
//                        .build())
//                .company(company)
//                .build();               // Transient


//        checkProxy(user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();

                // save company
                Company company = session.get(Company.class, 3);

                User user = User.builder()
                        .username("Ostap@gmail.com")
                        .personalInfo(PersonalInfo.builder()
                                .firstname("Ostap")
                                .lastname("Vishnya")
                                .birthDate(new Birthday(LocalDate.of(2001, 1, 1)))
                                .build())
                        .company(company)
                        .build();               // Transient
                // save user with company
                session.save(user);    // Transient -> Persistent


                session.getTransaction().commit();
            }
        }

    }

    private static void checkProxy(User user) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();

                // save company
//                session.save(company);
                // save user with company
//                session.save(user);    // Transient -> Persistent

                User user1 = session.get(User.class, 1L);
                Company company1 = user1.getCompany();      // no run intercept method in ByteBuddyInterceptor
                String company1Name = company1.getName();   // run ByteBuddyInterceptor.intercept

                Company realCompany = (Company) Hibernate.unproxy(company1);    // return Object

                session.getTransaction().commit();
            }
            log.warn("User is in DETACHED state: {}, session is closed: {}", user, session);
        }
    }
}
