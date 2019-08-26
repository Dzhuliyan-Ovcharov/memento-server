package com.memento.model;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "roles", indexes = {@Index(name = "uidx_role", unique = true, columnList = "role")})
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 7734444963238814777L;

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}

