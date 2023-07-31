package com.dmdev.entity;

import lombok.*;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Data
@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale",
    joinColumns = @JoinColumn(name = "company_id"))
//    @AttributeOverride(name = "lang", @Column(name = "language"))
    @Column(name = "description")   // Map value
    @MapKeyColumn(name = "lang")    // Map key
    private List<String> locales = new ArrayList<>();
//    private List<String> locales = new ArrayList<>();
//    private List<LocaleInfo> locales = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, orphanRemoval = true)
//    @org.hibernate.annotations.OrderBy(clause = "username DESC, lastname ASC")  // SQL
//    @javax.persistence.OrderBy("username DESC, personalInfo.lastname ASC")      // HQL
//    @OrderColumn(name = "id")                                                   // only for List<Integer>
    @SortNatural                                                                  // Map sort only for key, not value
//    @SortComparator()                                                           // Class<? extends Comparator
//    @JoinColumn(name = "company_id")
//    private List<User> users = new ArrayList<>();
//    private SortedSet<User> users = new TreeSet<>();
    @MapKey(name = "username")                                                    // Entity field name
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        // List or Set realisation
//        users.add(user);
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }
}
