package com.dmdev.util;

import com.dmdev.entity.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
// Private constructor + Final class
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        // In Hibernate:
        //  ConnectionPool -> SessionFactory
        //  Connection -> Session

        Configuration configuration = new Configuration();
        // XML in hibernate.cfg.hml
        // configuration.addClass(User.class);          // old variant
        configuration.addAnnotatedClass(User.class);    // modern variant
        configuration.addAnnotatedClass(Company.class); // modern variant
        configuration.addAnnotatedClass(Profile.class); // modern variant
        configuration.addAnnotatedClass(Chat.class);    // modern variant
        configuration.addAnnotatedClass(UserChat.class);// modern variant

        // birth_day (in DB) -> birthDay (in object)
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // one of the methods

        // set converter without annotation way
        // configuration.addAttributeConverter(new BirthdayConverter(), true);

        // registry Type
        configuration.registerTypeOverride(new JsonBinaryType());

        configuration.configure();
        return configuration.buildSessionFactory();
    }

}
