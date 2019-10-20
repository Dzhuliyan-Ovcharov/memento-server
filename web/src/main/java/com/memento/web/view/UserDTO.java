package com.memento.web.view;

import com.memento.shared.annotation.PasswordMatches;
import com.memento.shared.annotation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@PasswordMatches(password = "password", confirmPassword = "confirmPassword")
public class UserDTO {

    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    private String username;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @Size(min = 4, max = 20)
    private String password;

    @NotNull
    @Size(min = 4, max = 20)
    private String confirmPassword;
}
