package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Manager extends User{

    private String projectName;

    @Builder
    public Manager(Long id, PersonalInfo personalInfo, String info, Role role, String username, Company company, Profile profile, List<UserChat> userChats, String projectName) {
        super(id, personalInfo, info, role, username, company, profile, userChats);
        this.projectName = projectName;
    }
}
