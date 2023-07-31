package com.dmdev;

import com.dmdev.entity.*;
import com.dmdev.util.HibernateTestUtil;
import com.dmdev.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkH2() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = Company.builder()
                    .name("H2TestCompany")
                    .build();
            session.save(company);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrderBy() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 3);
            company.getUsers().forEach((k, v) -> System.out.println(v));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkElementCollectionLocaleInfoOneColumn() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 3);
            System.out.println(company.getLocales());

            session.getTransaction().commit();
        }
    }

    @Test
    void checkElementCollectionLocaleInfo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 3);
//            company.getLocales().add(LocaleInfo.of("UA", "Опис українською мовою"));
//            company.getLocales().add(LocaleInfo.of("EN", "English description"));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToManyCreateWithSyntheticKey() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 5L);

//            Chat chat = Chat.builder()
//                    .name("DMDev")
//                    .build();

            Chat chat = session.get(Chat.class, 2L);
            UserChat userChat = UserChat.builder()
                    .createdAt(Instant.now())
                    .createdBy(user.getUsername())
                    .build();

            userChat.setUser(user);
            userChat.setChat(chat);

            session.save(userChat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToManyGet() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 6L);
//            user.getChats().clear(); // // changed classes
//            user.getChats().removeIf(chat -> chat.getId().equals(2));

            session.flush();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToManyCreate() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 6L);

            Chat chat = Chat.builder()
                    .name("DMDev")
                    .build();

//            user.addChat(chat); // changed classes
            session.save(chat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOneGet() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 6L);// save the user, because -->
            System.out.println("Done!\n" + user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOneSave() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("BestWayOneToOne@gmail.com")
                    .build();

            Profile profile = Profile.builder()
                    .language("UA")
                    .street("Melnika, 2M")
                    .build();

            profile.setUser(user);
            session.save(user);     // User has CascadeType.ALL on field Profile

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOneGetForeignKeyAsPrimaryKeyToo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();         // start transaction

            User user = session.get(User.class, 5L);// save the user, because -->
            System.out.println("Done!\n" + user);

            session.getTransaction().commit();  // finish transaction
        }
    }

    @Test
    void checkOneToOneSaveForeignKeyAsPrimaryKeyToo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();         // start transaction

            User user = User.builder()
                    .username("testOneToOne@gmail.com")
                    .build();

            Profile profile = Profile.builder()
                    .language("UA")
                    .street("Bandery, 1B")
                    .build();

            session.save(user);     // save the user, because -->

            profile.setUser(user); // if user won't be saved - we will have error
//            session.save(profile); // if User @OneToOne doesn't have CascadeType.PERSIST or ALL

            session.getTransaction().commit();  // finish transaction
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();         // start transaction

            Company company = session.get(Company.class, 3);
            // for List or Set realisation
//            company.getUsers().removeIf(user -> user.getId().equals(3L)); // delete User with Id=3

            session.getTransaction().commit();  // finish transaction
        }
    }

    @Test
    void checkLazyInitialization() {
        Company companyForDelete = null;
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();         // start transaction

//                companyForDelete = session.get(Company.class, 3);
            companyForDelete = session.getReference(Company.class, 3); // wrap Object to Proxy with LAZY init (Company$HibernateProxy)


            session.getTransaction().commit();  // finish transaction
        }
        var users = companyForDelete.getUsers();
        System.out.println(users.size());
    }

    @Test
    void deleteUserToNewCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        {
            session.beginTransaction();         // start transaction

            Company companyForDelete = session.get(Company.class, 2);
            Hibernate.initialize(companyForDelete.getUsers());
//            session.delete(companyForDelete);

//            User userForDelete = session.get(User.class, 1L);
//            session.delete(userForDelete);

            session.getTransaction().commit();  // finish transaction
        }
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        {
            session.beginTransaction();         // start transaction

            Company company = Company.builder()
                    .name("Facebook")
                    .build();

            User user1 = User.builder()
                    .username("lesya@ukr.net")
                    .build();

//            user1.setCompany(company);
//            company.getUsers().add(user1);  ===>
            company.addUser(user1);

            session.save(company);

            session.getTransaction().commit();  // finish transaction
        }
    }

    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        {
            session.beginTransaction();         // start transaction

            Company company = session.get(Company.class, 1);
            System.out.println(company);

            session.getTransaction().commit();  // finish transaction
        }
    }

    @Test
    void checkGetReflectionApi() throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String username = resultSet.getString("username");
            String lastname = resultSet.getString("lastname");


            Class<User> clazz = User.class;
//        clazz.newInstance(); // @Deprecated

            Constructor<User> constructor = clazz.getConstructor();
            User user = constructor.newInstance();

//        Field[] declaredFields = clazz.getDeclaredFields();
            Field usernameField = clazz.getDeclaredField("username");
            usernameField.setAccessible(true);
            usernameField.set(user, username);

            Field lastnameField = clazz.getDeclaredField("lastname");
            lastnameField.setAccessible(true);
            lastnameField.set(user, lastname);
        }
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("new_user@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("New")
                        .lastname("User")
                        .birthDate(new Birthday(LocalDate.of(2020, 4, 19)))
                        .build()
                )
                .build();

        String expected = """
                insert\s
                    into
                        public.users
                        (username, firstname, lastname, birth_day)\s
                    values
                        (?, ?, ?, ?, ?)
                """;

        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        String sql = """
                insert\s
                    into
                        %s 
                        (%s)\s
                    values
                        (%s)
                """.formatted(tableName, columnNames, columnValues);

        System.out.println(sql);
//        assert

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres",
                "postgres", "postgres");
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField.get(user));
        }
    }
}