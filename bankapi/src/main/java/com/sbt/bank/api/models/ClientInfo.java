package com.sbt.bank.api.models;

import io.swagger.v3.oas.annotations.media.Schema;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность персональных данных клиента
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Gender
 * @see Client
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Embeddable
public class ClientInfo {

    /**
     * Индивидуальный код клиента (аналогия паспорта)
     */
    @Schema(description = "Персональный идентификатор(паспорт)", example = "4545 222222")
    @NotBlank(message = "Personal ID must not be blank")
    @Size(min = 5, max = 30, message = "The length of the personal ID must be from 5 to 30 characters")
    @Column(name = "personal_id")
    private String personalID;

    /**
     * Имя клиента
     */
    @Schema(description = "Имя клиента", example = "Максим")
    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, max = 20, message = "The length of the first name must be from 5 to 20 characters")
    @Column(name = "first_name")
    private String firstName;

    /**
     * Фамилия клиента
     */
    @Schema(description = "Фамилия клиента", example = "Максимов")
    @NotBlank(message = "Last name must not be blank")
    @Size(min = 5, max = 20, message = "The length of the last name must be from 5 to 20 characters")
    @Column(name = "last_name")
    private String lastName;

    /**
     * Возраст клиента
     */
    @Schema(description = "Возраст клиента", example = "35")
    @Min(value = 0, message = "Age must be more 0")
    @Max(value = 150, message = "Age must be less 150")
    @NotNull(message = "Age must not be null")
    private Integer age;

    /**
     * Электронная почта клиента
     */
    @Schema(description = "Электронная почта", example = "maxim1988@mail.ru")
    @NotBlank(message = "Email must not be blank")
    @Size(min = 5, max = 100, message = "The length of email must be from 5 to 100 characters")
    @Email(message = "Incorrect email")
    private String email;

    /**
     * {@link Gender Пол} клиента
     */
    @Schema(description = "Пол клиента", example = "MAN")
    @Column(name = "gender")
    @NotNull(message = "Gender must not be null")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * Адрес регистрации клиента
     */
    @Schema(description = "Адрес регистрации клиента", example = "Russia, Moscow, Kutuzovsky, 32, 21")
    @Size(min = 1, max = 200, message = "The length of address must be from 1 to 200 characters")
    @NotBlank(message = "Address must not be blank")
    private String address;

    /**
     * Номер телефона клиента
     */
    @Schema(description = "Номер телефона клиента", example = "+7(903)123-9900")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Incorrect phone format, support formats: +xxx (xxx) xxx xxxx (delimiters: \"-\", \" \" and without")
    private String phone;
}
