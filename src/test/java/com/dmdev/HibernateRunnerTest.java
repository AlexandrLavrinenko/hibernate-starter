package com.dmdev;

import com.dmdev.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

class HibernateRunnerTest {
    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("new_user@gmail.com")
                .firstname("New")
                .lastname("User")
                .birthDate(LocalDate.of(2020, 4, 19))
                .age(20)
                .build();

        String expected = """
                insert\s
                    into
                        public.users
                        (username, firstname, lastname, birth_day, age)\s
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

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (Field declaredField: declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField.get(user));
        }
    }
}