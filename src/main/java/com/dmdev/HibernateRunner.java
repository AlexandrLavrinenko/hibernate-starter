package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = session.get(User.class, 1L);    // get не подхоит, не обладает нужнім API
            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("WithCompanyAndChat")
            );
            User user = session.find(User.class, 1L, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());


            List<User> users = session.createQuery("select u from User u " +
                                                  "where 1 = 1", User.class)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("WithCompanyAndChat"))
                    .list();
            users.forEach(it -> System.out.println(it.getCompany().getName()));
            users.forEach(it -> System.out.println(it.getUserChats().size()));


            session.getTransaction().commit();
        }
    }

    private static void generatedTables() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            TestDataImporter.importData(sessionFactory);
        }
    }
}
