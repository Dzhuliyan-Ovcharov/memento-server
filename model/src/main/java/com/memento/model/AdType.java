package com.memento.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ad_types", indexes = {@Index(name = "uidx_ad_type", unique = true, columnList = "type")})
public class AdType implements Serializable {

    private static final long serialVersionUID = -6926770371567079871L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "estateType", fetch = FetchType.LAZY)
    private Set<Estate> estates;
}
