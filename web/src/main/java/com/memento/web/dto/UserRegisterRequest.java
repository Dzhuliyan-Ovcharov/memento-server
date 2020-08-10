package com.memento.web.dto;

import com.memento.model.Permission;
import com.memento.shared.annotation.PasswordMatches;
import com.memento.shared.annotation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@PasswordMatches(password = "password", confirmPassword = "confirmPassword")
public class UserRegisterRequest {

    @NotEmpty
    @Size(min = 3)
    private String firstName;

    @NotEmpty
    @Size(min = 3)
    private String lastName;

    @ValidEmail
    @NotEmpty
    private String email;

    @NotEmpty
    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}(|-| )\\d{3}(|-| )\\d{3}", message = "Моля добавете валиден телефонен номер.")
    private String phoneNumber;

    @NotEmpty
    private String agencyName;

    @NotEmpty
    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}(|-| )\\d{3}(|-| )\\d{3}", message = "Моля добавете валиден телефонен номер.")
    private String agencyPhoneNumber;

    @NotNull
    private Permission permission;

    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;

    @NotEmpty
    @Size(min = 8, max = 20)
    private String confirmPassword;
}
