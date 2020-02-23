package com.memento.web.dto.user;

import com.memento.model.Permission;
import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Permission permission;
}
