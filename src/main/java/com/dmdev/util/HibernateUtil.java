package com.dmdev.util;

import com.dmdev.converter.BirthdayConverter;
import com.dmdev.entity.*;
import com.dmdev.listener.AuditTableListener;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
// Private constructor + Final class
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        // In Hibernate:
        //  ConnectionPool -> SessionFactory
        //  Connection -> Session

        Configuration configuration = buildConfiguration();

        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        registerListeners(sessionFactory);


        return sessionFactory;
    }

    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry listenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        AuditTableListener auditTableListener = new AuditTableListener();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);     // добавляем в конец списка
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);     // добавляем в конец списка
//        listenerRegistry.prependListeners();    // добавляем в начало списка
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        // XML in hibernate.cfg.hml
        // configuration.addClass(User.class);          // old variant

        // birth_day (in DB) -> birthDay (in object)
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // one of the methods

        configuration.addAnnotatedClass(User.class);    // modern variant

        // set converter without annotation way
        configuration.addAttributeConverter(new BirthdayConverter(), true);

        // registry Type
        configuration.registerTypeOverride(new JsonBinaryType());

        configuration.addAnnotatedClass(Company.class); // modern variant
        configuration.addAnnotatedClass(Profile.class); // modern variant
        configuration.addAnnotatedClass(Chat.class);    // modern variant
        configuration.addAnnotatedClass(UserChat.class);// modern variant
//        configuration.addAnnotatedClass(Programmer.class);
//        configuration.addAnnotatedClass(Manager.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(Audit.class);
        return configuration;
    }

}
