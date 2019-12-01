package com.memento.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users", indexes = {@Index(name = "uidx_user_email", unique = true, columnList = "email")})
public class User implements Serializable {

    private static final long serialVersionUID = 7511863539873633069L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(nullable = false)
    private String firstName;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(nullable = false)
    private String lastName;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(nullable = false)
    private String email;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_roles_id"))
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Estate> estates;

}
