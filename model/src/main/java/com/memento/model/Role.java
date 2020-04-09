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
@Table(name = "roles", indexes = {@Index(name = "uidx_role", unique = true, columnList = "permission")})
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 7734444963238814777L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    @Override
    public String getAuthority() {
        return permission.getValue();
    }
}
