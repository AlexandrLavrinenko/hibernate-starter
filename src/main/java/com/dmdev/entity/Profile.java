package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
//    @PrimaryKeyJoinColumn // --> when the primary key (PRIMARY KEY) is also a FOREIGN KEY too
    private User user;

    private String street;

    private String language;

    public void setUser(User user) {
//        user.setProfile(this);
        this.user = user;
        // since the ID is not auto-generated, but depends on the USER entity, the following should be done:
//        this.id = user.getId(); // --> when the primary key (PRIMARY KEY) is also a FOREIGN KEY too
    }
}
