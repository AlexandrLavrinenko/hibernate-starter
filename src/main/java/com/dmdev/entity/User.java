package com.dmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// POJO (Plain Old Java Object
// getters/setters, non-final class, non-final fields (Hibernate use Proxy via CGLib [CodeGenerationLIBrary])
@Data
@EqualsAndHashCode(of = "id")                           // calculating by one field - id
//@ToString(of = "id")                                  // toString field name
@ToString(exclude = {"company", "profile", "userChats"})// exclude field name
@NoArgsConstructor  // no args constructor
@AllArgsConstructor // all args constructor
//@Builder
@Entity             // Entity = POJO + @Id (implements Serializable interface)
//@Table(name = "users", schema = "public")
@TypeDef(name = "shortname", typeClass = JsonBinaryType.class) // doesn't work with local H2
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements Comparable<User>, BaseEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Type(type = "shortname")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)    //bidirectional, write main mapping field name
    private Profile profile;

//    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

}