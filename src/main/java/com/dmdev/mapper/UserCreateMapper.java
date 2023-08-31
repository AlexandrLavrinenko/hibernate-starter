package com.dmdev.mapper;

import com.dmdev.dao2.CompanyRepository;
import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;
    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .info(object.info())
                .role(object.role())
//                .company(companyRepository.findById(object.companyId()).orElse(null))
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }

    @Override
    public UserCreateDto mapTo(User entity) {
        return null;
    }
}
