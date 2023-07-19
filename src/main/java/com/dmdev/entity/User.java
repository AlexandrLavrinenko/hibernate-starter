package com.dmdev.entity;

import com.dmdev.converter.BirthdayConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "shortname", typeClass = JsonBinaryType.class)
public class User {
    // POJO (Plain Old Java Object:
    // - getters/setters, [@Data]
    // - non-final class, [@Data]
    // - non-final fields (Hibernate use Proxy via CGLib [CodeGenerationLIBrary]) [@Data]
    // - no args constructor [@NoArgsConstructor]
    // - all args constructor [@AllArgsConstructor]

    // Entity = POJO + @Id (implements Serializable interface)

    @Id
    private String username;
    private String firstname;
    private String lastname;

    @Convert(converter = BirthdayConverter.class)
    @Column(name = "birth_date")
    private Birthday birthDate;

    @Type(type = "shortname")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;
}