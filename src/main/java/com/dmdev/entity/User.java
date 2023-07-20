package com.dmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

// POJO (Plain Old Java Object
@Data
// getters/setters, non-final class, non-final fields (Hibernate use Proxy via CGLib [CodeGenerationLIBrary])
@NoArgsConstructor  // no args constructor
@AllArgsConstructor // all args constructor
@Builder
@Entity             // Entity = POJO + @Id (implements Serializable interface)
@Table(name = "users", schema = "public")
@TypeDef(name = "shortname", typeClass = JsonBinaryType.class)
public class User {

    @EmbeddedId
//    @Embedded  - unnecessary annotation if there is @EmbeddedId
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Type(type = "shortname")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String username;

//    @Id
//    @GeneratedValue(generator = "custom_user_generator", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(
//            name = "custom_user_generator",
//            schema = "public",
//            sequenceName = "users_id_seq",  // default Hibernate sequence: hibernate_sequence
//            initialValue = 1,               // start position (default = 1)
//            allocationSize = 1)             // allocationSize() - as increment
//    private Long id;
}