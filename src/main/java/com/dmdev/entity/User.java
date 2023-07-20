package com.dmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @GeneratedValue(generator = "custom_user_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "custom_user_generator",
            schema = "public",
            sequenceName = "users_id_seq",  // default Hibernate sequence: hibernate_sequence
            initialValue = 1,               // start position (default = 1)
            allocationSize = 1)             // allocationSize() - as increment
    private Long id;

    @Column(unique = true)
    private String username;

    @Embedded
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Type(type = "shortname")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;
}