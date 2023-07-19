package com.dmdev;

import com.dmdev.entity.Birthday;
import com.dmdev.entity.User;

import javax.persistence.Column;
import javax.persistence.Table;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

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
                .firstname("New")
                .lastname("User")
                .birthDate(new Birthday(LocalDate.of(2020, 4, 19)))
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