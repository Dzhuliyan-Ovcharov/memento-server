package com.memento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users", indexes = {@Index(name = "uidx_user_username", unique = true, columnList = "username")})
public class User implements UserDetails {

    private static final long serialVersionUID = 7511863539873633069L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(nullable = false)
    private String username;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(nullable = false)
    @Email
    private String email;

    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_roles_id"))
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Estate> estates;

    @Override
    public Set<Role> getAuthorities() {
        return Set.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
