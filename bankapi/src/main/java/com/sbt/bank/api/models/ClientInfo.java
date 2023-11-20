package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ClientInfo {

    @NotBlank(message = "Personal ID must not be blank")
    @Size(min = 5, max = 30, message = "The length of the personal ID must be from 5 to 30 characters")
    @Column(name = "personal_id")
    private String personalID;

    @NotBlank(message = "First name must not be blank")
    @Size(min = 5, max = 20, message = "The length of the first name must be from 5 to 20 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(min = 5, max = 20, message = "The length of the last name must be from 5 to 20 characters")
    @Column(name = "last_name")
    private String lastName;

    @Min(value = 0, message = "Age must be more 0")
    @Max(value = 150, message = "Age must be less 150")
    @NotNull(message = "Age must not be null")
    private Integer age;

    @NotBlank(message = "Email must not be blank")
    @Size(min = 5, max = 100, message = "The length of email must be from 5 to 100 characters")
    @Email(message = "Incorrect email")
    private String email;

    @Column(name = "gender")
    @NotNull(message = "Gender must not be null")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(min = 1, max = 200, message = "The length of address must be from 1 to 200 characters")
    @NotBlank(message = "Address must not be blank")
    private String address;

    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Incorrect phone format, support formats: +xxx (xxx) xxx xxxx (delimiters: \"-\", \" \" and without")
    private String phone;
}
