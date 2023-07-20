package com.dmdev.entity;

import com.dmdev.converter.BirthdayConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable // embedded component
public class PersonalInfo {
    private String firstname;
    private String lastname;

    @Convert(converter = BirthdayConverter.class)
    @Column(name = "birth_date")
    private Birthday birthDate;
}
