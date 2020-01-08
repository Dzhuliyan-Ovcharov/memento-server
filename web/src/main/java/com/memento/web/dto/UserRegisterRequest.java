package com.memento.web.dto;

import com.memento.model.Permission;
import com.memento.shared.annotation.PasswordMatches;
import com.memento.shared.annotation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@PasswordMatches(password = "password", confirmPassword = "confirmPassword")
public class UserRegisterRequest {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @ValidEmail
    @NotEmpty
    private String email;

    @NotNull
    private Permission permission;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;
}
