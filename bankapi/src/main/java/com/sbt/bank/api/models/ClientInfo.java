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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ClientInfo {

    @NotBlank
    @Size(min = 1, max = 20)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 20)
    @Column(name = "last_name")
    private String lastName;

    @Min(0)
    @Max(150)
    @NotNull
    private Integer age;

    @Email(message = "Incorrect email")
    @Size(min = 10, max = 100)
    @NotNull
    private String email;

    @Column(name = "gender")
    @NonNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(max = 200)
    @NotNull
    private String address;

    @Pattern(regexp = "^(\\\\+\\\\d{1,3}( )?)?((\\\\(\\\\d{1,3}\\\\))|\\\\d{1,3})[- .]?\\\\d{3,4}[- .]?\\\\d{4}$")
    private String phone;
}
