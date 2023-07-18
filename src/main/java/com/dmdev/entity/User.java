package com.dmdev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {
    // POJO (Plain Old Java Object:
    // - getters/setters, [@Data]
    // - non-final class, [@Data]
    // - non-final fields (Hibernate use Proxy via CGLib [CodeGenerationLIBrary]) [@Data]
    // - no args constructor [@NoArgsConstructor]
    // - all args constructor [@AllArgsConstructor]

    // Entity = POJO + @Id (implements Serializable interface)

    @Id
    String username;
    String firstname;
    String lastname;
    @Column(name = "birth_day")
    LocalDate birthDate;
    Integer age;
}
