package com.memento.web.dto;

import com.memento.shared.annotation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class UserAuthenticateRequest {

    @ValidEmail
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;
}
