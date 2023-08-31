package com.dmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dmdev.util.StringUtils.SPACE;

//@NamedEntityGraph(name = "WithCompanyAndChat",
//attributeNodes = {
//        @NamedAttributeNode("company"),
//        @NamedAttributeNode(value = "userChats", subgraph = "chats")
//},
//subgraphs = {
//        @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat")) // UserChat.chat
//})
@NamedEntityGraph(name = "WithCompany",
        attributeNodes = {
                @NamedAttributeNode("company")
        }
)
@FetchProfile(name = "withCompanyAndPayments", fetchOverrides = {   // может ставиться над сущностью и над пакетом
        @FetchProfile.FetchOverride(
                entity = User.class,        // сущность, в которой нужно переписать нашу ассоциацию
                association = "company",    // название поля
                mode = FetchMode.JOIN       // JOIN, SELECT, SUBSELECT (для коллекций)
        ),
        @FetchProfile.FetchOverride(
                entity = User.class,
                association = "payments",
                mode = FetchMode.JOIN
        )
})
@NamedQuery(name = "findUserByName", query = "select u from User u " +
                                             "left join u.company c " +
                                             "where u.personalInfo.firstname = :firstname and c.name = :companyName " +
                                             "order by u.personalInfo.lastname desc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", /*"profile",*/ "userChats", "payments"})
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "dmdev", typeClass = JsonBinaryType.class)
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Comparable<User>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    @Valid
    private PersonalInfo personalInfo;

    @Column(unique = true)
    @NotNull
    private String username;

    @Type(type = "dmdev")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

//    @OneToOne(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//    )
//    private Profile profile;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
    private Set<UserChat> userChats = new HashSet<>();  // PersistintSet,  а не PersistentBag

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }
}